# Spring Boot Product Catalog
## With Embedded Database(Mongo)
## With External Database(Mongo) support
## With Docker and Docker Compose ready containerization

##### The README covers on the operational part of Catalog. For more details on the code level walkthrough / developer point of view, please contact developer.

##### Note

      • Only Mongo is supported as external Database
      
      				Or
      
      • Embedded Mongo is supported, which is non-persistent and data is lost, thus eliminating the need for external for demo runs and unit tests.

#### Instant local set-up - Refer to  <a href="https://github.com/maheshyaddanapudi/catalog/blob/develop/INIT_LOCAL_SETUP.md" target="_blank">Local Environment SetUp</a>

## Overview

The idea is to build a single production grade Spring Boot Jar with the following

      • Restful APIs for Product Catalog management - Create, Update, View, Delete Products.

## Tech / Framework used

      --> Docker Image to host the Jar. (Will be added soon - with pre built jar inside docker image)
	  			
      --> Spring Boot - 2.2.4 Release
			
            • Mongo
            
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
		
	Below command will start the Catalog, please pass the database connection details along (default values shown below)
        java \
      -DMONGODB_HOST=localhost \
      -DMONGODB_PORT=27017 \
      -DMONGODB_USERNAME=catalog \
      -DMONGODB_PASSWORD=Catalog!234 \
      -DMONGODB_DATABASE=catalog \
      -jar catalog-0.0.1-SNAPSHOT.jar

## Application URLs

		HTTP
			a) http://localhost:8080/ - To access the Swagger pertaining to APIs for Product Catalog

## Run Catalog Boot : Docker

To run the container :

      docker run --name catalog -p 8080:8080 \
            -e ENV MONGODB_HOST=localhost \
            -e MONGODB_PORT=27017
            -e MONGODB_USERNAME=root
            -e MONGODB_PASSWORD=Root!234
            -e MONGODB_DATABASE=catalog \
            -d catalog:latest

#### All the below mentioned configurables / properties (under Available Profiles section) can be passed as Docker Container environment variables and will be set accordingly.

## Run Catalog : Docker Compose

To run the docker-compose : For externalizing database into different containter, using their corresponding official dockerhub images.

    docker-compose up or docker-compose -d (For deamonizing the processes)

If -d was used, then after the containers startup, logs can be verified by the following command (the same command can be used in new terminal / command prompt window in case -d wasn't used)

    docker logs --follow catalog_catalog_1

Once all containers are started successfully, the "docker ps" output should look something similar to below.

    CONTAINER ID   IMAGE                   COMMAND                  CREATED          STATUS                   PORTS                                              NAMES
    2cc0e92ebf50   catalog:latest          "/bin/bash /appln/sc…"   5 minutes ago    Up 2 minutes (healthy)   0.0.0.0:8080->8080/tcp                             catalog_catalog_1
    718dea9898f0   mongo:latest         "/docker-entrypoint.…"   5 minutes ago    Up 3 minutes (healthy)   0.0.0.0:27017->27017/tcp                             catalog_mongodb_1

For mapping volumes i.e. having persistent container data, follow these steps.

    1) Create the following directories (These are custimizable to match docker-compose.yml volumes definitions)

        container/persistence/mongo

    2) Uncomment the volumes section under Mongo


## Test Case - 1

Open http://localhost:8080 in browser (Preferably Chrome)

1) Get Product Details 

          Request
                ID : 13860428
    
          Response
              {
                  "id": 13860428,
                  "name": "The Big Lebowski (Blu-ray)",
                  "current_price": null
              }
   
   Notice the name returned is from RedSky API Response "The Big Lebowski (Blu-ray)" and price is null as there is no price persisted yet, which will be done in #2 and reverified in #4 below.

2) Post new Product Pricing Request
        
            {
                "id": 13860428,
                "name": "Placeholder Name - from Database",
                "current_price": {
                "value": 13.49,
                "currency_code": "USD
            }

3) Post new Product Pricing Request

            {
                "id": 123456,
                "name": "RedSky Non existant - from Database",
                "current_price": {
                "value": 33.49,
                "currency_code": "USD
                }
            }

4) Get Product Details 

          Request
                ID : 13860428
    
          Response
              {
                  "id": 13860428,
                  "name": "The Big Lebowski (Blu-ray)",
                  "current_price": {
                  "value": 13.49,
                  "currency_code": "USD"
                  }
              }
   
   Notice the name returned is from RedSky API Response "The Big Lebowski (Blu-ray)" and not from database "Placeholder Name - from Database"

5) Get Product Details

       Request
            ID : 123456
    
       Response
            {
                "id": 123456,
                "name": "RedSky Non existant - from Database",
                "current_price": {
                "value": 33.49,
                "currency_code": "USD
                }
            }

   Notice the name returned is NOT from RedSky API Response as its not found on RedSky and from database "RedSky Non existant - from Database"
