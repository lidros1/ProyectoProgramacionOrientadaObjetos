package modelo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String mail;
    private Date fechaCreacion;
    private Date fechaUltimoAcceso;
    private String estado;
    private List<Permiso> permisos;

    public Usuario() {
        this.permisos = new ArrayList<>();
    }

    public Usuario(String nombreUsuario, String contrasena, String mail) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.mail = mail;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Date getFechaUltimoAcceso() { return fechaUltimoAcceso; }
    public void setFechaUltimoAcceso(Date fechaUltimoAcceso) { this.fechaUltimoAcceso = fechaUltimoAcceso; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public boolean tienePermiso(String nombreArea, String nombreFuncion) {
        for (Permiso permiso : this.permisos) {
            if (permiso.getNombreArea().equalsIgnoreCase(nombreArea) &&
                    permiso.getNombreFuncion().equalsIgnoreCase(nombreFuncion) &&
                    "Activo".equalsIgnoreCase(permiso.getEstado())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        // Asegúrate de que esto devuelve el nombre para el JList.
        return nombreUsuario;
    }
}