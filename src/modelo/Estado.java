package modelo;

public class Estado {
    private int idEstado;
    private String nombreEstado;
    private String descripcionEstado;

    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public String getDescripcionEstado() { return descripcionEstado; }
    public void setDescripcionEstado(String descripcionEstado) { this.descripcionEstado = descripcionEstado; }

    @Override
    public String toString() { return nombreEstado; }
}