# OnlineStore API

### Directions

To set up and run the application, follow these steps:

1. Clone the repository to your local machine
2. Download the H2 binary JAR
   from [http://www.h2database.com/html/download.html](http://www.h2database.com/html/download.html)
3. Open a terminal and navigate to the directory where the H2 JAR file is located
4. Run the H2 JAR by executing the command `java -jar h2-{VERSION}.jar`, replacing `{VERSION}` with the specific version
   number
5. Once the H2 console page opens in your web browser, click the `Connect` button to establish a connection
6. Navigate to the project directory in the terminal
7. Run the command `mvn spring-boot:run` to start the Java Maven application

### API Documentation

The API documentation is available through Swagger. You can access it using the following URL:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html#/)

### Credentials and Roles

API uses basic authentication and use the following credentials for testing the application:

- **Admin Role**
    - Username: `admin`
    - Password: `123`

- **User Role**
    - Username: `user`
    - Password: `pass`
