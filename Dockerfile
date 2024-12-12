# Use the latest Tomcat image as the base image
FROM tomcat:latest

# Update the package list for apt
RUN apt update

# Upgrade installed packages to their latest versions
RUN apt upgrade -y

# Install vim text editor
RUN apt install vim -y

# Modify tomcat-users.xml to add roles and users for manager access
RUN sed -i '/<\/tomcat-users>/i\
  <role rolename="manager-gui"/>\n  \
  <role rolename="manager-script"/>\n  \
  <role rolename="manager-jmx"/>\n  \
  <role rolename="manager-status"/>\n  \
  <user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx,manager-status"/>\n  \
  <user username="deployer" password="deployer" roles="manager-script"/>\n  \
  <user username="tomcat" password="s3cret" roles="manager-gui"/>' /usr/local/tomcat/conf/tomcat-users.xml

# Comment out the RemoteAddrValve section in the host-manager context configuration file
RUN sed -i '/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/,/allow=/s|^|<!-- |' /usr/local/tomcat/webapps.dist/host-manager/META-INF/context.xml

# Comment out the RemoteAddrValve section in the host-manager context configuration file (closing tag)
RUN sed -i '/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/,/allow=/s|$| -->|' /usr/local/tomcat/webapps.dist/host-manager/META-INF/context.xml

# Comment out the RemoteAddrValve section in the manager context configuration file
RUN sed -i '/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/,/allow=/s|^|<!-- |' /usr/local/tomcat/webapps.dist/manager/META-INF/context.xml

# Comment out the RemoteAddrValve section in the manager context configuration file (closing tag)
RUN sed -i '/<Valve className="org.apache.catalina.valves.RemoteAddrValve"/,/allow=/s|$| -->|' /usr/local/tomcat/webapps.dist/manager/META-INF/context.xml

# Copy the web applications from the 'webapps.dist' directory to the 'webapps' directory
RUN cp -R /usr/local/tomcat/webapps.dist/* /usr/local/tomcat/webapps

# Copy the WAR files from the current directory into the Tomcat webapps folder
COPY ./*.war /usr/local/tomcat/webapps
