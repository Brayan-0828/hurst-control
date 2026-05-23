package controller;

import dao.CronogramaDAO;
import dao.EstudianteDAO;
import dao.RegistroAccesoDAO;
import dao.RegistroDocenteDAO;
import dao.ServicioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AccesoController {

    @FXML private TextField txtCedula;
    @FXML private Label     lblEstado;
    @FXML private Circle    semaforo;
    @FXML private Label     lblMensaje;

    @FXML private Label lblContador;
    @FXML private Label lblBadgeCount;

    @FXML private TableView<RegistroAcceso>                 tablaActivos;
    @FXML private TableColumn<RegistroAcceso, Long>          colId;
    @FXML private TableColumn<RegistroAcceso, Estudiante>    colEstudiante;
    @FXML private TableColumn<RegistroAcceso, String>        colCedula;
    @FXML private TableColumn<RegistroAcceso, Servicio>      colServicio;
    @FXML private TableColumn<RegistroAcceso, LocalDateTime> colEntrada;
    @FXML private TableColumn<RegistroAcceso, String>        colTiempo;

    private EstudianteDAO    estudianteDAO;
    private CronogramaDAO    cronogramaDAO;
    private RegistroAccesoDAO registroDAO;
    private RegistroDocenteDAO docenteRegDAO;
    private ServicioDAO      servicioDAO;

    @FXML
    public void initialize() {
        estudianteDAO  = new EstudianteDAO();
        cronogramaDAO  = new CronogramaDAO();
        registroDAO    = new RegistroAccesoDAO();
        docenteRegDAO  = new RegistroDocenteDAO();
        servicioDAO    = new ServicioDAO();

        colId         .setCellValueFactory(new PropertyValueFactory<>("idRegistro"));
        colEstudiante .setCellValueFactory(new PropertyValueFactory<>("estudiante"));
        colServicio   .setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colEntrada    .setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));

        colCedula.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEstudiante() != null
                                ? data.getValue().getEstudiante().getCedula()
                                : ""));

        colTiempo.setCellValueFactory(data -> {
            RegistroAcceso r = data.getValue();
            if (r.getHoraEntrada() == null) return new javafx.beans.property.SimpleStringProperty("-");
            long min = ChronoUnit.MINUTES.between(r.getHoraEntrada(), LocalDateTime.now());
            return new javafx.beans.property.SimpleStringProperty(min + " min");
        });

        tablaActivos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setSemaforo(null);
        cargarActivos();
    }

    @FXML
    public void volverAlMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = (Stage) txtCedula.getScene().getWindow();
            stage.setTitle("HURST-Control");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registrarEntrada() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) return;

        Estudiante est = buscarEstudiantePorCedula(cedula);
        if (est == null || !est.isActivo()) {
            denegar("Estudiante no encontrado o inactivo en el sistema.");
            return;
        }

        Cronograma cron = buscarCronogramaHoy(est);
        if (cron == null) {
            denegar("No existe cronograma APROBADO para hoy en este estudiante.");
            return;
        }
/*
        if (!docentePresente(cron.getDocente())) {
            denegar("El docente responsable aún no ha registrado ingreso al hospital.");
            return;
        }
*/
        if (est.getEstadoInduccion() != Estudiante.EstadoInduccion.REALIZADA) {
            denegar("El estudiante no ha completado la inducción hospitalaria.");
            return;
        }

        if (est.getArlVigencia().isBefore(LocalDate.now())) {
            denegar("La ARL del estudiante está VENCIDA. No puede ingresar.");
            return;
        }

        Servicio servicio = cron.getServicio();
        long estudiantesAdentro = contarEstudiantesEnServicio(servicio);
        if (estudiantesAdentro >= servicio.getCapacidadMaxima()) {
            denegar("Cupo máximo alcanzado en " + servicio.getNombre() +
                    " (" + servicio.getCapacidadMaxima() + "/" + servicio.getCapacidadMaxima() + ").");
            return;
        }

        RegistroAcceso reg = new RegistroAcceso(est, servicio, cron);
        registroDAO.save(reg);

        permitir("Acceso PERMITIDO — " + est.getNombre() + " → " + servicio.getNombre());
        txtCedula.clear();
        cargarActivos();
    }

    @FXML
    public void registrarSalida() {
        RegistroAcceso reg = tablaActivos.getSelectionModel().getSelectedItem();
        if (reg == null) {
            mostrarAlerta("Seleccione un estudiante de la lista para registrar su salida.");
            return;
        }
        reg.setHoraSalida(LocalDateTime.now());
        registroDAO.save(reg);
        setSemaforo(null);
        lblMensaje.setText("Salida registrada para " + reg.getEstudiante().getNombre());
        cargarActivos();
    }

    private Estudiante buscarEstudiantePorCedula(String cedula) {
        return estudianteDAO.findAll().stream()
                .filter(e -> e.getCedula().equals(cedula))
                .findFirst().orElse(null);
    }

    private Cronograma buscarCronogramaHoy(Estudiante est) {
        LocalDate hoy   = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return cronogramaDAO.findAll().stream()
                .filter(c -> c.getEstudiante().getIdEstudiante().equals(est.getIdEstudiante()))
                .filter(c -> c.getFecha().equals(hoy))
                // Cambiado de '==' a '.equals()' o chequeo por Name del Enum para evitar fallos de memoria física orientada a objetos
                .filter(c -> c.getEstado() != null && c.getEstado().equals(Cronograma.Estado.APROBADO))
                .filter(c -> !ahora.isBefore(c.getHoraInicio()) && !ahora.isAfter(c.getHoraFin()))
                .findFirst().orElse(null);
    }

    private boolean docentePresente(Docente docente) {
        if (docente == null) return false;
        return docenteRegDAO.findAll().stream()
                .anyMatch(rd -> rd.getDocente().getIdDocente().equals(docente.getIdDocente())
                        && rd.getHoraEntrada().toLocalDate().equals(LocalDate.now())
                        && rd.getHoraSalida() == null);
    }

    private long contarEstudiantesEnServicio(Servicio servicio) {
        return registroDAO.findAll().stream()
                .filter(r -> r.getServicio().getIdServicio().equals(servicio.getIdServicio()))
                .filter(r -> r.getHoraSalida() == null)
                .count();
    }

    private void cargarActivos() {
        List<RegistroAcceso> activos = registroDAO.findAll().stream()
                .filter(r -> r.getHoraSalida() == null)
                .toList();

        tablaActivos.setItems(FXCollections.observableArrayList(activos));
        tablaActivos.refresh();

        int total = activos.size();
        lblContador.setText(String.valueOf(total));
        lblBadgeCount.setText(total + (total == 1 ? " estudiante" : " estudiantes"));
    }

    private void denegar(String mensaje) {
        setSemaforo(Color.RED);
        lblEstado.setText("ACCESO DENEGADO");
        lblEstado.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold; -fx-font-size: 13px;");
        lblMensaje.setText(mensaje);
    }

    private void permitir(String mensaje) {
        setSemaforo(Color.web("#27ae60"));
        lblEstado.setText("ACCESO PERMITIDO");
        lblEstado.setStyle("-fx-text-fill: #7fffb0; -fx-font-weight: bold; -fx-font-size: 13px;");
        lblMensaje.setText(mensaje);
    }

    private void setSemaforo(Color color) {
        if (color == null) {
            semaforo.setFill(Color.GRAY);
            lblEstado.setText("Esperando cédula...");
            lblEstado.setStyle("-fx-text-fill: #8aafd0; -fx-font-size: 13px;");
            lblMensaje.setText("Ingrese el documento del alumno para verificar su autorización de acceso.");
        } else {
            semaforo.setFill(color);
        }
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}