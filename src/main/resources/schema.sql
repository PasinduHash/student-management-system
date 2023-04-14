CREATE TABLE IF NOT EXISTS Student (
    student_id VARCHAR(20) PRIMARY KEY,
    register_name VARCHAR(200) NOT NULL,
    full_name VARCHAR(500) NOT NULL,
    dob DATE NOT NULL,
    class VARCHAR(5) NOT NULL,
    address VARCHAR(300) NOT NULL,
    contact VARCHAR(15),
    guardian_name VARCHAR(200) NOT NULL,
    guardian_occupation VARCHAR(100) NOT NULL,
    guardian_contact VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS Teacher (
    teacher_id VARCHAR(20) PRIMARY KEY,
    teacher_name VARCHAR(200) NOT NULL,
    class VARCHAR(5) NOT NULL,
    contact VARCHAR(15) NOT NULL,
    address VARCHAR(500) NOT NULL,
    picture MEDIUMBLOB NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Library (
    book_id VARCHAR(20) PRIMARY KEY,
    book_name VARCHAR(200) NOT NULL,
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

ALTER TABLE Results ADD CONSTRAINT fk_contact FOREIGN KEY (student_id) REFERENCES Student (student_id);