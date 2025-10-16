package modelo;

import java.util.ArrayList;
import java.util.List;

public class AreaSistema {
    private int idAreaSistema;
    private String nombreAreaSistema;
    private List<Funcion> funcionesDisponibles; // Para almacenar las funciones

    public AreaSistema() {
        this.funcionesDisponibles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdAreaSistema() { return idAreaSistema; }
    public void setIdAreaSistema(int idAreaSistema) { this.idAreaSistema = idAreaSistema; }
    public String getNombreAreaSistema() { return nombreAreaSistema; }
    public void setNombreAreaSistema(String nombreAreaSistema) { this.nombreAreaSistema = nombreAreaSistema; }
    public List<Funcion> getFuncionesDisponibles() { return funcionesDisponibles; }
    public void setFuncionesDisponibles(List<Funcion> funcionesDisponibles) { this.funcionesDisponibles = funcionesDisponibles; }

    @Override
    public String toString() {
        return nombreAreaSistema;
    }
}