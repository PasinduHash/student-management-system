CREATE TABLE IF NOT EXISTS Student (
    id VARCHAR(20) PRIMARY KEY,
    register_name VARCHAR(200) NOT NULL,
    full_name VARCHAR(500) NOT NULL,
    dob DATE NOT NULL,
    grade VARCHAR(5) NOT NULL,
    address VARCHAR(300) NOT NULL,
    contact VARCHAR(15),
    guardian_name VARCHAR(200) NOT NULL,
    guardian_occupation VARCHAR(100) NOT NULL,
    guardian_contact VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Picture (
    student_id VARCHAR(20),
    student_picture MEDIUMBLOB NOT NULL
);

CREATE TABLE IF NOT EXISTS Teacher (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    class VARCHAR(5) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Library (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    status ENUM('AVAILABLE', 'TAKEN') NOT NULL,
    borrower_id VARCHAR(20),
    borrowed_date DATE,
    return_date DATE
);

CREATE TABLE IF NOT EXISTS Results (
    student_id VARCHAR(20) PRIMARY KEY,
    sinhala INT,
    maths INT,
    science INT,
    english INT
);

CREATE TABLE IF NOT EXISTS Contacts (
    contact VARCHAR(15) NOT NULL ,
    teacher_id VARCHAR(20) NOT NULL ,
    CONSTRAINT uk_contact UNIQUE KEY (contact) ,
    CONSTRAINT pk_contact PRIMARY KEY (contact, teacher_id)
);

CREATE TABLE IF NOT EXISTS Subjects (
    subject VARCHAR(100) NOT NULL,
    teacher_id VARCHAR(20) NOT NULL,
    CONSTRAINT uk_subject UNIQUE KEY (subject),
    CONSTRAINT pk_subject PRIMARY KEY (subject, teacher_id)
);

CREATE TABLE IF NOT EXISTS TeacherPicture(
    teacher_id VARCHAR(20) PRIMARY KEY ,
    picture MEDIUMBLOB NOT NULL ,
    CONSTRAINT fk_teacher_picture FOREIGN KEY (teacher_id) REFERENCES Teacher(id)
);

ALTER TABLE Results ADD CONSTRAINT fk_result FOREIGN KEY (student_id) REFERENCES Student (id);

ALTER TABLE Contacts ADD CONSTRAINT fk_contact FOREIGN KEY (teacher_id) REFERENCES Teacher (id);

ALTER TABLE Subjects ADD CONSTRAINT fk_subject FOREIGN KEY (teacher_id) REFERENCES Teacher(id);