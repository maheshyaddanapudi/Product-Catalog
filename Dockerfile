FROM openjdk:8-jdk

ENV DEBIAN_FRONT_END noninteractive

# Declaring internal / defaults variables
ENV POSTGRES_URL jdbc:postgresql://localhost:54131/catalog
ENV POSTGRES_USER catalog
ENV POSTGRES_PASSWORD catalog@1234
ENV SPRING_PROFILES_ACTIVE default
ENV CATALOG_VERSION 0.0.1-SNAPSHOT
ENV USER_TIMEZONE IST

# Switching to root working  directory
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
COPY target/catalog*.jar /appln/bin/catalog/

# Creating the startup script, by passing the env variables to run the jar. Logs are written directly to continer logs.
RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin/catalog" >> /appln/scripts/startup.sh \
  && echo "java \
  -Dspring.profiles.active=\$SPRING_PROFILES_ACTIVE \
  -Duser.timezone=\$USER_TIMEZONE \
  -DPOSTGRES_URL=\$POSTGRES_URL \
  -DPOSTGRES_USER=\$POSTGRES_USER \
  -DPOSTGRES_PASSWORD=\$POSTGRES_PASSWORD \
  -jar catalog-$CATALOG_VERSION.jar" >> /appln/scripts/startup.sh

# Owning the executable scripts
RUN sudo chown -R catalog:catalog /appln/scripts /appln/bin \
    && sudo chmod -R +x /appln/scripts /appln/bin

# Exposing the necessary ports
EXPOSE 8080

# Enabling the startup
CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]
