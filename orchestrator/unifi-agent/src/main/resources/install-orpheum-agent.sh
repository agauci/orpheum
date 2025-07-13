#!/bin/bash

# === CONFIGURATION ===
APP_NAME="orpheum-unifi-agent"
JAR_NAME="orpheum-unifi-agent.jar"
APP_DIR="/orpheum-unifi-agent"
APP_USER="root"
JAVA_EXEC="/usr/bin/java"
ENV_FILE="/${APP_DIR}/${APP_NAME}.env"
SERVICE_FILE="/etc/systemd/system/${APP_NAME}.service"
RESTART_SERVICE="/etc/systemd/system/${APP_NAME}-restart.service"
RESTART_TIMER="/etc/systemd/system/${APP_NAME}-restart.timer"

# === Create systemd Service File ===
echo "Creating systemd service file..."
cat <<EOF | sudo tee "$SERVICE_FILE" > /dev/null
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

# === Create systemd Restart Service ===
echo "Creating restart service file..."
cat <<EOF | sudo tee "$RESTART_SERVICE" > /dev/null
[Unit]
Description=Restart $APP_NAME weekly on Tuesday at 3AM

[Service]
Type=oneshot
ExecStart=/bin/systemctl restart $APP_NAME.service
EOF

# === Create systemd Restart Timer ===
# This timer is implicitly linked with the service due to both having the same name, i.e.
# orpheum-unifi-agent-restart.service and orpheum-unifi-agent-restart.timer
echo "Creating restart timer file..."
cat <<EOF | sudo tee "$RESTART_TIMER" > /dev/null
[Unit]
Description=Weekly restart timer for $APP_NAME

[Timer]
OnCalendar=Tue *-*-* 03:00:00
Persistent=true

[Install]
WantedBy=timers.target
EOF

# === Set Permissions ===
echo "Setting permissions..."
sudo chown "$APP_USER":"$APP_USER" "$APP_DIR/$JAR_NAME"
sudo chmod 755 "$APP_DIR/$JAR_NAME"

# === Reload systemd and start services ===
echo "Reloading systemd and starting services..."
sudo systemctl daemon-reload
sudo systemctl enable --now "$APP_NAME"
sudo systemctl enable --now "${APP_NAME}-restart.timer"

echo "✅ Service '$APP_NAME' installed and started."
echo "🔁 Timer '${APP_NAME}-restart.timer' set to restart it weekly on Tuesday at 3AM."
echo "Check service status: sudo systemctl status $APP_NAME"
echo "Check timer status: sudo systemctl list-timers | grep $APP_NAME"
