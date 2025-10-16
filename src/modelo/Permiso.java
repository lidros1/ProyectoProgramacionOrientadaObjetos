package modelo;

public class Permiso {
    private int idPermisoUsuario;
    private int idUsuario;
    private int idAreaSistema;
    private String nombreArea; // Nombre del área del sistema (ej. "Gestion de Usuarios")
    private int idFuncion;
    private String nombreFuncion; // Nombre de la función (ej. "Crear")
    private String estado;

    // Constructor vacío
    public Permiso() {
    }

    // Getters y Setters
    public int getIdPermisoUsuario() {
        return idPermisoUsuario;
    }

    public void setIdPermisoUsuario(int idPermisoUsuario) {
        this.idPermisoUsuario = idPermisoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAreaSistema() {
        return idAreaSistema;
    }

    public void setIdAreaSistema(int idAreaSistema) {
        this.idAreaSistema = idAreaSistema;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public String getNombreFuncion() {
        return nombreFuncion;
    }

    public void setNombreFuncion(String nombreFuncion) {
        this.nombreFuncion = nombreFuncion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreArea + " - " + nombreFuncion;
    }
}