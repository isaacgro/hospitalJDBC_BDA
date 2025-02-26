/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.ConsultaBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.ConsultaDTO;
import DTO.MedicoDTO;
import DTO.PacienteDTO;
import Exception.NegocioException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author isaac
 */
public class HistorialConsultaPaciente extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(HistorialConsultaPaciente.class.getName());

    // Componentes para la vista de la lista
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private JPanel panelLista;
    private JPanel panelDetalles;
    private JLabel lblTitulo;
    private JTable tablaConsultas;
    private JScrollPane scrollPane;
    private DefaultTableModel modeloTabla;

    // Componentes para la vista de detalles
    private JLabel lblIdDetalles, lblFechaDetalles, lblHoraDetalles, lblMedicoDetalles;
    private JLabel lblEspecialidadDetalles, lblEstadoDetalles;
    private JLabel lblDiagnosticoDetalles, lblTratamientoDetalles;

    // Botones
    private JButton btnRegresar;
    private JButton btnVolverLista;

    // Componentes para el filtrado por fechas
    private JLabel lblFechaInicio, lblFechaFin;
    private JTextField txtFechaInicio, txtFechaFin;
    private JButton btnFiltrar, btnLimpiarFiltro;

    // Datos
    private final PacienteDTO pacienteDTO;
    private final ConsultaBO consultaBO;
    private final JFrame frameAnterior;
    private List<ConsultaDTO> listaConsultas;
    private List<ConsultaDTO> listaConsultasCompleta; // Lista completa sin filtros

    public HistorialConsultaPaciente(PacienteDTO pacienteDTO, JFrame frameAnterior) {
        this.pacienteDTO = pacienteDTO;
        this.frameAnterior = frameAnterior;

        // Inicializar el BO con una nueva conexión
        IConexion conexion = new Conexion();
        this.consultaBO = new ConsultaBO(conexion);

        initComponents();
        cargarConsultas();
    }

    private void initComponents() {
        setTitle("Historial Médico del Paciente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con CardLayout para alternar entre vistas
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        add(panelPrincipal);

        // Inicializar los paneles de lista y detalles
        inicializarPanelLista();
        inicializarPanelDetalles();

        // Agregar paneles al panel principal
        panelPrincipal.add(panelLista, "lista");
        panelPrincipal.add(panelDetalles, "detalles");

        // Mostrar inicialmente el panel de lista
        cardLayout.show(panelPrincipal, "lista");
    }

    private void inicializarPanelLista() {
        panelLista = new JPanel(new BorderLayout(10, 10));
        panelLista.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblTitulo = new JLabel("Historial de Consultas Médicas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);

        // Panel de filtrado por fechas usando JTextField
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar por fechas (formato: dd/MM/yyyy)"));

        lblFechaInicio = new JLabel("Desde:");
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setToolTipText("Ejemplo: 01/01/2023");

        lblFechaFin = new JLabel("Hasta:");
        txtFechaFin = new JTextField(10);
        txtFechaFin.setToolTipText("Ejemplo: 31/12/2023");

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarConsultas());

        btnLimpiarFiltro = new JButton("Limpiar");
        btnLimpiarFiltro.addActionListener(e -> {
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            if (listaConsultasCompleta != null) {
                mostrarConsultasEnTabla(listaConsultasCompleta);
                listaConsultas = new ArrayList<>(listaConsultasCompleta);
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
        String[] columnas = {"ID", "Fecha", "Hora", "Médico", "Especialidad", "Diagnóstico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla no es editable
            }
        };

        tablaConsultas = new JTable(modeloTabla);
        tablaConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConsultas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaConsultas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaConsultas.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaConsultas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaConsultas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaConsultas.getColumnModel().getColumn(5).setPreferredWidth(200);

        // Listener para detectar selección de consulta
        tablaConsultas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaConsultas.getSelectedRow() != -1) {
                mostrarDetallesConsulta(tablaConsultas.getSelectedRow());
            }
        });

        scrollPane = new JScrollPane(tablaConsultas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        JLabel lblTituloDetalles = new JLabel("Detalles de la Consulta");
        lblTituloDetalles.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperiorDetalles.add(lblTituloDetalles);
        panelDetalles.add(panelSuperiorDetalles, BorderLayout.NORTH);

        // Panel central con la información de la consulta
        JPanel panelInfoDetalles = new JPanel(new GridLayout(0, 1, 5, 10));
        panelInfoDetalles.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Información de la Consulta"),
                new EmptyBorder(10, 10, 10, 10)));

        // Crear campos para mostrar la información
        lblIdDetalles = crearCampoDetalle(panelInfoDetalles, "ID Consulta:");
        lblFechaDetalles = crearCampoDetalle(panelInfoDetalles, "Fecha:");
        lblHoraDetalles = crearCampoDetalle(panelInfoDetalles, "Hora:");
        lblMedicoDetalles = crearCampoDetalle(panelInfoDetalles, "Médico:");
        lblEspecialidadDetalles = crearCampoDetalle(panelInfoDetalles, "Especialidad:");
        lblEstadoDetalles = crearCampoDetalle(panelInfoDetalles, "Estado:");

        // Panel para diagnóstico
        JPanel panelDiagnostico = new JPanel(new BorderLayout(5, 5));
        panelDiagnostico.setBorder(BorderFactory.createTitledBorder("Diagnóstico"));
        lblDiagnosticoDetalles = new JLabel();
        lblDiagnosticoDetalles.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelDiagnostico.add(lblDiagnosticoDetalles, BorderLayout.CENTER);
        panelInfoDetalles.add(panelDiagnostico);

        // Panel para tratamiento
        JPanel panelTratamiento = new JPanel(new BorderLayout(5, 5));
        panelTratamiento.setBorder(BorderFactory.createTitledBorder("Tratamiento"));
        lblTratamientoDetalles = new JLabel();
        lblTratamientoDetalles.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelTratamiento.add(lblTratamientoDetalles, BorderLayout.CENTER);
        panelInfoDetalles.add(panelTratamiento);

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

    private void cargarConsultas() {
        try {
            // Limpiar tabla antes de cargar
            while (modeloTabla.getRowCount() > 0) {
                modeloTabla.removeRow(0);
            }

            // Obtener las consultas del paciente
            listaConsultas = consultaBO.obtenerConsultasPorPaciente(pacienteDTO.getId_Paciente());
            listaConsultasCompleta = new ArrayList<>(listaConsultas); // Guardar una copia completa

            if (listaConsultas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay consultas en el historial",
                        "Historial vacío",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarConsultasEnTabla(listaConsultas);
            }
        } catch (NegocioException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar consultas", ex);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el historial de consultas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

   private void mostrarConsultasEnTabla(List<ConsultaDTO> consultas) {
    // Limpiar tabla antes de cargar
    while (modeloTabla.getRowCount() > 0) {
        modeloTabla.removeRow(0);
    }
    
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Llenar la tabla con los datos
    for (ConsultaDTO consulta : consultas) {
        // Obtener datos del médico asociado
        MedicoDTO medicoAsociado = consulta.getMedicoAsociado();
        String nombreMedico = "No asignado";
        String especialidad = "N/A";
        
        if (medicoAsociado != null && medicoAsociado.getUsuario() != null) {
            nombreMedico = medicoAsociado.getUsuario().getNombre() + " " 
                    + medicoAsociado.getUsuario().getApellidoP();
            especialidad = medicoAsociado.getEspecialidad();
        }
        
        // Manejar la fecha y hora (sabemos que es LocalDateTime)
        String fechaStr = "";
        String horaStr = "";
        if (consulta.getFecha_hora() != null) {
            LocalDateTime fechaHora = (LocalDateTime) consulta.getFecha_hora();
            fechaStr = fechaHora.toLocalDate().format(dateFormatter);
            horaStr = fechaHora.toLocalTime().format(timeFormatter);
        }
        
        modeloTabla.addRow(new Object[]{
            consulta.getId_Consulta(),
            fechaStr,
            horaStr,
            nombreMedico,
            especialidad,
            consulta.getDiagnostico()
        });
    }
}


    private void mostrarDetallesConsulta(int filaSeleccionada) {
        if (filaSeleccionada >= 0 && filaSeleccionada < listaConsultas.size()) {
            ConsultaDTO consulta = listaConsultas.get(filaSeleccionada);

            // Formatear fechas y horas (sabemos que es LocalDateTime)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String fechaStr = "";
            String horaStr = "";

            if (consulta.getFecha_hora() != null) {
                LocalDateTime fechaHora = (LocalDateTime) consulta.getFecha_hora();
                fechaStr = fechaHora.toLocalDate().format(dateFormatter);
                horaStr = fechaHora.toLocalTime().format(timeFormatter);
            }

            // Obtener datos del médico
            MedicoDTO medicoAsociado = consulta.getMedicoAsociado();
            String nombreMedico = "No asignado";
            String especialidadMedico = "N/A";

            if (medicoAsociado != null && medicoAsociado.getUsuario() != null) {
                nombreMedico = medicoAsociado.getUsuario().getNombre() + " "
                        + medicoAsociado.getUsuario().getApellidoP() + " "
                        + medicoAsociado.getUsuario().getApellidoM();
                especialidadMedico = medicoAsociado.getEspecialidad();
            }

            // Mostrar los datos en los campos
            lblIdDetalles.setText(String.valueOf(consulta.getId_Consulta()));
            lblFechaDetalles.setText(fechaStr);
            lblHoraDetalles.setText(horaStr);
            lblMedicoDetalles.setText(nombreMedico);
            lblEspecialidadDetalles.setText(especialidadMedico);
            lblEstadoDetalles.setText("Completada");

            // Usar HTML para permitir text wrapping en diagnóstico y tratamiento
            lblDiagnosticoDetalles.setText("<html><p style='width:400px'>"
                    + (consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "No disponible")
                    + "</p></html>");
            lblTratamientoDetalles.setText("<html><p style='width:400px'>"
                    + (consulta.getTratamiento() != null ? consulta.getTratamiento() : "No disponible")
                    + "</p></html>");

            // Mostrar el panel de detalles
            cardLayout.show(panelPrincipal, "detalles");
        }
    }

    private void filtrarConsultas() {
        try {
            String fechaInicioStr = txtFechaInicio.getText().trim();
            String fechaFinStr = txtFechaFin.getText().trim();

            if (fechaInicioStr.isEmpty() && fechaFinStr.isEmpty()) {
                // Si no hay fechas ingresadas, mostrar todas las consultas
                mostrarConsultasEnTabla(listaConsultasCompleta);
                listaConsultas = new ArrayList<>(listaConsultasCompleta);
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

            // Filtrar las consultas que están dentro del rango de fechas
            List<ConsultaDTO> consultasFiltradas = new ArrayList<>();

            for (ConsultaDTO consulta : listaConsultasCompleta) {
                if (consulta.getFecha_hora() == null) {
                    continue;
                }

                // Sabemos que fecha_hora es LocalDateTime
                LocalDateTime fechaHora = (LocalDateTime) consulta.getFecha_hora();
                LocalDate fechaConsulta = fechaHora.toLocalDate();

                // Comprobar si la fecha está dentro del rango (incluyendo ambos límites)
                if ((fechaConsulta.isEqual(fechaInicio) || fechaConsulta.isAfter(fechaInicio))
                        && (fechaConsulta.isBefore(fechaFin))) {
                    consultasFiltradas.add(consulta);
                }
            }

            // Mostrar resultados
            if (consultasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron consultas en el rango de fechas seleccionado",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                // Mantener la tabla con los datos actuales
            } else {
                // Actualizar la lista actual y la tabla con los resultados filtrados
                listaConsultas = consultasFiltradas;
                mostrarConsultasEnTabla(consultasFiltradas);
            }

        } catch (DateTimeParseException e) {
            LOGGER.log(Level.SEVERE, "Error al parsear fechas", e);
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 01/01/2023)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar consultas", e);
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar las consultas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void regresar() {
        this.dispose();
        frameAnterior.setVisible(true);
    }
}
