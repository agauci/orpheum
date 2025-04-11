# orpheum

## Unifi Agent

Installation:
- Build the agent via command mvn clean install
- Copy agent.env, application.properties, uber jar orpheum-unifi-agent.jar
- Run chmod +x install-orpheum-agent.sh
- Run ./install-orpheum-agent.sh

To check if service is set up correctly:
- Run systemctl list-units --type=service and check if service is there

To view logs from set up service:
- Run journalctl -u orpheum-java-unifi-agent -f