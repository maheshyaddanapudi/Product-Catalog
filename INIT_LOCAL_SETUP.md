# Initial Run SetUp - Local - Developers / Demo

## Pre-Requisites

1) Docker

## Environment Setup

1) Build the Catalog Docker Image

        docker-compose build 

   This command might take 5-7 mins to build the image depending on the system configuration and network speed.

2) Run the Containers

        docker-compose up -d

   This command might take a minute to fire up the containers.

3) Verify Containers Health

        docker ps

        CONTAINER ID   IMAGE            COMMAND                  CREATED         STATUS                   PORTS                      NAMES
        de576cd18c27   catalog:latest   "/bin/bash /appln/sc…"   3 minutes ago   Up 3 minutes (healthy)   0.0.0.0:8080->8080/tcp     catalog_catalog_1
        6aa7b503eaf6   mongo:latest     "docker-entrypoint.s…"   3 minutes ago   Up 3 minutes (healthy)   0.0.0.0:27017->27017/tcp   catalog_mongodb_1

The container health status is displayed along with uptime. Once both containers are healthy, the environment is ready.

Open http://localhost:8080 in browser (Preferably Chrome)
