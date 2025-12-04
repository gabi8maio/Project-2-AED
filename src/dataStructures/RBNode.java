package dataStructures;

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

    public void setBlack() {
        isRed = false;
    }

    public void setRed() {
        isRed = true;
    }

    public boolean isBlack() {
        return !isRed;
    }
}