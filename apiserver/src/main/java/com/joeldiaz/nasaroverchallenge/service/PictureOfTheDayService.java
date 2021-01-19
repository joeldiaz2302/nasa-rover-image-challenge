package com.joeldiaz.nasaroverchallenge.service;

import com.joeldiaz.nasaroverchallenge.beans.nasa.PictureOfTheDay;
import com.joeldiaz.nasaroverchallenge.client.NasaRepositoryClient;

import org.springframework.stereotype.Component;

@Component
public class PictureOfTheDayService extends NasaService {

    public String getPictureOfTheDay(String earthDate) {
        NasaRepositoryClient client = new NasaRepositoryClient(this.getUrl(), PictureOfTheDay.class);
        client.addParam("api_key", this.config.getProperty("api_key"));
        return client.restGet();
    }

    @Override
    protected String getDestination() {
        return "planetary/apod";
    }
    
}
