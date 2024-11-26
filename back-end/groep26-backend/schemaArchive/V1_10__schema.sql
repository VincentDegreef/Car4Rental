-- Drop existing tables if they exist
DROP TABLE IF EXISTS wallets, rents, rentals, cars, users, roles, Notification, notifications_users CASCADE;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    given_verification_code INT DEFAULT NULL,
    verified BOOLEAN DEFAULT FALSE,
    experation_date DATE,
    banned BOOLEAN DEFAULT FALSE,
    wallet_id INT,
    rent_id INT,
    role_id INT
);

-- Create WALLET table
CREATE TABLE IF NOT EXISTS wallets (
    id SERIAL PRIMARY KEY,
    user_id INT,
    balance DOUBLE PRECISION DEFAULT 0.0
);

-- Create CAR table
CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    type VARCHAR(100) NOT NULL,
    number_of_seats INTEGER NOT NULL CHECK (number_of_seats > 0),
    number_of_child_seats INTEGER NOT NULL CHECK (number_of_child_seats >= 0),
    foldable_rear_seats BOOLEAN,
    towbar BOOLEAN,
    user_id INTEGER
);

-- Create RENTAL table
CREATE TABLE IF NOT EXISTS rentals (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    start_time TIME,
    end_date DATE NOT NULL,
    end_time TIME,
    street VARCHAR(100),
    number VARCHAR(3),
    postal VARCHAR(4),
    price DECIMAL NOT NULL,
    city VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(150) NOT NULL,
    car_id INTEGER
);

-- Create RENTS table
CREATE TABLE IF NOT EXISTS rents (
    id SERIAL PRIMARY KEY,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(150) NOT NULL,
    identification VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    driving_licence_number VARCHAR(10) NOT NULL,
    is_cancelled BOOLEAN DEFAULT FALSE,
    start_time TIME,
    end_date DATE,
    end_time TIME,
    start_millage INTEGER,
    return_millage INTEGER,
    start_fuel_quantity INTEGER,
    return_fuel_quantity INTEGER,
    start_date DATE NOT NULL,
    total_price DECIMAL,
    rental_id INTEGER,
    user_id INTEGER
);

-- Create NOTIFICATIONS table
CREATE TABLE IF NOT EXISTS Notification (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    received_at TIMESTAMP NOT NULL,
    seen BOOLEAN DEFAULT FALSE,
    confirmed BOOLEAN DEFAULT FALSE,
    tag VARCHAR(50),
    rent_id INT,
    FOREIGN KEY (rent_id) REFERENCES rents(id)
);

-- Create NOTIFICATIONS_USERS table
CREATE TABLE IF NOT EXISTS notifications_users (
    user_id INT,
    notification_id INT,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (notification_id) REFERENCES Notification(id)
);

-- Add foreign key constraints
ALTER TABLE users ADD CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id);
ALTER TABLE users ADD CONSTRAINT fk_rent FOREIGN KEY (rent_id) REFERENCES rents(id);
ALTER TABLE users ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE cars ADD FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE rentals ADD FOREIGN KEY (car_id) REFERENCES cars(id);
ALTER TABLE rents ADD FOREIGN KEY (rental_id) REFERENCES rentals(id);
ALTER TABLE rents ADD FOREIGN KEY (user_id) REFERENCES users(id);

-- Insert roles
INSERT INTO roles (name) VALUES ('OWNER');
INSERT INTO roles (name) VALUES ('RENTER');
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('ACCOUNTANT');

-- Insert the admin user
-- Ensure the role_id corresponds to the correct role
INSERT INTO users (username, password, email, phone_number, given_verification_code, verified, experation_date, banned, role_id)
VALUES ('admin26', '$2a$12$m.VOPHE1/nQ/qfFtmff1e.SheHO4e93Df9Gq.H..ZcHN9m0bE0tbu','groep26.softeng@gmail.com','0491257845', 123456, true, '2024-09-10', false, 3);

-- Insert a wallet for the admin user
INSERT INTO wallets (user_id, balance) VALUES (1, 0.0);

-- Update the admin user's wallet_id
UPDATE users SET wallet_id = (SELECT id FROM wallets WHERE user_id = 1) WHERE id = 1;

-- Insert the renter user
INSERT INTO users (username, password, email, phone_number, given_verification_code, verified, experation_date, banned, role_id)
VALUES ('renter26', '$2a$12$m.VOPHE1/nQ/qfFtmff1e.SheHO4e93Df9Gq.H..ZcHN9m0bE0tbu','renter26.softeng@gmail.com','0491257846', 123456, true, '2024-09-10', false, 2);

-- Insert a wallet for the renter user
INSERT INTO wallets (user_id, balance) VALUES (2, 0.0);

-- Update the renter user's wallet_id
UPDATE users SET wallet_id = (SELECT id FROM wallets WHERE user_id = 2) WHERE id = 2;

-- Insert the owner user
INSERT INTO users (username, password, email, phone_number, given_verification_code, verified, experation_date, banned, role_id)
VALUES ('owner26', '$2a$12$m.VOPHE1/nQ/qfFtmff1e.SheHO4e93Df9Gq.H..ZcHN9m0bE0tbu','owner26.softeng@gmail.com','0491257847', 123456, true, '2024-09-10', false, 1);

-- Insert a wallet for the owner user
INSERT INTO wallets (user_id, balance) VALUES (3, 0.0);

-- Update the owner user's wallet_id
UPDATE users SET wallet_id = (SELECT id FROM wallets WHERE user_id = 3) WHERE id = 3;
