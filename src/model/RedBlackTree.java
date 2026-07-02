package model;

import operations.Step;
import java.util.ArrayList;
import java.util.List;

/**
 * Árvore Rubro-Negra.
 *
 * Invariantes que sempre devem ser mantidas:
 *  1. Todo nó é vermelho (RED) ou preto (BLACK).
 *  2. A raiz é sempre preta.
 *  3. Todo nó folha nulo (null) é considerado preto.
 *  4. Se um nó é vermelho, ambos os filhos são pretos.
 *  5. Para qualquer nó, todos os caminhos até as folhas nulas
 *     contêm o mesmo número de nós pretos ("black-height").
 *
 * Por que herdar de ABBTree e não de AVLTree?
 * Rubro-Negra e AVL são estratégias alternativas de balanceamento —
 * uma não é extensão da outra. Ambas herdam da ABBTree (busca e
 * estrutura básica de insert/remove), mas têm balanceamento próprio.
 *
 * TODO (Pessoa B): implementar insert, insertFixUp, remove, removeFixUp
 */
public class RedBlackTree<T extends Comparable<T>> extends ABBTree<T> {

    // ─── ATALHO DE COR ───────────────────────────────────────────────────────

    /** Retorna a cor de um nó; nós nulos são considerados BLACK por definição */
    private TreeNode.Color colorOf(TreeNode<T> node) {
        return (node == null) ? TreeNode.Color.BLACK : node.color;
    }

    private boolean isRed(TreeNode<T> node) {
        return colorOf(node) == TreeNode.Color.RED;
    }

    private void setColor(TreeNode<T> node, TreeNode.Color color) {
        if (node != null) node.color = color;
    }

    // ─── ROTAÇÕES ─────────────────────────────────────────────────────────────

    /**
     * Rotação à esquerda em torno do nó X.
     * Igual à da AVL, mas também atualiza o ponteiro de pai (parent),
     * que a Rubro-Negra usa para subir na árvore durante o fixUp.
     *
     * TODO (Pessoa B): implementar
     */
    private TreeNode<T> rotateLeft(TreeNode<T> x, List<Step<T>> steps) {
        // TODO
        steps.add(new Step<>(Step.Type.ROTATE_LEFT, x, "Rotação à esquerda em " + x.value + " [TODO]"));
        return x;
    }

    /**
     * Rotação à direita em torno do nó Y.
     *
     * TODO (Pessoa B): implementar
     */
    private TreeNode<T> rotateRight(TreeNode<T> y, List<Step<T>> steps) {
        // TODO
        steps.add(new Step<>(Step.Type.ROTATE_RIGHT, y, "Rotação à direita em " + y.value + " [TODO]"));
        return y;
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────

    /**
     * Inserção na Rubro-Negra:
     *  1. Insere como numa ABB normal (novo nó sempre entra VERMELHO)
     *  2. Chama insertFixUp para restaurar as invariantes violadas
     *
     * Por que o novo nó entra vermelho?
     * Porque inserir preto sempre violaria a invariante 5 (black-height).
     * Vermelho pode violar a invariante 4 (dois vermelhos seguidos),
     * mas isso é mais fácil de corrigir no fixUp.
     *
     * TODO (Pessoa B): implementar insertRecursive e insertFixUp
     */
    @Override
    public List<Step<T>> insert(T value) {
        List<Step<T>> steps = new ArrayList<>();
        // TODO: inserir como ABB e chamar insertFixUp
        // root.color = TreeNode.Color.BLACK; // raiz sempre preta (invariante 2)
        steps.add(new Step<>(Step.Type.DONE, "Inserção RN de " + value + " [TODO]"));
        return steps;
    }

    /**
     * Corrige violações das invariantes após a inserção.
     *
     * Os 3 casos do fixUp (e seus simétricos):
     *  Caso 1: tio do nó inserido é vermelho → recolorir e subir
     *  Caso 2: tio é preto e nó está "por dentro" → rotacionar pra
     *          transformar no caso 3
     *  Caso 3: tio é preto e nó está "por fora" → rotacionar e recolorir
     *
     * TODO (Pessoa B): implementar
     */
    private void insertFixUp(TreeNode<T> node, List<Step<T>> steps) {
        // TODO
    }

    // ─── REMOVE ───────────────────────────────────────────────────────────────

    /**
     * Remoção na Rubro-Negra — a mais complexa das quatro árvores.
     * Envolve encontrar o nó, substituí-lo e restaurar as invariantes.
     *
     * TODO (Pessoa B): implementar remove e removeFixUp
     */
    @Override
    public List<Step<T>> remove(T value) {
        List<Step<T>> steps = new ArrayList<>();
        // TODO
        steps.add(new Step<>(Step.Type.DONE, "Remoção RN de " + value + " [TODO]"));
        return steps;
    }

    /**
     * Corrige o "double black" que surge quando um nó preto é removido.
     * Tem 4 casos principais (e seus simétricos).
     *
     * TODO (Pessoa B): implementar
     */
    private void removeFixUp(TreeNode<T> node, List<Step<T>> steps) {
        // TODO
    }

    // ─── SEARCH: herdada de ABBTree sem modificação ───────────────────────────
    // A busca na RN é idêntica à da ABB — não precisa sobrescrever.
}