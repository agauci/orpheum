FROM orpheum/base-image:latest

# Copy application files
COPY /src/main/resources/application.yaml /orpheum-backstage/
COPY /src/main/resources/logback.xml /orpheum-backstage/
COPY /src/main/resources/logback-prod.xml /orpheum-backstage/
COPY /target/orpheum-backstage-server.jar /orpheum-backstage/

# Expose ports
EXPOSE 80 443

# Command to run when container starts
CMD ["/start.sh"]