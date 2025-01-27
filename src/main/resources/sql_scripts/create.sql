CREATE TABLE Team (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    isDeleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE Employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    idTeam INT NOT NULL,
    isTeamLead BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idTeam) REFERENCES Team(id)
);