package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML private HBox btnCronograma;
    @FXML private HBox btnEstudiantes;
    @FXML private HBox btnDocentes;
    @FXML private HBox btnServicios;
    @FXML private HBox btnUniversidades;
    @FXML private HBox btnAcceso;

    @FXML
    public void abrirCronogramas(MouseEvent event) {
        cambiarVista("cronograma.fxml");
        actualizarSeleccion(btnCronograma);
    }

    @FXML
    public void abrirEstudiantes(MouseEvent event) {
        cambiarVista("estudiante.fxml");
        actualizarSeleccion(btnEstudiantes);
    }

    @FXML
    public void abrirDocentes(MouseEvent event) {
        cambiarVista("docente.fxml");
        actualizarSeleccion(btnDocentes);
    }

    @FXML
    public void abrirServicios(MouseEvent event) {
        cambiarVista("servicio.fxml");
        actualizarSeleccion(btnServicios);
    }

    @FXML
    public void abrirUniversidades(MouseEvent event) {
        cambiarVista("universidad.fxml");
        actualizarSeleccion(btnUniversidades);
    }

    @FXML
    public void abrirAcceso(MouseEvent event) {
        cambiarVista("acceso.fxml");
        actualizarSeleccion(btnAcceso);
    }

    @FXML
    public void salir(MouseEvent event) {
        System.exit(0);
    }

    private void cambiarVista(String vistaFxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("/view/" + vistaFxml));
            rootPane.setCenter(vista);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: /view/" + vistaFxml);
            e.printStackTrace();
        }
    }

    private void actualizarSeleccion(HBox botonSeleccionado) {
        HBox[] todosLosBotones = {btnCronograma, btnEstudiantes, btnDocentes, btnServicios, btnUniversidades, btnAcceso};

        for (HBox boton : todosLosBotones) {
            if (boton == botonSeleccionado) {
                boton.setStyle("-fx-background-color: #2a437e; -fx-background-radius: 6; -fx-padding: 11 14; -fx-cursor: hand;");
                ((SVGPath) boton.getChildren().get(0)).setFill(javafx.scene.paint.Color.web("#64d2ff"));
                ((Label) boton.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
            } else {
                boton.setStyle("-fx-background-color: transparent; -fx-background-radius: 6; -fx-padding: 11 14; -fx-cursor: hand;");
                ((SVGPath) boton.getChildren().get(0)).setFill(javafx.scene.paint.Color.web("#a4b6d4"));
                ((Label) boton.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: normal; -fx-text-fill: #a4b6d4;");
            }
        }
    }
}