package dataStructures;

/**
 * Implementation of the Red Black Node
 * @param <E> Generic element
 */
public class RBNode<E> extends BTNode<E> {

    private boolean isRed;

    RBNode(E elem) {
        super(elem);
        isRed = true;
    }

    RBNode(E elem, RBNode<E> parent) {
        super(elem, parent);
        isRed = true;
    }

    /**
     * Turns the node into a black node
     */
    public void setBlack() {
        isRed = false;
    }

    /**
     * Turns the node into a red node
      */
    public void setRed() {
        isRed = true;
    }

    /**
     * Checks if the node is black
     * @return true if the node is black or false if the node is red
     */
    public boolean isBlack() {
        return !isRed;
    }
}