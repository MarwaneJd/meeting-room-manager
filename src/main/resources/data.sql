-- Reset sequences if they exist
ALTER SEQUENCE IF EXISTS role_id_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS app_user_id_seq RESTART WITH 100;

-- Insert Roles if they don't exist
INSERT INTO role (name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ADMIN');

INSERT INTO role (name)
SELECT 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'USER');

-- Insert Admin user with BCrypt encoded password if it doesn't exist
INSERT INTO app_user (username, email, password, role_id)
SELECT 'admin', 'admin@example.com', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNip/LrK6W6K', (SELECT id FROM role WHERE name = 'ADMIN')
    WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE username = 'admin');

-- Insert regular user with BCrypt encoded password if it doesn't exist
INSERT INTO app_user (username, email, password, role_id)
SELECT 'user', 'user@example.com', '$2a$10$9tWe6xUhFq1X9Vvq9Tg39OEhHgf0JqJEp6kJY9.8HGPNnJ9QOIGsG', (SELECT id FROM role WHERE name = 'USER')
    WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE username = 'user');

-- Insert Rooms
INSERT INTO room (id, name, capacity, location)
SELECT 1, 'Room A', 10, 'First Floor'
    WHERE NOT EXISTS (SELECT 1 FROM room WHERE id = 1);

INSERT INTO room (id, name, capacity, location)
SELECT 2, 'Room B', 20, 'Second Floor'
    WHERE NOT EXISTS (SELECT 1 FROM room WHERE id = 2);

INSERT INTO room (id, name, capacity, location)
SELECT 3, 'Room C', 15, 'Third Floor'
    WHERE NOT EXISTS (SELECT 1 FROM room WHERE id = 3);

-- Reset reservation sequence
ALTER SEQUENCE IF EXISTS reservation_id_seq RESTART WITH 100;

-- Insert Reservations only if the referenced user and room exist
INSERT INTO reservation (start_time, end_time, title, user_id, room_id)
SELECT '2025-05-09 10:00:00', '2025-05-09 11:00:00', 'Team Meeting',
       (SELECT id FROM app_user WHERE username = 'user'),
       (SELECT id FROM room WHERE name = 'Room A')
    WHERE EXISTS (SELECT 1 FROM app_user WHERE username = 'user')
  AND EXISTS (SELECT 1 FROM room WHERE name = 'Room A');