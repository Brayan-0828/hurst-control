package controller;

import dao.ServicioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Servicio;

public class ServicioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtUbicacion;
    @FXML private TextField txtCapacidad;
    @FXML private TableView<Servicio> tablaServicios;
    @FXML private TableColumn<Servicio, Long>   colId;
    @FXML private TableColumn<Servicio, String> colNombre;
    @FXML private TableColumn<Servicio, String> colUbicacion;
    @FXML private TableColumn<Servicio, Integer> colCapacidad;

    private ServicioDAO dao;

    @FXML
    public void initialize() {
        dao = new ServicioDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("idServicio"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidadMaxima"));

        tablaServicios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cargar();
    }

    @FXML
    public void guardar() {
        if (txtNombre.getText().isEmpty() || txtCapacidad.getText().isEmpty()) return;
        try {
            int cap = Integer.parseInt(txtCapacidad.getText());
            dao.save(new Servicio(txtNombre.getText(), txtUbicacion.getText(), cap));
            txtNombre.clear();
            txtUbicacion.clear();
            txtCapacidad.clear();
            cargar();
        } catch (NumberFormatException e) {
            mostrarAlerta("La capacidad debe ser un número entero.");
        }
    }

    @FXML
    public void eliminar() {
        Servicio s = tablaServicios.getSelectionModel().getSelectedItem();
        if (s != null) {
            dao.delete(s.getIdServicio());
            cargar();
        }
    }

    private void cargar() {
        tablaServicios.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}
