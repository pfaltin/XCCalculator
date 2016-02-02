--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-06-15 08:32:18 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE "RacunarPreleta";
--
-- TOC entry 2929 (class 1262 OID 35103)
-- Name: RacunarPreleta; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "RacunarPreleta" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


ALTER DATABASE "RacunarPreleta" OWNER TO postgres;

\connect "RacunarPreleta"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2930 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 188 (class 3079 OID 12616)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2932 (class 0 OID 0)
-- Dependencies: 188
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 207 (class 1255 OID 50564)
-- Name: dizanjetds(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION dizanjetds(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS double precision
    LANGUAGE sql
    AS $$


WITH 
goreDesno AS (
SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabazegd, ( (203073*(temperaturazraka/10 - temperaturarose/10)*122)/156250000-0.0508) AS dizanjegd, (ceil(lat)-gsirina) AS xgd,(ceil(lon)-gduljina) AS ygd
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE lat=ceil(gsirina) AND lon= ceil(gduljina)
AND (vrijeme,interval '2 hours') OVERLAPS (dansat+sat ,interval '2 hours')
AND tlak=10000 ),
goreLijevo AS (
SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabazegl, ( (203073*(temperaturazraka/10 - temperaturarose/10)*122)/156250000-0.0508) AS dizanjegl, (ceil(lat)-gsirina) AS xgl,(gduljina-floor(lon)) AS ygl
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE lat=ceil(gsirina) AND lon= floor(gduljina)
AND (vrijeme,interval '2 hours') OVERLAPS (dansat+sat ,interval '2 hours')
AND tlak=10000 ),


doljeDesno AS (
SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabazedd, ( (203073*(temperaturazraka/10 - temperaturarose/10)*122)/156250000-0.0508) AS dizanjedd, (gsirina-floor(lat)) AS xdd, (ceil(lon)-gduljina) AS ydd
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE lat=floor(gsirina) AND lon= ceil(gduljina)
AND (vrijeme,interval '2 hours') OVERLAPS (dansat+sat ,interval '2 hours')
AND tlak=10000 ),

doljeLijevo AS (
SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabazedl, ( (203073*(temperaturazraka/10 - temperaturarose/10)*122)/156250000-0.0508) AS dizanjedl, (gsirina-floor(lat)) AS xdl, (gduljina-floor(lon)) AS ydl
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE lat=floor(gsirina) AND lon= floor(gduljina)
AND (vrijeme,interval '2 hours') OVERLAPS (dansat+sat ,interval '2 hours')
AND tlak=10000 ),

dgd AS (SELECT (sqrt(xgd*xgd+ygd*ygd))AS duljgd FROM goreDesno),
dgl AS (SELECT (sqrt(xgl*xgl+ygl*ygl))AS duljgl FROM goreLijevo),
ddd AS (SELECT (sqrt(xdd*xdd+ydd*ydd))AS duljdd FROM doljeDesno),
ddl AS (SELECT (sqrt(xdl*xdl+ydl*ydl))AS duljdl FROM doljeLijevo)

SELECT 
((dizanjegd/duljgd)+(dizanjegl/duljgl)+(dizanjedd/duljdd)+(dizanjedl/duljdl))/((1/duljgd)+(1/duljgl)+(1/duljdd)+(1/duljdl)) AS dizanjetocka

FROM goreDesno, goreLijevo, doljeDesno, doljeLijevo, dgd, dgl, ddd, ddl; 


$$;


ALTER FUNCTION public.dizanjetds(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 175 (class 1259 OID 35359)
-- Name: slojzraka; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE slojzraka (
    id_sloj integer NOT NULL,
    id_tockameteo bigint NOT NULL,
    tlak integer,
    visina integer,
    temperaturazraka integer,
    temperaturarose integer,
    vjetarsmjer integer,
    vjetarbrzina integer
);


ALTER TABLE public.slojzraka OWNER TO postgres;

--
-- TOC entry 201 (class 1255 OID 50515)
-- Name: meteotockasat2(double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION meteotockasat2(latitude double precision, longitude double precision) RETURNS slojzraka
    LANGUAGE sql IMMUTABLE STRICT
    AS $$
SELECT id_sloj, slojzraka.id_tockameteo,tlak, visina,  temperaturazraka, temperaturarose, vjetarsmjer, vjetarbrzina 
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (floor(lat)= floor(latitude)OR ceil(lat)=ceil(latitude))AND (floor(lon)=floor(longitude)OR ceil(lat)= ceil(longitude))
AND (vrijeme,interval '1 hours') OVERLAPS (DATE '2014-05-02' + interval '14 hours' ,interval '2 hours');
$$;


ALTER FUNCTION public.meteotockasat2(latitude double precision, longitude double precision) OWNER TO postgres;

--
-- TOC entry 205 (class 1255 OID 50540)
-- Name: meteotockasat4(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION meteotockasat4(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS numeric
    LANGUAGE sql
    AS $$SELECT   ( (203073*(temperaturazraka/10 - temperaturarose/10)*122)/156250000-0.0508) AS dizanje
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (vrijeme,interval '1 hours') OVERLAPS (dansat + sat  ,interval '2 hours')


AND tlak=10000;
$$;


ALTER FUNCTION public.meteotockasat4(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 210 (class 1255 OID 50653)
-- Name: pocetak(double precision, double precision, date, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION pocetak(gsirina double precision, gduljina double precision, dansat date, mindizanje double precision) RETURNS TABLE(sat double precision, visinabaze integer, dizanje numeric)
    LANGUAGE plpgsql
    AS $$
DECLARE
   sirina double precision;
   duljina double precision;
BEGIN
		IF  gsirina-floor(gsirina) < 0.25 THEN SELECT floor(gsirina) INTO STRICT sirina;
			END IF;
		IF  gsirina-floor(gsirina) BETWEEN 0.25 AND 0.75 THEN SELECT floor(gsirina)+0.5 INTO STRICT sirina;
			END IF;
		IF  gsirina-floor(gsirina) > 0.75 THEN SELECT ceil(gsirina) INTO STRICT sirina;
			END IF;
		IF  gduljina-floor(gduljina) < 0.25 THEN SELECT floor(gduljina) INTO STRICT duljina;
			END IF;
		IF  gduljina-floor(gduljina) BETWEEN 0.25 AND 0.75 THEN SELECT floor(gduljina)+0.5 INTO STRICT duljina;
			END IF;
		IF  gduljina-floor(gduljina) > 0.75 THEN SELECT ceil(gduljina) INTO STRICT duljina;
			END IF;
		RETURN QUERY WITH 
		pocetakdana AS(
			SELECT EXTRACT(HOUR FROM vrijeme) AS sati ,
			 ((temperaturazraka/10 - temperaturarose/10)*122) AS visinad, 
			 ( (-10.0 + (0.078* ((temperaturazraka/10 - temperaturarose/10)*122)*3.28))*0.00508) AS dizanjeb
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina  AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat  ,interval '24 hours')
			AND tlak=10000 )

			SELECT sati, visinad, dizanjeb FROM pocetakdana WHERE dizanjeb >mindizanje*2 ORDER BY pocetakdana.sati LIMIT 1;

END
$$;


ALTER FUNCTION public.pocetak(gsirina double precision, gduljina double precision, dansat date, mindizanje double precision) OWNER TO postgres;

--
-- TOC entry 209 (class 1255 OID 50628)
-- Name: prognoza(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION prognoza(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS TABLE(sirina double precision, duljina double precision, dizanjebr numeric, dizanjevis integer, vjetarbr integer, vjetarsm integer, vjertcb integer, vjerstratus integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
   sirina double precision;
   duljina double precision;
BEGIN
		IF  gsirina-floor(gsirina) < 0.25 THEN SELECT floor(gsirina) INTO STRICT sirina;
			END IF;
		IF  gsirina-floor(gsirina) BETWEEN 0.25 AND 0.75 THEN SELECT floor(gsirina)+0.5 INTO STRICT sirina;
			END IF;
		IF  gsirina-floor(gsirina) > 0.75 THEN SELECT ceil(gsirina) INTO STRICT sirina;
			END IF;
		IF  gduljina-floor(gduljina) < 0.25 THEN SELECT floor(gduljina) INTO STRICT duljina;
			END IF;
		IF  gduljina-floor(gduljina) BETWEEN 0.25 AND 0.75 THEN SELECT floor(gduljina)+0.5 INTO STRICT duljina;
			END IF;
		IF  gduljina-floor(gduljina) > 0.75 THEN SELECT ceil(gduljina) INTO STRICT duljina;
			END IF;
		RETURN QUERY WITH 
		baza AS(
        SELECT ((temperaturazraka/10 - temperaturarose/10)*122) AS dizanjevisina
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina  AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=10000 ),
		dizanje AS(	
        SELECT ( (-10.0 + (0.078* ((temperaturazraka/10 - temperaturarose/10)*122)*3.28))*0.00508) AS dizanjebrzina
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=10000),
		vjb AS(	
        SELECT (vjetarbrzina) AS vjbr
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=9000 ),
		vjs AS(	
        SELECT (vjetarsmjer) AS vjsm
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=9000 ),		
		stratus AS(	
			SELECT  (temperaturazraka - temperaturarose) AS vjerojatnoststratus
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina AND (vrijeme,interval '1 hours') OVERLAPS (dansat + interval '10 hours' ,interval '8 hours')
            AND tlak>5000
			ORDER BY vjerojatnoststratus LIMIT 1), 	
		cb AS(	
        SELECT  (tz850 -tz500) + tr850 - (tz700-tr700) AS vjerojatnostcb FROM
			(SELECT temperaturazraka tz850, temperaturarose TR850
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina
			AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=8500 ) AS t850,
			(SELECT temperaturazraka TZ700, temperaturarose TR700
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina
			AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=7000 ) AS t700,
			(SELECT temperaturazraka TZ500
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = sirina AND lon = duljina
			AND (vrijeme,interval '1 hours') OVERLAPS (dansat + sat ,interval '2 hours')
			AND tlak=5000 ) AS t500)
			SELECT sirina, duljina, dizanjebrzina, dizanjevisina, vjbr, vjsm, vjerojatnoststratus, vjerojatnostcb FROM baza, dizanje, vjb, vjs, stratus,cb ;

END
$$;


ALTER FUNCTION public.prognoza(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 208 (class 1255 OID 50575)
-- Name: test1(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION test1(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS TABLE(visinabaze integer, sirina double precision, duljina double precision)
    LANGUAGE plpgsql
    AS $$
DECLARE
   sirina double precision;
   duljina double precision;
BEGIN
		SELECT gsirina-floor(gsirina) INTO STRICT sirina;
		IF sirina < 0.5 THEN
        RETURN QUERY SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabaze, lat , lon
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = floor(gsirina) 
            AND lon = 16.00 
            AND tlak = 10000 AND (vrijeme,interval '1 hours') OVERLAPS (DATE '2014-05-27' + INTERVAL '12:00'  ,interval '2 hours');
        ELSE
		RETURN QUERY SELECT  ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabaze, lat , lon
			FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
            WHERE lat = ceil(gsirina)
            AND lon = 16.00 
            AND tlak = 10000 AND (vrijeme,interval '1 hours') OVERLAPS (DATE '2014-05-27' + INTERVAL '12:00'  ,interval '2 hours');
		END IF;
END
$$;


ALTER FUNCTION public.test1(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 203 (class 1255 OID 50560)
-- Name: visinatds(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION visinatds(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS integer
    LANGUAGE sql
    AS $$
SELECT ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabaze
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (vrijeme,interval '1 hours') OVERLAPS (dansat + sat  ,interval '2 hours')
AND lat= gsirina
AND lon= gduljina
AND tlak=10000;
$$;


ALTER FUNCTION public.visinatds(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 202 (class 1255 OID 50543)
-- Name: visinatockadansat(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION visinatockadansat(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS integer
    LANGUAGE sql
    AS $$
SELECT ((temperaturazraka/10 - temperaturarose/10)*122) AS visinabaze
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (vrijeme,interval '1 hours') OVERLAPS (dansat + sat  ,interval '2 hours')
AND lat= gsirina
AND lon= gduljina
AND tlak=10000;
$$;


ALTER FUNCTION public.visinatockadansat(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 206 (class 1255 OID 50562)
-- Name: vjetarbrzinatds(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION vjetarbrzinatds(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS integer
    LANGUAGE sql
    AS $$
SELECT vjetarbrzina
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (vrijeme,interval '1 hours') OVERLAPS (dansat + sat  ,interval '2 hours')
AND lat= gsirina
AND lon= gduljina
AND tlak=10000;
$$;


ALTER FUNCTION public.vjetarbrzinatds(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 204 (class 1255 OID 50561)
-- Name: vjetarsmjertds(double precision, double precision, date, interval); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION vjetarsmjertds(gsirina double precision, gduljina double precision, dansat date, sat interval) RETURNS integer
    LANGUAGE sql
    AS $$
SELECT vjetarsmjer
FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo
WHERE (vrijeme,interval '1 hours') OVERLAPS (dansat + sat  ,interval '2 hours')
AND lat= gsirina
AND lon= gduljina
AND tlak=10000;
$$;


ALTER FUNCTION public.vjetarsmjertds(gsirina double precision, gduljina double precision, dansat date, sat interval) OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 35382)
-- Name: ruta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ruta (
    id_ruta bigint NOT NULL,
    naziv_rute character varying(32) DEFAULT NULL::character varying,
    opis character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE public.ruta OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 35380)
-- Name: ruta_id_ruta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ruta_id_ruta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ruta_id_ruta_seq OWNER TO postgres;

--
-- TOC entry 2933 (class 0 OID 0)
-- Dependencies: 178
-- Name: ruta_id_ruta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ruta_id_ruta_seq OWNED BY ruta.id_ruta;


--
-- TOC entry 181 (class 1259 OID 35396)
-- Name: rutapreletaizracun; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rutapreletaizracun (
    id_rutapreletaizracun integer NOT NULL,
    naziv_rute character varying(32) DEFAULT NULL::character varying,
    opis character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE public.rutapreletaizracun OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 35394)
-- Name: rutapreletaizracun_id_rutapreletaizracun_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rutapreletaizracun_id_rutapreletaizracun_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rutapreletaizracun_id_rutapreletaizracun_seq OWNER TO postgres;

--
-- TOC entry 2934 (class 0 OID 0)
-- Dependencies: 180
-- Name: rutapreletaizracun_id_rutapreletaizracun_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rutapreletaizracun_id_rutapreletaizracun_seq OWNED BY rutapreletaizracun.id_rutapreletaizracun;


--
-- TOC entry 187 (class 1259 OID 50718)
-- Name: rutatocke; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rutatocke (
    id integer NOT NULL,
    id_tocka integer NOT NULL,
    id_ruta integer NOT NULL,
    napomena character varying(64)
);


ALTER TABLE public.rutatocke OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 50716)
-- Name: rutatocke_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rutatocke_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rutatocke_id_seq OWNER TO postgres;

--
-- TOC entry 2935 (class 0 OID 0)
-- Dependencies: 186
-- Name: rutatocke_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rutatocke_id_seq OWNED BY rutatocke.id;


--
-- TOC entry 174 (class 1259 OID 35357)
-- Name: slojzraka_id_sloj_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE slojzraka_id_sloj_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.slojzraka_id_sloj_seq OWNER TO postgres;

--
-- TOC entry 2936 (class 0 OID 0)
-- Dependencies: 174
-- Name: slojzraka_id_sloj_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE slojzraka_id_sloj_seq OWNED BY slojzraka.id_sloj;


--
-- TOC entry 171 (class 1259 OID 35316)
-- Name: tocka; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tocka (
    id_tocka integer NOT NULL,
    naziv character varying(32) NOT NULL,
    napomena character varying(32) DEFAULT NULL::character varying,
    lat double precision,
    lon double precision,
    elev integer
);


ALTER TABLE public.tocka OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 35314)
-- Name: tocka_id_tocka_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tocka_id_tocka_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tocka_id_tocka_seq OWNER TO postgres;

--
-- TOC entry 2937 (class 0 OID 0)
-- Dependencies: 170
-- Name: tocka_id_tocka_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tocka_id_tocka_seq OWNED BY tocka.id_tocka;


--
-- TOC entry 183 (class 1259 OID 50656)
-- Name: tockaizracunapreleta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tockaizracunapreleta (
    id_tockaizp integer NOT NULL,
    id_rutapreletaizracun integer NOT NULL,
    naziv character varying(32) NOT NULL,
    lat double precision,
    lon double precision,
    elev integer,
    dizanjebrzina real,
    dizanjevisina real,
    vjetarbrzina real,
    vetarsmjer integer,
    brzinaetapa real,
    brzinastart real,
    vjerojatnostcb integer,
    vjerojatnoststratus integer,
    vrijemedolaska integer,
    trajanjeetapa integer,
    trajanjeodstart integer
);


ALTER TABLE public.tockaizracunapreleta OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 50654)
-- Name: tockaizracunapreleta_id_tockaizp_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tockaizracunapreleta_id_tockaizp_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tockaizracunapreleta_id_tockaizp_seq OWNER TO postgres;

--
-- TOC entry 2938 (class 0 OID 0)
-- Dependencies: 182
-- Name: tockaizracunapreleta_id_tockaizp_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tockaizracunapreleta_id_tockaizp_seq OWNED BY tockaizracunapreleta.id_tockaizp;


--
-- TOC entry 173 (class 1259 OID 35348)
-- Name: tockameteo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tockameteo (
    id_tockameteo bigint NOT NULL,
    naziv character varying(32) NOT NULL,
    lat double precision,
    lon double precision,
    elev integer,
    vrijeme timestamp without time zone
);


ALTER TABLE public.tockameteo OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 35346)
-- Name: tockameteo_id_tockameteo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tockameteo_id_tockameteo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tockameteo_id_tockameteo_seq OWNER TO postgres;

--
-- TOC entry 2939 (class 0 OID 0)
-- Dependencies: 172
-- Name: tockameteo_id_tockameteo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tockameteo_id_tockameteo_seq OWNED BY tockameteo.id_tockameteo;


--
-- TOC entry 185 (class 1259 OID 50683)
-- Name: tockaokretna; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tockaokretna (
    id_oktocka integer NOT NULL,
    wpname character varying(32),
    code character varying(32),
    country character varying(32),
    lat character varying(32),
    lon character varying(32),
    elev character varying(32),
    style character varying(32),
    rwdir character varying(32),
    rwlen character varying(32),
    freq character varying(32),
    descr character varying(500)
);


ALTER TABLE public.tockaokretna OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 50681)
-- Name: tockaokretna_id_oktocka_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tockaokretna_id_oktocka_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tockaokretna_id_oktocka_seq OWNER TO postgres;

--
-- TOC entry 2940 (class 0 OID 0)
-- Dependencies: 184
-- Name: tockaokretna_id_oktocka_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tockaokretna_id_oktocka_seq OWNED BY tockaokretna.id_oktocka;


--
-- TOC entry 177 (class 1259 OID 35367)
-- Name: zrakoplovi; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE zrakoplovi (
    id_zrakoplovi integer NOT NULL,
    zrakoplov_naziv character varying(32) DEFAULT NULL::character varying,
    opterecenje real,
    balast integer,
    postotakbalasta integer,
    min_brzina real,
    polara_a double precision,
    polara_b double precision,
    polara_c double precision,
    brzina_v1 real,
    brzina_v2 real,
    brzina_v3 real,
    brzina_w1 real,
    brzina_w2 real,
    brzina_w3 real,
    zrakoplov_foto character varying(32) DEFAULT NULL::character varying,
    zrakoplov_opis character varying(500) DEFAULT NULL::character varying
);


ALTER TABLE public.zrakoplovi OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 35365)
-- Name: zrakoplovi_id_zrakoplovi_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE zrakoplovi_id_zrakoplovi_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.zrakoplovi_id_zrakoplovi_seq OWNER TO postgres;

--
-- TOC entry 2941 (class 0 OID 0)
-- Dependencies: 176
-- Name: zrakoplovi_id_zrakoplovi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE zrakoplovi_id_zrakoplovi_seq OWNED BY zrakoplovi.id_zrakoplovi;


--
-- TOC entry 2764 (class 2604 OID 50721)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rutatocke ALTER COLUMN id SET DEFAULT nextval('rutatocke_id_seq'::regclass);


--
-- TOC entry 2753 (class 2604 OID 35362)
-- Name: id_sloj; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY slojzraka ALTER COLUMN id_sloj SET DEFAULT nextval('slojzraka_id_sloj_seq'::regclass);


--
-- TOC entry 2751 (class 2604 OID 35319)
-- Name: id_tocka; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tocka ALTER COLUMN id_tocka SET DEFAULT nextval('tocka_id_tocka_seq'::regclass);


--
-- TOC entry 2762 (class 2604 OID 50659)
-- Name: id_tockaizp; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tockaizracunapreleta ALTER COLUMN id_tockaizp SET DEFAULT nextval('tockaizracunapreleta_id_tockaizp_seq'::regclass);


--
-- TOC entry 2763 (class 2604 OID 50686)
-- Name: id_oktocka; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tockaokretna ALTER COLUMN id_oktocka SET DEFAULT nextval('tockaokretna_id_oktocka_seq'::regclass);


--
-- TOC entry 2754 (class 2604 OID 35370)
-- Name: id_zrakoplovi; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY zrakoplovi ALTER COLUMN id_zrakoplovi SET DEFAULT nextval('zrakoplovi_id_zrakoplovi_seq'::regclass);


--
-- TOC entry 2916 (class 0 OID 35382)
-- Dependencies: 179
-- Data for Name: ruta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO ruta (id_ruta, naziv_rute, opis) VALUES (436189381, 'Grobnik-Karlovac-Gospic', 'null');
INSERT INTO ruta (id_ruta, naziv_rute, opis) VALUES (949172797, 'Buzet Poreč Vozilići', 'null');


--
-- TOC entry 2942 (class 0 OID 0)
-- Dependencies: 178
-- Name: ruta_id_ruta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ruta_id_ruta_seq', 1, false);


--
-- TOC entry 2918 (class 0 OID 35396)
-- Dependencies: 181
-- Data for Name: rutapreletaizracun; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO rutapreletaizracun (id_rutapreletaizracun, naziv_rute, opis) VALUES (1348694544, 'AERODROM-GOSPIC_2014-6-5', 'Zrakoplov ASW 15 metoda:4');
INSERT INTO rutapreletaizracun (id_rutapreletaizracun, naziv_rute, opis) VALUES (949506813, 'BUZET-BUZET_2014-6-5', 'Zrakoplov Advance Omega 7 metoda:4');


--
-- TOC entry 2943 (class 0 OID 0)
-- Dependencies: 180
-- Name: rutapreletaizracun_id_rutapreletaizracun_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('rutapreletaizracun_id_rutapreletaizracun_seq', 1, false);


--
-- TOC entry 2924 (class 0 OID 50718)
-- Dependencies: 187
-- Data for Name: rutatocke; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (21, 38567, 436189381, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (22, 38601, 436189381, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (23, 38592, 436189381, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (24, 38567, 436189381, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (25, 38579, 949172797, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (26, 38636, 949172797, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (27, 38667, 949172797, NULL);
INSERT INTO rutatocke (id, id_tocka, id_ruta, napomena) VALUES (28, 38579, 949172797, NULL);


--
-- TOC entry 2944 (class 0 OID 0)
-- Dependencies: 186
-- Name: rutatocke_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('rutatocke_id_seq', 28, true);


--
-- TOC entry 2912 (class 0 OID 35359)
-- Dependencies: 175
-- Data for Name: slojzraka; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2945 (class 0 OID 0)
-- Dependencies: 174
-- Name: slojzraka_id_sloj_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('slojzraka_id_sloj_seq', 1227283, true);


--
-- TOC entry 2908 (class 0 OID 35316)
-- Dependencies: 171
-- Data for Name: tocka; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38567, 'AERODROM', 'AERODR ', 45.3666666666666671, 14.5118166666666664, 298);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38568, 'BABINP', 'BABINP ', 44.8333333333333357, 15.4964833333333338, 763);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38569, 'BASKAS', 'BASKAS ', 44.9666666666666686, 14.7566666666666659, 317);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38570, 'BIJELO', 'BIJELO ', 44.68333333333333, 15.7818166666666659, 608);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38571, 'BOLJUN', 'BOLJUN ', 45.2999999999999972, 14.121316666666667, 211);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38572, 'BREST', 'BREST ', 45.31666666666667, 14.1622166666666658, 589);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38573, 'BRESTO', 'BRESTO ', 45.1333333333333329, 14.2238000000000007, 90);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38574, 'BRESTS', 'BRESTS ', 45.3333333333333357, 14.1413833333333336, 451);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38575, 'BREZE', 'BREZE ', 45.18333333333333, 14.8710666666666675, 797);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38576, 'BRGUDS', 'BRGUDS ', 45.2333333333333343, 14.1948000000000008, 667);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38577, 'BRIBIR', 'BRIBIR ', 45.1499999999999986, 14.7599999999999998, 99);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38578, 'BRINJE', 'BRINJE ', 44.9833333333333343, 15.1235166666666672, 468);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38579, 'BUZET', 'BUZET ', 45.3999999999999986, 13.9708333333333332, 80);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38580, 'CEPIC', 'CEPIC ', 45.2000000000000028, 14.1362833333333331, 77);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38581, 'CRIKVE', 'CRIKVE ', 45.1666666666666643, 14.6913833333333326, 0);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38582, 'CRKVAJ', 'CRKVAJ ', 45.3833333333333329, 14.4498499999999996, 320);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38583, 'CRNILU', 'CRNILU ', 45.4166666666666643, 14.7054833333333335, 720);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38584, 'CUNA', 'CUNA ', 45.43333333333333, 14.4789666666666665, 1112);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38585, 'DELNIC', 'DELNIC ', 45.3999999999999986, 14.8039166666666659, 693);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38586, 'DRAGUC', 'DRAGUC ', 45.31666666666667, 14.0083333333333329, 318);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38587, 'DREZNI', 'DREZNI ', 45.1333333333333329, 15.0902999999999992, 494);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38588, 'DRIVEN', 'DRIVEN ', 45.2333333333333343, 14.6465999999999994, 117);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38589, 'GENSTO', 'GENSTO ', 45.3333333333333357, 15.3785500000000006, 192);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38590, 'GOLOVI', 'GOLOVI ', 45.18333333333333, 14.2151999999999994, 580);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38591, 'GOMANC', 'GOMANC ', 45.4833333333333343, 14.4214500000000001, 903);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38592, 'GOSPIC', 'GOSPIC ', 44.5333333333333314, 15.3757666666666672, 561);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38593, 'GRACAC', 'GRACAC ', 44.2833333333333314, 15.8496333333333332, 550);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38594, 'GRACIS', 'GRACIS ', 45.2166666666666686, 14.0147166666666667, 360);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38595, 'GRIZAN', 'GRIZAN ', 45.2000000000000028, 14.7182666666666666, 209);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38596, 'IBISTR', 'IBISTR ', 45.5499999999999972, 14.2453000000000003, 415);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38597, 'ICICI', 'ICICI ', 45.2999999999999972, 14.2860166666666668, 56);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38598, 'JAZVIN', 'JAZVIN ', 45.3999999999999986, 14.5508333333333333, 841);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38599, 'JOSIPD', 'JOSIPD 29', 45.18333333333333, 15.2840166666666661, 338);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38600, 'KAMENJ', 'KAMENJ 29', 45.3666666666666671, 14.5549999999999997, 837);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38601, 'KARLOV', 'KARLOV 29', 45.4833333333333343, 15.5557999999999996, 111);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38602, 'KICHEL', 'KICHEL 29', 45.4166666666666643, 14.4232166666666668, 582);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38603, 'KLEK', 'KLEK 29', 45.25, 15.1442166666666669, 1058);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38604, 'KOBILJ', 'KOBILJ 29', 45.25, 14.7146500000000007, 1026);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38605, 'KOVK', 'KOVK 29', 45.8833333333333329, 13.9698833333333337, 904);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38606, 'KOVKST', 'KOVKST 29', 45.8833333333333329, 13.9590333333333341, 857);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38607, 'KOZLEK', 'KOZLEK 29', 45.5333333333333314, 14.3215666666666674, 918);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38608, 'KOZLJA', 'KOZLJA 29', 45.1666666666666643, 14.1792333333333325, 33);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38609, 'KRIZIS', 'KRIZIS 29', 45.25, 14.6155500000000007, 220);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38610, 'LANISC', 'LANISC 29', 45.3999999999999986, 14.1158333333333328, 522);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38611, 'LAZAC', 'LAZAC 29', 45.43333333333333, 14.600833333333334, 1090);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38612, 'LICSLE', 'LICSLE 29', 45.2666666666666657, 14.7528666666666659, 722);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38613, 'LUPOGL', 'LUPOGL 29', 45.3500000000000014, 14.1094500000000007, 402);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38614, 'MERAG', 'MERAG 29', 44.9666666666666686, 14.4486000000000008, 3);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38615, 'MOTOVU', 'MOTOVU 29', 45.3333333333333357, 13.8341666666666665, 257);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38616, 'MUNE', 'MUNE 29', 45.4500000000000028, 14.1613833333333332, 688);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38617, 'N-VAS', 'N-VAS 29', 45.2333333333333343, 14.1684000000000001, 42);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38618, 'NEBESA', 'NEBESA 29', 45.43333333333333, 14.5167999999999999, 1048);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38619, 'NOVVIN', 'NOVVIN 29', 45.1166666666666671, 14.7919499999999999, 18);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38620, 'NUGLA', 'NUGLA 29', 45.3999999999999986, 14.024166666666666, 379);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38621, 'OBRUC', 'OBRUC 29', 45.4500000000000028, 14.4657166666666672, 1288);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38622, 'ORLJAK', 'ORLJAK 29', 45.3999999999999986, 14.1274999999999995, 892);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38623, 'OTOCAC', 'OTOCAC 29', 44.8666666666666671, 15.237166666666667, 454);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38624, 'PAZIN', 'PAZIN 29', 45.2333333333333343, 13.9427833333333329, 284);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38625, 'PEHLIN', 'PEHLIN 29', 45.3500000000000014, 14.4030500000000004, 261);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38626, 'PERUSI', 'PERUSI 29', 44.6333333333333329, 15.3896499999999996, 573);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38627, 'PETEHI', 'PETEHI 29', 45.06666666666667, 13.9661166666666663, 302);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38628, 'PICAN', 'PICAN 29', 45.2000000000000028, 14.0405499999999996, 272);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38629, 'PIVKA', 'PIVKA 29', 45.6666666666666643, 14.1966833333333327, 554);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38630, 'PLATAK', 'PLATAK 29', 45.4166666666666643, 14.5645000000000007, 1084);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38631, 'PLESA', 'PLESA 29', 45.7666666666666657, 14.0547666666666675, 1213);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38632, 'PLITVI', 'PLITVI 29', 44.8666666666666671, 15.6208500000000008, 606);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38633, 'PLOMIN', 'PLOMIN 29', 45.1333333333333329, 14.1797166666666659, 75);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38634, 'PODNAN', 'PODNAN 29', 45.7833333333333314, 13.9709833333333329, 174);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38635, 'POKLON', 'POKLON 29', 45.2999999999999972, 14.2050000000000001, 928);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38636, 'POREC', 'POREC 29', 45.2166666666666686, 13.5941666666666663, 0);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38637, 'POROZI', 'POROZI 29', 45.1166666666666671, 14.2870833333333334, 0);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38638, 'RASPAD', 'RASPAD 29', 45.4166666666666643, 13.9963833333333341, 385);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38639, 'RAVGOR', 'RAVGOR 29', 45.3666666666666671, 14.9494833333333332, 817);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38640, 'ROC', 'ROC 29', 45.3833333333333329, 14.046383333333333, 254);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38641, 'ROPCIT', 'ROPCIT 29', 45.2166666666666686, 14.659416666666667, 67);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38642, 'RSKURI', 'RSKURI 29', 45.3333333333333357, 14.421383333333333, 178);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38643, 'SEDLOU', 'SEDLOU 29', 45.2666666666666657, 14.2019833333333327, 1138);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38644, 'SEMIC', 'SEMIC 29', 45.3833333333333329, 14.5162666666666667, 366);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38645, 'SISOL', 'SISOL 29', 45.1666666666666643, 14.2015166666666666, 569);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38646, 'SLJEME', 'SLJEME 29', 45.3999999999999986, 14.5698166666666662, 1193);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38647, 'SLJUNA', 'SLJUNA 29', 45.3999999999999986, 14.4841666666666669, 319);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38648, 'SLUNJ', 'SLUNJ 29', 45.1000000000000014, 15.5848166666666668, 262);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38649, 'SNEZNI', 'SNEZNI 29', 45.5833333333333357, 14.4471833333333333, 1683);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38650, 'SNJEZN', 'SNJEZN 29', 45.43333333333333, 14.5841166666666666, 1405);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38651, 'STARTB', 'STARTB 29', 44.7000000000000028, 15.8114666666666661, 1077);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38652, 'STUDEN', 'STUDEN 29', 45.4166666666666643, 14.3933333333333326, 579);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38653, 'SUHIVR', 'SUHIVR 29', 45.3500000000000014, 14.5189500000000002, 423);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38654, 'SUNGER', 'SUNGER 29', 45.31666666666667, 14.8208333333333329, 804);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38655, 'SVVINC', 'SVVINC 29', 45.0833333333333357, 13.8855500000000003, 308);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38656, 'TRIBLJ', 'TRIBLJ 29', 45.2333333333333343, 14.6816666666666666, 718);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38657, 'TUHOBI', 'TUHOBI 29', 45.3333333333333357, 14.6417000000000002, 1059);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38658, 'UCKA', 'UCKA 29', 45.2833333333333314, 14.2020666666666671, 1209);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38659, 'UDBINA', 'UDBINA 29', 44.5166666666666657, 15.7668166666666671, 811);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38660, 'VELIML', 'VELIML 29', 45.3999999999999986, 13.9354499999999994, 242);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38661, 'VELORU', 'VELORU 29', 44.3500000000000014, 15.4236500000000003, 875);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38662, 'VINKUR', 'VINKUR 29', 44.81666666666667, 13.8605499999999999, 0);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38663, 'VINTIJ', 'VINTIJ 29', 44.8333333333333357, 13.8583333333333325, 7);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38664, 'VIPAVA', 'VIPAVA 29', 45.8333333333333357, 13.9609666666666659, 99);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38665, 'VISEVI', 'VISEVI 29', 45.25, 14.8008833333333332, 1352);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38666, 'VODNJA', 'VODNJA 29', 44.9500000000000028, 13.8577833333333338, 141);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38667, 'VOZILI', 'VOZILI 29', 45.1499999999999986, 14.1659666666666659, 85);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38668, 'VPLIS', 'VPLIS 29', 45.3833333333333329, 14.5905500000000004, 1114);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38669, 'VRBOVS', 'VRBOVS 29', 45.3666666666666671, 15.0695166666666669, 386);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38670, 'WALD', 'WALD 29', 45.2999999999999972, 14.1931833333333337, 847);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38671, 'ZABICE', 'ZABICE 29', 45.5166666666666657, 14.3423999999999996, 437);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38672, 'ZAVOJ', 'ZAVOJ 29', 45.43333333333333, 13.9649999999999999, 392);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38673, 'ZBEVNI', 'ZBEVNI 29', 45.4500000000000028, 14.0152833333333326, 855);
INSERT INTO tocka (id_tocka, naziv, napomena, lat, lon, elev) VALUES (38674, 'ZMINJ', 'ZMINJ 29', 45.1333333333333329, 13.9091666666666658, 349);


--
-- TOC entry 2946 (class 0 OID 0)
-- Dependencies: 170
-- Name: tocka_id_tocka_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tocka_id_tocka_seq', 38674, true);


--
-- TOC entry 2920 (class 0 OID 50656)
-- Dependencies: 183
-- Data for Name: tockaizracunapreleta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (783, 1348694544, 'AERODROM', 45.3666666666666671, 14.5118166666666664, 298, 1.37623453, 1098, 6, 230, 0, 0, 17, 0, 0, 0, 0);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (784, 1348694544, 'KARLOV', 45.4833333333333343, 15.5557999999999996, 111, 1.21767521, 976, 5, 193, 57.203064, 57.203064, 12, 0, 5192, 5192, 5192);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (785, 1348694544, 'GOMANC', 45.4833333333333343, 14.4214500000000001, 903, 1.53479397, 1220, 5, 213, 60.2160072, 58.7095375, 22, 0, 10479, 5287, 10479);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (786, 1348694544, 'GOSPIC', 44.5333333333333314, 15.3757666666666672, 561, 0, 0, 0, 0, 60.0141296, 59.1444016, 0, 0, 18251, 7772, 18251);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (787, 949506813, 'BUZET', 45.3999999999999986, 13.9708333333333332, 80, 1.21767521, 976, 5, 203, 0, 0, 49, 0, 0, 0, 0);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (788, 949506813, 'POREC', 45.2166666666666686, 13.5941666666666663, 0, 1.21767521, 976, 8, 205, 24.2270584, 24.2270584, 54, 0, 5323, 5323, 5323);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (789, 949506813, 'VOZILI', 45.1499999999999986, 14.1659666666666659, 85, 1.05911577, 854, 9, 191, 20.2081909, 22.2176247, 21, 0, 13415, 8092, 13415);
INSERT INTO tockaizracunapreleta (id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vjerojatnostcb, vjerojatnoststratus, vrijemedolaska, trajanjeetapa, trajanjeodstart) VALUES (790, 949506813, 'BUZET', 45.3999999999999986, 13.9708333333333332, 80, 0, 0, 0, 0, 16.5907726, 20.3420067, 0, 0, 20297, 6882, 20297);


--
-- TOC entry 2947 (class 0 OID 0)
-- Dependencies: 182
-- Name: tockaizracunapreleta_id_tockaizp_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tockaizracunapreleta_id_tockaizp_seq', 790, true);


--
-- TOC entry 2910 (class 0 OID 35348)
-- Dependencies: 173
-- Data for Name: tockameteo; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2948 (class 0 OID 0)
-- Dependencies: 172
-- Name: tockameteo_id_tockameteo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tockameteo_id_tockameteo_seq', 1, true);


--
-- TOC entry 2922 (class 0 OID 50683)
-- Dependencies: 185
-- Data for Name: tockaokretna; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2949 (class 0 OID 0)
-- Dependencies: 184
-- Name: tockaokretna_id_oktocka_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tockaokretna_id_oktocka_seq', 1, false);


--
-- TOC entry 2914 (class 0 OID 35367)
-- Dependencies: 177
-- Data for Name: zrakoplovi; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64387, 'Advance Epsilon 6', 3, NULL, NULL, 23, 19.6295999999999999, -11.9296000000000006, 2.86870000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64388, 'Advance Omega 7', 3, NULL, NULL, 26, 17.3684000000000012, -11.8053000000000008, 3.06050000000000022, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64389, 'Advance Sigma 7', 3, NULL, NULL, 26, 20.3383000000000003, -13.3659999999999997, 3.28929999999999989, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64390, 'Antares 18m', 43, NULL, NULL, 72, 1.41999999999999993, -2.33000000000000007, 1.42999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64391, 'Antares 20m', 38, NULL, NULL, 72, 1.16999999999999993, -2, 1.33000000000000007, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64392, 'Apis 13m', 20, NULL, NULL, 57, 1.70999999999999996, -2.43000000000000016, 1.45999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64393, 'ASH 22BLE', 34, NULL, NULL, 72, 1.09000000000000008, -1.58000000000000007, 0.959999999999999964, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64394, 'ASH 25', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64395, 'ASH 25E', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64396, 'ASH 26', 36, NULL, NULL, 72, 1.05000000000000004, -1.41999999999999993, 0.930000000000000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64397, 'ASH 26E', 34, NULL, NULL, 72, 1.45999999999999996, -2.5299999999999998, 1.6399999999999999, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64398, 'ASK 13', 30, NULL, NULL, 72, 3.02000000000000002, -4.36000000000000032, 2.37999999999999989, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64399, 'ASK 21', 30, NULL, NULL, 72, 2.5, -4.08000000000000007, 2.39999999999999991, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64400, 'ASK 23', 30, NULL, NULL, 72, 2.14999999999999991, -3.04000000000000004, 1.73999999999999999, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64401, 'Astir CS', 30, NULL, NULL, 72, 2.04000000000000004, -3.08999999999999986, 1.8600000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64402, 'ASW 15', 30, NULL, NULL, 72, 2.04000000000000004, -2.89999999999999991, 1.65999999999999992, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64403, 'ASW 17', 30, NULL, NULL, 72, 1.27000000000000002, -1.93999999999999995, 1.30000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64404, 'ASW 19', 30, NULL, NULL, 72, 1.65999999999999992, -2.35999999999999988, 1.44999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64405, 'ASW 20', 30, NULL, NULL, 72, 1.17999999999999994, -1.53000000000000003, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64406, 'ASW 20C', 30, NULL, NULL, 72, 1.02000000000000002, -1.26000000000000001, 0.910000000000000031, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64407, 'ASW 22', 30, NULL, NULL, 72, 1.31000000000000005, -1.8600000000000001, 1.05000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64408, 'ASW 24', 31, NULL, NULL, 72, 1.58000000000000007, -2.20999999999999996, 1.27000000000000002, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64409, 'ASW 27', 35, NULL, NULL, 72, 1.28000000000000003, -2.20999999999999996, 1.53000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64410, 'ASW 28', 29, NULL, NULL, 72, 1.3899999999999999, -1.8600000000000001, 1.10000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64411, 'Atos', 10, NULL, NULL, 29, 7.73000000000000043, -5.71999999999999975, 1.78000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64412, 'Atos VX', 9, NULL, NULL, 29, 7.84999999999999964, -5.87999999999999989, 1.71999999999999997, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64413, 'Carat', 40, NULL, NULL, 72, 2.33000000000000007, -4.09999999999999964, 2.60000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64414, 'Cir.L265', 28, NULL, NULL, 72, 1.29000000000000004, -1.3600000000000001, 0.770000000000000018, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64415, 'Cirrus 18m', 30, NULL, NULL, 72, 2.29999999999999982, -3.45999999999999996, 1.89999999999999991, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64416, 'Cirrus Std.', 30, NULL, NULL, 72, 1.70999999999999996, -2.43000000000000016, 1.45999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64417, 'Club Astir', 30, NULL, NULL, 72, 2.10999999999999988, -2.93999999999999995, 1.67999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64418, 'Club. class', 30, NULL, NULL, 72, 1.70999999999999996, -2.43000000000000016, 1.45999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64419, 'Cobra', 30, NULL, NULL, 72, 2.08000000000000007, -3.41000000000000014, 2.06999999999999984, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64420, 'DG100', 30, NULL, NULL, 72, 1.93999999999999995, -2.89999999999999991, 1.68999999999999995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64421, 'DG1000/18m', 30, NULL, NULL, 72, 2.50999999999999979, -4.29999999999999982, 2.47999999999999998, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64422, 'DG1000/20m', 28, NULL, NULL, 72, 2.66000000000000014, -4.51999999999999957, 2.45000000000000018, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64423, 'DG200', 30, NULL, NULL, 72, 1.14999999999999991, -1.6399999999999999, 1.16999999999999993, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64424, 'DG300', 30, NULL, NULL, 72, 1.64999999999999991, -2.54999999999999982, 1.58000000000000007, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64425, 'DG400', 30, NULL, NULL, 72, 1.12999999999999989, -1.84000000000000008, 1.39999999999999991, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64426, 'DG400/17m', 30, NULL, NULL, 72, 1.58000000000000007, -2.7200000000000002, 1.78000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64427, 'DG500 TR', 30, NULL, NULL, 72, 1.51000000000000001, -2.43999999999999995, 1.6100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64428, 'DG500/20m', 35, NULL, NULL, 72, 1.46999999999999997, -2.35000000000000009, 1.51000000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64429, 'DG500M', 35, NULL, NULL, 72, 1.43999999999999995, -2.27000000000000002, 1.43999999999999995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64430, 'DG600', 30, NULL, NULL, 72, 1.31000000000000005, -2.08999999999999986, 1.37999999999999989, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64431, 'DG600/17m', 32, NULL, NULL, 72, 1.6399999999999999, -2.4700000000000002, 1.42999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64432, 'DG800/15m', 36, NULL, NULL, 72, 0.92000000000000004, -1.1100000000000001, 0.810000000000000053, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64433, 'DG800/18m', 36, NULL, NULL, 72, 1.05000000000000004, -1.41999999999999993, 0.930000000000000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64434, 'Dimona', 30, NULL, NULL, 72, 2.41000000000000014, -3.70000000000000018, 2.58000000000000007, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64435, 'Discus', 33, NULL, NULL, 72, 1.58000000000000007, -2.45999999999999996, 1.54000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64436, 'Discus 2', 33, NULL, NULL, 72, 2.14000000000000012, -3.87000000000000011, 2.37999999999999989, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64437, 'Duo Discus', 31, NULL, NULL, 72, 1.56000000000000005, -2.25, 1.34000000000000008, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64438, 'G102 club', 30, NULL, NULL, 72, 1.68999999999999995, -2.0299999999999998, 1.21999999999999997, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64439, 'G103 acro', 30, NULL, NULL, 72, 2.14999999999999991, -3.22999999999999998, 1.84000000000000008, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64440, 'H205', 30, NULL, NULL, 72, 1.78000000000000003, -2.12999999999999989, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64441, 'H304', 30, NULL, NULL, 72, 1.14999999999999991, -1.57000000000000006, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64442, 'H304CZ', 36, NULL, NULL, 72, 1.54000000000000004, -2.41999999999999993, 1.56000000000000005, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64443, 'H304CZ17', 35, NULL, NULL, 72, 1.37999999999999989, -1.89999999999999991, 1.15999999999999992, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64444, 'Hang glider', 10, NULL, NULL, 36, 17.2100000000000009, -13.5899999999999999, 3.54999999999999982, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64445, 'Hornet', 34, NULL, NULL, 72, 1.69999999999999996, -2.43999999999999995, 1.46999999999999997, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64446, 'IS28B2', 34, NULL, NULL, 72, 2.56999999999999984, -4.24000000000000021, 2.5299999999999998, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64447, 'IS29D2', 34, NULL, NULL, 72, 1.81000000000000005, -2.31000000000000005, 1.29000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64448, 'Jantar 1', 30, NULL, NULL, 72, 3.00999999999999979, -5.57000000000000028, 3.22999999999999998, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64449, 'Jantar 2b', 30, NULL, NULL, 72, 1.57000000000000006, -2.35999999999999988, 1.35000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64450, 'Jantar St2', 30, NULL, NULL, 72, 2.12000000000000011, -3.56000000000000005, 2.16999999999999993, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64451, 'Jantar St3', 30, NULL, NULL, 72, 2.25999999999999979, -4.09999999999999964, 2.66999999999999993, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64452, 'Janus 3', 30, NULL, NULL, 72, 1.75, -2.79999999999999982, 1.80000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64453, 'Janus B', 30, NULL, NULL, 72, 1.58000000000000007, -2.43999999999999995, 1.57000000000000006, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64454, 'Janus C', 30, NULL, NULL, 72, 1.62000000000000011, -2.66999999999999993, 1.71999999999999997, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64455, 'Janus CM', 39, NULL, NULL, 72, 1.56000000000000005, -2.95999999999999996, 2.10000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64456, 'Jeans Astir', 30, NULL, NULL, 72, 1.78000000000000003, -2.41000000000000014, 1.51000000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64457, 'LAK17-15m', 35, NULL, NULL, 72, 1.28000000000000003, -2.20999999999999996, 1.53000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64458, 'LAK17-18m', 36, NULL, NULL, 72, 1.05000000000000004, -1.41999999999999993, 0.930000000000000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64459, 'LAK19-15m', 32, NULL, NULL, 72, 1.62000000000000011, -2.5, 1.52000000000000002, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64460, 'LAK19-18m', 30, NULL, NULL, 72, 1.23999999999999999, -1.6100000000000001, 0.969999999999999973, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64461, 'LS 1', 30, NULL, NULL, 72, 1.81000000000000005, -2.60999999999999988, 1.57000000000000006, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64462, 'LS 1cd', 30, NULL, NULL, 72, 2.20999999999999996, -3.08000000000000007, 1.66999999999999993, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64463, 'LS 3', 30, NULL, NULL, 72, 1.6100000000000001, -2.5, 1.60000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64464, 'LS 3 17m', 30, NULL, NULL, 72, 2.25999999999999979, -3.81000000000000005, 2.16000000000000014, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64465, 'LS 4', 30, NULL, NULL, 72, 1.93999999999999995, -3.35000000000000009, 2.10000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64466, 'LS 6', 33, NULL, NULL, 72, 1.21999999999999997, -1.73999999999999999, 1.21999999999999997, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64467, 'LS 6-18', 36, NULL, NULL, 72, 1.05000000000000004, -1.41999999999999993, 0.930000000000000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64468, 'LS 7', 30, NULL, NULL, 72, 1.78000000000000003, -3.0299999999999998, 1.92999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64469, 'LS 8', 33, NULL, NULL, 72, 2.14000000000000012, -3.87000000000000011, 2.37999999999999989, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64470, 'LS 8-18', 32, NULL, NULL, 72, 1.6399999999999999, -2.4700000000000002, 1.42999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64471, 'Mini Nimbus', 33, NULL, NULL, 72, 1.23999999999999999, -1.58000000000000007, 1.03000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64472, 'Mistral', 30, NULL, NULL, 72, 2.14000000000000012, -2.97999999999999998, 2.0299999999999998, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64473, 'Mosquito', 34, NULL, NULL, 72, 1.12999999999999989, -1.28000000000000003, 0.82999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64474, 'Nimbus 2', 30, NULL, NULL, 72, 1.40999999999999992, -2.10000000000000009, 1.28000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64475, 'Nimbus 2C', 30, NULL, NULL, 72, 1.5, -2.25, 1.34000000000000008, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64476, 'Nimbus 3', 30, NULL, NULL, 72, 0.900000000000000022, -1.14999999999999991, 0.760000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64477, 'Nimbus 3D', 30, NULL, NULL, 72, 0.900000000000000022, -1.14999999999999991, 0.760000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64478, 'Nimbus 4', 34, NULL, NULL, 72, 1.09000000000000008, -1.58000000000000007, 0.959999999999999964, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64479, 'Nimbus 4D', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64480, 'Nimbus 4DM', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64481, 'Nimbus 4DT', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64482, 'Nimbus 4M', 34, NULL, NULL, 72, 1.17999999999999994, -1.75, 1.04000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64483, 'Nimbus 4T', 34, NULL, NULL, 72, 1.41999999999999993, -2.39999999999999991, 1.43999999999999995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64484, 'Open class', 42, NULL, NULL, 72, 1.05000000000000004, -1.68999999999999995, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64485, 'Paraglider', 30, NULL, NULL, 72, 1.64999999999999991, -2.54999999999999982, 1.58000000000000007, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64486, 'Pegase', 32, NULL, NULL, 72, 1.8600000000000001, -2.66000000000000014, 1.52000000000000002, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64487, 'Phoebus A', 30, NULL, NULL, 72, 1.28000000000000003, -1.42999999999999994, 1.35000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64488, 'Phoebus B1', 26, NULL, NULL, 60, 6.94979999999999976, -16.7489999999999988, 11.0286000000000008, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64489, 'Phoebus C', 30, NULL, NULL, 72, 1.28000000000000003, -1.42999999999999994, 0.849999999999999978, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64490, 'PIK 20D', 30, NULL, NULL, 72, 1.59000000000000008, -2.9700000000000002, 2.10000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64491, 'PIK 20E', 30, NULL, NULL, 72, 1.62999999999999989, -2.97999999999999998, 2.04999999999999982, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64492, 'Pirat', 30, NULL, NULL, 72, 2.29000000000000004, -2.62000000000000011, 1.3600000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64493, 'Puchacz', 30, NULL, NULL, 72, 2.08000000000000007, -2.5, 1.41999999999999993, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64494, 'PW5 Smyk', 29, NULL, NULL, 61, 2.50999999999999979, -3.2200000000000002, 1.67999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64495, 'S-10', 30, NULL, NULL, 72, 2.31000000000000005, -5.37999999999999989, 3.87000000000000011, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64496, 'SF26', 30, NULL, NULL, 72, 3.0299999999999998, -4.57000000000000028, 2.4700000000000002, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64497, 'SF27', 30, NULL, NULL, 72, 3.64000000000000012, -5.71999999999999975, 2.89000000000000012, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64498, 'SF27M', 30, NULL, NULL, 72, 1.59000000000000008, -2.2799999999999998, 1.51000000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64499, 'SF34', 30, NULL, NULL, 72, 1.80000000000000004, -2.60000000000000009, 1.64999999999999991, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64500, 'SGS 1-26', 30, NULL, NULL, 72, 3.45000000000000018, -3.87000000000000011, 1.89999999999999991, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64501, 'Speed Astir', 30, NULL, NULL, 72, 1.37000000000000011, -1.93999999999999995, 1.31000000000000005, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64502, 'St.Cirrus', 30, NULL, NULL, 72, 1.62999999999999989, -1.94999999999999996, 1.1100000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64503, 'Std Libelle', 30, NULL, NULL, 72, 2.14000000000000012, -3, 1.69999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64504, 'Std. class', 30, NULL, NULL, 72, 1.64999999999999991, -2.54999999999999982, 1.58000000000000007, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64505, 'SZD 51-1', 30, NULL, NULL, 72, 2.75999999999999979, -3.85999999999999988, 2.00999999999999979, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64506, 'SZD 53-1', 30, NULL, NULL, 72, 2.54000000000000004, -4.62999999999999989, 2.75, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64507, 'SZD 55', 30, NULL, NULL, 72, 1.58000000000000007, -2.45999999999999996, 1.54000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64508, 'Twin Ast.3', 30, NULL, NULL, 72, 1.91999999999999993, -2.75, 1.60000000000000009, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64509, 'Twin Ast.I', 30, NULL, NULL, 72, 1.91999999999999993, -3.16000000000000014, 2.0299999999999998, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64510, 'Twin Ast.II', 30, NULL, NULL, 72, 1.6100000000000001, -1.94999999999999996, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64511, 'Ultralight', 29, NULL, NULL, 61, 2.50999999999999979, -3.2200000000000002, 1.67999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64512, 'Ventus', 33, NULL, NULL, 72, 1.20999999999999996, -1.78000000000000003, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64513, 'Ventus 2CM', 39, NULL, NULL, 72, 0.989999999999999991, -1.41999999999999993, 0.979999999999999982, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64514, 'Ventus A', 33, NULL, NULL, 72, 1.3899999999999999, -2.25999999999999979, 1.51000000000000001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64515, 'Ventus B', 35, NULL, NULL, 72, 1.62999999999999989, -2.68000000000000016, 1.68999999999999995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64516, 'Ventus C', 35, NULL, NULL, 72, 1.62999999999999989, -2.68000000000000016, 1.68999999999999995, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64517, 'Ventus2/15m', 35, NULL, NULL, 72, 1.28000000000000003, -2.20999999999999996, 1.53000000000000003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64518, 'Ventus2/18m', 34, NULL, NULL, 72, 1.05000000000000004, -1.41999999999999993, 0.930000000000000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64519, 'VentusA/16m', 33, NULL, NULL, 72, 1.29000000000000004, -1.8899999999999999, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64520, 'VentusB/16m', 33, NULL, NULL, 72, 1.29000000000000004, -1.8899999999999999, 1.19999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64521, 'VentusC/17m', 32, NULL, NULL, 72, 1.6399999999999999, -2.4700000000000002, 1.42999999999999994, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64522, 'W.Wing Eagle', 9, NULL, NULL, 29, 25.5199999999999996, -17.6499999999999986, 4.08999999999999986, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64523, 'W.Wing Falcon', 9, NULL, NULL, 29, 16.25, -7.30999999999999961, 1.79000000000000004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64524, 'W.Wing Sport2', 9, NULL, NULL, 29, 20.5799999999999983, -15.8300000000000001, 5.95999999999999996, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64525, 'W.Wing Talon', 9, NULL, NULL, 29, 16.1900000000000013, -13.7200000000000006, 3.75999999999999979, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64526, 'W.Wing U/S', 9, NULL, NULL, 29, 27.2899999999999991, -21.1900000000000013, 5.07000000000000028, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO zrakoplovi (id_zrakoplovi, zrakoplov_naziv, opterecenje, balast, postotakbalasta, min_brzina, polara_a, polara_b, polara_c, brzina_v1, brzina_v2, brzina_v3, brzina_w1, brzina_w2, brzina_w3, zrakoplov_foto, zrakoplov_opis) VALUES (64527, 'WillsWing U2', 9, NULL, NULL, 29, 20.379999999999999, -16.7199999999999989, 4.28000000000000025, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 2950 (class 0 OID 0)
-- Dependencies: 176
-- Name: zrakoplovi_id_zrakoplovi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('zrakoplovi_id_zrakoplovi_seq', 64527, true);


--
-- TOC entry 2785 (class 2606 OID 35406)
-- Name: RutaPreletaIzracun_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rutapreletaizracun
    ADD CONSTRAINT "RutaPreletaIzracun_pkey" PRIMARY KEY (id_rutapreletaizracun);


--
-- TOC entry 2781 (class 2606 OID 50479)
-- Name: Ruta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ruta
    ADD CONSTRAINT "Ruta_pkey" PRIMARY KEY (id_ruta);


--
-- TOC entry 2776 (class 2606 OID 35364)
-- Name: SlojZraka_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY slojzraka
    ADD CONSTRAINT "SlojZraka_pkey" PRIMARY KEY (id_sloj);


--
-- TOC entry 2788 (class 2606 OID 50661)
-- Name: TockaIzracunaPreleta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tockaizracunapreleta
    ADD CONSTRAINT "TockaIzracunaPreleta_pkey" PRIMARY KEY (id_tockaizp);


--
-- TOC entry 2772 (class 2606 OID 35355)
-- Name: TockaMeteo_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tockameteo
    ADD CONSTRAINT "TockaMeteo_name_key" UNIQUE (naziv);


--
-- TOC entry 2774 (class 2606 OID 50451)
-- Name: TockaMeteo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tockameteo
    ADD CONSTRAINT "TockaMeteo_pkey" PRIMARY KEY (id_tockameteo);


--
-- TOC entry 2767 (class 2606 OID 35324)
-- Name: Tocka_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tocka
    ADD CONSTRAINT "Tocka_name_key" UNIQUE (naziv);


--
-- TOC entry 2769 (class 2606 OID 35322)
-- Name: Tocka_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tocka
    ADD CONSTRAINT "Tocka_pkey" PRIMARY KEY (id_tocka);


--
-- TOC entry 2779 (class 2606 OID 35378)
-- Name: Zrakoplovi_zrakoplov_naziv_key1; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY zrakoplovi
    ADD CONSTRAINT "Zrakoplovi_zrakoplov_naziv_key1" UNIQUE (zrakoplov_naziv);


--
-- TOC entry 2795 (class 2606 OID 50723)
-- Name: rutatocke_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rutatocke
    ADD CONSTRAINT rutatocke_pkey PRIMARY KEY (id, id_tocka, id_ruta);


--
-- TOC entry 2791 (class 2606 OID 50693)
-- Name: tockaokretna_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tockaokretna
    ADD CONSTRAINT tockaokretna_name_key UNIQUE (wpname);


--
-- TOC entry 2793 (class 2606 OID 50691)
-- Name: tockaokretna_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tockaokretna
    ADD CONSTRAINT tockaokretna_pkey PRIMARY KEY (id_oktocka);


--
-- TOC entry 2783 (class 1259 OID 35407)
-- Name: RutaPreletaIzracunIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "RutaPreletaIzracunIND" ON rutapreletaizracun USING btree (naziv_rute);


--
-- TOC entry 2765 (class 1259 OID 35325)
-- Name: TockaIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "TockaIND" ON tocka USING btree (naziv);


--
-- TOC entry 2786 (class 1259 OID 50667)
-- Name: TockaIzracunaPreletaIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "TockaIzracunaPreletaIND" ON tockaizracunapreleta USING btree (naziv);


--
-- TOC entry 2770 (class 1259 OID 35356)
-- Name: TockaMeteoIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "TockaMeteoIND" ON tockameteo USING btree (naziv);


--
-- TOC entry 2777 (class 1259 OID 35379)
-- Name: ZrakoploviIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "ZrakoploviIND" ON zrakoplovi USING btree (zrakoplov_naziv);


--
-- TOC entry 2782 (class 1259 OID 35393)
-- Name: ruteIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "ruteIND" ON ruta USING btree (naziv_rute);


--
-- TOC entry 2789 (class 1259 OID 50694)
-- Name: tockaokretnaIND; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX "tockaokretnaIND" ON tockaokretna USING btree (wpname);


--
-- TOC entry 2797 (class 2606 OID 50662)
-- Name: id_ruta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tockaizracunapreleta
    ADD CONSTRAINT id_ruta FOREIGN KEY (id_rutapreletaizracun) REFERENCES rutapreletaizracun(id_rutapreletaizracun) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2798 (class 2606 OID 50724)
-- Name: id_ruta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rutatocke
    ADD CONSTRAINT id_ruta FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2799 (class 2606 OID 50729)
-- Name: id_tocka; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rutatocke
    ADD CONSTRAINT id_tocka FOREIGN KEY (id_tocka) REFERENCES tocka(id_tocka) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2796 (class 2606 OID 50463)
-- Name: id_tockaMeteo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY slojzraka
    ADD CONSTRAINT "id_tockaMeteo" FOREIGN KEY (id_tockameteo) REFERENCES tockameteo(id_tockameteo) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2931 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-06-15 08:32:19 CEST

--
-- PostgreSQL database dump complete
--

