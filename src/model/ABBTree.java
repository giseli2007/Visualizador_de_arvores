package model;

import operations.Step;
import java.util.ArrayList;
import java.util.List;

/**
 * Árvore Binária de Busca (ABB).
 *
 * Regra invariante: para qualquer nó N,
 *   - todos os valores na subárvore esquerda são MENORES que N.value
 *   - todos os valores na subárvore direita são MAIORES que N.value
 *
 * AVL e Rubro-Negra estendem essa classe e herdam o search() —
 * a lógica de busca é idêntica nas três, pois todas são ABBs no fundo.
 * O que muda é o insert/remove, que nessas subclasses adiciona
 * balanceamento depois de fazer a inserção básica da ABB.
 */
public class ABBTree<T extends Comparable<T>> extends AbstractBinaryTree<T> {

    // ─── INSERT ──────────────────────────────────────────────────────────────

    @Override
    public List<Step<T>> insert(T value) {
        List<Step<T>> steps = new ArrayList<>();
        root = insertRecursive(root, null, value, steps);
        steps.add(new Step<>(Step.Type.DONE, "Inserção de " + value + " concluída."));
        return steps;
    }

    /**
     * Insere recursivamente descendo na árvore.
     *
     * Por que recursivo? Porque a estrutura de uma árvore é naturalmente
     * recursiva: cada nó é a raiz de uma subárvore. A recursão mapeia
     * diretamente essa natureza sem precisar de pilha manual.
     *
     * Por que retornar o nó? Para reconectar o ponteiro do pai após a
     * recursão. É o padrão mais limpo em Java para modificar estruturas
     * de árvore recursivamente (evita passar referência de referência).
     *
     * @param node    nó atual que estamos analisando (pode ser null = posição vazia)
     * @param parent  pai do nó atual (necessário pra setar node.parent)
     * @param value   valor a inserir
     * @param steps   lista onde registramos os passos
     * @return        o nó que deve ocupar essa posição após a inserção
     */
    protected TreeNode<T> insertRecursive(TreeNode<T> node, TreeNode<T> parent, T value, List<Step<T>> steps) {

        // Caso base: chegamos a uma posição vazia → inserir aqui
        if (node == null) {
            TreeNode<T> newNode = new TreeNode<>(value);
            newNode.parent = parent;
            steps.add(new Step<>(Step.Type.INSERT, newNode, "Inserindo " + value));
            return newNode;
        }

        // Comparar o valor a inserir com o nó atual
        int cmp = value.compareTo(node.value);

        if (cmp < 0) {
            // Valor menor → vai pra esquerda
            steps.add(new Step<>(Step.Type.GO_LEFT, node, "Valor " + value + " < " + node.value + ", indo pra esquerda"));
            node.left = insertRecursive(node.left, node, value, steps);

        } else if (cmp > 0) {
            // Valor maior → vai pra direita
            steps.add(new Step<>(Step.Type.GO_RIGHT, node, "Valor " + value + " > " + node.value + ", indo pra direita"));
            node.right = insertRecursive(node.right, node, value, steps);

        } else {
            // Valor igual → não inserimos duplicatas (decisão de projeto)
            steps.add(new Step<>(Step.Type.FOUND, node, "Valor " + value + " já existe na árvore, ignorando."));
        }

        // Após inserir, atualizamos a altura (AVL vai sobrescrever e fazer mais coisas aqui)
        updateHeight(node);
        return node;
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────

    /**
     * Busca herdada por AVL e Rubro-Negra sem modificação.
     * Segue a propriedade da ABB: menor → esquerda, maior → direita.
     */
    @Override
    public List<Step<T>> search(T value) {
        List<Step<T>> steps = new ArrayList<>();
        searchRecursive(root, value, steps);
        return steps;
    }

    protected void searchRecursive(TreeNode<T> node, T value, List<Step<T>> steps) {

        if (node == null) {
            steps.add(new Step<>(Step.Type.NOT_FOUND, "Valor " + value + " não encontrado."));
            return;
        }

        int cmp = value.compareTo(node.value);

        if (cmp == 0) {
            steps.add(new Step<>(Step.Type.FOUND, node, "Valor " + value + " encontrado!"));

        } else if (cmp < 0) {
            steps.add(new Step<>(Step.Type.GO_LEFT, node, "Valor " + value + " < " + node.value + ", buscando à esquerda"));
            searchRecursive(node.left, value, steps);

        } else {
            steps.add(new Step<>(Step.Type.GO_RIGHT, node, "Valor " + value + " > " + node.value + ", buscando à direita"));
            searchRecursive(node.right, value, steps);
        }
    }

    // ─── REMOVE ──────────────────────────────────────────────────────────────

    @Override
    public List<Step<T>> remove(T value) {
        List<Step<T>> steps = new ArrayList<>();
        root = removeRecursive(root, value, steps);
        steps.add(new Step<>(Step.Type.DONE, "Remoção de " + value + " concluída."));
        return steps;
    }

    protected TreeNode<T> removeRecursive(TreeNode<T> node, T value, List<Step<T>> steps) {

        if (node == null) {
            steps.add(new Step<>(Step.Type.NOT_FOUND, "Valor " + value + " não encontrado para remoção."));
            return null;
        }

        int cmp = value.compareTo(node.value);

        if (cmp < 0) {
            steps.add(new Step<>(Step.Type.GO_LEFT, node, "Buscando " + value + " à esquerda de " + node.value));
            node.left = removeRecursive(node.left, value, steps);

        } else if (cmp > 0) {
            steps.add(new Step<>(Step.Type.GO_RIGHT, node, "Buscando " + value + " à direita de " + node.value));
            node.right = removeRecursive(node.right, value, steps);

        } else {
            // Encontrou o nó a remover — 3 casos clássicos:
            steps.add(new Step<>(Step.Type.REMOVE, node, "Removendo nó " + value));

            if (node.left == null) {
                // Caso 1: sem filho esquerdo → substitui pelo filho direito (pode ser null)
                return node.right;

            } else if (node.right == null) {
                // Caso 2: sem filho direito → substitui pelo filho esquerdo
                return node.left;

            } else {
                // Caso 3: dois filhos → substitui pelo sucessor in-order
                // (menor valor da subárvore direita), depois remove o sucessor de lá
                TreeNode<T> successor = findMin(node.right);
                steps.add(new Step<>(Step.Type.COMPARE, successor, "Sucessor in-order: " + successor.value));
                node.value = successor.value;
                node.right = removeRecursive(node.right, successor.value, steps);
            }
        }

        updateHeight(node);
        return node;
    }

    /**
     * Retorna o nó com o menor valor de uma subárvore.
     * Usado na remoção para encontrar o sucessor in-order.
     * O menor valor está sempre no caminho mais à esquerda possível.
     */
    protected TreeNode<T> findMin(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}