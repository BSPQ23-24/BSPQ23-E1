# AeroLogix

AeroLogix is a Maven-based Java project that creates a management system for airports.

1. **Set up the database**
    ```
    mysql -u root -p < sql/create-aerologix.sql
    ```
    
1. **Compile the Maven project**

    ```
    mvn clean compile
    ```

2. **Create Database Schema**

    ```
    mvn datanucleus:schema-create
    ```

3. **Start the server**

    ```
    mvn jetty:run
    ```


4. **Start the client**
   
    ```
    mvn exec:java -Pclient
    ```

**Testing the application**
-
1. **Run tests**

    ```
    mvn test
    ```

2. **Run performance tests**

    ```
    mvn verify -Pperformance-test
    ```