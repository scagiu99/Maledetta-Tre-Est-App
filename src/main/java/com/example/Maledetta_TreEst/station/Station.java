package com.example.Maledetta_TreEst.station;

public class Station {
    private String sname;
    private double latitudine;
    private double longitudine;

    public Station(String name, String lat, String lon) {
        sname = name;
        latitudine = Double.parseDouble(lat);
        longitudine = Double.parseDouble(lon);
    }

    public String getSname() {
        return sname;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }
}
