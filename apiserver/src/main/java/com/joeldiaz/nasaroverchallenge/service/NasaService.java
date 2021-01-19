package com.joeldiaz.nasaroverchallenge.service;

import com.joeldiaz.nasaroverchallenge.properties.ConfigProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
abstract public class NasaService {

    @Inject
    protected ConfigProperties config;
    private Logger logger = LoggerFactory.getLogger(NasaService.class);

    protected abstract String getDestination();

    protected String getUrl(){
        return this.config.getProperty("url") + this.getDestination();
    }

    protected String formatEarthDate(String earthDate) throws ParseException {
        return formatEarthDate(earthDate, "yyyy-M-d");
    }

    private String formatEarthDate(String earthDate, String dateFormat) throws ParseException {
        String [] validDates = {
            "yyyy-M-d", "MMM dd, yyyy", "MMM dd yyyy", "MM/dd/yy", "MM-dd-yy", "MM/dd/yyyy", "MM-dd-yyyy", "MMM/dd/yyyy", "MMM-dd-yyyy", "E MMM dd HH:mm:ss z yyyy"
        };
        Date validDate = null;
        SimpleDateFormat earthDateformatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        for (String format : validDates) {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            formatter.setLenient(false);
            try{
                validDate = formatter.parse(earthDate);
                break;
            } catch (ParseException e){
                logger.debug("Date format did not match valid format : " + format);
            }
        }
        if(validDate == null){
            logger.debug("Date format could not be determined for : " + earthDate);
            throw new ParseException("Date format not supported", 0);
        }
        return earthDateformatter.format(validDate);
    }

    public String uploadFileFromInputStream(InputStream uploadedInputStream, String fileName) {
        String fileLocation = "src/main/resources/static/uploads/" + fileName;
        logger.warn(fileLocation);
        try {
            FileUtils.forceMkdir(new File("src/main/resources/static/uploads/"));
            FileOutputStream out = new FileOutputStream(new File(fileLocation));  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            out = new FileOutputStream(new File(fileLocation));  
            while ((read = uploadedInputStream.read(bytes)) != -1) {  
                out.write(bytes, 0, read);  
            }  
            out.flush();  
            out.close();  
            return fileLocation;   
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn(fileLocation);
        return null;
    }

    public ConfigProperties getConfig() {
        if (config == null){
            config = new ConfigProperties();
        }
        return config;
    }

    public void setConfig(ConfigProperties config) {
        this.config = config;
    }
}
