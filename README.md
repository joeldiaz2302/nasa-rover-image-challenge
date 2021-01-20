#Nasa Image Viewer

## Description
This project is an attempted solution to the following exercise. My solution for the backend utilizes Spring Boot with Jersey and my frontend solution is a ReactJS single page application.

As I understood the challenge I have several endpoints that allow a user to view the rovers and the cameras attached to each rover. Also, since it specified that a file would contain the dates I approched that as a text file upload with a process for validating the dates and then downloading all available images to the server. The front end is shared and contains a form that you can specify which image you want to view and it will filter all images. The current available filters are by rover and further by rover camera. 

I have also set up a basic docker configuration utilizing nginx as a proxy server. It serves the built node application and proxies for the api/image cache server. For this to work both the java project and the node project need to be built.

For the java project navigate to the ./apiserver directory in terminal and run:
``` bash
./gradlew build
```

For the node project navigate to the ./frontend directory in terminal and run:
``` bash
npm install
yarn build
```

For the node application, you need to change the .env file found in the frontend directory. If you are running this in the docker setup you should use:
```env
REACT_APP_API_LOCATION=
```
if you are running locally you should use:
```env
REACT_APP_API_LOCATION=http://localhost:8086
```
### *NOTE*:
Because of some issues with running the initial node install `npm install` in docker on windows I have not included the docerized build commands in the dockerfiles so the builds should be run before starting with compose. 


### Improvements  
This could be improved with the following consideration

* Writing a build script to take care of building the packages and setting configs for build environment (preferably utilizing some docker containers to remove the unreliability of local configutations)
* Add date filtering to the image viewer
* Expand on the endpoint testing


## Programming Exercise

The exercise we’d like to see is to use the NASA API described [here](https://api.nasa.gov) to build a project in GitHub that calls the Mars Rover API and selects a picture on a given day. We want your application to download and store each image locally.

Here is an <https://github.com/jlowery457/nasa-exercise | example> of this exercise done by one of our senior developers.  This is the level of effort we are looking for from someone who wants to join the LAO development team.  

### Acceptance Criteria
* Please send a link to the GitHub repo via <matt.hawkes@livingasone.com> when you are complete.
* Use list of dates below to pull the images were captured on that day by reading in a text ﬁle:
    * 02/27/17
    * June 2, 2018
    * Jul-13-2016
    * April 31, 2018
* Language needs to be *Java*.
* We should be able to run and build (if applicable) locally after you submit it
* Include relevant documentation (.MD, etc.) in the repo

### Bonus 
* Bonus - Unit Tests, Static Analysis, Performance tests or any other things you feel are important for Deﬁnition of Done
* Double Bonus - Have the app display the image in a web browser
* Triple Bonus – Have it run in a Docker or K8s (Preferable)