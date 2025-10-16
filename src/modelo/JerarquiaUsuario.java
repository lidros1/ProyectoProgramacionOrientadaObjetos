package modelo;

public class JerarquiaUsuario {
    private int idJerarquiaUsuario;
    private String nombreJerarquia;
    private String descripcionJerarquia;

    public int getIdJerarquiaUsuario() {
        return idJerarquiaUsuario;
    }

    public void setIdJerarquiaUsuario(int idJerarquiaUsuario) {
        this.idJerarquiaUsuario = idJerarquiaUsuario;
    }

    public String getNombreJerarquia() {
        return nombreJerarquia;
    }

    public void setNombreJerarquia(String nombreJerarquia) {
        this.nombreJerarquia = nombreJerarquia;
    }

    public String getDescripcionJerarquia() {
        return descripcionJerarquia;
    }

    public void setDescripcionJerarquia(String descripcionJerarquia) {
        this.descripcionJerarquia = descripcionJerarquia;
    }

    @Override
    public String toString() {
        return nombreJerarquia;
    }

    public boolean esProjectManagerOScrumMaster() {
        return "Project Manager".equalsIgnoreCase(nombreJerarquia) || "Scrum Master".equalsIgnoreCase(nombreJerarquia);
    }
}