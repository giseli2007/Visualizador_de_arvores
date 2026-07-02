package model;

import operations.Step;
import java.util.ArrayList;
import java.util.List;

/**
 * Base comum para AB, ABB, AVL e Rubro-Negra.
 *
 * Implementa tudo que é idêntico nas quatro árvores e delega
 * pras subclasses apenas o que varia: insert, remove e search.
 *
 * "abstract" significa que essa classe não pode ser instanciada
 * diretamente — ela só existe pra ser herdada.
 */
public abstract class AbstractBinaryTree<T extends Comparable<T>> implements Tree<T> {

    // ─── Estado ──────────────────────────────────────────────────────────────

    /** A raiz da árvore. Null quando a árvore está vazia. */
    protected TreeNode<T> root;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public AbstractBinaryTree() {
        this.root = null;
    }

    // ─── Implementações do contrato Tree<T> ──────────────────────────────────

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public void clear() {
        root = null;
        // A JVM vai coletar os nós órfãos automaticamente (garbage collection).
        // Não precisamos percorrer a árvore deletando nó a nó como faríamos em C.
    }

    // ─── Métodos auxiliares protegidos (disponíveis pras subclasses) ──────────

    /**
     * Retorna a altura de um nó.
     * Separado num método porque null.height causaria NullPointerException —
     * precisamos tratar o caso "nó inexistente = altura 0" em um só lugar.
     */
    protected int height(TreeNode<T> node) {
        return (node == null) ? 0 : node.height;
    }

    /**
     * Recalcula e atualiza a altura de um nó com base nos filhos.
     * Chamado pelas subclasses (AVL, principalmente) após rotações.
     *
     * Altura de um nó = 1 + a maior altura entre filho esquerdo e direito.
     */
    protected void updateHeight(TreeNode<T> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    /**
     * Retorna o maior valor entre dois inteiros.
     * Math.max existe no Java, mas ter aqui deixa o código mais legível
     * no contexto de árvores (facilita leitura dos cálculos de altura).
     */
    protected int max(int a, int b) {
        return (a > b) ? a : b;
    }

    /**
     * Atalho para criar um Step de "passo de comparação" e adicionar na lista.
     * Evita repetir a construção do Step em cada subclasse.
     */
    protected void addCompareStep(List<Step<T>> steps, TreeNode<T> current, TreeNode<T> target) {
        steps.add(new Step<>(
            Step.Type.COMPARE,
            current,
            target,
            "Comparando " + current.value + " com " + target.value
        ));
    }

    // ─── Métodos abstratos — cada subclasse implementa do seu jeito ──────────

    @Override
    public abstract List<Step<T>> insert(T value);

    @Override
    public abstract List<Step<T>> remove(T value);

    @Override
    public abstract List<Step<T>> search(T value);
}