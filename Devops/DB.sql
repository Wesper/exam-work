su - postgres
psql
CREATE DATABASE auth;
CREATE USER authuser WITH PASSWORD 'authpassword';
\q
psql auth
CREATE SCHEMA auth;
CREATE TABLE auth.users
(
	id SERIAL PRIMARY KEY,
	username VARCHAR(64) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(64) NOT NULL UNIQUE,
	role VARCHAR(16) NOT NULL
);
GRANT ALL PRIVILEGES ON DATABASE users to authuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA auth TO authuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA auth TO authuser;
GRANT USAGE ON SCHEMA auth TO authuser;
\q
su - postgres
psql
CREATE DATABASE products;
CREATE USER productuser WITH PASSWORD 'productpassword';
\q
psql products
CREATE SCHEMA products;
CREATE TABLE products.products
(
	id SERIAL PRIMARY KEY,
	image_id VARCHAR(64) NOT NULL,
	title VARCHAR(128) NOT NULL,
	description VARCHAR(256) NOT NULL,
	subtitle TEXT NOT NULL,
	price NUMERIC NOT NULL,
	type VARCHAR(32) NOT NULL,
	measure VARCHAR(16) NOT NULL,
	unit_measure VARCHAR(16) NOT NULL,
	actual BOOLEAN NOT NULL
); 
CREATE TABLE products.ratings
(
	id SERIAL PRIMARY KEY,
	rating NUMERIC NOT NULL,
	user_id NUMERIC NOT NULL,
	product_id NUMERIC NOT NULL
);
CREATE TABLE products.reviews
(
	id SERIAL PRIMARY KEY,
	user_id NUMERIC NOT NULL,
	product_id NUMERIC NOT NULL,
	review TEXT NOT NULL
);
GRANT ALL PRIVILEGES ON DATABASE products to productuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA products TO productuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA products TO productuser;
GRANT USAGE ON SCHEMA products TO productuser;
\q
su - postgres
psql
CREATE DATABASE profiles;
CREATE USER profileuser WITH PASSWORD 'profilepassword';
\q
psql profiles;
CREATE SCHEMA profiles;
CREATE TABLE profiles.profiles
(
	id SERIAL PRIMARY KEY,
	user_id NUMERIC UNIQUE NOT NULL,
	first_name VARCHAR(32),
	last_name VARCHAR(32),
	middle_name VARCHAR(32),
	city VARCHAR(64),
	birthday VARCHAR(10),
	address VARCHAR(256)
);
GRANT ALL PRIVILEGES ON DATABASE profiles to profileuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA profiles TO profileuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA profiles TO profileuser;
GRANT USAGE ON SCHEMA profiles TO profileuser;
\q
su - postgres
psql
CREATE DATABASE orders;
CREATE USER orderuser WITH PASSWORD 'orderpassword';
\q
psql orders
CREATE SCHEMA orders;
CREATE TABLE orders.orders
(
	id SERIAL PRIMARY KEY,
	user_id NUMERIC NOT NULL,
	date VARCHAR(32) NOT NULL,
	address VARCHAR(256) NOT NULL,
	promocode VARCHAR(32),
	total_price NUMERIC NOT NULL,
	total_promo_price NUMERIC,
	details TEXT NOT NULL,
	status VARCHAR(16) NOT NULL
); 
CREATE TABLE orders.baskets
(
	id SERIAL PRIMARY KEY,
	user_id NUMERIC NOT NULL,
	product_id NUMERIC NOT NULL,
	count NUMERIC NOT NULL
);
CREATE TABLE orders.promocodes
(
	promocode VARCHAR(32) UNIQUE PRIMARY KEY,
	percent NUMERIC NOT NULL,
	actual BOOLEAN NOT NULL
);
GRANT ALL PRIVILEGES ON DATABASE orders to orderuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA orders TO orderuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA orders TO orderuser;
GRANT USAGE ON SCHEMA orders TO orderuser;
\q