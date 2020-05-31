package com.document.appfiles.Clases;

public class ClsClientes {

    String id_cliente;
    String nomnre_cliente;
    String celular_cliente;
    String correo_cliente;
    String direccion_cliente;

    public ClsClientes(String id_cliente, String nomnre_cliente, String celular_cliente, String correo_cliente, String direccion_cliente) {
        this.id_cliente = id_cliente;
        this.nomnre_cliente = nomnre_cliente;
        this.celular_cliente = celular_cliente;
        this.correo_cliente = correo_cliente;
        this.direccion_cliente = direccion_cliente;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNomnre_cliente() {
        return nomnre_cliente;
    }

    public void setNomnre_cliente(String nomnre_cliente) {
        this.nomnre_cliente = nomnre_cliente;
    }

    public String getCelular_cliente() {
        return celular_cliente;
    }

    public void setCelular_cliente(String celular_cliente) {
        this.celular_cliente = celular_cliente;
    }

    public String getCorreo_cliente() {
        return correo_cliente;
    }

    public void setCorreo_cliente(String correo_cliente) {
        this.correo_cliente = correo_cliente;
    }

    public String getDireccion_cliente() {
        return direccion_cliente;
    }

    public void setDireccion_cliente(String direccion_cliente) {
        this.direccion_cliente = direccion_cliente;
    }
}
