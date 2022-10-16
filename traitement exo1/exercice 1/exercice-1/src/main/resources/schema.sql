CREATE TABLE IF NOT EXISTS role(
id INT AUTO_INCREMENT PRIMARY KEY,
authority VARCHAR(20) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS Users(
Username VARCHAR(100) PRIMARY KEY,
Christian VARCHAR(100),
Country VARCHAR(100) NOT NULL,
Lang VARCHAR(100) NOT NULL,
Balance FLOAT NOT NULL DEFAULT 5000,
Password VARCHAR(100) NOT NULL,
Active BOOLEAN NOT NULL DEFAULT false,
role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(id) ON UPDATE CASCADE ON DELETE RESTRICT);




CREATE TABLE IF NOT EXISTS Transfer(
transfer_Id INT AUTO_INCREMENT PRIMARY KEY,
sendername VARCHAR(100) NOT NULL,
receivername VARCHAR(100) NOT NULL,
amount FLOAT NOT NULL,
mycurrency  VARCHAR(100) NOT NULL,
message VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Users_Transfer(
Username Varchar(100) NOT NULL,
transfer_Id INT NOT NULL,
FOREIGN KEY (Username) REFERENCES Users(Username) ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (transfer_Id) REFERENCES Transfer(transfer_Id) ON DELETE RESTRICT ON UPDATE CASCADE,
PRIMARY KEY (Username,transfer_Id)
);