-- Insert Roles
INSERT INTO role (id, name)
SELECT 1, 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 1);
INSERT INTO role (id, name)
SELECT 2, 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 2);

-- Insert Users
INSERT INTO app_user (id, username, email, password, role_id)
VALUES (1, 'admin', 'admin@example.com', 'adminpass', 1);

INSERT INTO app_user (id, username, email, password, role_id)
VALUES (2, 'john', 'john@example.com', 'johnpass', 2);

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

-- Insert Reservations
INSERT INTO reservation (id, user_id, room_id, start_time, end_time)
VALUES (1, 2, 1, '2025-05-08 10:00:00', '2025-05-08 11:00:00');