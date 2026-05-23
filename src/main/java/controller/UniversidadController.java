package controller;

import dao.UniversidadDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Universidad;

public class UniversidadController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtNit;
    @FXML private TextField txtCiudad;
    @FXML private TableView<Universidad> tablaUniversidades;
    @FXML private TableColumn<Universidad, Long>    colId;
    @FXML private TableColumn<Universidad, String>  colNombre;
    @FXML private TableColumn<Universidad, String>  colNit;
    @FXML private TableColumn<Universidad, String>  colCiudad;
    @FXML private TableColumn<Universidad, Boolean> colActivo;

    private UniversidadDAO dao;

    @FXML
    public void initialize() {
        dao = new UniversidadDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idUniversidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNit.setCellValueFactory(new PropertyValueFactory<>("nit"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("convenioActivo"));

        tablaUniversidades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cargar();
    }

    @FXML
    public void guardar() {
        if (txtNombre.getText().isEmpty() || txtNit.getText().isEmpty() || txtCiudad.getText().isEmpty()) return;

        dao.save(new Universidad(txtNombre.getText(), txtNit.getText(), txtCiudad.getText()));

        txtNombre.clear();
        txtNit.clear();
        txtCiudad.clear();
        cargar();
    }

    @FXML
    public void eliminar() {
        Universidad u = tablaUniversidades.getSelectionModel().getSelectedItem();
        if (u != null) {
            dao.delete(u.getIdUniversidad());
            cargar();
        }
    }

    private void cargar() {
        tablaUniversidades.setItems(FXCollections.observableArrayList(dao.findAll()));
    }
}
