package controller;

import dao.DocenteDAO;
import dao.EstudianteDAO;
import dao.UniversidadDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Docente;
import model.Estudiante;
import model.Universidad;

import java.time.LocalDate;

public class EstudianteController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrograma;
    @FXML private TextField txtSemestre;
    @FXML private ComboBox<Universidad>  cbUniversidad;
    @FXML private ComboBox<Docente>      cbDocente;
    @FXML private DatePicker dpArlInicio;
    @FXML private DatePicker dpArlVigencia;
    @FXML private DatePicker dpSeguroInicio;
    @FXML private DatePicker dpSeguroVigencia;
    @FXML private ComboBox<Estudiante.EstadoInduccion> cbInduccion;

    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Long>   colId;
    @FXML private TableColumn<Estudiante, String> colCedula;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colPrograma;
    @FXML private TableColumn<Estudiante, Integer> colSemestre;
    @FXML private TableColumn<Estudiante, Universidad> colUniversidad;
    @FXML private TableColumn<Estudiante, LocalDate>   colArlVigencia;
    @FXML private TableColumn<Estudiante, Estudiante.EstadoInduccion> colInduccion;

    private EstudianteDAO dao;
    private UniversidadDAO univDAO;
    private DocenteDAO docenteDAO;

    private Estudiante estudianteSeleccionado = null;

    @FXML
    public void initialize() {
        dao = new EstudianteDAO();
        univDAO = new UniversidadDAO();
        docenteDAO = new DocenteDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrograma.setCellValueFactory(new PropertyValueFactory<>("programa"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colUniversidad.setCellValueFactory(new PropertyValueFactory<>("universidad"));
        colArlVigencia.setCellValueFactory(new PropertyValueFactory<>("arlVigencia"));
        colInduccion.setCellValueFactory(new PropertyValueFactory<>("estadoInduccion"));

        tablaEstudiantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        cbUniversidad.setItems(FXCollections.observableArrayList(univDAO.findAll()));
        cbDocente.setItems(FXCollections.observableArrayList(docenteDAO.findAll()));
        cbInduccion.setItems(FXCollections.observableArrayList(Estudiante.EstadoInduccion.values()));
        cbInduccion.setValue(Estudiante.EstadoInduccion.PENDIENTE);

        configurarConvertidores();

        tablaEstudiantes.setRowFactory(tv -> {
            TableRow<Estudiante> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editar();
                }
            });
            return row;
        });

        cargar();
    }

    private void configurarConvertidores() {
        cbUniversidad.setConverter(new StringConverter<>() {
            @Override public String toString(Universidad u) { return u != null ? u.getNombre() : ""; }
            @Override public Universidad fromString(String s) { return null; }
        });
        cbDocente.setConverter(new StringConverter<>() {
            @Override public String toString(Docente d) { return d != null ? d.getNombre() : ""; }
            @Override public Docente fromString(String s) { return null; }
        });
    }

    @FXML
    public void editar() {
        estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            mostrarAlerta("Seleccione un estudiante de la tabla para editar.");
            return;
        }

        txtCedula.setText(estudianteSeleccionado.getCedula());
        txtNombre.setText(estudianteSeleccionado.getNombre());
        txtPrograma.setText(estudianteSeleccionado.getPrograma());
        txtSemestre.setText(String.valueOf(estudianteSeleccionado.getSemestre()));
        cbUniversidad.setValue(estudianteSeleccionado.getUniversidad());
        cbDocente.setValue(estudianteSeleccionado.getDocente());
        dpArlInicio.setValue(estudianteSeleccionado.getArlInicio());
        dpArlVigencia.setValue(estudianteSeleccionado.getArlVigencia());
        dpSeguroInicio.setValue(estudianteSeleccionado.getSeguroInicio());
        dpSeguroVigencia.setValue(estudianteSeleccionado.getSeguroVigencia());
        cbInduccion.setValue(estudianteSeleccionado.getEstadoInduccion());
    }

    @FXML
    public void guardar() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtPrograma.getText().isEmpty() || txtSemestre.getText().isEmpty()
                || cbUniversidad.getValue() == null || cbDocente.getValue() == null
                || dpArlInicio.getValue() == null || dpArlVigencia.getValue() == null
                || dpSeguroInicio.getValue() == null || dpSeguroVigencia.getValue() == null) {
            mostrarAlerta("Por favor complete todos los campos obligatorios.");
            return;
        }

        try {
            if (estudianteSeleccionado == null) {
                estudianteSeleccionado = new Estudiante();
            }

            estudianteSeleccionado.setCedula(txtCedula.getText());
            estudianteSeleccionado.setNombre(txtNombre.getText());
            estudianteSeleccionado.setPrograma(txtPrograma.getText());
            estudianteSeleccionado.setSemestre(Integer.parseInt(txtSemestre.getText()));
            estudianteSeleccionado.setUniversidad(cbUniversidad.getValue());
            estudianteSeleccionado.setDocente(cbDocente.getValue());
            estudianteSeleccionado.setArlInicio(dpArlInicio.getValue());
            estudianteSeleccionado.setArlVigencia(dpArlVigencia.getValue());
            estudianteSeleccionado.setSeguroInicio(dpSeguroInicio.getValue());
            estudianteSeleccionado.setSeguroVigencia(dpSeguroVigencia.getValue());

            if (cbInduccion.getValue() != null) {
                estudianteSeleccionado.setEstadoInduccion(cbInduccion.getValue());
            }

            dao.save(estudianteSeleccionado);

            limpiar();
            cargar();
        } catch (NumberFormatException ex) {
            mostrarAlerta("El semestre debe ser un número entero.");
        }
    }

    @FXML
    public void eliminar() {
        Estudiante est = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (est != null) {
            dao.delete(est.getIdEstudiante());
            limpiar();
            cargar();
        }
    }

    private void limpiar() {
        txtCedula.clear(); txtNombre.clear(); txtPrograma.clear(); txtSemestre.clear();
        cbUniversidad.setValue(null); cbDocente.setValue(null);
        dpArlInicio.setValue(null); dpArlVigencia.setValue(null);
        dpSeguroInicio.setValue(null); dpSeguroVigencia.setValue(null);
        cbInduccion.setValue(Estudiante.EstadoInduccion.PENDIENTE);

        estudianteSeleccionado = null;
    }

    private void cargar() {
        tablaEstudiantes.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}