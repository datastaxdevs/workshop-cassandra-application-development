# workshop-cassandra-application-development

1. [Database Setup](#1-database-setup)
2. Run the app:

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
_Download or note down all components of the token before navigating away: these will not be shown again._
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

<a href="https://gitpod.io/#https://github.com/datastaxdevs/workshop-cassandra-application-development"><img src="images/open_in_gitpod.svg?raw=true" /></a>

In a couple of minutes you will have your Gitpod IDE up and running, with this repo cloned,
ready and waiting for you.

_Note_: The next steps are to be executed _within the Gitpod IDE._

#### Provide DB access credentials

**First** upload the secure-connect-bundle to Gitpod, simply by locating
it on your system with the file explorer and dragging it over to the left-side
Gitpod panel (the file manager). Make sure the file is in the repo's root.

**Second** prepare a `.env` file with some environment variables that will
be picked up by the application to connect to the database. To do so, in the main
repo's root directory:

- copy the template and edit it, `cp .env.sample .env ; gp open .env`
- insert the Client ID and Client Secret from your DB Token
- insert the full path to your secure-connect bundle. Chances are you can leave it as it is; to make sure, check the output of `ls /workspace/workshop-cassandra-application-development/*.zip`
- check the keyspace name (most likely you don't have to change it)
- Finally, `source` the .env file:

```bash
source .env
```

#### DB content reset

If you did the exercises in previous episodes, your database already contains the right tables
and the correct data. In any case, we provide a handy script to copy and paste in your Astra DB's Web-based CQL Console to populate the database with everything needed for the next steps.

> Note: if you created some tables, but messed up with their structure, you can
> issue `DROP TABLE <tablename>;` commands before running the DB-population script.
> _(if you get timeout errors, don't worry, it's an UI thing - the table has been dropped nevertheless.)_

Now, click here to copy the DB-population script and paste it in the CQL Console: [`initialize.cql`](https://raw.githubusercontent.com/datastaxdevs/workshop-cassandra-application-development/main/initialize.cql).

## 2. Now to the exercises!

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
