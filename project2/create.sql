CREATE TABLE IF NOT EXISTS Items(
	ItemID INT,
	UserID VARCHAR(30),
	Name VARCHAR(100),
	Buy_Price DECIMAL(8,2),
	Description VARCHAR(4000),
	First_Bid DECIMAL(8,2),
	Started TIMESTAMP,
	Ends TIMESTAMP,
	Currently DECIMAL(8,2),
	Location VARCHAR(50),
	Country VARCHAR(40),
	Latitude VARCHAR(30),
	Longitude VARCHAR(30),
	PRIMARY KEY(ItemID)
	);

CREATE TABLE IF NOT EXISTS Bids(
	BidID INT,
	UserID VARCHAR(30),
	ItemID INT,
	Time TIMESTAMP,
	Amount DECIMAL(8,2),
	PRIMARY KEY(BidID)
	);

CREATE TABLE IF NOT EXISTS Users(
	UserID VARCHAR(30),
	Location VARCHAR(50),
	Country VARCHAR(40),
	Rating INT,
	PRIMARY KEY(UserID)
	);

CREATE TABLE IF NOT EXISTS Categories(
	ItemID INT,
	Category varchar(40)
	);
