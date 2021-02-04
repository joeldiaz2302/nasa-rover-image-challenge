package com.joeldiaz.nasaroverchallenge.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class NasaRepositoryClient {

    Logger logger = LoggerFactory.getLogger(NasaRepositoryClient.class);

    private Client client;
    private String url;
    private Class<?> classObject;
    private HashMap<String, String> params;

    public NasaRepositoryClient() {
        this.client = ClientBuilder.newClient();
        this.params = new HashMap<>();
    }

    public NasaRepositoryClient(String url) {
        this.client = ClientBuilder.newClient();
        this.url = url;
        this.params = new HashMap<>();
    }

    public NasaRepositoryClient(String url, Class<?> classObject) {
        this.client = ClientBuilder.newClient();
        this.url = url;
        this.classObject = classObject;
        this.params = new HashMap<>();
    }

    public String restGet() {
        ObjectMapper objectMapper = new ObjectMapper();
        WebTarget target = client.target(this.url);

        if (this.params != null) {
            for (String key : this.params.keySet()) {
                target = target.queryParam(key, this.params.get(key));
            }
        }
        logger.debug(target.getUri().toString());
        try {
            return objectMapper.writeValueAsString(target.request(MediaType.APPLICATION_JSON).get(this.classObject));
        } catch (ResponseProcessingException e) {
            logger.error("could not process request", e);
            logger.warn(this.url);
            logger.warn(this.classObject.getName());
        } catch (JsonProcessingException e) {
            logger.error("could not process json data", e);
            logger.warn(this.url);
            logger.warn(this.classObject.getName());
        }
        return null;
    }

    public static String encodeString(String src){
        return DigestUtils.md5DigestAsHex((src.getBytes())) + src.substring(src.lastIndexOf("."));
    }

    public static String downloadFromUrl(String src, String location, String fileName){
        String fileLocation = "src/main/resources/" + location + "/";
        String filePath = fileLocation + fileName;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        boolean completed = false;
        File testFile = new File(filePath);
        if (testFile.exists()){
            return filePath;
        }
        try {
            FileUtils.forceMkdir(new File(fileLocation));
            URL url = new URL(src);
            inputStream = url.openStream();
            outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[2048];
            int length;
 
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            completed = true;
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException :- " + e.getMessage());
 
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException :- " + e.getMessage());
 
        } catch (IOException e) {
            System.out.println("IOException :- " + e.getMessage());
 
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                if (completed){
                    return filePath;
                }
            } catch (IOException e) {
                System.out.println("Finally IOException :- " + e.getMessage());
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<?> getClassObject() {
        return classObject;
    }

    public void setClassObject(Class<?> classObject) {
        this.classObject = classObject;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public void removeParam(String key) {
        if(this.params.containsKey(key)){
            this.params.remove(key);
        }
    }
}
