package com.joeldiaz.nasaroverchallenge.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {

    private Logger logger = LoggerFactory.getLogger(ConfigProperties.class);
    private Properties props;

    public ConfigProperties(){
        String realPath = "src/main/resources/configuration.properties";
        try {
            this.props = new Properties();
            this.props.load(new FileInputStream(new File(realPath)));
 
        } catch (IOException ex) {
            logger.error("Nasa Api Properties not loaded", ex);
        }
    }

    public String getProperty(String key) {
        return this.props.getProperty(key);
    }
}
