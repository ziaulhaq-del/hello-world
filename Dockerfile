# Use Ubuntu 20.04 LTS as the base image
FROM ubuntu:20.04

# Install OpenJDK 8 (Java Runtime Environment) and other utilities
RUN apt-get update \
    && apt-get install -y openjdk-8-jdk wget \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV CATALINA_HOME /opt/tomcat

# Create a directory for Tomcat
RUN mkdir -p $CATALINA_HOME

# Download Apache Tomcat 8
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.77/bin/apache-tomcat-8.5.77.tar.gz -O /tmp/tomcat.tar.gz

# Extract Tomcat to the installation directory
RUN tar xzvf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1

# Remove unnecessary files
RUN rm -f /tmp/tomcat.tar.gz

#Copying App war file to tomcat
COPY ./webapp/target/*.war /opt/tomcat/webapps/

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]
