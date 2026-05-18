-- NexaBank Database Initialization Script
-- Creates separate databases for each microservice

SELECT 'Creating nexabank_auth database...' AS status;
CREATE DATABASE nexabank_auth;

SELECT 'Creating nexabank_accounts database...' AS status;
CREATE DATABASE nexabank_accounts;

SELECT 'Creating nexabank_transactions database...' AS status;
CREATE DATABASE nexabank_transactions;

SELECT 'All NexaBank databases created successfully.' AS status;
