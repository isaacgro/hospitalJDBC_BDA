/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.CitaBO;
import Conexion.Conexion;
import DTO.CitaDTO;
import DTO.MedicoDTO;
import Exception.NegocioException;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author isaac
 */


public class HistorialCitasMedico extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(HistorialCitasMedico.class.getName());

    // Componentes de interfaz
    private JPanel panelPrincipal;
    private JPanel panelLista;
    private JPanel panelDetalles;
    private CardLayout cardLayout;
    private JLabel lblTitulo;
    private JTable tablaCitas;
    private JScrollPane scrollPane;
    private DefaultTableModel modeloTabla;

    // Componentes para detalles
    private JLabel lblIdDetalles, lblFechaDetalles, lblHoraDetalles, lblPacienteDetalles;
    private JLabel lblTipoCitaDetalles, lblEstadoDetalles, lblEspecialidadDetalles;

    // Botones
    private JButton btnRegresar;
    private JButton btnVolverLista;

    // Componentes para filtrado de fechas
    private JLabel lblFechaInicio, lblFechaFin;
    private JTextField txtFechaInicio, txtFechaFin;
    private JButton btnFiltrar, btnLimpiarFiltro;

    // Datos
    private final MedicoDTO medicoDTO;
    private final CitaBO citaBO;
    private final JFrame frameAnterior;
    private List<CitaDTO> listaCitas = new ArrayList<>();
    private List<CitaDTO> listaCitasCompleta = new ArrayList<>();

    public HistorialCitasMedico(MedicoDTO medicoDTO, JFrame frameAnterior) {
        this.medicoDTO = medicoDTO;
        this.frameAnterior = frameAnterior;
        this.citaBO = new CitaBO(new Conexion());

        initComponents();
        cargarCitas();
    }

    private void initComponents() {
        setTitle("Historial de Citas del Médico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuración del layout con CardLayout
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        add(panelPrincipal);

        inicializarPanelLista();
        inicializarPanelDetalles();

        panelPrincipal.add(panelLista, "lista");
        panelPrincipal.add(panelDetalles, "detalles");

        cardLayout.show(panelPrincipal, "lista");
    }

    private void inicializarPanelLista() {
        panelLista = new JPanel(new BorderLayout(10, 10));
        panelLista.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con título y filtros
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblTitulo = new JLabel("Historial de Citas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);

        // Panel de filtrado por fechas
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar por fechas (formato: dd/MM/yyyy)"));

        lblFechaInicio = new JLabel("Desde:");
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setToolTipText("Ejemplo: 01/01/2023");

        lblFechaFin = new JLabel("Hasta:");
        txtFechaFin = new JTextField(10);
        txtFechaFin.setToolTipText("Ejemplo: 31/12/2023");

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarCitas());

        btnLimpiarFiltro = new JButton("Limpiar");
        btnLimpiarFiltro.addActionListener(e -> {
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            if (listaCitasCompleta != null) {
                mostrarCitasEnTabla(listaCitasCompleta);
                listaCitas = new ArrayList<>(listaCitasCompleta);
            }
        });

        panelFiltro.add(lblFechaInicio);
        panelFiltro.add(txtFechaInicio);
        panelFiltro.add(lblFechaFin);
        panelFiltro.add(txtFechaFin);
        panelFiltro.add(btnFiltrar);
        panelFiltro.add(btnLimpiarFiltro);

        panelSuperior.add(panelFiltro, BorderLayout.CENTER);
        panelLista.add(panelSuperior, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnas = {"ID", "Fecha", "Hora", "Paciente", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listener para mostrar detalles
        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCitas.getSelectedRow() != -1) {
                mostrarDetallesCita(tablaCitas.getSelectedRow());
            }
        });

        scrollPane = new JScrollPane(tablaCitas);
        panelLista.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón de regreso
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.addActionListener(e -> regresar());
        panelInferior.add(btnRegresar);
        panelLista.add(panelInferior, BorderLayout.SOUTH);
    }

    private void inicializarPanelDetalles() {
        panelDetalles = new JPanel(new BorderLayout(10, 10));
        panelDetalles.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con título
        JPanel panelSuperiorDetalles = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTituloDetalles = new JLabel("Detalles de la Cita");
        lblTituloDetalles.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperiorDetalles.add(lblTituloDetalles);
        panelDetalles.add(panelSuperiorDetalles, BorderLayout.NORTH);

        // Panel central con la información de la cita
        JPanel panelInfoDetalles = new JPanel(new GridLayout(0, 1, 5, 10));
        panelInfoDetalles.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Información de la Cita"),
                new EmptyBorder(10, 10, 10, 10)));

        // Crear campos para mostrar la información
        lblIdDetalles = crearCampoDetalle(panelInfoDetalles, "ID Cita:");
        lblFechaDetalles = crearCampoDetalle(panelInfoDetalles, "Fecha:");
        lblHoraDetalles = crearCampoDetalle(panelInfoDetalles, "Hora:");
        lblPacienteDetalles = crearCampoDetalle(panelInfoDetalles, "Paciente:");
        lblTipoCitaDetalles = crearCampoDetalle(panelInfoDetalles, "Tipo de Cita:");
        lblEstadoDetalles = crearCampoDetalle(panelInfoDetalles, "Estado:");
        lblEspecialidadDetalles = crearCampoDetalle(panelInfoDetalles, "Especialidad:");

        JScrollPane scrollDetalles = new JScrollPane(panelInfoDetalles);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);

        // Panel inferior con botón para volver a la lista
        JPanel panelInferiorDetalles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolverLista = new JButton("Volver a la Lista");
        btnVolverLista.addActionListener(e -> cardLayout.show(panelPrincipal, "lista"));
        panelInferiorDetalles.add(btnVolverLista);
        panelDetalles.add(panelInferiorDetalles, BorderLayout.SOUTH);
    }

    private JLabel crearCampoDetalle(JPanel panel, String etiqueta) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblValor = new JLabel();
        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);
        panel.add(fila);
        return lblValor;
    }

    private void cargarCitas() {
        try {
            // Limpiar tabla
            while (modeloTabla.getRowCount() > 0) {
                modeloTabla.removeRow(0);
            }

            // Obtener citas del médico
            listaCitas = citaBO.obtenerCitasPorMedico(medicoDTO.getId_Medico());
            listaCitasCompleta = new ArrayList<>(listaCitas);

            if (listaCitas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay citas en el historial",
                        "Sin citas",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarCitasEnTabla(listaCitas);
            }
        } catch (NegocioException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar citas", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el historial de citas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarCitasEnTabla(List<CitaDTO> citas) {
        // Limpiar tabla
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (CitaDTO cita : citas) {
            // Formatear fecha y hora
            String fechaStr = "";
            String horaStr = "";
            if (cita.getFecha_hora() != null) {
                LocalDateTime fechaHora = cita.getFecha_hora().toLocalDateTime();
                fechaStr = fechaHora.toLocalDate().format(dateFormatter);
                horaStr = fechaHora.toLocalTime().format(timeFormatter);
            }

            // Obtener nombre del paciente
            String nombrePaciente = cita.getPaciente() != null && cita.getPaciente().getUsuario() != null
                    ? cita.getPaciente().getUsuario().getNombre() + " "
                    + cita.getPaciente().getUsuario().getApellidoP()
                    : "No asignado";

            modeloTabla.addRow(new Object[]{
                cita.getId_Cita(),
                fechaStr,
                horaStr,
                nombrePaciente,
                cita.getTipo(),
                cita.getEstado()
            });
        }
    }

    private void mostrarDetallesCita(int filaSeleccionada) {
        if (filaSeleccionada >= 0 && filaSeleccionada < listaCitas.size()) {
            CitaDTO cita = listaCitas.get(filaSeleccionada);

            // Formatear fechas y horas
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String fechaStr = "";
            String horaStr = "";

            if (cita.getFecha_hora() != null) {
                LocalDateTime fechaHora = cita.getFecha_hora().toLocalDateTime();
                fechaStr = fechaHora.toLocalDate().format(dateFormatter);
                horaStr = fechaHora.toLocalTime().format(timeFormatter);
            }

            // Obtener datos del paciente
            String nombrePaciente = "No asignado";
            if (cita.getPaciente() != null && cita.getPaciente().getUsuario() != null) {
                nombrePaciente = cita.getPaciente().getUsuario().getNombre() + " "
                        + cita.getPaciente().getUsuario().getApellidoP() + " "
                        + cita.getPaciente().getUsuario().getApellidoM();
            }

            // Obtener especialidad del médico
            String especialidad = "N/A";
            if (cita.getMedico() != null) {
                especialidad = cita.getMedico().getEspecialidad();
            }

            // Mostrar los datos en los campos
            lblIdDetalles.setText(String.valueOf(cita.getId_Cita()));
            lblFechaDetalles.setText(fechaStr);
            lblHoraDetalles.setText(horaStr);
            lblPacienteDetalles.setText(nombrePaciente);
            lblTipoCitaDetalles.setText(cita.getTipo());
            lblEstadoDetalles.setText(cita.getEstado());
            lblEspecialidadDetalles.setText(especialidad);

            // Mostrar el panel de detalles
            cardLayout.show(panelPrincipal, "detalles");
        }
    }

    private void filtrarCitas() {
        try {
            String fechaInicioStr = txtFechaInicio.getText().trim();
            String fechaFinStr = txtFechaFin.getText().trim();

            if (fechaInicioStr.isEmpty() && fechaFinStr.isEmpty()) {
                // Si no hay fechas ingresadas, mostrar todas las citas
                mostrarCitasEnTabla(listaCitasCompleta);
                listaCitas = new ArrayList<>(listaCitasCompleta);
                return;
            }

            // Patrón para validar el formato de fecha dd/MM/yyyy
            String patronFecha = "\\d{2}/\\d{2}/\\d{4}";

            LocalDate fechaInicio = null;
            if (!fechaInicioStr.isEmpty()) {
                if (!fechaInicioStr.matches(patronFecha)) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 01/01/2023)",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fechaInicio = LocalDate.parse(fechaInicioStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                // Si no hay fecha de inicio, usar una fecha muy antigua
                fechaInicio = LocalDate.of(2000, 1, 1);
            }

            LocalDate fechaFin = null;
            if (!fechaFinStr.isEmpty()) {
                if (!fechaFinStr.matches(patronFecha)) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 31/12/2023)",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fechaFin = LocalDate.parse(fechaFinStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Sumar un día para incluir el día final completo
                fechaFin = fechaFin.plusDays(1);
            } else {
                // Si no hay fecha de fin, usar la fecha actual más un día
                fechaFin = LocalDate.now().plusDays(1);
            }

            // Filtrar las citas que están dentro del rango de fechas
            List<CitaDTO> citasFiltradas = new ArrayList<>();

            for (CitaDTO cita : listaCitasCompleta) {
                if (cita.getFecha_hora() == null) {
                    continue;
                }

                LocalDateTime fechaHora = cita.getFecha_hora().toLocalDateTime();
                LocalDate fechaCita = fechaHora.toLocalDate();

                // Comprobar si la fecha está dentro del rango (incluyendo ambos límites)
                if ((fechaCita.isEqual(fechaInicio) || fechaCita.isAfter(fechaInicio))
                        && (fechaCita.isBefore(fechaFin))) {
                    citasFiltradas.add(cita);
                }
            }

            // Mostrar resultados
            if (citasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron citas en el rango de fechas seleccionado",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar la lista actual y la tabla con los resultados filtrados
                listaCitas = citasFiltradas;
                mostrarCitasEnTabla(citasFiltradas);
            }

        } catch (DateTimeParseException e) {
            LOGGER.log(Level.SEVERE, "Error al parsear fechas", e);
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 01/01/2023)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar citas", e);
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar las citas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void regresar() {
        this.dispose();
        frameAnterior.setVisible(true);
    }
}
