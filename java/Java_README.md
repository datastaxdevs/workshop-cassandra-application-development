# Java instructions

### Setup

First (in the GitPod terminal) `cd java`.

Execute a `mvn clean install` to bring in the dependent packages and build everything.

### Run stuff

💻 Run connectivity test:
```bash
java -jar target/examples-0.0.1-jar-with-dependencies.jar
```
<details><summary>Show expected result</summary>

```
$> java -jar target/examples-0.0.1-jar-with-dependencies.jar
[OK] Success
[OK] Welcome to Astra DB! Connected to Keyspace sensor_data
** Connected to cluster 'cndb' at data center 'us-east1' **
```

_Note: You may see warnings for SLF4J.  These can be safely ignored._

</details>
