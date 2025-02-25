
import Conexion.Conexion;
import Conexion.IConexion;
import DAO.CitaDAO;
import Entidades.*;
import Excepciones.PersistenciaExcption;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        // Crear conexión
        IConexion conexion = new Conexion();
        CitaDAO citaDAO = new CitaDAO(conexion);

        // Crear objetos
        Paciente paciente = crearPaciente();
        Medico medico = crearMedico();
        Cita cita = crearCita(paciente, medico);

        // Intentar registrar la cita
        registrarCita(citaDAO, cita);
    }

    private static Paciente crearPaciente() {
        Usuario usuarioPaciente = new Usuario();
        usuarioPaciente.setContra("paciente123");

        Paciente paciente = new Paciente();
        paciente.setId_Paciente(1);
        usuarioPaciente.setNombre("Luis");
        usuarioPaciente.setApellidoP("Gómez");
        usuarioPaciente.setApellidoM("Ramírez");
        paciente.setFecha_nacimiento(LocalDate.of(1995, 5, 20));
        paciente.setEdad(28);
        paciente.setTelefono("6629876543");
        paciente.setCorreoE("luis.gomez@gmail.com");
        paciente.setUsuario(usuarioPaciente);

        return paciente;
    }

    private static Medico crearMedico() {
        Usuario usuarioMedico = new Usuario();
        usuarioMedico.setContra("medico456");

        Horario horario = new Horario();
        horario.setFecha(LocalDate.of(2025, 2, 23));
        horario.setHora_inicio(Time.valueOf(LocalTime.of(9, 0)));
        horario.setHora_fin(Time.valueOf(LocalTime.of(9, 30)));

        Medico medico = new Medico();
        medico.setId_Medico(1);
        usuarioMedico.setNombre("Dr. Carlos");
        usuarioMedico.setApellidoP("Martínez");
        usuarioMedico.setApellidoM("López");
        medico.setCedulaPro("MED789101");
        medico.setEspecialidad("Pediatría");
        medico.setHorario(horario);
        medico.setUsuario(usuarioMedico);

        return medico;
    }

    private static Cita crearCita(Paciente paciente, Medico medico) {
        Cita cita = new Cita();
        cita.setEstado("pendiente");
        cita.setFecha_hora(Timestamp.valueOf(LocalDate.of(2025, 2, 23).atTime(9, 0)));
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        String tipo = "regular";
        cita.setTipo(tipo);

        return cita;
    }

    private static void registrarCita(CitaDAO citaDAO, Cita cita) {
        try {
            boolean resultado = citaDAO.registraCitaR(cita);
            System.out.println(resultado ? " Cita registrada correctamente." : "️ No se pudo registrar la cita.");
        } catch (PersistenciaExcption e) {
            System.err.println(" Error al registrar la cita: " + e.getMessage());
        }
    }
}
