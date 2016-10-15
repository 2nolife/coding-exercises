# Credit Suisse loan platform Scala coding exercise

## Running the application ##

You need to install [SBT](http://www.scala-sbt.org) to build and run the server. Then clone the repository and from the project directory `sbt run`:

```
$ sbt run
[info] ...
Server online at http://localhost:8080/
Press RETURN to stop...
```

To stop the server press ENTER

## Testing ##

Unit tests
```
$ sbt test
```

Integration tests
```
$ sbt it:test
```

Manual tests
```
$ sbt run

And then from another terminal:
curl -i 'http://localhost:8080/api/offers' -X POST -H "Content-Type: application/json" -d '{"amount": 100, "rate": 5.0}'
curl -i 'http://localhost:8080/api/offers' -X POST -H "Content-Type: application/json" -d '{"amount": 500, "rate": 8.6}'
curl -i 'http://localhost:8080/api/loans' -X POST -H "Content-Type: application/json" -d '{"amount": 1000, "days": 100}'

Use the ID from the last response instead of ID_CHANGE_ME:
curl -i 'http://localhost:8080/api/offers?loan_id=ID_CHANGE_ME' -X GET
```

## Libraries ##

* Scala
* Akka HTTP for the REST web server
* Apache HTTP client for integration tests (failed to use Akka HTTP client)
* Mockito for unit tests (not used but just in case for future testing)
* Logback for logging (not used at the moment)
* ScalaTest for unit and integration tests
