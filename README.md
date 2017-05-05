# Getting started
## Setup database
This application uses a local CouchDB database.  The database address comes from a configuration file, `https://github.com/microservices-api/flight-booking/tree/master/src/main/resources/config.properties`, or at the WAR's `WEB-INF/classes/config.properties` file.  

>Note: you can run `bash database_init.sh` to initiate your database.

We also assume the following databases have been created:  `airlines` and `bookings`.   You can pre-populate your `airlines` database with the following airlines (note: in the future we'll do a database init helper class so that you don't have to manually do this):

```
{"name":"Acme Air","contactPhone":"1-888-1234-567"}
{"name":"Acme Air Partner","contactPhone":"1-855-1284-563"}
{"name":"Mock Air","contactPhone":"1-855-4254-000"}
```

To setup CouchDB, please see http://couchdb.apache.org/

## Clone application
```
git clone https://github.com/microservices-api/flight-booking.git
```
## Create a deployable WAR file
```
cd flight-booking
mvn package
```
The packaged WAR file will be at the following location: `flight-booking/deployment_artifacts/airlines.war`

## Deploy locally in a Docker container
* `cd flight-booking/deployment_artifacts`
* `docker build .`
* `docker run -p 80:9080 -p 443:9443 -i <image>`
* open a browser and navigate to `http://<your_host>/api/explorer` to see the OpenAPI UI
* if you want to use the API Connect integration endpoint, navigate to `http://<your_host>/ibm/api/explorer` and use the credentials defined in your server.xml (admin/admin)

## Deploy locally in an application server (outside of a container)
* download the application server (Liberty) jar from https://developer.ibm.com/wasdev/downloads/
* create a new server by running `wlp/bin server create myServer`
* copy the file `flight-booking/deployment_artifacts/server.xml` into `wlp/usr/servers/myServer`
* copy the file `flight-booking/deployment_artifacts/airlines.war` into `wlp/usr/servers/myServer/apps`
* install the needed features by running `wlp/bin installUtility install --acceptLicense myServer`
* start the server by running `wlp/bin server start myServer`
* open a browser and navigate to `http://<your_host>/api/explorer` to see the OpenAPI UI
* if you want to use the API Connect integration endpoint, navigate to `http://<your_host>/ibm/api/explorer` and use the credentials defined in your server.xml (admin/admin)




