package operations;

import model.TreeNode;

/**
 * Representa uma única microação durante uma operação na árvore.
 *
 * Quando chamamos avlTree.insert(15), a árvore não só insere —
 * ela devolve uma List<Step> descrevendo cada comparação, cada
 * rotação, cada recoloração que aconteceu internamente.
 *
 * A view consome essa lista e exibe um passo de cada vez,
 * no ritmo que o usuário escolher no slider de velocidade.
 */
public class Step<T extends Comparable<T>> {

    // ─── Tipos de evento que podem acontecer numa operação ───────────────────

    public enum Type {
        COMPARE,        // comparou dois valores para decidir onde ir
        GO_LEFT,        // desceu para o filho esquerdo
        GO_RIGHT,       // desceu para o filho direito
        INSERT,         // inseriu um novo nó nessa posição
        REMOVE,         // removeu o nó
        FOUND,          // encontrou o valor buscado
        NOT_FOUND,      // terminou a busca sem encontrar
        ROTATE_LEFT,    // rotação à esquerda (AVL / Rubro-Negra)
        ROTATE_RIGHT,   // rotação à direita
        RECOLOR,        // mudou a cor de um nó (Rubro-Negra)
        DONE            // operação concluída — sinal de fim para a view
    }

    // ─── Campos ──────────────────────────────────────────────────────────────

    /** Que tipo de evento foi esse passo */
    public final Type type;

    /**
     * Nó principal envolvido no passo.
     * Ex: no GO_LEFT, é o nó de onde descemos.
     * No INSERT, é o nó recém-criado.
     */
    public final TreeNode<T> nodeA;

    /**
     * Segundo nó envolvido, quando necessário.
     * Ex: no COMPARE, nodeA e nodeB são os dois valores comparados.
     * Pode ser null quando o passo envolve só um nó.
     */
    public final TreeNode<T> nodeB;

    /** Descrição legível pra exibir na interface ("comparando 15 com 10") */
    public final String description;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public Step(Type type, TreeNode<T> nodeA, TreeNode<T> nodeB, String description) {
        this.type        = type;
        this.nodeA       = nodeA;
        this.nodeB       = nodeB;
        this.description = description;
    }

    /** Atalho para passos que envolvem só um nó */
    public Step(Type type, TreeNode<T> nodeA, String description) {
        this(type, nodeA, null, description);
    }

    /** Atalho para passos finais sem nó (ex: NOT_FOUND, DONE) */
    public Step(Type type, String description) {
        this(type, null, null, description);
    }

    @Override
    public String toString() {
        return "[" + type + "] " + description;
    }
}