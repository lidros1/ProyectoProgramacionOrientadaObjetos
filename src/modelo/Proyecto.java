package modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Proyecto {
    private int idProyecto;
    private String nombreProyecto;
    private String descripcionProyecto;
    private BigDecimal porcentajeAvance;
    private Date fechaInicio;
    private Date fechaFinalEstimada;
    private String nombreEstado;
    private int idEstado; // <-- ATRIBUTO AÑADIDO
    private String nombrePrioridad;
    private int idPrioridad;
    private int idJerarquiaUsuario;
    private List<Usuario> usuariosDesignados;

    public Proyecto() {
        this.usuariosDesignados = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }
    public String getNombreProyecto() { return nombreProyecto; }
    public void setNombreProyecto(String nombreProyecto) { this.nombreProyecto = nombreProyecto; }
    public String getDescripcionProyecto() { return descripcionProyecto; }
    public void setDescripcionProyecto(String descripcionProyecto) { this.descripcionProyecto = descripcionProyecto; }
    public BigDecimal getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(BigDecimal porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFinalEstimada() { return fechaFinalEstimada; }
    public void setFechaFinalEstimada(Date fechaFinalEstimada) { this.fechaFinalEstimada = fechaFinalEstimada; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public String getNombrePrioridad() { return nombrePrioridad; }
    public void setNombrePrioridad(String nombrePrioridad) { this.nombrePrioridad = nombrePrioridad; }
    public int getIdJerarquiaUsuario() { return idJerarquiaUsuario; }
    public void setIdJerarquiaUsuario(int idJerarquiaUsuario) { this.idJerarquiaUsuario = idJerarquiaUsuario; }
    public int getIdPrioridad() { return idPrioridad; }
    public void setIdPrioridad(int idPrioridad) { this.idPrioridad = idPrioridad; }
    public List<Usuario> getUsuariosDesignados() { return usuariosDesignados; }
    public void setUsuariosDesignados(List<Usuario> usuariosDesignados) { this.usuariosDesignados = usuariosDesignados; }

    // --- MÉTODOS AÑADIDOS PARA CORREGIR EL ERROR ---
    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }
    // ---------------------------------------------

    @Override
    public String toString() {
        return nombreProyecto;
    }
}