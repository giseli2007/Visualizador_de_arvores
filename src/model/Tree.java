package model;

import operations.Step;
import java.util.List;

/**
 * Contrato que todas as árvores do projeto devem cumprir.
 *
 * Essa interface é o "acordo" entre quem implementa a árvore
 * (model) e quem vai usá-la (controller/view). Enquanto esse
 * contrato for respeitado, qualquer uma pode trocar a AVL pela
 * Rubro-Negra sem precisar mudar nada na view.
 *
 * Cada método retorna List<Step> — a lista de microações que
 * aconteceram durante a operação, usada pela view pra animar.
 */
public interface Tree<T extends Comparable<T>> {

    /**
     * Insere um valor na árvore e retorna os passos do processo.
     * A estrutura da árvore já estará modificada quando o método retornar.
     * A List<Step> descreve como chegamos até o estado final.
     */
    List<Step<T>> insert(T value);

    /**
     * Remove um valor da árvore e retorna os passos do processo.
     * Se o valor não existir, os passos descrevem a busca malsucedida.
     */
    List<Step<T>> remove(T value);

    /**
     * Busca um valor e retorna os passos da busca.
     * Não modifica a árvore — só registra o caminho percorrido.
     */
    List<Step<T>> search(T value);

    /**
     * Retorna a raiz atual da árvore.
     * A view usa isso pra pedir ao TreeLayoutAlgorithm
     * que calcule as posições de todos os nós para o desenho.
     */
    TreeNode<T> getRoot();

    /**
     * Remove todos os nós — útil pro botão "Limpar" na interface.
     */
    void clear();
}