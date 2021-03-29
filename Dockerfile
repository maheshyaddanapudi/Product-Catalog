# To Run this file try "docker build -t catalog:latest -f Dockerfile_WithBuild ."

FROM maven:3.6.3-jdk-8 as builder

# Setting JAVA_HOME for performing Maven build.
ENV JAVA_HOME /usr/local/openjdk-8
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Creating base directory
RUN mkdir /tmp/catalog

COPY pom.xml /tmp/catalog/
COPY src /tmp/catalog/src

# Building the executable.
RUN cd /tmp/catalog \
  && mvn clean install -Dmaven.test.skip=true -q

FROM openjdk:8-jdk as server

ENV DEBIAN_FRONT_END noninteractive

# Declaring internal / defaults variables
ENV MONGODB_HOST localhost
ENV MONGODB_PORT 27017
ENV MONGODB_USERNAME root
ENV MONGODB_PASSWORD Root!234
ENV MONGODB_DATABASE catalog
ENV SPRING_PROFILES_ACTIVE default
ENV CATALOG_VERSION 0.0.1-SNAPSHOT

WORKDIR /

# Starting up as root user
USER root

# Installing all the base necessary packages for execution of embedded MariaDB4j i.e. Open SSL, libaio & libncurses5
RUN apt-get -y -qq update --ignore-missing --fix-missing \
  && apt-get -y -qq install sudo

# Creating necessary directory structures to host the platform
RUN mkdir /appln /appln/bin /appln/bin/catalog /appln/scripts

# Creating a dedicated user catalog
RUN groupadd -g 999 catalog \
  && useradd -u 999 -g catalog -G sudo --create-home -m -s /bin/bash catalog \
  && echo -n 'catalog:catalog' | chpasswd

# Delegating password less SUDO access to the user catalog
RUN sed -i.bkp -e \
      's/%sudo\s\+ALL=(ALL\(:ALL\)\?)\s\+ALL/%sudo ALL=NOPASSWD:ALL/g' \
      /etc/sudoers

# Taking the ownership of working directories
RUN chown -R catalog:catalog /appln

# Changing to the user catalog
USER catalog

# Moving the executable / build to the run location
COPY --from=builder /tmp/catalog/target/catalog*.jar /appln/bin/catalog/

# Creating the startup script, by passing the env variables to run the jar. Logs are written directly to continer logs.
RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin/catalog" >> /appln/scripts/startup.sh \
  && echo "java \
  -Dspring.profiles.active=\$SPRING_PROFILES_ACTIVE \
  -DMONGODB_HOST=\$MONGODB_HOST \
  -DMONGODB_PORT=\$MONGODB_PORT \
  -DMONGODB_USERNAME=\$MONGODB_USERNAME \
  -DMONGODB_PASSWORD=\$MONGODB_PASSWORD \
  -DMONGODB_DATABASE=\$MONGODB_DATABASE \
  -jar catalog-$CATALOG_VERSION.jar" >> /appln/scripts/startup.sh

# Owning the executable scripts
RUN sudo chown -R catalog:catalog /appln/scripts /appln/bin \
    && sudo chmod -R +x /appln/scripts /appln/bin

# Exposing the necessary ports
EXPOSE 8080

# Enabling the startup
CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]