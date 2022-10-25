# workshop-cassandra-application-development

1. [Setup](#1-database-setup)

- [Python](python/Python_README.md)
- [Java](java/Java_README.md)

_Step #2 and #3 are under the Java and Python paths.  You must pick either Java or Python before proceeding to Steps #2 and #3._

Learn about drivers, connectivity and requests by running a simple API with
Apache Cassandra/Astra DB as its data backend. The practice is available in both
Python and Java.

Click here for the [slide deck](slides/java-and-python-apps-with-cassandra-slides.pdf) of this workshop.

### Pre-requisites

This workshop, the third in a series, builds on the same example used in the two previous
episodes (an IoT application to access temperature measurements collected from a network of sensors).

Besides some knowledge of the [example domain](https://www.datastax.com/learn/data-modeling-by-example/sensor-data-model) used in this workshop, it is desirable to have
familiarity with the concepts explored in the two previous installments of the series:

- [Cassandra Fundamentals](https://github.com/datastaxdevs/workshop-cassandra-fundamentals)
- [Data Modeling with Cassandra](https://github.com/datastaxdevs/workshop-cassandra-data-modeling)

#### Database pre-requisites

It is assumed in the following that you already have created
your [Astra DB instance](https://github.com/datastaxdevs/workshop-cassandra-fundamentals#4-create-your-astra-db-instance) as instructed in the first episode,
and that you have a valid "DB Administrator" Token.
**Note**: the Token that is created with the database does not have all permissions we need,
so you _need_ to manually [create a Token](https://awesome-astra.github.io/docs/pages/astra/create-token/)
with the higher "DB Administrator"
permission and use it in what comes next.

_In case you haven't your Astra DB yet, go ahead and create it now for free by clicking here:_

<a href="https://astra.dev/yt-8-10"><img src="images/create_astra_db_button.png?raw=true" /></a>

> _Tip_: call the database `workshops` and the keyspace `sensor_data`.

_In case you already have a database `workshops` but no `sensor_data` keyspace,
simply add it using the "Add Keyspace" button on the bottom right hand corner of your DB dashboard._

## Steps

## 1. Setup

#### Astra DB "Administrator" token

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
> The instructor will show you on screen how to create a token 
> but will have to destroy to token immediately for security reasons.
> ```

Mind that, as mentioned already, _the default Token auto-created for you when
creating the database is not powerful enough for us today._

#### Gitpod

First, open this repo in Gitpod by right-clicking the following button ("open in new tab"):

**FIXME** this Gitpod button currently points to a branch in Stefano's fork!

<a href="https://gitpod.io/#https://github.com/hemidactylus/workshop-cassandra-application-development/tree/astra-cli"><img src="images/open_in_gitpod.svg?raw=true" /></a>

In a couple of minutes you will have your Gitpod IDE up and running, with this repo cloned,
ready and waiting for you (you may have to authorize the Gitpod single-sign-on to continue).

_Note_: The next steps are to be executed _within the Gitpod IDE._

#### Install and configure the Astra CLI

In a console within Gitpod, install Astra CLI with

```
curl -Ls "https://dtsx.io/get-astra-cli" | bash
```

Then provide the "token proper" part of the Token, the string starting with `AstraCS:...`), by running

```
export PATH="$PATH:~/.astra/cli"    # TEMPORARY workaround
. ~/.bashrc ; astra setup
```

(_Optional)_ Get some information on your Astra DB with:

```
astra db list
astra db list-keyspaces workshops
astra db get workshops
```

#### Create and populate tables

The Astra CLI can also launch a `cqlsh` session for you, automatically connected
to your database. Use this feature to execute a `cql` script that resets the
contents of the `sensor_data` keyspace, creating the right tables and writing
representative data on them:

```
astra db cqlsh workshops -f initialize.cql
```

You are encouraged to peek at the contents of the script to see what it does.

(_Optional)_ Interactively run some test queries on the newly-populated keyspace

<details><summary>Click to show test queries</summary>

Open an interactive `cqlsh` shell with:

```
astra db cqlsh workshops -k sensor_data
```

Now you can copy-paste any of the queries below and execute them with the `Enter` key:

```
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

#### Download the Secure Connect Bundle

Besides the "Client ID" and the "Client Secret" from the Token, the drivers
also need the "Secure Connect Bundle" zipfile to work (it contains proxy
and routing information as well as the necessary certificates).

To download it:

```
astra db download-scb -f secure-connect-workshops.zip workshops
```

You can check it has been saved with `ls *.zip`.

#### Configure the dot-env file

Copy the template dot-env and edit it with:

```
cp .env.sample .env ; gp open .env
```

Replace the Client ID and Client Secret strings from the database Token.

Finally, `source` the .env file:

```bash
source .env
```


## 2 & 3. Now to the exercises!

_Note: it is suggested to check the [sensor data model](https://www.datastax.com/learn/data-modeling-by-example/sensor-data-model)
in order to be better prepared for what follows. Keep it open in another tab._

Choose your path:

- [Python](python/Python_README.md)
- [Java](java/Java_README.md)

## Homework instructions

<img src="images/api-micro.png?raw=true" width="150" align="left" />

In order to get a badge of completion for this workshop,
complete the following assignment:

> Add a GET endpoint to your API corresponding to query `Q1`
> (_"Find information about all networks; order by name (asc)"_).
> **Tip**: remember the optimization of having inserted the `bucket` column.

Take a _screenshot_ of the relevant code block and of a successful
request to that endpoint and head over to [this form](https://dtsx.io/homework-appdev). Answer a couple of
"theory" questions, attach your screenshot, and hit "Submit".

That's it! Expect to be awarded your badge in the next week or so!

## Conclusion

This is not the end of your journey, rather the start: come visit us for more
cool content, and learn how to succeed using
Cassandra and Astra DB in your applications!

Congratulations and see you at our next workshop!

> Sincerely yours, the DataStax Developers
> 
