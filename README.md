# orpheum

## Flow

The following is the captive portal flow implemented across the backstage server (once instance, running on the cloud), and each UniFi agent (running within each gateway device). This flow is an implementation of [RFC 8908] (https://www.rfc-editor.org/rfc/rfc8908.html)

1. The device connects to the UniFi gateway via a WiFi connection which is captive portal enabled.
2. As part of its DHCP Options response, the gateway sends back a URL which the device is to query to discover if it has access to the WiFi network, or if it needs the to access a captive portal first. 
	1. This is configured via the UniFi gateway portal under Settings -> Network -> Default -> DHCP Service Management -> Custom DHCP Options. The standard DHCP option number is 114 (See Evernote for more details).
	2. The captive portal DHCP options url is of the form https://{{local_agent_url}}/.well-known/captive-portal
	3. The UniFi gateway is configured so that the {{local_agent_url}} points to 192.168.1.1 (i.e. the gateway itself). Internally, the gateway's nginx instance is modded to point traffic hitting this domain to the Orpheum UniFi agent running inside, which in turn is running a lightweight server to serve this request.
		1. This configuration is found under Routing -> DNS. Add a Host (A) entry.
3. The Orpheum UniFi agent captures the device's assigned IP, which is forwarded by the gateway's internal nginx instance via header X-Forwarded-For. The server checks if this device is already authorised against its internal cache, and in doing so, returns whether the device should be redirected to a captive portal. The JSON response sent by this server is something of the following form:
```
{
    "captive": true,
    "user_portal_url": "https://backstage.orpheum.cloud/guest/s/default/?ip=192.168.1.49&ssid=Teatru+Guest&t=1744751393880",
    "venue_info_url": "https://orpheum.com.mt",
    "can_extend_session": false
}
```
4. If the captive attribute is true, the device is redirected to the provided URL.
	1. The UniFi device has its own captive portal configuration set, and is seemingly injecting (i) the device's MAC address (with the name id), (ii) the access point's mac address (with the name ap), (iii) a timestamp (with the name t), and (iv) the WiFi network's SSID (under tha name ssid).
	2. If the UniFi device fails to inject said parameters, the URL forwarded by the Orpheum Agent forwards the device's assigned IP address (under the name ip), as well as the timestamp and SSID as per above.
5. At this stage, the device hits the backstage captive portal page, where the user is requested to provide their name and email address.
6. Once done, the backstage server captures either the MAC addresses or the forwarded IP address, and exposes this information for polling to the Orpheum agent.
7. In turn, the agent captures this available pending device authorisation, executes it with the UniFi Gateway running on the same device, and once done, adds the device's details to the agent's internal cache of authorised devices with a duration which matches the UniFi gateway's session timeout duration.
	1. The Orpheum UniFi agent is built to support both authorisation via MAC addresses as well as by device IP. If the former are provided, authorisation can happen directly by calling the gateway API. If the device IP is provided, this is first resolved to the required MAC addresses by calling a GET active devices API on the gateway, and then authorising the device.
8. Any subsequent calls by the device to the DHCP Option 114 url in order to query its captive portal state will be met with a "captive": false	response, indicating that the device is authorised on the network.
	1. Should the device authorisation expire, it is automatically cleared from the cache and the process starts all over again.

## Backstage Server

Installation:
- Build the server via command mvn clean install -DskipTests
- If we are to build a docker image use command mvn clean install -Pdocker -DskipTests
- If running via docker, go to /backstage/deploy folder and run docker compose up -d

## Orpheum UniFi Agent

Installation:
- Build the agent via command mvn clean install
- SSH into UniFI gateway and install Java 17
- Create folder /orpheum-unifi-agent
- Copy agent.env, application.properties, agent-control.properties, and uber jar orpheum-unifi-agent.jar. Also copy fullchain.pem and privkey.pem obtained from the backstage server, which also contains a certificate for the local agent url.
- Run chmod +x install-orpheum-agent.sh
- Run ./install-orpheum-agent.sh
- Go to file /etc/nginx/conf.d/overrides.conf and add the following config at the end of the file. Don't forget to change the local agent URL as defined in the gateway itself

```
	server {
    listen 443 ssl http2;
    server_name {{local_agent_url}};

    ssl_certificate /orpheum-unifi-agent/fullchain.pem;
    ssl_certificate_key /orpheum-unifi-agent/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384;
    ssl_session_timeout 1d;
    ssl_session_cache shared:SSL_2:10m;
    ssl_stapling off;
    ssl_stapling_verify off;

    location / {
        proxy_pass http://127.0.0.1:7070;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

```

- Turn off auto updates for both the UI and network. Allowing auto updates will result in a format of the device, losing any custom installed software (Java) and nginx configuration.

To check if service is set up correctly:
- systemctl list-units --type=service and check if service is there

To check when the next restart timer will run:
- systemctl list-timers --all | grep orpheum-unifi-agent

To view logs from set up service:
- journalctl -u orpheum-unifi-agent.service -f

## Adding A New Location
- Add the {{local_agent_url}} to the let's encrypt command (see backstage/src/main/resources/Dockerfile)
- Set up the Orpheum Unifi Agent as described above.
- Add the new site configuration on Backstage's side at backstage/src/main/resources/application.yaml

## TO DO
- If we migrate from the current docker compose based solution, we'll need to install a systemctl based solution for backstage similar to the agent. Currently, restarts are handled by docker compose.
- To Limit traffic from general public? 
	- Remove basic landing page if not using dev profile?

## Limitations
- It is currently unclear how to identify the lease duration left for the device on the network via the gateway GET active devices API call. This means that we can only infer this duration from the moment the authorisation API call is made to the gateway and the session duration configured in the UniFi hotspot portal. As a result, we have to assume that the agent is started at the same time as the gateway, since restarting the gateway and resolving the device via polling will set the same cache TTL duration as if the device was freshly authorised. In short, restarting the agent but not the gateway is *dangerous*.
	
## Notes
- To sniff traffic landing on unifi gateway related to dhcp, run this command: tcpdump -i br0 port 67 or port 68 -n -vv	
- To verify when the certificate used by the orpheum agent expires:  openssl x509 -in fullchain.pem -noout -enddate
- Command to renew certificate
- To obtain snapshot of gateway status: echo "=== CPU Usage ==="; mpstat 1 1 | awk '/Average:/ {print 100 - $12 "% used"}'; echo "=== Memory Usage ==="; free -h; echo "=== Disk Space ==="; df -h /
- Initial testing seems to show that if the captive portal state API returns "captive": true, but the device is already authorised, the device will ignore the API direction and connect. This needs to be tested further.
	
	