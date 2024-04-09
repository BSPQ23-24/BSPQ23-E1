AeroLogix
----------------------

First, the whole code can be compile using:

    mvn compile

Then, in three separate shell windows, run the following commands:
    
    mvn jetty:run
    mvn exec:java -Pserver
    mvn exec:java -Pclient
