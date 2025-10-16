package modelo;

public class Funcion {
    private int idFuncion;
    private String nombreFuncion;

    // Getters y Setters
    public int getIdFuncion() { return idFuncion; }
    public void setIdFuncion(int idFuncion) { this.idFuncion = idFuncion; }
    public String getNombreFuncion() { return nombreFuncion; }
    public void setNombreFuncion(String nombreFuncion) { this.nombreFuncion = nombreFuncion; }

    @Override
    public String toString() {
        return nombreFuncion;
    }
}