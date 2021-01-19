package com.joeldiaz.nasaroverchallenge.beans.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo {
    public int id;
    public int sol;
    public RoverCamera camera;
    @JsonProperty("img_src")
    public String imgSrc;
    @JsonProperty("earth_date")
    public String earthDate;
    public SlimRover rover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public RoverCamera getCamera() {
        return camera;
    }

    public void setCamera(RoverCamera camera) {
        this.camera = camera;
    }

    public String getImgSrc() {
        String src = imgSrc;
        if(src.indexOf("https://") != 0 ){
            if(src.indexOf("http://") == 0){
                src = "https://" + imgSrc.substring(7);
            }else{
                src = "https://" + imgSrc;
            }
        }
        return src;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public SlimRover getRover() {
        return rover;
    }

    public void setRover(SlimRover rover) {
        this.rover = rover;
    }

}
