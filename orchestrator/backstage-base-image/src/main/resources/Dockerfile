FROM ubuntu:24.04

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Update package lists
RUN apt-get update

# Update package lists, Install Java, nginx, certbot, and other dependencies
RUN apt-get update && apt-get install -y \
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

# Install open telemetry collector
RUN wget https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.128.0/otelcol_0.128.0_linux_amd64.deb
RUN dpkg -i otelcol_0.128.0_linux_amd64.deb

# Copy files
COPY /src/main/resources/nginx/default.conf /etc/nginx/sites-available/default
COPY /src/main/resources/otel/config.yaml /etc/otelcol/config.yaml
COPY /src/main/resources/start.sh /start.sh