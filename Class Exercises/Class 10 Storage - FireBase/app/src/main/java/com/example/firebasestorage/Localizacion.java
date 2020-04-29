package com.example.firebasestorage;

public class Localizacion
{
    private Double latitude;
    private Double longitude;
    private String name;

    public Localizacion()
    {

    }

    public Localizacion(Double latitud, Double longitud, String nombre) {
        this.latitude = latitud;
        this.longitude = longitud;
        this.name = nombre;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }
}
