package com.example.Maledetta_TreEst.line;

public class Line {
    private String arrival;
    private String departure;
    private String did;
    private String direction;

    public Line( String arrival, String departure, String didArrival, String direction) {
        this.departure = departure;
        this.arrival = arrival;
        this.did = didArrival;
        this.direction = direction;
    }

    public String getDidArrival() {
        return did;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDirection() { return direction; }
}
