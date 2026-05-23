package controller;

import dao.CronogramaDAO;
import dao.DocenteDAO;
import dao.EstudianteDAO;
import dao.ServicioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleStringProperty;
import model.Cronograma;
import model.Docente;
import model.Estudiante;
import model.Servicio;

import java.time.LocalDate;
import java.time.LocalTime;

public class CronogramaController {

    @FXML private ComboBox<Estudiante> cbEstudiante;
    @FXML private ComboBox<Docente>    cbDocente;
    @FXML private ComboBox<Servicio>   cbServicio;
    @FXML private DatePicker           dpFecha;
    @FXML private TextField            txtHoraInicio;
    @FXML private TextField            txtHoraFin;
    @FXML private ComboBox<Cronograma.Estado> cbEstado;

    @FXML private TableView<Cronograma> tablaCronogramas;
    @FXML private TableColumn<Cronograma, Long>   colId;
    @FXML private TableColumn<Cronograma, Estudiante> colEstudiante;
    @FXML private TableColumn<Cronograma, String>     colDocente;
    @FXML private TableColumn<Cronograma, Servicio>   colServicio;
    @FXML private TableColumn<Cronograma, LocalDate>  colFecha;
    @FXML private TableColumn<Cronograma, LocalTime>  colInicio;
    @FXML private TableColumn<Cronograma, LocalTime>  colFin;
    @FXML private TableColumn<Cronograma, Cronograma.Estado> colEstado;

    private CronogramaDAO dao;

    @FXML
    public void initialize() {
        dao = new CronogramaDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idCronograma"));
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("estudiante"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colDocente.setCellValueFactory(cellData -> {
            Cronograma crono = cellData.getValue();
            if (crono != null && crono.getDocente() != null) {
                return new SimpleStringProperty(crono.getDocente().getNombre());
            }
            return new SimpleStringProperty("Sin Docente");
        });

        tablaCronogramas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        cbEstudiante.setItems(FXCollections.observableArrayList(new EstudianteDAO().findAll()));
        cbDocente.setItems(FXCollections.observableArrayList(new DocenteDAO().findAll()));
        cbServicio.setItems(FXCollections.observableArrayList(new ServicioDAO().findAll()));
        cbEstado.setItems(FXCollections.observableArrayList(Cronograma.Estado.values()));
        cbEstado.setValue(Cronograma.Estado.PENDIENTE);

        configurarConvertidoresCombos();
        forzarEstiloCeldasBlancas();

        cargar();
    }

    private void configurarConvertidoresCombos() {
        cbDocente.setConverter(new StringConverter<Docente>() {
            @Override public String toString(Docente d) { return d != null ? d.getNombre() : ""; }
            @Override public Docente fromString(String s) { return null; }
        });
        cbEstudiante.setConverter(new StringConverter<Estudiante>() {
            @Override public String toString(Estudiante e) { return e != null ? e.getNombre() : ""; }
            @Override public Estudiante fromString(String s) { return null; }
        });
        cbServicio.setConverter(new StringConverter<Servicio>() {
            @Override public String toString(Servicio s) { return s != null ? s.getNombre() : ""; }
            @Override public Servicio fromString(String s) { return null; }
        });
    }

    private void forzarEstiloCeldasBlancas() {
        // Estilo común para inyectar texto blanco en las filas de la tabla
        String estiloBlanco = "-fx-text-fill: #ffffff; -fx-alignment: CENTER-LEFT;";

        colId.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.valueOf(item));
                setStyle(estiloBlanco);
            }
        });

        colEstudiante.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Estudiante item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
                setStyle(estiloBlanco);
            }
        });

        colDocente.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle(estiloBlanco);
            }
        });

        colServicio.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Servicio item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
                setStyle(estiloBlanco);
            }
        });

        colFecha.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
                setStyle(estiloBlanco);
            }
        });

        colInicio.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
                setStyle(estiloBlanco);
            }
        });

        colFin.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
                setStyle(estiloBlanco);
            }
        });

        colEstado.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Cronograma.Estado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item.toString());
                    // Colores temáticos por estado con alto contraste
                    if (item == Cronograma.Estado.APROBADO) {
                        setStyle("-fx-text-fill: #52d689; -fx-font-weight: bold;");
                    } else if (item == Cronograma.Estado.RECHAZADO) {
                        setStyle("-fx-text-fill: #ffa3a3; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #ffb875; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    @FXML
    public void guardar() {
        if (cbEstudiante.getValue() == null || cbDocente.getValue() == null
                || cbServicio.getValue() == null || dpFecha.getValue() == null
                || txtHoraInicio.getText().isEmpty() || txtHoraFin.getText().isEmpty()) {
            mostrarAlerta("Complete todos los campos obligatorios.");
            return;
        }
        try {
            LocalTime inicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime fin    = LocalTime.parse(txtHoraFin.getText());
            Cronograma c = new Cronograma(
                    cbEstudiante.getValue(), cbDocente.getValue(), cbServicio.getValue(),
                    dpFecha.getValue(), inicio, fin
            );
            if (cbEstado.getValue() != null) c.setEstado(cbEstado.getValue());
            dao.save(c);
            limpiar();
            cargar();
        } catch (Exception ex) {
            mostrarAlerta("Hora inválida. Use formato HH:mm (ej: 08:00).");
        }
    }

    @FXML
    public void aprobar() {
        cambiarEstado(Cronograma.Estado.APROBADO);
    }

    @FXML
    public void rechazar() {
        cambiarEstado(Cronograma.Estado.RECHAZADO);
    }

    private void cambiarEstado(Cronograma.Estado nuevoEstado) {
        Cronograma c = tablaCronogramas.getSelectionModel().getSelectedItem();
        if (c != null) {
            c.setEstado(nuevoEstado);
            dao.save(c);
            cargar();
        }
    }

    @FXML
    public void eliminar() {
        Cronograma c = tablaCronogramas.getSelectionModel().getSelectedItem();
        if (c != null) {
            dao.delete(c.getIdCronograma());
            cargar();
        }
    }

    private void limpiar() {
        cbEstudiante.setValue(null); cbDocente.setValue(null);
        cbServicio.setValue(null); dpFecha.setValue(null);
        txtHoraInicio.clear(); txtHoraFin.clear();
        cbEstado.setValue(Cronograma.Estado.PENDIENTE);
    }

    private void cargar() {
        tablaCronogramas.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}