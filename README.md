# AeroLogix

AeroLogix is a Maven-based Java project that creates a management system for airports.

1. **Set up the database**

    mysql -u root -p < sql/create-aerologix.sql

2. **Compile the Maven project**

    mvn clean compile

3. **Create Database Schema**

    mvn datanucleus:schema-create

4. **Start the server**

    mvn jetty:run

5. **Start the client**

    mvn exec:java -Pclient

