package model;

import operations.Step;
import java.util.ArrayList;
import java.util.List;

/**
 * Árvore Binária simples (AB) — sem regra de ordenação.
 *
 * Diferente da ABB, aqui não existe invariante de posição:
 * valores menores não vão necessariamente à esquerda.
 * A inserção é feita por nível (BFS), preenchendo a árvore
 * da esquerda pra direita — o que garante árvore sempre "cheia".
 *
 * O search aqui é uma busca genérica (percorre tudo),
 * não aproveita ordenação porque não existe ordenação.
 */
public class ABTree<T extends Comparable<T>> extends AbstractBinaryTree<T> {

    // ─── INSERT ──────────────────────────────────────────────────────────────

    /**
     * Insere por nível (level-order / BFS):
     * Percorre a árvore por largura e insere no primeiro espaço vazio
     * (filho esquerdo ou direito nulo) que encontrar.
     *
     * Por que BFS e não recursão simples à esquerda?
     * Pra manter a árvore balanceada naturalmente — sem regra de ordenação,
     * inserir sempre à esquerda criaria uma lista encadeada.
     */
    @Override
    public List<Step<T>> insert(T value) {
        List<Step<T>> steps = new ArrayList<>();
        TreeNode<T> newNode = new TreeNode<>(value);

        if (root == null) {
            root = newNode;
            steps.add(new Step<>(Step.Type.INSERT, root, "Inserindo " + value + " como raiz"));
            steps.add(new Step<>(Step.Type.DONE, "Inserção concluída."));
            return steps;
        }

        // Fila para percurso BFS
        java.util.Queue<TreeNode<T>> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode<T> current = queue.poll();
            steps.add(new Step<>(Step.Type.COMPARE, current, "Verificando filho de " + current.value));

            if (current.left == null) {
                current.left = newNode;
                newNode.parent = current;
                steps.add(new Step<>(Step.Type.INSERT, newNode, "Inserindo " + value + " à esquerda de " + current.value));
                break;
            } else {
                queue.add(current.left);
            }

            if (current.right == null) {
                current.right = newNode;
                newNode.parent = current;
                steps.add(new Step<>(Step.Type.INSERT, newNode, "Inserindo " + value + " à direita de " + current.value));
                break;
            } else {
                queue.add(current.right);
            }
        }

        steps.add(new Step<>(Step.Type.DONE, "Inserção de " + value + " concluída."));
        return steps;
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────

    /** Busca por percurso completo (sem atalhos de ordenação) */
    @Override
    public List<Step<T>> search(T value) {
        List<Step<T>> steps = new ArrayList<>();
        searchInOrder(root, value, steps);
        return steps;
    }

    private boolean searchInOrder(TreeNode<T> node, T value, List<Step<T>> steps) {
        if (node == null) return false;
        steps.add(new Step<>(Step.Type.COMPARE, node, "Verificando nó " + node.value));
        if (node.value.compareTo(value) == 0) {
            steps.add(new Step<>(Step.Type.FOUND, node, "Valor " + value + " encontrado!"));
            return true;
        }
        return searchInOrder(node.left, value, steps) || searchInOrder(node.right, value, steps);
    }

    // ─── REMOVE ──────────────────────────────────────────────────────────────

    /**
     * Remove substituindo pelo nó mais à direita no nível mais profundo
     * (para manter a propriedade de árvore completa).
     */
    @Override
    public List<Step<T>> remove(T value) {
        List<Step<T>> steps = new ArrayList<>();
        if (root == null) {
            steps.add(new Step<>(Step.Type.NOT_FOUND, "Árvore vazia."));
            return steps;
        }

        // BFS pra achar o nó alvo e o último nó
        TreeNode<T> targetNode = null;
        TreeNode<T> lastNode   = null;
        TreeNode<T> lastParent = null;
        boolean lastWasLeft    = false;

        java.util.Queue<TreeNode<T>> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode<T> current = queue.poll();
            steps.add(new Step<>(Step.Type.COMPARE, current, "Verificando " + current.value));

            if (current.value.compareTo(value) == 0) {
                targetNode = current;
                steps.add(new Step<>(Step.Type.FOUND, current, "Nó " + value + " encontrado para remoção"));
            }

            if (current.left != null) {
                lastParent  = current;
                lastNode    = current.left;
                lastWasLeft = true;
                queue.add(current.left);
            }
            if (current.right != null) {
                lastParent  = current;
                lastNode    = current.right;
                lastWasLeft = false;
                queue.add(current.right);
            }
        }

        if (targetNode == null) {
            steps.add(new Step<>(Step.Type.NOT_FOUND, "Valor " + value + " não encontrado."));
            return steps;
        }

        // Copia o valor do último nó pro nó alvo e remove o último nó
        steps.add(new Step<>(Step.Type.REMOVE, targetNode, "Substituindo " + value + " por " + lastNode.value));
        targetNode.value = lastNode.value;

        if (lastParent != null) {
            if (lastWasLeft) lastParent.left  = null;
            else             lastParent.right = null;
        } else {
            root = null;
        }

        steps.add(new Step<>(Step.Type.DONE, "Remoção de " + value + " concluída."));
        return steps;
    }
}