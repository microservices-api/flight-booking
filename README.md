# Getting started
## Clone application
```
git clone https://github.com/microservices-api/airlines.git
```
## Create a deployable WAR file
```
cd airlines/airlines
mvn package
```
The packaged WAR file will be at the following location: `airlines/airlines/deployment_artifacts/java-rest-user-application.war`

## Deploy in an application server
For **Liberty**:
* download the jar from https://developer.ibm.com/wasdev/downloads/
* install apiDiscovery-1.0 by running `wlp/bin installUtility install apiDiscovery-1.0`
* create a new server by running `wlp/bin server create myServer`
* copy the file `java-rest-user/swagger-sample-application/deployment_artifacts/server.xml` into `wlp/usr/servers/myServer`
* copy the file `java-rest-user/swagger-sample-application/deployment_artifacts/java-rest-user-application.war` into `wlp/usr/servers/myServer/apps`
* start the server by running `wlp/bin server start myServer`
* open a browser and paste the URL `http://localhost:9080/ibm/api/explorer`, using the credentials `admin / admin` or a different set of credentials if you modified the registry in server.xml
* you can then use the Open API UI to view and test the REST APIs of this application!



