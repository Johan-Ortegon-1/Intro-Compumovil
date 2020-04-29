package com.example.firebasestorage;

import java.util.ArrayList;

public class Usuario
{
    private String uID;
    private String correo;
    private String password;
    private String autorFvorito;
    private ArrayList<Libro> librosLeidos;

    //Localizacion
    private Double latitud;
    private Double longitud;

    public Usuario()
    {

    }

    public Usuario(String uID, String correo, String password, String autorFvorito, ArrayList<Libro> librosLeidos, Double latitud, Double longitud) {
        this.uID = uID;
        this.correo = correo;
        this.password = password;
        this.autorFvorito = autorFvorito;
        this.librosLeidos = librosLeidos;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**GETTERS AND SETTERS**/

    public String getuID() {
        return uID;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public String getAutorFvorito() {
        return autorFvorito;
    }

    public ArrayList<Libro> getLibrosLeidos() {
        return librosLeidos;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAutorFvorito(String autorFvorito) {
        this.autorFvorito = autorFvorito;
    }

    public void setLibrosLeidos(ArrayList<Libro> librosLeidos) {
        this.librosLeidos = librosLeidos;
    }
}
