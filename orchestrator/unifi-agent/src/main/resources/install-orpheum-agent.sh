#!/bin/bash

# === CONFIGURATION ===
APP_NAME="orpheum-unifi-agent"
JAR_NAME="orpheum-unifi-agent.jar"
APP_DIR="/orpheum-unifi-agent"
APP_USER="root"
JAVA_EXEC="/usr/bin/java"
ENV_FILE="/${APP_DIR}/${APP_NAME}.env"
SERVICE_FILE="/etc/systemd/system/${APP_NAME}.service"

# === Create systemd Service File ===
echo "Creating systemd service file..."
cat <<EOF | sudo tee $SERVICE_FILE > /dev/null
[Unit]
Description=Java App: $APP_NAME
After=network.target

[Service]
User=$APP_USER
WorkingDirectory=$APP_DIR
ExecStart=$JAVA_EXEC -jar $JAR_NAME
Restart=always
RestartSec=5
EnvironmentFile=-$ENV_FILE
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

# === Set Permissions ===
echo "Setting permissions..."
sudo chown $APP_USER:$APP_USER "$APP_DIR/$JAR_NAME"
sudo chmod 755 "$APP_DIR/$JAR_NAME"

# === Reload systemd and start the service ===
echo "Reloading systemd and starting service..."
sudo systemctl daemon-reload
sudo systemctl enable "$APP_NAME"
sudo systemctl start "$APP_NAME"

echo "✅ Service '$APP_NAME' installed and started."
echo "Check status: sudo systemctl status $APP_NAME"
echo "Logs: journalctl -u $APP_NAME -f"