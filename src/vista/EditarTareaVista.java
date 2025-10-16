package vista;

import modelo.Proyecto;
import javax.swing.*;
import java.awt.*;

public class EditarTareaVista extends JFrame {
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> listModelProyectos;
    private JButton btnVolver;

    public EditarTareaVista() {
        setTitle("Editar Tarea - Seleccionar Proyecto");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de Búsqueda y Selección
        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtBuscarProyecto = new JTextField();
        listModelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(listModelProyectos);

        panelPrincipal.add(new JLabel("Buscar Proyecto:"), BorderLayout.NORTH);
        panelPrincipal.add(txtBuscarProyecto, BorderLayout.CENTER);
        panelPrincipal.add(new JScrollPane(listaProyectos), BorderLayout.SOUTH);

        // Instrucción para el usuario
        JLabel instruccion = new JLabel("Doble clic en un proyecto para ver sus tareas", SwingConstants.CENTER);
        instruccion.setForeground(Color.GRAY);

        // Panel inferior con botón de volver
        JPanel panelSur = new JPanel(new BorderLayout());
        btnVolver = new JButton("Volver");
        panelSur.add(instruccion, BorderLayout.CENTER);
        panelSur.add(btnVolver, BorderLayout.WEST);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtBuscarProyecto() { return txtBuscarProyecto; }
    public JList<Proyecto> getListaProyectos() { return listaProyectos; }
    public DefaultListModel<Proyecto> getListModelProyectos() { return listModelProyectos; }
    public JButton getBtnVolver() { return btnVolver; }
}