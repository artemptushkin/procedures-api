## Spring Boot SQL over HTTP service

This is Spring Boot service that allows to call SQL commands against multiple data sources over HTTP.

### How to build

`./gradlew clean build`

### How to run locally

Terminal:
`./gradlew bootRun --args='--spring.profiles.active=dev'`

Or use Intellij Idea Run configurations from the [run directory](.run) for Idea 2019.3+

#### Todo
1. Would be interesting to see SQL execution check mechanism, i.e. put something to the properties and
check it after the execution