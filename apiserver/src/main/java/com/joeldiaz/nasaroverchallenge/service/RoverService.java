package com.joeldiaz.nasaroverchallenge.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeldiaz.nasaroverchallenge.beans.nasa.Photo;
import com.joeldiaz.nasaroverchallenge.beans.nasa.PhotoList;
import com.joeldiaz.nasaroverchallenge.beans.nasa.Rover;
import com.joeldiaz.nasaroverchallenge.beans.nasa.RoverList;
import com.joeldiaz.nasaroverchallenge.client.NasaRepositoryClient;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RoverService extends NasaService {

    private Logger logger = LoggerFactory.getLogger(RoverService.class);

    public String getRoversList() {
        NasaRepositoryClient client = new NasaRepositoryClient(this.getUrl(), RoverList.class);
        client.addParam("api_key", this.config.getProperty("api_key"));
        return client.restGet();
    }

    public Response downloadPhotosForRoverCamera(String rover, String camera, String dateFile) {
        Set<String> rovers = getRoversNamesSet(rover);
        ObjectMapper objectMapper = new ObjectMapper();
        for (String rov : rovers) {
            new Thread(() -> {
                this.processImageUpload(rov, camera, dateFile, objectMapper);
            }).start();
        }
        return Response.ok("File Uploaded").build();
    }

    public Response getRoversStoredPhotosList(String roverName, String camera) {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<String> rovers = getRoversNamesSet(roverName);
        Set<String> foundImages = new HashSet<String>();
        String list = "[]";
        try {
            for (String rover : rovers) {
                File dir;
                if(rover == null){
                    dir = new File("src/main/resources/static/images");
                }else{
                    dir = new File("src/main/resources/static/images/" + rover);
                }
                if (dir.exists() && dir.isDirectory()) {
                    List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

                    try {
                        for (File file : files) {
                            if(camera != null){
                                if (!file.getAbsolutePath().contains(camera)){
                                    continue;
                                }
                            }
                            logger.debug("images/" + rover + file.toPath().toString().replaceAll("\\\\", "/")
                                        .substring(file.toPath().toString().indexOf(rover) + rover.length()));
                            URI uri = new URI("images/" + rover + file.toPath().toString().replaceAll("\\\\", "/")
                                        .substring(file.toPath().toString().indexOf(rover) + rover.length()));
                            foundImages.add(uri.toString());
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } 
            list = objectMapper.writeValueAsString(foundImages);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Response.ok(list).build();
    }

    //These are tested by virtue of testign the upload to download method/flow

    private Set<String> getRoversNamesSet(String rover){
        Set<String> rovers = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if(rover == null){
            try {
                List<Rover> roversList = (objectMapper.readValue(this.getRoversList(), RoverList.class)).getRovers();
                for (Rover r : roversList) {
                    logger.debug(r.getName());
                    rovers.add(r.getName());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            rovers.add(rover);
        }
        return rovers;
    }

    private String getRoversCameraPhotosList(String roverName, String camera, String date) {
        NasaRepositoryClient client = new NasaRepositoryClient(this.getUrl() + "/" + roverName + "/photos",
                PhotoList.class);
        logger.debug("getting photos for" + roverName + " from camera " + camera);
        client.addParam("api_key", this.config.getProperty("api_key"));
        try {
            if (date != null) {
                client.addParam("earth_date", this.formatEarthDate(date));
            }
        } catch (ParseException e) {
            logger.error("Date failed to parse", e);
        }
        if (camera != null) {
            client.addParam("camera", camera);
        }
        return client.restGet();
    }

    private void processImageUpload(String rover, String camera, String dateFileLocation, ObjectMapper objectMapper){
        String downloadLocation = "static/images/" + rover + "/";
        logger.debug("static/images/" + rover + "/");
        logger.debug(camera + " " + dateFileLocation);
        if(dateFileLocation != null){
            try {
                Scanner scanner = new Scanner(new File(dateFileLocation));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    try {
                        String earthDate = this.formatEarthDate(line);
                        PhotoList photos = objectMapper
                                .readValue(this.getRoversCameraPhotosList(rover, camera, earthDate), PhotoList.class);
                        for (Photo photo : photos.getPhotos()) {
                            String photoName = NasaRepositoryClient.encodeString(photo.getImgSrc());
                            NasaRepositoryClient.downloadFromUrl(photo.getImgSrc(),
                                    downloadLocation + "/" + photo.camera.getName() + "/" + earthDate.replaceAll("-", ""), photoName);
                        }
                    } catch (ParseException e) {
                        //This exception doesn't need to be logged as an error, it is expected and fine to not load a bad date
                        logger.debug("Could not parse date from file line: " + line);
                    } catch (JsonMappingException e) {
                        logger.error("Could not load photo data", e);
                    } catch (JsonProcessingException e) {
                        logger.error("Could not load photo data", e);
                    }
                }
            } catch (IOException e) {
                logger.error("input stream could not be loaded", e);
            }
        }
    }
    
    @Override
    protected String getDestination() {
        return "mars-photos/api/v1/rovers";
    }
    
}
