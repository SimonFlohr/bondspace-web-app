-- Create the 'users' table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    email_address VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    user_bio TEXT,
    user_picture VARCHAR(255),
    notifications INT[]
);

-- Create the 'spaces' table
CREATE TABLE IF NOT EXISTS spaces (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    space_name VARCHAR(255) NOT NULL,
    description TEXT,
    space_picture VARCHAR(255),
    memories INT[],
    space_notifications INT[]
);

-- Create the 'user_space' join table
CREATE TABLE IF NOT EXISTS user_space (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    space_id INT NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    user_joined_at TIMESTAMP,
    CONSTRAINT fk_user_space_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_space_space FOREIGN KEY (space_id) REFERENCES spaces (id) ON DELETE CASCADE
);

-- Create the 'memories' table
CREATE TABLE IF NOT EXISTS memories (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    space_id INT NOT NULL,
    uploaded_by INT NOT NULL,
    memory_type VARCHAR(50),
    memory_name VARCHAR(255),
    description TEXT,
    tags TEXT[],
    participants INT[],
    text_content TEXT,
    media_content TEXT,
    CONSTRAINT fk_memory_space FOREIGN KEY (space_id) REFERENCES spaces (id) ON DELETE CASCADE,
    CONSTRAINT fk_memory_user FOREIGN KEY (uploaded_by) REFERENCES users (id) ON DELETE CASCADE
);

-- Create the 'space_notifications' table
CREATE TABLE IF NOT EXISTS space_notifications (
    id SERIAL PRIMARY KEY,
    space_id INT NOT NULL,
    space_notification_type VARCHAR(50),
    message VARCHAR(255),
    CONSTRAINT fk_space_notification_space FOREIGN KEY (space_id) REFERENCES spaces (id) ON DELETE CASCADE
);

-- Create the 'user_notifications' table
CREATE TABLE IF NOT EXISTS user_notifications (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    user_notification_type VARCHAR(50),
    message VARCHAR(255),
    CONSTRAINT fk_user_notification_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);