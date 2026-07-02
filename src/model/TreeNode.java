package model;

/**
 * Nó genérico usado por todas as árvores do projeto.
 *
 * <T> é o tipo do valor armazenado (ex: Integer, String).
 * A restrição "Comparable<T>" garante que sempre dá pra comparar
 * dois valores, o que é necessário pra manter a ordenação nas
 * árvores de busca (ABB, AVL, Rubro-Negra).
 */
public class TreeNode<T extends Comparable<T>> {

    // ─── Dados do nó ────────────────────────────────────────────────────────

    public T value;

    // ─── Referências estruturais ─────────────────────────────────────────────

    public TreeNode<T> left;
    public TreeNode<T> right;

    /**
     * Referência ao pai — necessária na Rubro-Negra para subir na árvore
     * durante recoloração e rotações. AB e ABB podem ignorar esse campo.
     */
    public TreeNode<T> parent;

    // ─── Campos auxiliares para árvores específicas ──────────────────────────

    /**
     * Altura do nó — usada pela AVL para calcular fator de balanceamento.
     * Nós folha têm altura 1. Null tem altura 0 (convenção).
     */
    public int height;

    /**
     * Cor do nó — usada exclusivamente pela Rubro-Negra.
     * Enum definido aqui dentro para manter tudo no mesmo lugar.
     */
    public enum Color { RED, BLACK }
    public Color color;

    // ─── Construtor ──────────────────────────────────────────────────────────

    /**
     * Todo nó nasce como folha (sem filhos), com altura 1 e cor vermelha.
     * Cor vermelha é o padrão de inserção na Rubro-Negra — a árvore decide
     * depois se precisa recolorir. Para AB e ABB, a cor é simplesmente ignorada.
     */
    public TreeNode(T value) {
        this.value  = value;
        this.left   = null;
        this.right  = null;
        this.parent = null;
        this.height = 1;
        this.color  = Color.RED;
    }
}