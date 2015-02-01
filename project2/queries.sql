/*(1)*/
SELECT COUNT(*) 
FROM Users;

/*(2)*/
SELECT COUNT(*)
FROM Users U
INNER JOIN Items I
ON U.UserID = I.UserID
WHERE BINARY I.Location = 'New York';

/*(3)*/
SELECT COUNT(*) 
FROM(
	SELECT COUNT(DISTINCT Category) AS C
	FROM Categories
	GROUP BY ItemID
	HAVING C = 4
) AS G;

/*(4)*/
SELECT B.ItemID
FROM Bids B
INNER JOIN Items I
ON B.ItemID = I.ItemID
WHERE Ends > '2001-12-20 00:00:01'
AND Amount = (
				SELECT MAX(Amount) 
				FROM Bids
);

/*(5)*/
SELECT COUNT(*)
FROM(
	SELECT COUNT(*)
	FROM Users U
	INNER JOIN Items I
	ON U.UserID = I.UserID
	GROUP BY U.UserID, U.Rating
	HAVING U.Rating > 1000
) AS G;

/*(6)*/
SELECT COUNT(*)
FROM(
	SELECT COUNT(*)
	FROM Items I
	INNER JOIN Bids B
	ON B.UserID = I.UserID
	GROUP BY B.UserID
) AS G;

/*(7)*/
SELECT COUNT(DISTINCT Category)
FROM Bids B
INNER JOIN Categories C
ON C.ItemID = B.ItemID
WHERE B.Amount > 100; 