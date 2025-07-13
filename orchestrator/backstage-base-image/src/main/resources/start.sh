#!/bin/bash 
set -e 

echo "Starting container setup..." 

# Attempt to generate certificate with timeout 
echo "Attempting to generate SSL certificate..." 
timeout 60 certbot certonly --standalone \
  --non-interactive \
  --agree-tos \
  --email admin@orpheum.com.mt \
  --domains backstage.orpheum.cloud,opensearch.orpheum.cloud,backoffice.orpheum.cloud,aura.orpheum.cloud,teatru.orpheum.cloud \
  --deploy-hook "nginx -s reload" || echo "Certificate generation timed out or failed - will continue anyway" 

# Start nginx in background 
echo "Starting nginx..." 
nginx 

# Start the Open telemetry collector 
nohup otelcol --config /etc/otelcol/config.yaml &

# Start the Java application
echo "Starting Orpheum Backstage application..."
cd /orpheum-backstage
exec java -Dlogging.config=logback-prod.xml -jar orpheum-backstage-server.jar --spring.config.location=application.yaml
