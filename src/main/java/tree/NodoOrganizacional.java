package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic node for the Hurst Control organizational tree.
 * Each node holds a typed value and an ordered list of children,
 * forming an N-ary tree that mirrors the University → Docente → Estudiante hierarchy.
 *
 * @param <T> the type of the value stored in this node
 */
public class NodoOrganizacional<T> {

    private final T valor;
    private final List<NodoOrganizacional<?>> hijos;

    public NodoOrganizacional(T valor) {
        if (valor == null) throw new IllegalArgumentException("El valor del nodo no puede ser null.");
        this.valor = valor;
        this.hijos = new ArrayList<>();
    }

    public T getValor() {
        return valor;
    }

    public List<NodoOrganizacional<?>> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }

    public void agregarHijo(NodoOrganizacional<?> hijo) {
        if (hijo == null) throw new IllegalArgumentException("El nodo hijo no puede ser null.");
        hijos.add(hijo);
    }

    public boolean eliminarHijo(NodoOrganizacional<?> hijo) {
        return hijos.remove(hijo);
    }

    public void recorrerPreOrden(NodoVisitor visitor) {
        visitor.visitar(this);
        for (NodoOrganizacional<?> hijo : hijos) {
            hijo.recorrerPreOrden(visitor);
        }
    }

    public int profundidad() {
        if (esHoja()) return 0;
        int max = 0;
        for (NodoOrganizacional<?> hijo : hijos) {
            max = Math.max(max, hijo.profundidad());
        }
        return 1 + max;
    }

    public int contarDescendientes() {
        int total = 0;
        for (NodoOrganizacional<?> hijo : hijos) {
            total += 1 + hijo.contarDescendientes();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Nodo[" + valor.toString() + "]";
    }
}
