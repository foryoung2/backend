package com.foryoung.foryoung.global.image;

public enum ImageType {


    VENUE_VIEW("venue-views"),
    PERFORMANCE_POSTER("performance-posters");

    private final String directory;


    ImageType(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }


}