# [AeroLogix](https://github.com/BSPQ23-24/BSPQ23-E1)

AeroLogix is a Maven-based Java project that creates a management system for airports.

**Building and running the application**
-
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
1. **Run unit tests**
   
   To run unit test you should run the command below. Do not run the server for units tests since it is using Mockito.

    ```
    mvn test
    ```

2. **Run performance tests**

   To run perfomance tests you should run the command below. Do not run the server for units tests since it is using Mockito.

    ```
    mvn verify -Pperformance-tests
    ```

3. **Run integration tests**
   
   In a terminal run the server with:

    ```
    mvn jetty:run
    ```

    Once the server is running, run the following command to run the integration tests:
    
    ```
    mvn verify -Pintegration-tests
    ```
**Generate the documentation**
-
To generate the documentation run the command below. This will generate both html and LaTeX outputs in `aerologix/target/doxygen`.

```
mvn doxygen:report
```

## Documentation
You can find the official project documentation for developers [here](https://bspq23-24.github.io/BSPQ23-E1).
