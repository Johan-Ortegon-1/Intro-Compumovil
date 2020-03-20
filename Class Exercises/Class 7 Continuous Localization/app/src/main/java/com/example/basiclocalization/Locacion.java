package com.example.basiclocalization;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Locacion
{
    private Date fecha;
    private double latitud;
    private double longitud;

    public Locacion(Date fecha, double latitud, double longitud) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Locacion() {

    }

    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("Latitud: ", getLatitud());
            obj.put("Longitud ", getLongitud());
            obj.put("Fecha: ", getFecha());
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
