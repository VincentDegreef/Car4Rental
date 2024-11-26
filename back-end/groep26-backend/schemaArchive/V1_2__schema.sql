-- Drop existing tables if they exist
DROP TABLE IF EXISTS wallet, rents, rental, car, users, roles;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    givenVerificationCode INT,
    verified BOOLEAN DEFAULT false,
    experationDate DATE,
    banned BOOLEAN DEFAULT false,
    role_id INT, -- Create the role_id column
    FOREIGN KEY (rent_id) REFERENCES rental(id), -- Define foreign key constraint
    FOREIGN KEY (role_id) REFERENCES roles(id) -- Define foreign key constraint
);

-- Create CAR table
CREATE TABLE IF NOT EXISTS car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    year_of_construction INTEGER NOT NULL,
    fuel_type VARCHAR(255) NOT NULL,
    price_per_day DECIMAL NOT NULL,
    location VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL
);

-- Create RENTAL table
CREATE TABLE IF NOT EXISTS rental (
    id SERIAL PRIMARY KEY,
    car_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create RENTS table
CREATE TABLE IF NOT EXISTS rents (
    id SERIAL PRIMARY KEY,
    rental_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES rental(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create WALLET table
CREATE TABLE IF NOT EXISTS wallet (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    balance DECIMAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert roles
INSERT INTO roles (name) VALUES ('OWNER');
INSERT INTO roles (name) VALUES ('RENTER');
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('ACCOUNTANT');

-- Insert the admin user
-- Ensure the role_id corresponds to the correct role
INSERT INTO users (username, password, email, phoneNumber, givenVerificationCode, verified, experationDate, banned, role_id)
VALUES ('admin26', '$2a$12$m.VOPHE1/nQ/qfFtmff1e.SheHO4e93Df9Gq.H..ZcHN9m0bE0tbu','groep26.softeng@gmail.com','0491257845', NULL, false, NULL, false, 3);
