// Archivo: src/modelo/Prioridad.java
package modelo;

public class Prioridad {
    private int idPrioridad;
    private String nombrePrioridad;
    private String descripcionPrioridad;

    // --- CONSTRUCTOR VACÍO (EXISTENTE) ---
    public Prioridad() {}

    // --- CONSTRUCTOR AÑADIDO PARA SOLUCIONAR EL ERROR ---
    public Prioridad(int idPrioridad, String nombrePrioridad) {
        this.idPrioridad = idPrioridad;
        this.nombrePrioridad = nombrePrioridad;
    }

    // --- Getters y Setters (existentes) ---
    public int getIdPrioridad() { return idPrioridad; }
    public void setIdPrioridad(int idPrioridad) { this.idPrioridad = idPrioridad; }
    public String getNombrePrioridad() { return nombrePrioridad; }
    public void setNombrePrioridad(String nombrePrioridad) { this.nombrePrioridad = nombrePrioridad; }
    public String getDescripcionPrioridad() { return descripcionPrioridad; }
    public void setDescripcionPrioridad(String descripcionPrioridad) { this.descripcionPrioridad = descripcionPrioridad; }

    @Override
    public String toString() { return nombrePrioridad; }
}