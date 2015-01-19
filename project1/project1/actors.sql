CREATE TABLE Actors(Name char(40) PRIMARY KEY, Movie char(80), Year int, Role char(40));
LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
SELECT Actors.Name
FROM Actors
WHERE Actors.Movie = 'Die Another Day';
SELECT Actors.Name
FROM Actors;
SELECT Actors.Movie
FROM Actors
WHERE Actors.Name = 'Dicaprio, Leonardo';
DROP TABLE Actors;
