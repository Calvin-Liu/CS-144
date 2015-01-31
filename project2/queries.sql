/*(1)*/
SELECT COUNT(*) 
FROM Users;

/*(2)*/

/*(3)*/
SELECT COUNT(*) 
FROM(
	SELECT COUNT(DISTINCT Category) AS C
	FROM Categories
	GROUP BY ItemID
	HAVING C = 4
) AS G;

/*(4)*/
SELECT ItemID
FROM(
	SELECT I.ItemID, I.Currently
	FROM Items I
	INNER JOIN Bids B
	ON I.ItemID = B.ItemID
	WHERE Ends > '2001-12-20 00:00:01'
	) AS BidI
WHERE Currently = (
	SELECT MAX(Currently)
	FROM(
		SELECT I.ItemID, I.Currently
		FROM Items I
		INNER JOIN Bids B
		ON I.ItemID = B.ItemID
		WHERE Ends > '2001-12-20 00:00:01'
	) AS BidI
);

/*(5)*/
/*
SELECT COUNT(*)
FROM(
	SELECT COUNT(*)
	FROM Users U
	INNER JOIN Items I
	ON U.UserID = I.UserID
	WHERE U.Rating > 100;
)
*/
/*(6)*/


/*(7)*/
