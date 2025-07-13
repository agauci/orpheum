FROM ubuntu:24.04

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Update package lists
RUN apt-get update

# Install Java, nginx, certbot, and other dependencies
RUN apt-get install -y \
    openjdk-17-jdk \
    nginx \
    certbot \
    python3-certbot-nginx \
    curl \
    vim \
    coreutils \
    wget \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Create directory for certbot and orpheum-backstage
RUN mkdir -p /var/www/certbot && \
    mkdir -p /orpheum-backstage

# Copy application files
COPY /src/main/resources/application.yaml /orpheum-backstage/
COPY /src/main/resources/logback.xml /orpheum-backstage/
COPY /src/main/resources/logback-prod.xml /orpheum-backstage/
COPY /target/orpheum-backstage-server.jar /orpheum-backstage/
COPY /src/main/resources/nginx/default.conf /etc/nginx/sites-available/default

# Install open telemetry collector
RUN wget https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.128.0/otelcol_0.128.0_linux_amd64.deb
RUN dpkg -i otelcol_0.128.0_linux_amd64.deb

# Open telemetry collector configuration which accepts logs via HTTP & GRPC
RUN echo 'processors: \n\
  batch: \n\
    send_batch_size: 5000 \n\
    timeout: 10s \n\
\n\
receivers: \n\
  otlp: \n\
    protocols: \n\
      grpc: \n\
      http: \n\
\n\
exporters: \n\
  otlphttp: \n\
    endpoint: 'https://otel.kloudmate.com:4318' \n\
    headers: \n\
      Authorization: 'sk_9DfnrMIdYuDaBSMuaQfyGFAO' \n\
\n\
service: \n\
  pipelines: \n\
    logs: \n\
      receivers: [otlp] \n\
      processors: [batch] \n\
      exporters: [otlphttp] \n\
  telemetry: \n\
    logs: \n\
      level: "debug"' > /etc/otelcol/config.yaml

# Startup script with error handling, logging, and jar execution
RUN echo '#!/bin/bash \n\
set -e \n\
\n\
echo "Starting container setup..." \n\
\n\
# Attempt to generate certificate with timeout \n\
echo "Attempting to generate SSL certificate..." \n\
timeout 60 certbot certonly --standalone \
  --non-interactive \
  --agree-tos \
  --email admin@orpheum.com.mt \
  --domains backstage.orpheum.cloud,opensearch.orpheum.cloud,backoffice.orpheum.cloud,aura.orpheum.cloud,teatru.orpheum.cloud \
  --deploy-hook "nginx -s reload" || echo "Certificate generation timed out or failed - will continue anyway" \n\
\n\
# Start nginx in background \n\
echo "Starting nginx..." \n\
nginx \n\
\n\
# Start the Open telemetry collector \n\
nohup otelcol --config /etc/otelcol/config.yaml & \n\
# Start the Java application \n\
echo "Starting Orpheum Backstage application..." \n\
cd /orpheum-backstage \n\
exec java -Dlogging.config=logback.xml -jar orpheum-backstage-server.jar --spring.config.location=application.yaml \n\
' > /start.sh && chmod +x /start.sh

# Expose ports
EXPOSE 80 443

# Command to run when container starts
CMD ["/start.sh"]