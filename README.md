## Wildlife Tracker

An app for the forest service to track animals for an environmental impact study.

### Description

The Forest Service is considering a proposal from a timber company to clearcut a nearby forest of Douglas Fir. Before this proposal may be approved, they must complete an environmental impact study. This application was developed to allow Rangers to track wildlife sightings in the area.

### Known Bugs

If I had more time, here's what I would attack next:

* Restrict user from making mistakes in the sighting report (i.e., make badge numbers and names dropdown menus)

* Can’t update endangered animal to non-endangered or vice versa (have to just remove and create new one).

* Display animal's name in ranger page

### Setup

To create the necessary databases, launch postgres, then psql, and run the following commands:

<!-- * `\c mf -->
* `\c YOUR_DEFAULT_DATABASE_HERE`
* `DROP DATABASE wildlife_tracker;`
* `DROP DATABASE wildlife_tracker_test;`

* `CREATE DATABASE wildlife_tracker;`
* `\c wildlife_tracker;`
* `CREATE TABLE animals (id serial PRIMARY KEY, type varchar, name varchar, health varchar, age varchar);`
* `CREATE TABLE sightings (id serial PRIMARY KEY, time_sighted timestamp, animal_id int, type varchar, location varchar, ranger_name varchar, ranger_badge int);`
* `CREATE TABLE rangers (id serial PRIMARY KEY, badge_number int, name varchar, email varchar);`
* `CREATE DATABASE wildlife_tracker_test WITH TEMPLATE wildlife_tracker;`

OR

* _Clone the repository_
* _Install [postgres](https://www.learnhowtoprogram.com/java/database-basics/installing-postgres-300b6a5b-7e65-4c23-b024-3d9e22dc5fe9) and [gradle](https://www.learnhowtoprogram.com/java/behavior-driven-development-with-java-604c2c27-3431-444d-8047-2fb947d022c6/gradle-and-project-dependencies) _
* _Set up a terminal session and run `postgres`_
* _Set up a second terminal session and run `psql`_
* _Create a new database in your psql session by typing,_ `CREATE DATABASE wildlife_tracker;`
* _Connect to the database by typing,_ `\c wildlife_tracker`
* _In another terminal session, navigate to the wildlife-tracker repo directory from the command line, type,_ `psql wildlife_tracker < wildlife_tracker.sql`
* _Run the command `gradle run`_
* _Open browser and go to localhost:4567_

### License

Copyright (c) 2017 **_MIT License_**
