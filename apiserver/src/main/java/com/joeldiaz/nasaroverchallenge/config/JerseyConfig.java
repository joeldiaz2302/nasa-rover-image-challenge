package com.joeldiaz.nasaroverchallenge.config;

import com.joeldiaz.nasaroverchallenge.resource.PictureOfTheDayResource;
import com.joeldiaz.nasaroverchallenge.resource.RoverResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

        public JerseyConfig() {
            property(ServletProperties.FILTER_FORWARD_ON_404, true);
            register(CORSResponseFilter.class);
            register(PictureOfTheDayResource.class);
            register(RoverResource.class);
        }
}
