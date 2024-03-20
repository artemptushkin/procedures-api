## Spring Boot SQL over HTTP service

This is Spring Boot service that allows to call SQL commands against multiple data sources over HTTP.

### Features:

1. Multiple data-sources inside a single running application.
2. Dynamic HTTP url and body based on Spring Application properties.

### How to use

1. Setup properties using [the samples from tests](src/test/resources/application.yaml)
2. Build an HTTP request using [the samples](sample-calls)

### How does it work

The url pattern is: `/procedures/{datasource}/{procedureName}`\
Where:
* datasource - a value from the `operations.dataSource` keys of application properties
* procedureName - a value from `operations.procedures` keys of application properties

The request body is JSON and each field will be mapped by the JSON field name with `operations.procedures.your-procedure.parameters[keys]`
For example:
```yaml
#property

lastName:
  key: last_name
  type: VARCHAR
```
Requires in the body
```json
{
  "lastName": "The Great"
}
```

The value of JSON field will be used as SQL named query parameters:
```sql
insert into cat (last_name) values (:last_name)
```

### How to build

`./gradlew clean build`

### How to run locally

Terminal:
`./gradlew bootRun --args='--spring.profiles.active=dev'`

Or use Intellij Idea Run configurations from the [run directory](.run) for Idea 2019.3+

### Used Gradle tasks
* javadoc - generate Spring Boot Configuration properties metadata to the [build directory](build/tmp/kapt3/classes/main/META-INF/spring-configuration-metadata.json)

#### Todo

1. Would be interesting to see SQL execution check mechanism, i.e. put something to the properties and check it after the execution.
2. Spring Cloud Config Server and Spring Cloud eureka configurable integration
3. Documentation on application properties
4. Logging of database queries
5. Kubernetes setup
6. integration test sourceSet refactor
7. Write test for multiple database: blocked by the issue with same bean names in auto configurations `datasourceDependencyPostProcessor`