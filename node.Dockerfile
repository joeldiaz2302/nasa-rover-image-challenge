FROM node:11

# Install dependencies
RUN npm install
RUN npm install yarn -g
RUN apt-get install -y bash

