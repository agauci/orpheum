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

# Install OpenTelemetry Collector
RUN ARCH=$(dpkg --print-architecture) && \
    echo "Detected architecture: $ARCH" && \
    if [ "$ARCH" = "amd64" ]; then \
        FILE=otelcol_0.128.0_linux_amd64.deb; \
    elif [ "$ARCH" = "arm64" ]; then \
        FILE=otelcol_0.128.0_linux_arm64.deb; \
    else \
        echo "Unsupported architecture: $ARCH" && exit 1; \
    fi && \
    echo "Downloading $FILE..." && \
    wget https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.128.0/$FILE && \
    echo "Installing $FILE with dpkg..." && \
    dpkg -i $FILE

# Copy files
COPY /src/main/resources/nginx/default.conf /etc/nginx/sites-available/default
COPY /src/main/resources/otel/config.yaml /etc/otelcol/config.yaml
COPY /src/main/resources/start.sh /start.sh
RUN chmod +x /start.sh