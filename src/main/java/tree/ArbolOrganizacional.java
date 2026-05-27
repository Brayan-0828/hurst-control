package tree;

import model.Docente;
import model.Estudiante;
import model.Universidad;

import java.util.ArrayList;
import java.util.List;

public class ArbolOrganizacional {

    private final NodoOrganizacional<String> raiz;

    public ArbolOrganizacional() {
        raiz = new NodoOrganizacional<>("HURST CONTROL");
    }

    public void construir(List<Universidad> universidades,
                          List<Docente>     docentes,
                          List<Estudiante>  estudiantes) {

        for (NodoOrganizacional<?> hijo : new ArrayList<>(raiz.getHijos())) {
            raiz.eliminarHijo(hijo);
        }

        for (Universidad univ : universidades) {
            NodoOrganizacional<Universidad> nodoUniv = new NodoOrganizacional<>(univ);

            for (Docente doc : docentes) {
                if (doc.getUniversidad() != null
                        && doc.getUniversidad().getIdUniversidad()
                               .equals(univ.getIdUniversidad())) {

                    NodoOrganizacional<Docente> nodoDoc = new NodoOrganizacional<>(doc);

                    for (Estudiante est : estudiantes) {
                        if (est.getDocente() != null
                                && est.getDocente().getIdDocente()
                                       .equals(doc.getIdDocente())) {
                            nodoDoc.agregarHijo(new NodoOrganizacional<>(est));
                        }
                    }

                    nodoUniv.agregarHijo(nodoDoc);
                }
            }

            raiz.agregarHijo(nodoUniv);
        }
    }

    @SuppressWarnings("unchecked")
    public List<NodoOrganizacional<Universidad>> getUniversidades() {
        List<NodoOrganizacional<Universidad>> result = new ArrayList<>();
        for (NodoOrganizacional<?> hijo : raiz.getHijos()) {
            result.add((NodoOrganizacional<Universidad>) hijo);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<NodoOrganizacional<Docente>> getDocentesPorUniversidad(Universidad universidad) {
        List<NodoOrganizacional<Docente>> result = new ArrayList<>();
        for (NodoOrganizacional<Universidad> nodoUniv : getUniversidades()) {
            if (nodoUniv.getValor().getIdUniversidad()
                        .equals(universidad.getIdUniversidad())) {
                for (NodoOrganizacional<?> nodoDoc : nodoUniv.getHijos()) {
                    result.add((NodoOrganizacional<Docente>) nodoDoc);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<NodoOrganizacional<Estudiante>> getEstudiantesPorDocente(Docente docente) {
        List<NodoOrganizacional<Estudiante>> result = new ArrayList<>();
        for (NodoOrganizacional<Universidad> nodoUniv : getUniversidades()) {
            for (NodoOrganizacional<?> nodoDoc : nodoUniv.getHijos()) {
                Docente d = (Docente) nodoDoc.getValor();
                if (d.getIdDocente().equals(docente.getIdDocente())) {
                    for (NodoOrganizacional<?> nodoEst : nodoDoc.getHijos()) {
                        result.add((NodoOrganizacional<Estudiante>) nodoEst);
                    }
                }
            }
        }
        return result;
    }

    public void recorrer(NodoVisitor visitor) {
        raiz.recorrerPreOrden(visitor);
    }

    public int totalEstudiantes() {
        int[] count = {0};
        recorrer(nodo -> {
            if (nodo.esHoja() && nodo.getValor() instanceof Estudiante) {
                count[0]++;
            }
        });
        return count[0];
    }

    public String imprimirArbol() {
        StringBuilder sb = new StringBuilder();
        imprimirNodo(raiz, 0, sb);
        return sb.toString();
    }

    private void imprimirNodo(NodoOrganizacional<?> nodo, int nivel, StringBuilder sb) {
        sb.append("  ".repeat(nivel)).append("- ").append(nodo.getValor().toString()).append("\n");
        for (NodoOrganizacional<?> hijo : nodo.getHijos()) {
            imprimirNodo(hijo, nivel + 1, sb);
        }
    }

    public NodoOrganizacional<String> getRaiz() {
        return raiz;
    }
}
