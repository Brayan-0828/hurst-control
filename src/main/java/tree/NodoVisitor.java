package tree;

/**
 * Visitor interface used during tree traversal.
 * Implement this to perform an action on every node visited.
 */
@FunctionalInterface
public interface NodoVisitor {
    void visitar(NodoOrganizacional<?> nodo);
}
