package model;

import operations.Step;
import java.util.ArrayList;
import java.util.List;

/**
 * Árvore AVL — ABB que se auto-balanceia após cada inserção/remoção.
 *
 * Invariante: para qualquer nó N,
 *   |altura(N.left) - altura(N.right)| <= 1
 *
 * Quando essa propriedade é violada após uma inserção/remoção,
 * realizamos rotações para restaurá-la.
 *
 * Herda de ABBTree:
 *  - search() completo (idêntico pra ABB e AVL)
 *  - lógica básica de insert e remove (descida na árvore)
 *  Sobrescreve:
 *  - insertRecursive() pra adicionar verificação de balanço após subir
 *  - removeRecursive() idem
 */
public class AVLTree<T extends Comparable<T>> extends ABBTree<T> {

    // ─── FATOR DE BALANCEAMENTO ───────────────────────────────────────────────

    /**
     * Fator de balanceamento = altura(esquerda) - altura(direita).
     *   0 = perfeitamente balanceado
     *  +1 ou -1 = levemente desbalanceado, ainda aceitável
     *  +2 ou -2 = violação da invariante AVL → precisa de rotação
     */
    private int balanceFactor(TreeNode<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // ─── ROTAÇÕES ────────────────────────────────────────────────────────────

    /**
     * Rotação à direita em torno do nó Y.
     *
     * Antes:        Depois:
     *      Y               X
     *     / \             / \
     *    X   C    →      A   Y
     *   / \                 / \
     *  A   B               B   C
     *
     * O filho direito de X (B) vira filho esquerdo de Y.
     * X sobe para onde Y estava.
     * Usada quando o desequilíbrio está na subárvore esquerda-esquerda.
     */
    private TreeNode<T> rotateRight(TreeNode<T> y, List<Step<T>> steps) {
        TreeNode<T> x = y.left;
        TreeNode<T> b = x.right;

        // Realiza a rotação
        x.right = y;
        y.left  = b;

        // Atualiza alturas — Y primeiro porque agora está abaixo de X
        updateHeight(y);
        updateHeight(x);

        steps.add(new Step<>(Step.Type.ROTATE_RIGHT, x, y, "Rotação à direita: " + x.value + " sobe, " + y.value + " desce"));
        return x; // X é a nova raiz dessa subárvore
    }

    /**
     * Rotação à esquerda em torno do nó X.
     *
     * Antes:        Depois:
     *    X                Y
     *   / \              / \
     *  A   Y    →       X   C
     *     / \          / \
     *    B   C        A   B
     *
     * Simétrica à rotação à direita.
     * Usada quando o desequilíbrio está na subárvore direita-direita.
     */
    private TreeNode<T> rotateLeft(TreeNode<T> x, List<Step<T>> steps) {
        TreeNode<T> y = x.right;
        TreeNode<T> b = y.left;

        y.left  = x;
        x.right = b;

        updateHeight(x);
        updateHeight(y);

        steps.add(new Step<>(Step.Type.ROTATE_LEFT, y, x, "Rotação à esquerda: " + y.value + " sobe, " + x.value + " desce"));
        return y;
    }

    // ─── REBALANCEAMENTO ─────────────────────────────────────────────────────

    /**
     * Verifica o fator de balanceamento e aplica a rotação necessária.
     * Chamado após cada insert/remove, durante a subida recursiva.
     *
     * Os 4 casos:
     *
     * 1. Esquerda-Esquerda (bf > 1 e novo nó foi pra esquerda do filho esquerdo)
     *    → rotação simples à direita
     *
     * 2. Direita-Direita (bf < -1 e novo nó foi pra direita do filho direito)
     *    → rotação simples à esquerda
     *
     * 3. Esquerda-Direita (bf > 1 e novo nó foi pra direita do filho esquerdo)
     *    → rotação à esquerda no filho, depois rotação à direita no nó atual
     *
     * 4. Direita-Esquerda (bf < -1 e novo nó foi pra esquerda do filho direito)
     *    → rotação à direita no filho, depois rotação à esquerda no nó atual
     */
    private TreeNode<T> rebalance(TreeNode<T> node, List<Step<T>> steps) {
        updateHeight(node);
        int bf = balanceFactor(node);

        // Caso 1: Esquerda-Esquerda
        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node, steps);
        }

        // Caso 2: Direita-Direita
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node, steps);
        }

        // Caso 3: Esquerda-Direita
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left, steps);
            return rotateRight(node, steps);
        }

        // Caso 4: Direita-Esquerda
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right, steps);
            return rotateLeft(node, steps);
        }

        return node; // já estava balanceado
    }

    // ─── INSERT (sobrescreve ABBTree) ─────────────────────────────────────────

    @Override
    public List<Step<T>> insert(T value) {
        List<Step<T>> steps = new ArrayList<>();
        root = insertRecursive(root, null, value, steps);
        steps.add(new Step<>(Step.Type.DONE, "Inserção de " + value + " na AVL concluída."));
        return steps;
    }

    /**
     * Igual ao insert da ABB, mas adiciona rebalanceamento ao subir.
     * A recursão desce inserindo e, na volta (ao subir), cada nó
     * verifica se ficou desbalanceado e corrige se necessário.
     */
    @Override
    protected TreeNode<T> insertRecursive(TreeNode<T> node, TreeNode<T> parent, T value, List<Step<T>> steps) {

        if (node == null) {
            TreeNode<T> newNode = new TreeNode<>(value);
            newNode.parent = parent;
            steps.add(new Step<>(Step.Type.INSERT, newNode, "Inserindo " + value));
            return newNode;
        }

        int cmp = value.compareTo(node.value);

        if (cmp < 0) {
            steps.add(new Step<>(Step.Type.GO_LEFT, node, "Valor " + value + " < " + node.value + ", indo à esquerda"));
            node.left = insertRecursive(node.left, node, value, steps);

        } else if (cmp > 0) {
            steps.add(new Step<>(Step.Type.GO_RIGHT, node, "Valor " + value + " > " + node.value + ", indo à direita"));
            node.right = insertRecursive(node.right, node, value, steps);

        } else {
            steps.add(new Step<>(Step.Type.FOUND, node, "Valor " + value + " já existe, ignorando."));
            return node;
        }

        // Aqui está a diferença da AVL: após inserir, rebalancea ao subir
        return rebalance(node, steps);
    }

    // ─── REMOVE (sobrescreve ABBTree) ────────────────────────────────────────

    @Override
    public List<Step<T>> remove(T value) {
        List<Step<T>> steps = new ArrayList<>();
        root = removeRecursive(root, value, steps);
        steps.add(new Step<>(Step.Type.DONE, "Remoção de " + value + " na AVL concluída."));
        return steps;
    }

    @Override
    protected TreeNode<T> removeRecursive(TreeNode<T> node, T value, List<Step<T>> steps) {
        // Faz a remoção igual à ABB (herdada)
        node = super.removeRecursive(node, value, steps);
        // E depois rebalancea ao subir, igual ao insert
        if (node == null) return null;
        return rebalance(node, steps);
    }
}