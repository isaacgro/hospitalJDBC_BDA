/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.CitaBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.CitaDTO;
import DTO.MedicoDTO;
import DTO.PacienteDTO;
import Exception.NegocioException;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author isaac
 */
public class CitasProximasPaciente extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(CitasProximasPaciente.class.getName());

    // Componentes de la interfaz
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JTable tablaCitas;
    private JScrollPane scrollPane;
    private DefaultTableModel modeloTabla;
    private JButton btnRegresar;
    private JButton btnVerDetalles;
    private JButton btnCancelar;

    // Datos
    private final PacienteDTO pacienteDTO;
    private final JFrame frameAnterior;
    private List<CitaDTO> listaCitas;
    private final CitaBO citaBO;
    private final IConexion conexion;

    public CitasProximasPaciente(PacienteDTO pacienteDTO, JFrame frameAnterior) {
        this.pacienteDTO = pacienteDTO;
        this.frameAnterior = frameAnterior;

        // Inicializar conexión y BO
        this.conexion = new Conexion();
        this.citaBO = new CitaBO(conexion);

        initComponents();
        cargarCitasPendientes();
    }

    private void initComponents() {
        setTitle("Citas Próximas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(panelPrincipal);

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblTitulo = new JLabel("Citas Próximas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperior.add(lblTitulo);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnas = {"ID", "Fecha", "Hora", "Médico", "Especialidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla no es editable
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCitas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaCitas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaCitas.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaCitas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaCitas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaCitas.getColumnModel().getColumn(5).setPreferredWidth(80);

        scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnVerDetalles = new JButton("Ver Detalles");
        btnCancelar = new JButton("Cancelar Cita");
        btnRegresar = new JButton("Regresar al Menú");

        btnVerDetalles.addActionListener(e -> mostrarDetallesCita());
        btnCancelar.addActionListener(e -> cancelarCita());
        btnRegresar.addActionListener(e -> regresar());

        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnRegresar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCitasPendientes() {
        try {
            // Limpiar tabla antes de cargar
            while (modeloTabla.getRowCount() > 0) {
                modeloTabla.removeRow(0);
            }

            // Obtener citas pendientes del paciente
            listaCitas = citaBO.obtenerCitasPendientesPorPaciente(pacienteDTO.getId_Paciente());

            if (listaCitas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No tienes citas próximas programadas",
                        "Sin citas pendientes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                // Llenar la tabla con los datos
                for (CitaDTO cita : listaCitas) {
                    // Obtener datos del médico
                    MedicoDTO medico = cita.getMedico();
                    String nombreMedico = "No asignado";
                    String especialidad = "N/A";

                    if (medico != null && medico.getUsuario() != null) {
                        nombreMedico = medico.getUsuario().getNombre() + " "
                                + medico.getUsuario().getApellidoP();
                        especialidad = medico.getEspecialidad();
                    }

                    // Formatear fecha y hora
                    String fechaStr = "";
                    String horaStr = "";
                    if (cita.getFecha_hora() != null) {
                        // Convertir de Timestamp a LocalDateTime
                        java.sql.Timestamp timestamp = cita.getFecha_hora();
                        LocalDateTime fechaHora = timestamp.toLocalDateTime();
                        fechaStr = fechaHora.toLocalDate().format(dateFormatter);
                        horaStr = fechaHora.toLocalTime().format(timeFormatter);
                    }

                    modeloTabla.addRow(new Object[]{
                        cita.getId_Cita(),
                        fechaStr,
                        horaStr,
                        nombreMedico,
                        especialidad,
                        cita.getEstado()
                    });
                }
            }

            // Habilitar/deshabilitar botones según si hay citas
            boolean hayCitas = !listaCitas.isEmpty();
            btnVerDetalles.setEnabled(hayCitas);
            btnCancelar.setEnabled(hayCitas);

        } catch (NegocioException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar citas pendientes", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las citas pendientes: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallesCita() {
        int filaSeleccionada = tablaCitas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cita para ver sus detalles",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        CitaDTO cita = listaCitas.get(filaSeleccionada);

        // Crear diálogo para mostrar detalles
        JDialog dialogoDetalles = new JDialog(this, "Detalles de la Cita", true);
        dialogoDetalles.setSize(400, 350);
        dialogoDetalles.setLocationRelativeTo(this);
        dialogoDetalles.setLayout(new BorderLayout(10, 10));

        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 10));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Información de la Cita"),
                new EmptyBorder(10, 10, 10, 10)));

        // Formatear fecha y hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String fechaStr = "";
        String horaStr = "";
        if (cita.getFecha_hora() != null) {
            // Convertir de Timestamp a LocalDateTime
            java.sql.Timestamp timestamp = cita.getFecha_hora();
            LocalDateTime fechaHora = timestamp.toLocalDateTime();
            fechaStr = fechaHora.toLocalDate().format(dateFormatter);
            horaStr = fechaHora.toLocalTime().format(timeFormatter);
        }

        // Obtener datos del médico
        MedicoDTO medico = cita.getMedico();
        String nombreMedico = "No asignado";
        String especialidadMedico = "N/A";

        if (medico != null && medico.getUsuario() != null) {
            nombreMedico = medico.getUsuario().getNombre() + " "
                    + medico.getUsuario().getApellidoP() + " "
                    + medico.getUsuario().getApellidoM();
            especialidadMedico = medico.getEspecialidad();
        }

        // Añadir campos de información
        agregarCampoDetalle(panelInfo, "ID Cita:", String.valueOf(cita.getId_Cita()));
        agregarCampoDetalle(panelInfo, "Fecha:", fechaStr);
        agregarCampoDetalle(panelInfo, "Hora:", horaStr);
        agregarCampoDetalle(panelInfo, "Médico:", nombreMedico);
        agregarCampoDetalle(panelInfo, "Especialidad:", especialidadMedico);
        agregarCampoDetalle(panelInfo, "Estado:", cita.getEstado());
        agregarCampoDetalle(panelInfo, "Tipo de cita:", cita.getTipo());

        dialogoDetalles.add(new JScrollPane(panelInfo), BorderLayout.CENTER);

        // Botón para cerrar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogoDetalles.dispose());
        panelBoton.add(btnCerrar);

        dialogoDetalles.add(panelBoton, BorderLayout.SOUTH);
        dialogoDetalles.setVisible(true);
    }

    private void agregarCampoDetalle(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblValor = new JLabel(valor);
        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);
        panel.add(fila);
    }

    private void cancelarCita() {
        int filaSeleccionada = tablaCitas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cita para cancelarla",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        CitaDTO cita = listaCitas.get(filaSeleccionada);

        // Confirmar cancelación
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas cancelar esta cita?\nEsta acción no se puede deshacer.",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Actualizar el estado de la cita a "cancelado"
                cita.setEstado("cancelado");
                boolean resultado = citaBO.actualizarEstadoCita(cita);

                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "La cita ha sido cancelada correctamente",
                            "Cita cancelada",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Recargar la lista de citas para reflejar el cambio
                    cargarCitasPendientes();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo cancelar la cita",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NegocioException ex) {
                LOGGER.log(Level.SEVERE, "Error al cancelar cita", ex);
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar la cita: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void regresar() {
        this.dispose();
        frameAnterior.setVisible(true);
    }
}
