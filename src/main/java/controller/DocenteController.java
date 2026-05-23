package controller;

import dao.DocenteDAO;
import dao.UniversidadDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Docente;
import model.Universidad;

public class DocenteController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<Universidad> cbUniversidad;
    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, Long>   colId;
    @FXML private TableColumn<Docente, String> colCedula;
    @FXML private TableColumn<Docente, String> colNombre;
    @FXML private TableColumn<Docente, String> colCorreo;
    @FXML private TableColumn<Docente, Universidad> colUniversidad;

    private DocenteDAO dao;
    private UniversidadDAO univDAO;

    @FXML
    public void initialize() {
        dao = new DocenteDAO();
        univDAO = new UniversidadDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idDocente"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colUniversidad.setCellValueFactory(new PropertyValueFactory<>("universidad"));

        tablaDocentes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cbUniversidad.setItems(FXCollections.observableArrayList(univDAO.findAll()));
        cargar();
    }

    @FXML
    public void guardar() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || cbUniversidad.getValue() == null) return;

        dao.save(new Docente(
                txtCedula.getText(),
                txtNombre.getText(),
                txtCorreo.getText(),
                cbUniversidad.getValue()
        ));

        txtCedula.clear();
        txtNombre.clear();
        txtCorreo.clear();
        cbUniversidad.setValue(null);
        cargar();
    }

    @FXML
    public void eliminar() {
        Docente d = tablaDocentes.getSelectionModel().getSelectedItem();
        if (d != null) {
            dao.delete(d.getIdDocente());
            cargar();
        }
    }

    private void cargar() {
        tablaDocentes.setItems(FXCollections.observableArrayList(dao.findAll()));
    }
}
