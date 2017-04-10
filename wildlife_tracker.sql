--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: animals; Type: TABLE; Schema: public; Owner: mf
--

CREATE TABLE animals (
    id integer NOT NULL,
    type character varying,
    name character varying,
    health character varying,
    age character varying
);


ALTER TABLE animals OWNER TO mf;

--
-- Name: animals_id_seq; Type: SEQUENCE; Schema: public; Owner: mf
--

CREATE SEQUENCE animals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE animals_id_seq OWNER TO mf;

--
-- Name: animals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mf
--

ALTER SEQUENCE animals_id_seq OWNED BY animals.id;


--
-- Name: rangers; Type: TABLE; Schema: public; Owner: mf
--

CREATE TABLE rangers (
    id integer NOT NULL,
    badge_number integer,
    name character varying,
    email character varying
);


ALTER TABLE rangers OWNER TO mf;

--
-- Name: rangers_id_seq; Type: SEQUENCE; Schema: public; Owner: mf
--

CREATE SEQUENCE rangers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rangers_id_seq OWNER TO mf;

--
-- Name: rangers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mf
--

ALTER SEQUENCE rangers_id_seq OWNED BY rangers.id;


--
-- Name: sightings; Type: TABLE; Schema: public; Owner: mf
--

CREATE TABLE sightings (
    id integer NOT NULL,
    time_sighted timestamp without time zone,
    animal_id integer,
    type character varying,
    location character varying,
    ranger_name character varying,
    ranger_badge integer
);


ALTER TABLE sightings OWNER TO mf;

--
-- Name: sightings_id_seq; Type: SEQUENCE; Schema: public; Owner: mf
--

CREATE SEQUENCE sightings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sightings_id_seq OWNER TO mf;

--
-- Name: sightings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mf
--

ALTER SEQUENCE sightings_id_seq OWNED BY sightings.id;


--
-- Name: animals id; Type: DEFAULT; Schema: public; Owner: mf
--

ALTER TABLE ONLY animals ALTER COLUMN id SET DEFAULT nextval('animals_id_seq'::regclass);


--
-- Name: rangers id; Type: DEFAULT; Schema: public; Owner: mf
--

ALTER TABLE ONLY rangers ALTER COLUMN id SET DEFAULT nextval('rangers_id_seq'::regclass);


--
-- Name: sightings id; Type: DEFAULT; Schema: public; Owner: mf
--

ALTER TABLE ONLY sightings ALTER COLUMN id SET DEFAULT nextval('sightings_id_seq'::regclass);


--
-- Data for Name: animals; Type: TABLE DATA; Schema: public; Owner: mf
--

COPY animals (id, type, name, health, age) FROM stdin;
1	endangered	Snow leopard	Healthy	Newborn
\.


--
-- Name: animals_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mf
--

SELECT pg_catalog.setval('animals_id_seq', 2, true);


--
-- Data for Name: rangers; Type: TABLE DATA; Schema: public; Owner: mf
--

COPY rangers (id, badge_number, name, email) FROM stdin;
6	12	Mark	mark.aaron.fisher@gmail.com
\.


--
-- Name: rangers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mf
--

SELECT pg_catalog.setval('rangers_id_seq', 6, true);


--
-- Data for Name: sightings; Type: TABLE DATA; Schema: public; Owner: mf
--

COPY sightings (id, time_sighted, animal_id, type, location, ranger_name, ranger_badge) FROM stdin;
1	2017-04-09 20:32:00.641907	1	EndangeredAnimal	Lake	Mark	12
8	2017-04-09 21:58:42.055854	1	EndangeredAnimal	Mountain	Mark	12
3	2017-04-09 21:39:47.245367	1	EndangeredAnimal	Home	Philip	12
\.


--
-- Name: sightings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mf
--

SELECT pg_catalog.setval('sightings_id_seq', 8, true);


--
-- Name: animals animals_pkey; Type: CONSTRAINT; Schema: public; Owner: mf
--

ALTER TABLE ONLY animals
    ADD CONSTRAINT animals_pkey PRIMARY KEY (id);


--
-- Name: rangers rangers_pkey; Type: CONSTRAINT; Schema: public; Owner: mf
--

ALTER TABLE ONLY rangers
    ADD CONSTRAINT rangers_pkey PRIMARY KEY (id);


--
-- Name: sightings sightings_pkey; Type: CONSTRAINT; Schema: public; Owner: mf
--

ALTER TABLE ONLY sightings
    ADD CONSTRAINT sightings_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

