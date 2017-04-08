## Wildlife Tracker

An app for the forest service to track animals for an environmental impact study.

### Description

The Forest Service is considering a proposal from a timber company to clearcut a nearby forest of Douglas Fir. Before this proposal may be approved, they must complete an environmental impact study. This application was developed to allow Rangers to track wildlife sightings in the area.

### Setup

To create the necessary databases, launch postgres, then psql, and run the following commands:

* `\c mf`
<!-- * `\c YOUR_DEFAULT_HERE` -->
* `DROP DATABASE wildlife_tracker;`
* `DROP DATABASE wildlife_tracker_test;`

* `CREATE DATABASE wildlife_tracker;`
* `\c wildlife_tracker;`
* `CREATE TABLE animals (id serial PRIMARY KEY, type varchar, name varchar, health varchar, age varchar);`
* `CREATE TABLE sightings (id serial PRIMARY KEY, time_sighted timestamp, animal_id int, location varchar, ranger_name varchar);`
* `CREATE TABLE rangers (id serial PRIMARY KEY, badge_number int, name varchar, email varchar);`
* `CREATE DATABASE wildlife_tracker_test WITH TEMPLATE wildlife_tracker;`

### License

Copyright (c) 2017 **_MIT License_**
