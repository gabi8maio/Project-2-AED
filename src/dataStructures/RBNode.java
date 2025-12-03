package dataStructures;

class RBNode<E> extends BTNode<E> {
    private boolean red;

    public RBNode(E elem){
        super(elem);
        this.red = true;
    }

    public RBNode (E elem, RBNode<E> parent){
        super(elem, parent);
        this.red = true;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isBlack() {
        return !red;
    }

    public void setRed(){
        red = true;
    }

    public void setBlack(){
        red = false;
    }
}
