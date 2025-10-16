package modelo;

import java.util.Date;

public class Comentario {
    private int idComentario;
    private int idTarea;
    private int idUsuario;
    private String contenido;
    private Date fechaCreacion;
    private String estado;
    private String nombreUsuarioComentario; // Para mostrar quién hizo el comentario

    // Constructor vacío
    public Comentario() {}

    // Getters y Setters
    public int getIdComentario() { return idComentario; }
    public void setIdComentario(int idComentario) { this.idComentario = idComentario; }
    public int getIdTarea() { return idTarea; }
    public void setIdTarea(int idTarea) { this.idTarea = idTarea; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNombreUsuarioComentario() { return nombreUsuarioComentario; }
    public void setNombreUsuarioComentario(String nombreUsuarioComentario) { this.nombreUsuarioComentario = nombreUsuarioComentario; }

    @Override
    public String toString() {
        return "Comentario{" +
                "contenido='" + contenido + '\'' +
                ", nombreUsuarioComentario='" + nombreUsuarioComentario + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}