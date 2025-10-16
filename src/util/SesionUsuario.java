package util;

import modelo.Usuario;

public class SesionUsuario {
    private static Usuario usuarioLogueado;

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void setUsuarioLogueado(Usuario usuarioLogueado) {
        SesionUsuario.usuarioLogueado = usuarioLogueado;
    }
}