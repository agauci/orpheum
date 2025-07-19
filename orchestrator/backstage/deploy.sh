#!/bin/bash

set -e  # Exit on any command failure

# Step 1: Pull from git
echo "Pulling latest changes from git..."
git pull || { echo "Git pull failed. Exiting."; exit 1; }

# Step 2: Ask to continue
read -p "Continue to build the project? (y/n): " confirm
if [[ "$confirm" != "y" ]]; then
    echo "Aborted by user."
    exit 0
fi

# Step 3: Run Maven build
echo "Building project with Maven..."
mvn clean install -Pdocker -DskipTests || { echo "Maven build failed. Exiting."; exit 1; }

# Step 4: Ask to continue
read -p "Continue to deploy? (y/n): " confirm
if [[ "$confirm" != "y" ]]; then
    echo "Aborted by user."
    exit 0
fi

# Step 5: Change directory to /deploy
cd /deploy || { echo "Failed to change directory to /deploy"; exit 1; }

# Step 6: Run Docker Compose
echo "Starting Docker Compose..."
docker compose up -d || { echo "Docker Compose failed. Exiting."; exit 1; }

echo "Deployment complete."
