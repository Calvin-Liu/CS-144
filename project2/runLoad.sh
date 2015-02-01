#!/bin/bash

mysql CS144 < drop.sql
mysql CS144 < create.sql

ant
ant run-all
#ant run-all > outputOfAnt.txt
sort -u item.dat > sortUItems.dat
sort -u user.dat > sortUUsers.dat
sort -u bids.dat > sortUBids.dat
sort -u category.dat > sortUCategories.dat

mysql CS144 < load.sql
mysql CS144 < queries.sql

rm *.dat
