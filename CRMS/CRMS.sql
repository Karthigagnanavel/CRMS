CREATE TABLE criminals (
    criminal_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
	fatherName VARCHAR(50)
    mostwanted VARCHAR(50), 
	age INT,
    gender VARCHAR(50),
    date_of_birth DATE,
    nationality VARCHAR(50),
	maritalStatus VARCHAR(50),
    address TEXT,
	occupation VARCHAR(50),
    phoneNumer VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE crimes (
    crime_id INT PRIMARY KEY AUTO_INCREMENT,
    criminal_id INT,
    crime_type VARCHAR(100) NOT NULL,
    crime_description TEXT NOT NULL,
    crime_date DATE NOT NULL,
    place VARCHAR(255),
    status VARCHAR(100),
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id) ON DELETE CASCADE
);

CREATE TABLE arrests (
    arrest_id INT PRIMARY KEY AUTO_INCREMENT,
    criminal_id INT,
    crime_id INT,
    arrest_date DATE NOT NULL,
    arresting_officer VARCHAR(100),
    police_station VARCHAR(100),
    status VARCHAR(100),
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id) ON DELETE CASCADE,
    FOREIGN KEY (crime_id) REFERENCES crimes(crime_id) ON DELETE CASCADE
);

CREATE TABLE court_cases (
    case_id INT PRIMARY KEY AUTO_INCREMENT,
    crime_id INT,
    court_name VARCHAR(100),
    judge_name VARCHAR(100),
    hearing_date DATE,
    case_status VARCHAR(100),
    FOREIGN KEY (crime_id) REFERENCES crimes(crime_id) ON DELETE CASCADE
);

CREATE TABLE evidence (
    evidence_id INT PRIMARY KEY AUTO_INCREMENT,
    crime_id INT,
    evidence_type VARCHAR(100),
    description TEXT,
    storage_location VARCHAR(255),
    FOREIGN KEY (crime_id) REFERENCES crimes(crime_id) ON DELETE CASCADE
);
