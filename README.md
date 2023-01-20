# workshop-cassandra-application-development

1. [Setup](#1-setup)

_Steps #2 and #3 are found in any of the following language-specific paths. Choose your path to proceed:_

- [Python](python/Python_README.md)
- [Java](java/Java_README.md)
- [Javascript](javascript/Javascript_README.md)

Learn about drivers, connectivity and requests by running a simple API with
Apache Cassandra/Astra DB as its data backend. The practice is available in several languages.

Click here for the workshop [slide deck](slides/slides.pdf).

### Pre-requisites

This workshop, the third in a series, builds on the same example used in the two previous episodes (an IoT application to access temperature measurements collected from a network of sensors).

Besides some knowledge of the [example domain](https://www.datastax.com/learn/data-modeling-by-example/sensor-data-model) used in this workshop, it is desirable to have familiarity with the concepts explored in the two previous installments of the series:

- [Cassandra Fundamentals](https://github.com/datastaxdevs/workshop-cassandra-fundamentals)
- [Data Modeling with Cassandra](https://github.com/datastaxdevs/workshop-cassandra-data-modeling)

#### Database pre-requisites

It is assumed in the following that you already have created your [Astra DB instance](https://github.com/datastaxdevs/workshop-cassandra-fundamentals#4-create-your-astra-db-instance) as instructed in the first episode, and that you have a valid "DB Administrator" Token.
**Note**: the Token that is created with the database does not have all permissions we need, so you _need_ to manually [create a Token](https://awesome-astra.github.io/docs/pages/astra/create-token/) with the higher "DB Administrator" permission and use it in what comes next.

_In case you haven't your Astra DB yet, go ahead and create it now for free by clicking here:_

<a href="https://astra.dev/yt-01-25-23"><img src="images/create_astra_db_button.png?raw=true" /></a>

> _Tip_: call the database `workshops` and the keyspace `sensor_data`.

_In case you already have a database `workshops` but no `sensor_data` keyspace, simply add it using the "Add Keyspace" button on the bottom right hand corner of your DB dashboard: please do so, avoiding the creation of another database with the same name. (Also, on the free tier you have to "Resume" the database if it is "Hibernated" for prolonged inactivity.)_

## Steps

## 1. Setup

### Astra DB "Administrator" token

If you don't have a "DB Administrator" token yet, log in to your Astra DB
and create a token with this role.
To create the token, click on the "..." menu next to your database in the main
Astra dashboard and choose "Generate token". Then make sure you select the "DB Administrator" role.
_Download or note down all components of the token before navigating away:
these will not be shown again._
[See here](https://awesome-astra.github.io/docs/pages/astra/create-token/)
for more on token creation.

> **⚠️ Important**
> ```
> The instructor will show the token creation on screen,
> but will then destroy it immediately for security reasons.
> ```

Mind that, as mentioned already, _the default Token auto-created for you when
creating the database is not powerful enough for us today._

### Gitpod

First, open this repo in Gitpod by right-clicking the following button ("open in new tab"):

<a href="https://gitpod.io/#https://github.com/datastaxdevs/workshop-cassandra-application-development"><img src="images/open_in_gitpod.svg?raw=true" /></a>

In a couple of minutes you will have your Gitpod IDE up and running, with this repo cloned, ready and waiting for you (you may have to authorize the Gitpod single-sign-on to continue).

> You may see a dialog about "opening this workspace in VS Code Desktop": you can safely dismiss it.

_Note_: The next steps are to be executed _within the Gitpod IDE._

### Configure the Astra CLI

Astra CLI is preinstalled: configure it by providing your
`AstraCS:...` database token when prompted:

```bash
astra setup
```

(_Optional)_ Now you can use the CLI to get some info on your database(s):

```bash
astra db list
astra db list-keyspaces workshops
astra db get workshops
```

<details><summary>Click here if you have <strong>multiple databases</strong> called "workshops"</summary>

DB names are not required to be unique: what _is_ unique is the ["Database ID"](https://awesome-astra.github.io/docs/pages/astra/faq/#where-should-i-find-a-database-identifier).

In case you find yourself having more than one "workshops" database, you can provide the ID instead of the name to the CLI commands
and, being able to unambiguously determine the target, it will work flawlessly.

</details>

### Create and populate tables

The Astra CLI can also launch a `cqlsh` session for you, automatically connected to your database. Use this feature to execute a `cql` script that resets the contents of the `sensor_data` keyspace, creating the right tables and writing representative data on them:

```bash
# Make sure the DB exists (resuming it if hibernated)
astra db create workshops -k sensor_data --if-not-exist --wait

# Launch the initialization script
astra db cqlsh workshops -f initialize.cql
```

You are encouraged to peek at the contents of the script to see what it does.

_(Optional)_ Interactively run some test queries on the newly-populated keyspace

<details><summary>Click to show test queries</summary>

Open an interactive `cqlsh` shell with:

```bash
astra db cqlsh workshops -k sensor_data
```

Now you can copy-paste any of the queries below and execute them with the <kbd>Enter</kbd> key:

```sql
-- Q1 (note 'all' is the only partition key in this table)
SELECT  name, description, region, num_sensors
FROM    networks
WHERE   bucket = 'all';

-- Q2
SELECT  date_hour, avg_temperature, latitude, longitude, sensor
FROM    temperatures_by_network
WHERE   network    = 'forest-net'
  AND   week       = '2020-07-05'
  AND   date_hour >= '2020-07-05'
  AND   date_hour  < '2020-07-07';

-- Q3
SELECT  *
FROM    sensors_by_network
WHERE   network = 'forest-net';

-- Q4
SELECT  timestamp, value
FROM    temperatures_by_sensor
WHERE   sensor = 's1003'
  AND   date   = '2020-07-06';

```

To close `cqlsh` and get back to the shell prompt, execute the `EXIT` command.

</details>

### Prepare connection settings

You can use the Astra CLI to prepare a dotenv file which defines all connection
parameters and secrets needed for your application to run:

```bash
astra db create-dotenv workshops -k sensor_data
```

A `.env` file will be created (you can peek at it with Gitpod's file editor, e.g. running `gp open .env`).
You can now source it with:

```bash
source .env
```

> Note that, while creating the `.env`, the database's [Secure Connect Bundle](https://awesome-astra.github.io/docs/pages/astra/download-scb/)
> has also been downloaded for you: you may want to check that the file
> is about 12-13 KiB in size with `ls $ASTRA_DB_SECURE_BUNDLE_PATH -lh`.

## 2 & 3. Now to the exercises!

_Note: it is suggested to check the [sensor data model](https://www.datastax.com/learn/data-modeling-by-example/sensor-data-model) in order to be better prepared for what follows. Keep it open in another tab._

Choose your path:

- [Python](python/Python_README.md)
- [Java](java/Java_README.md)
- [Javascript](javascript/Javascript_README.md)

## Homework instructions

<img src="images/api-micro.png?raw=true" width="150" align="left" />

In order to get a badge of completion for this workshop, complete the following assignment:

> Add a GET endpoint to your API corresponding to query `Q1`
> (_"Find information about all networks; order by name (asc)"_).
> **Tip**: remember the data-modeling optimization of having inserted the `bucket` column.

Take a _screenshot_ of the relevant code block and of a successful request to that endpoint and head over to [this form](https://dtsx.io/homework-appdev). Answer a couple of "theory" questions, attach your screenshot, and hit "Submit".

That's it! Expect to be awarded your badge in the next week or so!

## Conclusion

This is not the end of your journey, rather the start: come visit us for more cool content, and learn how to succeed using Cassandra and Astra DB in your applications!

Congratulations and see you at our next workshop!

> Sincerely yours, the DataStax Developers
