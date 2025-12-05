package dataStructures;
/**
 * AVL Tree Node
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class AVLNode<E> extends BTNode<E> {
    // Height of the node
    protected int height;

    public AVLNode(E elem) {
        super(elem);
        height=0;
    }

    public AVLNode( E element, AVLNode<E> parent,
                    AVLNode<E> left, AVLNode<E> right ){
        super(element, parent, left, right);
        updateHeight();

    }
    public AVLNode( E element, AVLNode<E> parent){
        super(element, parent,null, null);
        height= 0;
    }

    /**
     * The height of the node
     * @param no the node
     * @return the height
     */
    private int height(AVLNode<E> no) {
        if (no==null)	return -1;
        return no.getHeight();
    }

    /**
     * Gives the height of the nodes
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Update the left child and height
     * @param node
     */
    public void setLeftChild(AVLNode<E> node) {
        super.setLeftChild(node);
        updateHeight();
    }

    /**
     * Update the right child and height
     * @param node
     */
    public void setRightChild(AVLNode<E> node) {
        super.setRightChild(node);
        updateHeight();
    }
// others public methods
//TODO: Left as an exercise.

    /**
     * Updates the height of the node
     */
    void updateHeight() {
        // Calcula a altura baseada nos filhos:
        // Altura = 1 + máxima altura entre os filhos
        int leftHeight = height((AVLNode<E>) getLeftChild());
        int rightHeight = height((AVLNode<E>) getRightChild());
        height = 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Gets the balance factor
     * @return the factor
     */
    public int getBalanceFactor() {
        // FATOR DE BALANCEAMENTO = altura(esquerda) - altura(direita)
        // Positivo: mais pesado à esquerda
        // Negativo: mais pesado à direita
        // Zero: balanceado
        return height((AVLNode<E>) getLeftChild()) - height((AVLNode<E>) getRightChild());
    }

}
