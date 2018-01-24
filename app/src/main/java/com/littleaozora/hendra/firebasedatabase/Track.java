package com.littleaozora.hendra.firebasedatabase;

/**
 * Created by Hendra on 1/23/2018.
 */

public class Track {
    private String trackId, trackName;
    private int trackRating;

    public Track(){

    }

    public Track(String trackId, String trackName, int trackRating) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.trackRating = trackRating;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getTrackRating() {
        return trackRating;
    }
}
