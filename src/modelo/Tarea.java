package modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Tarea {

    // Campos directos de la tabla 'tareas'
    private int idTarea;
    private int idProyecto;
    private int idEstado;
    private int idPrioridad;
    private String nombreTarea;
    private BigDecimal porcentajeAvance;
    private Date fechaInicio;
    private Date fechaFinalEstimada;
    private String estado; // 'Activo' o 'Inactivo'

    // Campos que vienen de las tablas relacionadas (JOINs)
    private String nombreEstado;
    private String nombrePrioridad;
    private List<Usuario> usuariosDesignados; // <-- NUEVO: Usuarios asignados a la tarea

    public Tarea() {
        this.usuariosDesignados = new ArrayList<>(); // Inicializar la lista
    }

    // --- GETTERS Y SETTERS ---

    public int getIdTarea() { return idTarea; }
    public void setIdTarea(int idTarea) { this.idTarea = idTarea; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }

    public int getIdPrioridad() { return idPrioridad; }
    public void setIdPrioridad(int idPrioridad) { this.idPrioridad = idPrioridad; }

    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }

    public BigDecimal getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(BigDecimal porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFinalEstimada() { return fechaFinalEstimada; }
    public void setFechaFinalEstimada(Date fechaFinalEstimada) { this.fechaFinalEstimada = fechaFinalEstimada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // --- Métodos para los campos de los JOINs ---
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    public String getNombrePrioridad() { return nombrePrioridad; }
    public void setNombrePrioridad(String nombrePrioridad) { this.nombrePrioridad = nombrePrioridad; }

    // --- NUEVOS GETTER Y SETTER ---
    public List<Usuario> getUsuariosDesignados() { return usuariosDesignados; }
    public void setUsuariosDesignados(List<Usuario> usuariosDesignados) { this.usuariosDesignados = usuariosDesignados; }
    // -----------------------------

    @Override
    public String toString() {
        return nombreTarea;
    }
}