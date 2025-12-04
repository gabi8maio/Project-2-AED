package dataStructures;
/**
 * Advanced Binary Search Tree
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
abstract class AdvancedBSTree <K extends Comparable<K>,V> extends BSTSortedMap<K,V>{
    /**
     * Performs a single left rotation rooted at z node.
     * Node y was a  right  child  of z before the  rotation,
     * then z becomes the left child of y after the rotation.
     * @param z - root of the rotation
     * @pre: z has a right child
     */
    protected void rotateLeft( BTNode<Entry<K,V>> z){
        //TODO: Left as an exercise.
        //  a single rotation modifies a constant number of parent-child relationships,
        // it can be implemented in O(1)time
        if (z == null || z.getRightChild() == null) return;

        BTNode <Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getRightChild(); //filho direito de z
        BTNode<Entry<K,V>> T2 = (BTNode<Entry<K,V>>) y.getLeftChild();  // Subárvore esquerda de y
        BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) z.getParent(); // Pai de z

        y.setLeftChild(z);
        z.setRightChild(T2);
        z.setParent(y);

        if (T2 != null)
            T2.setParent(z);

        if (parent == null)
            root = y;

        else if (parent.getLeftChild() == z)
            parent.setLeftChild(y);

        else
            parent.setRightChild(y);

        y.setParent(parent);
    }

    /**
     * Performs a single right rotation rooted at z node.
     * Node y was a left  child  of z before the  rotation,
     * then z becomes the right child of y after the rotation.
     * @param z - root of the rotation
     * @pre: z has a left child
     */
    protected void rotateRight( BTNode<Entry<K,V>> z){
        //TODO: Left as an exercise.
        //  a single rotation modifies a constant number of parent-child relationships,
        // it can be implemented in O(1)time
        if (z == null || z.getLeftChild() == null) return;

        BTNode <Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getLeftChild(); //filho direito de z
        BTNode<Entry<K,V>> T2 = (BTNode<Entry<K,V>>) y.getRightChild();  // Subárvore esquerda de y
        BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) z.getParent(); // Pai de z

        y.setRightChild(z);
        z.setLeftChild(T2);
        z.setParent(y);

        if (T2 != null)
            T2.setParent(z);

        if (parent == null)
            root = y;

        else if (parent.getLeftChild() == z)
            parent.setLeftChild(y);

        else
            parent.setRightChild(y);
        y.setParent(parent);
    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param x - root of the rotation
     * <pre>
     *          z=c       z=c        z=a         z=a
     *          /  \      /  \       /  \        /  \
     *        y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *       /  \      /  \           /  \         /  \
     *     x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *    /  \          /  \       /  \             /  \
     *   t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     * @return the new root of the restructured subtree
     */
    protected BTNode<Entry<K,V>> restructure (BTNode<Entry<K,V>> x) {
        //TODO: Left as an exercise.
        // the modification of a tree T caused by a trinode restructuring operation
        // can be implemented through case analysis either as a single rotation or as a double rotation.
        // The double rotation arises when position x has the middle of the three relevant keys
        // and is first rotated above its parent Y, and then above what was originally its grandparent Z.
        // In any of the cases, the trinode restructuring is completed with O(1)running time
        BTNode <Entry<K,V>> y = (BTNode<Entry<K,V>>) x.getParent();
        BTNode <Entry<K, V>> z = (BTNode<Entry<K, V>>) y.getParent();

        if (z == null)
            return x;

        if ((y == z.getLeftChild() && x == y.getLeftChild()) || (y == z.getRightChild() && x == y.getRightChild())){
            if (y == z.getLeftChild()){
                rotateRight(z);
            }
            else {
                rotateLeft(z);
            }
        }
        else {
            if (y == z.getLeftChild()){
                rotateLeft(y);
                rotateRight(z);
            }
            else {
                rotateRight(y);
                rotateLeft(z);
            }
        }
        return (BTNode<Entry<K, V>>) root;
    }


}
