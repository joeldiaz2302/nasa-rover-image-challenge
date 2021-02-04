package com.joeldiaz.nasaroverchallenge;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeldiaz.nasaroverchallenge.beans.nasa.PictureOfTheDay;
import com.joeldiaz.nasaroverchallenge.client.NasaRepositoryClient;
import com.joeldiaz.nasaroverchallenge.properties.ConfigProperties;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ConfigProperties.class)
@SpringBootTest
public class NasaRepositoryClientTest {
    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod";
    
    @Test
    public void clientDownloadFromUrlTest() {
        ConfigProperties config = new ConfigProperties();
        NasaRepositoryClient client = new NasaRepositoryClient(BASE_URL, PictureOfTheDay.class);
        client.addParam("api_key", config.getProperty("api_key"));
        String response = client.restGet();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PictureOfTheDay data = objectMapper.readValue(response, PictureOfTheDay.class);
            String photoName = NasaRepositoryClient.encodeString(data.url);
            String location = NasaRepositoryClient.downloadFromUrl(data.url, "static/images/test/potd", photoName);
            File download = new File(location);
            assertTrue(download.exists());
            assertTrue(download.isFile());
            assertTrue(FileUtils.sizeOf(download) > 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    public void clientRequestSuccessTest() {
        ConfigProperties config = new ConfigProperties();
        NasaRepositoryClient client = new NasaRepositoryClient(BASE_URL, PictureOfTheDay.class);
        client.addParam("api_key", config.getProperty("api_key"));
        String response = client.restGet();
        assertNotNull(response, "client response is not null");
    }

    @Test
    public void clientRequestFailedTest() {
        ConfigProperties config = new ConfigProperties();
        NasaRepositoryClient client = new NasaRepositoryClient(BASE_URL + "dead", PictureOfTheDay.class);
        client.addParam("api_key", config.getProperty("api_key"));
        Exception exception = assertThrows(NotFoundException.class, () -> {
            client.restGet();
        });
        assertNotNull(exception);
    }
}
