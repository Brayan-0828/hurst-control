package tree;

@FunctionalInterface
public interface NodoVisitor {
    void visitar(NodoOrganizacional<?> nodo);
}
