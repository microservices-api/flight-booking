FROM websphere-liberty:kernel
COPY server.xml  /config/
COPY airlines.war /config/apps/ 
RUN installUtility install --acceptLicense defaultServer