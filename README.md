# orpheum

## Backstage Server

Installation:
- Build the server via command mvn clean install -DskipTests
- If we are to build a docker image use command mvn clean install -Pdocker -DskipTests
- If running via docker, go to /backstage/deploy folder and run docker compose up -d

## Unifi Agent

Installation:
- Build the agent via command mvn clean install
- Copy agent.env, application.properties, uber jar orpheum-unifi-agent.jar
- Run chmod +x install-orpheum-agent.sh
- Run ./install-orpheum-agent.sh

To check if service is set up correctly:
- Run systemctl list-units --type=service and check if service is there

To view logs from set up service:
- Run journalctl -u orpheum-unifi-agent.service -f

## TO DO
- If we migrate from the current docker compose based solution, we'll need to install a systemctl based solution for backstage similar to the agent. Currently, restarts are handled by docker compose.
- To Limit traffic from general public? 
	- Remove basic landing page if not using dev profile?
	