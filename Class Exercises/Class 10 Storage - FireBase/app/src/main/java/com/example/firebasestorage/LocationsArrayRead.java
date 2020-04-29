package com.example.firebasestorage;

import java.util.List;

public class LocationsArrayRead
{
    private List<Localizacion> misLocalizaciones;

    public LocationsArrayRead()
    {

    }

    public LocationsArrayRead(List<Localizacion> misLocalizaciones) {
        this.misLocalizaciones = misLocalizaciones;
    }

    public List<Localizacion> getMisLocalizaciones() {
        return misLocalizaciones;
    }

    public void setMisLocalizaciones(List<Localizacion> misLocalizaciones) {
        this.misLocalizaciones = misLocalizaciones;
    }
}
