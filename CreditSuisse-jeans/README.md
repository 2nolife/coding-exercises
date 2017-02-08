# CreditSuisse Jeans Scala test

This exercise features a microservice and a simple web UI.
It provides a page with a list of orders aggregated by various
fields: manufacturer, country etc. The aggregation is done on
the backend with the help of Mongo DB aggregate function.
The UI talks to the microservice by using REST HTTP with JSON format.

### Technologies ###
* Backend: Scala, Akka HTTP, Akka Actors, MongoDB, SBT
* Frontend: AngularJS, Bootstrap, jQuery, Node JS

### Requirements ###
* Java 8
* SBT
* Node JS
* MongoDB running locally on the default port 27017
* Port 8021 must be available for the microservice to bind to
* Port 8080 must be available for the UI weblet to bind to

### Run ###
To run the integration tests and populate the database with test data, approx 2.5k orders.
From the project's root directory:

```sbt it:test```

Start the backend from the project's root directory:

```sbt run```

Start the frontend from the project's "weblet" directory: 

```npm install express request```

```node weblet.js```

Open the UI in a browser:

```http://localhost:8080/cp ```

### Mongo Databases ###
The following two databases will be created: "jeans" and "jeans-test". Both should have a
collection "ms-orders" pupulated with sample data.
