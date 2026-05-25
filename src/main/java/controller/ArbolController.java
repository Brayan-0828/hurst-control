package controller;

import facade.HurstFacade;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import model.Docente;
import model.Estudiante;
import model.Universidad;
import tree.ArbolOrganizacional;
import tree.NodoOrganizacional;

/**
 * Controller for arbol.fxml.
 *
 * Builds a JavaFX TreeView from the ArbolOrganizacional returned by HurstFacade,
 * and populates a detail panel whenever the user clicks a node.
 *
 * Hierarchy levels and their colours:
 *   Level 0 – Root (HURST CONTROL)   → #64d2ff  (cyan)
 *   Level 1 – Universidad             → #a78bfa  (violet)
 *   Level 2 – Docente                 → #fbbf24  (amber)
 *   Level 3 – Estudiante              → #34d399  (green)
 */
public class ArbolController {

    // ── FXML injections ───────────────────────────────────────────────────────

    @FXML private TreeView<String>  arbolView;

    // Stats bar
    @FXML private Label lblTotalUniversidades;
    @FXML private Label lblTotalDocentes;
    @FXML private Label lblTotalEstudiantes;

    // Detail panel
    @FXML private VBox  panelVacio;
    @FXML private VBox  panelInfo;
    @FXML private Label lblTipoBadge;
    @FXML private Label lblProfundidad;
    @FXML private Label lblNombreNodo;
    @FXML private Label lblTipoValor;
    @FXML private Label lblHijos;
    @FXML private Label lblDescendientes;
    @FXML private Label lblProfundidadSub;
    @FXML private Label lblExtraKey;
    @FXML private Label lblExtraVal;
    @FXML private Label lblExtra2Key;
    @FXML private Label lblExtra2Val;
    @FXML private SVGPath iconoHoja;
    @FXML private Label lblEsHoja;

    private final HurstFacade facade = HurstFacade.getInstance();

    @FXML
    public void initialize() {
        facade.refrescarArbol();
        construirArbolView();
        configurarSeleccion();
    }

    @FXML
    public void refrescarArbol() {
        facade.refrescarArbol();
        construirArbolView();
    }

    private void construirArbolView() {
        ArbolOrganizacional arbol = facade.getArbol();
        NodoOrganizacional<String> raiz = arbol.getRaiz();

        TreeItem<String> raizItem = crearItem("🏥  " + raiz.getValor(), "#64d2ff", raiz);
        raizItem.setExpanded(true);

        int totalDocentes = 0;

        for (NodoOrganizacional<?> nodoUniv : raiz.getHijos()) {
            Universidad univ = (Universidad) nodoUniv.getValor();
            TreeItem<String> itemUniv = crearItem(
                    "🏛  " + univ.getNombre() + "  ·  " + univ.getCiudad(),
                    "#a78bfa",
                    nodoUniv);
            itemUniv.setExpanded(true);

            for (NodoOrganizacional<?> nodoDoc : nodoUniv.getHijos()) {
                Docente doc = (Docente) nodoDoc.getValor();
                TreeItem<String> itemDoc = crearItem(
                        "👨‍🏫  " + doc.getNombre(),
                        "#fbbf24",
                        nodoDoc);
                itemDoc.setExpanded(true);
                totalDocentes++;

                for (NodoOrganizacional<?> nodoEst : nodoDoc.getHijos()) {
                    Estudiante est = (Estudiante) nodoEst.getValor();
                    TreeItem<String> itemEst = crearItem(
                            "🎓  " + est.getNombre() + "  ·  Sem " + est.getSemestre(),
                            "#34d399",
                            nodoEst);
                    itemDoc.getChildren().add(itemEst);
                }

                itemUniv.getChildren().add(itemDoc);
            }

            raizItem.getChildren().add(itemUniv);
        }

        arbolView.setRoot(raizItem);
        arbolView.setShowRoot(true);

        // Update stats
        int univCount = raiz.getHijos().size();
        lblTotalUniversidades.setText(String.valueOf(univCount));
        lblTotalDocentes.setText(String.valueOf(totalDocentes));
        lblTotalEstudiantes.setText(String.valueOf(arbol.totalEstudiantes()));
    }

    @SuppressWarnings("unchecked")
    private TreeItem<String> crearItem(String label, String color, NodoOrganizacional<?> nodo) {
        TreeItem<String> item = new TreeItem<>(label);
        item.setGraphic(buildDot(color));
        item.setExpanded(false);
        item.getGraphic().setUserData(nodo);
        return item;
    }

    private javafx.scene.shape.Circle buildDot(String hexColor) {
        javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(5);
        c.setFill(javafx.scene.paint.Color.web(hexColor));
        return c;
    }

    private void configurarSeleccion() {
        arbolView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal == null || newVal.getGraphic() == null) {
                        mostrarPanelVacio();
                        return;
                    }
                    Object userData = newVal.getGraphic().getUserData();
                    if (userData instanceof NodoOrganizacional) {
                        mostrarDetalle((NodoOrganizacional<?>) userData, newVal);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void mostrarDetalle(NodoOrganizacional<?> nodo, TreeItem<String> item) {
        Object valor = nodo.getValor();

        panelVacio.setVisible(false);
        panelVacio.setManaged(false);
        panelInfo.setVisible(true);
        panelInfo.setManaged(true);

        int nivel = calcularNivel(item);
        lblProfundidad.setText("Nivel " + nivel);
        lblHijos.setText(String.valueOf(nodo.getHijos().size()));
        lblDescendientes.setText(String.valueOf(nodo.contarDescendientes()));
        lblProfundidadSub.setText(String.valueOf(nodo.profundidad()));

        if (valor instanceof String) {
            configurarBadge("RAÍZ", "#64d2ff");
            lblNombreNodo.setText((String) valor);
            lblTipoValor.setText("Nodo raíz (virtual)");
            lblExtraKey.setText("Universidades hijas");
            lblExtraVal.setText(String.valueOf(nodo.getHijos().size()));
            lblExtra2Key.setText("");
            lblExtra2Val.setText("");
            marcarHoja(false);

        } else if (valor instanceof Universidad) {
            Universidad u = (Universidad) valor;
            configurarBadge("UNIVERSIDAD", "#a78bfa");
            lblNombreNodo.setText(u.getNombre());
            lblTipoValor.setText("Universidad");
            lblExtraKey.setText("NIT");
            lblExtraVal.setText(u.getNit());
            lblExtra2Key.setText("Ciudad");
            lblExtra2Val.setText(u.getCiudad()
                    + (u.isConvenioActivo() ? "  ✔ Convenio activo" : "  ✖ Sin convenio"));
            marcarHoja(nodo.esHoja());

        } else if (valor instanceof Docente) {
            Docente d = (Docente) valor;
            configurarBadge("DOCENTE", "#fbbf24");
            lblNombreNodo.setText(d.getNombre());
            lblTipoValor.setText("Docente");
            lblExtraKey.setText("Cédula");
            lblExtraVal.setText(d.getCedula());
            lblExtra2Key.setText("Correo");
            lblExtra2Val.setText(d.getCorreo() != null ? d.getCorreo() : "—");
            marcarHoja(nodo.esHoja());

        } else if (valor instanceof Estudiante) {
            Estudiante e = (Estudiante) valor;
            configurarBadge("ESTUDIANTE", "#34d399");
            lblNombreNodo.setText(e.getNombre());
            lblTipoValor.setText("Estudiante  ·  " + e.getPrograma());
            lblExtraKey.setText("Cédula");
            lblExtraVal.setText(e.getCedula());
            lblExtra2Key.setText("Semestre  /  Inducción");
            lblExtra2Val.setText(e.getSemestre()
                    + "°  ·  " + e.getEstadoInduccion().toString().toLowerCase());
            marcarHoja(true); // always leaf
        }
    }

    private void configurarBadge(String texto, String color) {
        lblTipoBadge.setText(texto);
        lblTipoBadge.setStyle(
                "-fx-background-color: " + color + "22;"
                + "-fx-text-fill: " + color + ";"
                + "-fx-font-size: 11px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 20;"
                + "-fx-padding: 4 12;"
                + "-fx-border-color: " + color + "55;"
                + "-fx-border-radius: 20;");
        iconoHoja.setFill(javafx.scene.paint.Color.web(color));
    }

    private void marcarHoja(boolean esHoja) {
        if (esHoja) {
            lblEsHoja.setText("Nodo hoja — no tiene hijos en el árbol");
            iconoHoja.setContent("M17 12h-5v5h5v-5zM16 1v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2h-1V1h-2zm3 18H5V8h14v11z");
        } else {
            lblEsHoja.setText("Nodo interno — tiene hijos en el árbol");
            iconoHoja.setContent("M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z");
        }
    }

    private void mostrarPanelVacio() {
        panelVacio.setVisible(true);
        panelVacio.setManaged(true);
        panelInfo.setVisible(false);
        panelInfo.setManaged(false);
    }

    private int calcularNivel(TreeItem<?> item) {
        int nivel = 0;
        TreeItem<?> current = item.getParent();
        while (current != null) {
            nivel++;
            current = current.getParent();
        }
        return nivel;
    }
}
