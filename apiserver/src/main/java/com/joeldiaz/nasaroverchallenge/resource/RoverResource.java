package com.joeldiaz.nasaroverchallenge.resource;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.joeldiaz.nasaroverchallenge.service.RoverService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Path("api/")
public class RoverResource {

    @Inject
    private RoverService service;

    protected Logger logger = LoggerFactory.getLogger(RoverResource.class);

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("rover")
    public String getRovers() {
        return service.getRoversList();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("rover/images")
    public Response getAllRoversStoredPhotos() {
        return service.getRoversStoredPhotosList(null, null);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("rover/{rover}/images")
    public Response getRoversStoredPhotos(@PathParam("rover") String rover) {
        return service.getRoversStoredPhotosList(rover, null);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("rover/{rover}/images/{camera}")
    public Response getRoversStoredPhotosForCamera(@PathParam("rover") String rover, @PathParam("camera") String camera) {
        return service.getRoversStoredPhotosList(rover, camera);
    }

    @POST
    @Path("rover/dates_upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response getAllRoversPhotosDateFileUpload(@Context HttpServletRequest request) {
        return processImageDownloadFromUploadedDateFile(request, null, null);
    }

    @POST
    @Path("rover/{rover}/dates_upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response getRoversPhotosDateFileUpload(@Context HttpServletRequest request,
            @PathParam("rover") String rover) {
        return processImageDownloadFromUploadedDateFile(request, rover, null);
    }

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @Path("rover/{rover}/dates_upload/{camera}")
    public Response getRoversPhotosDateFileUploadForCamera(@Context HttpServletRequest request,
            @PathParam("rover") String rover, @PathParam("camera") String camera) {
        return processImageDownloadFromUploadedDateFile(request, rover, camera);
    }

    private Response processImageDownloadFromUploadedDateFile(HttpServletRequest request, String rover, String camera) {
        String fileLocation;
        try {
            fileLocation = service.uploadFileFromInputStream(request.getInputStream(), "dates_config.txt");
            logger.info(fileLocation);
            return service.downloadPhotosForRoverCamera(rover, camera, fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Upload file could not be found.").build();
    }

}
