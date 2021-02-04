package com.joeldiaz.nasaroverchallenge.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.joeldiaz.nasaroverchallenge.service.PictureOfTheDayService;

import org.springframework.web.bind.annotation.RestController;

@RestController
@Path("api/")
public class PictureOfTheDayResource {
    
    @Inject
    private PictureOfTheDayService potdService;

    @Produces(MediaType.APPLICATION_JSON)
    @GET @Path("potd")
    public String potd() {
        return potdService.getPictureOfTheDay(null);
    }

}
