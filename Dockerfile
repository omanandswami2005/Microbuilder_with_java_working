# Use a base image with Java and Tomcat
FROM tomcat:10-jdk17

# Remove the default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the generated WAR file into the Tomcat webapps directory
COPY target/VAPM_Omiii_Project-1.0.0.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port that Tomcat listens on (usually 8080)
EXPOSE 8080

# Start Tomcat when the container is launched
CMD ["catalina.sh", "run"]
