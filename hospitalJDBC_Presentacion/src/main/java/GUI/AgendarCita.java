/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.CitaBO;
import BO.MedicoBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.CitaDTO;
import DTO.MedicoDTO;
import DTO.PacienteDTO;
import DTO.UsuarioDTO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author isaac
 */
public class AgendarCita extends JFrame {
    
    private JPanel panela;
    private JComboBox<String> comboEspecialidades;
    private JComboBox<MedicoDTO> comboMedicos;
    private JSpinner spinnerDia;
    private JSpinner spinnerMes;
    private JSpinner spinnerAnio;
    private JComboBox<String> comboHora;
    private JComboBox<String> comboTipoCita;
    private JButton btnAceptar;
    private JButton btnCanelar;
    
    // aqui agregar datos
    private PacienteDTO pacienteDTO;
    private CitaBO citaBO;
    private MedicoBO medicoBO;
    private JFrame frameAnterior;
    
    //constructor
    
    public AgendarCita(CitaBO citaBO, MedicoBO medicoBO, PacienteDTO pacienteDTO, JFrame frameAnterior) {
        
        this.pacienteDTO = pacienteDTO;
        this.citaBO = citaBO;
        this.medicoBO = medicoBO;
        this.frameAnterior = frameAnterior;
        
        initComponents();
        cargarDatos();
        
    }
    //inicializar componentes de la interfaz
    
    private void initComponents() {
        setTitle("Agendar cita");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        panela = new JPanel();
        panela.setLayout(new GridLayout(8, 2, 10, 10));
        panela.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Especialidad
        panela.add(new JLabel("Especialidad:"));
        comboEspecialidades = new JComboBox<>();
        panela.add(comboEspecialidades);
        
        // Médico
        panela.add(new JLabel("Médico:"));
        comboMedicos = new JComboBox<>();
        panela.add(comboMedicos);
        
        // Configurar el combo de médicos para mostrar nombres
        comboMedicos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MedicoDTO) {
                    MedicoDTO medico = (MedicoDTO) value;
                    setText("Dr. " + medico.getUsuario().getNombre() + " " + medico.getUsuario().getApellidoP());
                }
                return this;
            }
        });
        
        // Fecha (Día)
        panela.add(new JLabel("Día:"));
        spinnerDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        panela.add(spinnerDia);
        
        // Fecha (Mes)
        panela.add(new JLabel("Mes:"));
        spinnerMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        panela.add(spinnerMes);
        
        // Fecha (Año)
        panela.add(new JLabel("Año:"));
        int añoActual = LocalDate.now().getYear();
        spinnerAnio = new JSpinner(new SpinnerNumberModel(añoActual, añoActual, añoActual + 2, 1));
        panela.add(spinnerAnio);
        
        // Hora
        panela.add(new JLabel("Hora:"));
        comboHora = new JComboBox<>();
        panela.add(comboHora);
        
        // Tipo de cita
        panela.add(new JLabel("Tipo de cita:"));
        comboTipoCita = new JComboBox<>(new String[]{"regular", "emergencia"});
        panela.add(comboTipoCita);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCanelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCanelar);
        
        // Agregar todo al frame
        setLayout(new BorderLayout());
        add(panela, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Listeners
        comboEspecialidades.addActionListener(e -> cargarMedicosPorEspecialidad());
        spinnerDia.addChangeListener(e -> validarFecha());
        spinnerMes.addChangeListener(e -> validarFecha());
        spinnerAnio.addChangeListener(e -> validarFecha());
        
        btnAceptar.addActionListener(e -> agendarCita());
        btnCanelar.addActionListener(e -> {
            dispose();
            if (frameAnterior != null) {
                frameAnterior.setVisible(true);
            }
        });
    }
    
    /**
     * Carga los datos iniciales
     */
    private void cargarDatos() {
        // Cargar especialidades (simulado, en un caso real se obtendría de la base de datos)
        String[] especialidades = {"General", "Cardiología", "Dermatología", "Neurología", "Pediatría", "Ortopedia"};
        for (String esp : especialidades) {
            comboEspecialidades.addItem(esp);
        }
        
        // Cargar horas disponibles (8 AM a 6 PM)
        for (int i = 8; i <= 18; i++) {
            comboHora.addItem(String.format("%02d:00", i));
        }
        
        // Cargar médicos de la primera especialidad
        cargarMedicosPorEspecialidad();
    }
    
    /**
     * Carga los médicos según la especialidad seleccionada
     */
    private void cargarMedicosPorEspecialidad() {
        String especialidad = (String) comboEspecialidades.getSelectedItem();
        if (especialidad == null) return;
        
        // Limpiar combo de médicos
        comboMedicos.removeAllItems();
        
        // En un caso real, se consultaría a la base de datos
        // Aquí simulamos con datos de ejemplo
        List<MedicoDTO> medicos = obtenerMedicosPorEspecialidad(especialidad);
        
        for (MedicoDTO medico : medicos) {
            comboMedicos.addItem(medico);
        }
    }
    
    /**
     * Obtiene los médicos por especialidad (simulado)
     */
    private List<MedicoDTO> obtenerMedicosPorEspecialidad(String especialidad) {
        List<MedicoDTO> medicos = new ArrayList<>();
        
        // Datos de ejemplo
        UsuarioDTO usuario1 = new UsuarioDTO(1, "pass123", "Juan", "Pérez", "Gómez");
        UsuarioDTO usuario2 = new UsuarioDTO(2, "pass456", "María", "López", "Hernández");
        UsuarioDTO usuario3 = new UsuarioDTO(3, "pass789", "Carlos", "Martínez", "Sánchez");
        
        medicos.add(new MedicoDTO(1, "MED123456", especialidad, null, usuario1));
        medicos.add(new MedicoDTO(2, "MED789012", especialidad, null, usuario2));
        medicos.add(new MedicoDTO(3, "MED345678", especialidad, null, usuario3));
        
        return medicos;
    }
    
    /**
     * Valida que la fecha seleccionada sea correcta
     */
    private void validarFecha() {
        int dia = (Integer) spinnerDia.getValue();
        int mes = (Integer) spinnerMes.getValue();
        int año = (Integer) spinnerAnio.getValue();
        
        try {
            LocalDate fecha = LocalDate.of(año, mes, dia);
            
            // Validar que la fecha no sea anterior a hoy
            if (fecha.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, 
                    "La fecha no puede ser anterior a hoy", 
                    "Fecha inválida", 
                    JOptionPane.WARNING_MESSAGE);
                
                // Establecer fecha actual
                spinnerDia.setValue(LocalDate.now().getDayOfMonth());
                spinnerMes.setValue(LocalDate.now().getMonthValue());
                spinnerAnio.setValue(LocalDate.now().getYear());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Fecha inválida", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
                
            // Establecer valores predeterminados
            spinnerDia.setValue(1);
            spinnerMes.setValue(1);
            spinnerAnio.setValue(LocalDate.now().getYear());
        }
    }
    
    /**
     * Realiza el registro de la cita
     */
    private void agendarCita() {
        try {
            // Validar selecciones
            if (comboMedicos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un médico", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener valores seleccionados
            MedicoDTO medicoSeleccionado = (MedicoDTO) comboMedicos.getSelectedItem();
            String tipoCita = (String) comboTipoCita.getSelectedItem();
            
            // Crear fecha y hora
            int dia = (Integer) spinnerDia.getValue();
            int mes = (Integer) spinnerMes.getValue();
            int año = (Integer) spinnerAnio.getValue();
            
            String horaStr = (String) comboHora.getSelectedItem();
            LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            
            LocalDateTime fechaHora = LocalDateTime.of(año, mes, dia, hora.getHour(), hora.getMinute());
            Timestamp timestamp = Timestamp.valueOf(fechaHora);
            
            // Crear objeto Cita
            CitaDTO cita = new CitaDTO(
                "pendiente",
                timestamp,
                pacienteDTO,
                medicoSeleccionado,
                tipoCita
            );
            
            // Registrar cita
            citaBO.registrarCitaR(cita);
            
            JOptionPane.showMessageDialog(this, 
                "Cita agendada correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar ventana y volver
            dispose();
            if (frameAnterior != null) {
                frameAnterior.setVisible(true);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al agendar cita: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    
    


