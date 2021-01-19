FROM openjdk:8-jdk

RUN apt-get update && apt-get install -y bash

# Set working directory
WORKDIR /var/www
# Copy existing application directory contents
COPY . /var/www

RUN ./apiserver/gradlew build
