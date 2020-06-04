package com.document.appfiles.Clases;

public class ClsArchivos {

    String id_archivo;
    String nombre_archivo;
    String tipo_archivo;
    String peso_archivo;
    String fecha_archivo;
    String ruta_archivo;
    String ruta_local_archivo;
    public ClsArchivos(){

    }

    public ClsArchivos(String id_archivo, String nombre_archivo, String tipo_archivo, String peso_archivo, String fecha_archivo,String ruta_archivo) {
        this.id_archivo = id_archivo;
        this.nombre_archivo = nombre_archivo;
        this.tipo_archivo = tipo_archivo;
        this.peso_archivo = peso_archivo;
        this.fecha_archivo = fecha_archivo;
        this.ruta_archivo=ruta_archivo;
    }

    public String getId_archivo() {
        return id_archivo;
    }

    public void setId_archivo(String id_archivo) {
        this.id_archivo = id_archivo;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }

    public String getTipo_archivo() {
        return tipo_archivo;
    }

    public void setTipo_archivo(String tipo_archivo) {
        this.tipo_archivo = tipo_archivo;
    }

    public String getPeso_archivo() {
        return peso_archivo;
    }

    public void setPeso_archivo(String peso_archivo) {
        this.peso_archivo = peso_archivo;
    }

    public String getFecha_archivo() {
        return fecha_archivo;
    }

    public void setFecha_archivo(String fecha_archivo) {
        this.fecha_archivo = fecha_archivo;
    }

    public String getRuta_archivo() {
        return ruta_archivo;
    }

    public void setRuta_archivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }
}
