package com.thegiantgames.wallpapers;

public class wallpaperModel {


    private int id;
    private String originalurl , mediumurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalurl() {
        return originalurl;
    }

    public void setOriginalurl(String originalurl) {
        this.originalurl = originalurl;
    }

    public String getMediumurl() {
        return mediumurl;
    }

    public void setMediumurl(String mediumurl) {
        this.mediumurl = mediumurl;
    }

    public wallpaperModel(int id, String originalurl, String mediumurl) {
        this.id = id;
        this.originalurl = originalurl;
        this.mediumurl = mediumurl;
    }

    public wallpaperModel() {
    }
}
