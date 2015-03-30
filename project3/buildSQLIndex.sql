CREATE TABLE IF NOT EXISTS particles(
	ItemID VARCHAR(20) NOT NULL,
	Coordinates POINT NOT NULL,
	PRIMARY KEY(ItemID)
) ENGINE=MyISAM;

INSERT INTO particles(ItemID, Coordinates)
	SELECT ItemID, POINT(Latitude, Longitude)
	FROM Items
	WHERE Latitude != 0 AND Longitude != 0;

CREATE SPATIAL INDEX CoordinatesCol ON particles(Coordinates);
