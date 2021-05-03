SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS Guilds CASCADE;
DROP TABLE IF EXISTS Users CASCADE; 
DROP TABLE IF EXISTS GpqCurrent CASCADE; 
DROP TABLE IF EXISTS GpqConfirmed CASCADE; 
DROP TABLE IF EXISTS GpqParticipants CASCADE;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE Guilds (
	gid VARCHAR(18) PRIMARY KEY
); 

CREATE TABLE Users (
	gid VARCHAR(18),
    uid VARCHAR(18), 
    job int DEFAULT 0,
    floor int NOT NULL DEFAULT 0,
    PRIMARY KEY (gid, uid),
    FOREIGN KEY (gid) REFERENCES Guilds(gid)
); 

CREATE TABLE GpqCurrent (
	gid VARCHAR(18),
    created timestamp DEFAULT NOW(), 
    link text NOT NULL,
    PRIMARY KEY (gid),
    FOREIGN KEY (gid) REFERENCES Guilds(gid)
); 

CREATE TABLE GpqConfirmed (
	gid VARCHAR(18), 
    confirmedTime timestamp DEFAULT NOW(), 
    score int DEFAULT 0,
    PRIMARY KEY (gid, confirmedTime),
    FOREIGN KEY (gid) REFERENCES Guilds(gid)
); 

/* This table does not refers to Users table as FK, because some participants might not be registered users */ 
CREATE TABLE GpqParticipants (
	gid VARCHAR(18) NOT NULL, 
    confirmedTime timestamp NOT NULL, 
    uid VARCHAR(18) NOT NULL, 
    PRIMARY KEY (gid, confirmedTime, uid),
    FOREIGN KEY (gid, confirmedTime) REFERENCES GpqConfirmed(gid, confirmedTime)
); 