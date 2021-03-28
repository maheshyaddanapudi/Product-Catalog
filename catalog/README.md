# Spring Boot Product Catalog 
## With Embedded Database(Postgres) 
## With External Database(Postgres) support
## With Docker and Docker Compose ready containerization

##### The README covers on the operational part of Catalog. For more details on the code level walkthrough / developer point of view, please contact developer.

##### Note

      • Only Postgres is supported as external Database
      
      				Or
      
      • Embedded Postgres is supported, which is non-persistent and data is lost, thus eliminating the need for external for demo runs and unit tests.


## Overview

The idea is to build a single production grade Spring Boot Jar with the following 

      • Restful APIs for Product Catalog management - Create, Update, View, Delete Products.
      
      • Optional Embedded Non-Persistent Postgres

## Tech / Framework used

      --> Docker Image to host the Jar. (Will be added soon - with pre built jar inside docker image)
	  			
      --> Spring Boot - 2.2.4 Release
			
            • Postgres
            
            • JPA
            
            • OpenAPI Swagger UI
            
            • Logbook Splunk style HTTP logging
## Build using maven

		cd <to project root folder>
		mvn clean install
		
	The maven build should place the catalog-${CATALOG_VERSION}.jar inside the target folder.

### Build Docker Image

After Maven Build. Build the Docker Image with the following command

	    docker build -t catalog:latest .

Verify docker image with

        docker images

## Run Catalog : Java

		cd <to project root folder>/target
		
	Below command will start the Catalog with Embedded Postgres as Database
		java -jar catalog-0.0.1-SNAPSHOT.jar

    The profiles included by default are
        - default (This includes embedded Postgres Database)

### Available Profiles

    1) default
        This profile holds the basic startup configuration needed for the Spring Boot Jar with embedded Postgres Database.

    2) postgres
        This profile configures the external Postgres database details.
        Configurations available are as below. Shown are default values.
            POSTGRES_URL=jdbc:postgresql://localhost:54131/catalog
            POSTGRES_USER=catalog
            POSTGRES_PASSWORD=catalog@1234

    For more detailed properties, refer to application-{profile}.yml file as per the required profile properties.

## Application URLs

		HTTP
			a) http://localhost:8080/ - To access the Swagger pertaining to APIs for Product Catalog

## Run Catalog Boot : Docker

To run the container :

    docker run --name catalog -p 8080:8080 -d catalog:latest

Few other examples / ways / configurations to run the container as:

    1) Running with external Postgres - The database  is decoupled just leaving the core functionality.

        docker run --name catalog -p 8080:8080 \
            -e SPRING_PROFILES_ACTIVE=postgres \
            -e POSTGRES_URL=jdbc:postgresql://localhost:54131/catalog \
            -e POSTGRES_USER=catalog \
            -e POSTGRES_PASSWORD=catalog@1234 \
            -d catalog:latest

#### All the below mentioned configurables / properties (under Available Profiles section) can be passed as Docker Container environment variables and will be set accordingly.

Available configurables - shown below with default values.

    POSTGRES_URL jdbc:postgresql://localhost:54131/catalog
    POSTGRES_USER catalog
    POSTGRES_PASSWORD catalog@1234
    SPRING_PROFILES_ACTIVE default
    USER_TIMEZONE IST

## Run Catalog : Docker Compose

To run the docker-compose : For externalizing database into different containter, using their corresponding official dockerhub images.

    docker-compose up or docker-compose -d (For deamonizing the processes)

If -d was used, then after the containers startup, logs can be verified by the following command (the same command can be used in new terminal / command prompt window in case -d wasn't used)

    docker logs --follow catalog_catalog_1

Once all containers are started successfully, the "docker ps" output should look something similar to below.

    CONTAINER ID   IMAGE                   COMMAND                  CREATED          STATUS                   PORTS                                              NAMES
    2cc0e92ebf50   catalog:latest          "/bin/bash /appln/sc…"   5 minutes ago    Up 2 minutes (healthy)   0.0.0.0:8080->8080/tcp                             catalog_catalog_1
    718dea9898f0   postgres:latest         "/docker-entrypoint.…"   5 minutes ago    Up 3 minutes (healthy)   0.0.0.0:5432->5432/tcp                             catalog_postgres_1

For mapping volumes i.e. having persistent container data, follow these steps.

    1) Create the following directories (These are custimizable to match docker-compose.yml volumes definitions)

        container/persistence/postgres

    2) Uncomment the volumes section under postgres
