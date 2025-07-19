-- Drop the existing heartbeat table
DROP TABLE IF EXISTS heartbeat;

-- Create a new heartbeat table with the updated structure
CREATE TABLE heartbeat (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);