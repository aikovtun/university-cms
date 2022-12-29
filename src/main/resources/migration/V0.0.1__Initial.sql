CREATE TABLE users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email TEXT UNIQUE,
    password TEXT,
    comment TEXT
);

CREATE TABLE roles
(
    role_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT,
    comment TEXT
);

CREATE TABLE users_roles
(
    users_roles_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles(role_id) ON DELETE CASCADE,
    UNIQUE(user_id, role_id)
);

CREATE TABLE buildings
(
    building_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT,
    description TEXT
);

CREATE TABLE rooms
(
    room_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    building_id BIGINT REFERENCES buildings(building_id),
    name TEXT,
    type TEXT,
    description TEXT
);

CREATE TABLE courses
(
    course_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT,
    description TEXT
);

CREATE TABLE groups
(
    group_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id BIGINT REFERENCES courses(course_id),
    name TEXT,
    description TEXT

);

CREATE TABLE subjects
(
    subject_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT,
    description TEXT
);

CREATE TABLE students
(
    student_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    group_id BIGINT REFERENCES groups(group_id),
    user_id BIGINT REFERENCES users(user_id)
);

CREATE TABLE teachers
(
    teacher_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    user_id BIGINT REFERENCES users(user_id)
);

CREATE TABLE administrators
(
    administrator_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    user_id BIGINT REFERENCES users(user_id)
);

CREATE TABLE lessons
(
    lesson_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_id BIGINT REFERENCES rooms(room_id),
    group_id BIGINT REFERENCES groups(group_id),
    subject_id BIGINT REFERENCES subjects(subject_id),
    teacher_id BIGINT REFERENCES teachers(teacher_id),
    lesson_date DATE,
    lesson_number SMALLINT
);

INSERT INTO roles(name, comment)
VALUES ('ROLE_ADMIN', 'Administrator'),
       ('ROLE_STUDENT', 'Student'),
       ('ROLE_TEACHER', 'Teacher');

INSERT INTO users(email, password, comment)
VALUES ('administrator@university.local', '$2a$12$v3DzMxIX2A2fYjywtDJfGuZfHny6IkM/y8C7htyRQ3d0y1pG9cQeW', 'Default administrator account');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1);

INSERT INTO administrators(first_name, last_name, user_id)
VALUES ('Default', 'Administrator', 1);