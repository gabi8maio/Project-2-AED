package dataStructures;

//This class may have errors

/**
 * RedBlackSortedMap implementation
 * @param <K> Generic key
 * @param <V> Generic value
 */
public class RedBlackSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{
    public RedBlackSortedMap(){

    }

    /**
     * If exists an entry with this Key, update the node with new element
     * and return the old value of the entry
     * otherwise, insert the newNode, "rebalance" or “recoloring” from the insertion position
     * and return value
     * @param key the key
     * @param value the new value
     * @return the old value if exists an entry with this Key, null otherwise
     */
    public V put(K key,V value) {
        if (isEmpty()) {
            root = new RBNode<>(new Map.Entry<>(key, value));
            ((RBNode<Entry<K,V>>) root).setBlack();
            currentSize++;
            return null;
        }
        return findInsert((RBNode<Entry<K,V>>) root, key, value);
    }


    /**
     * This method is used to find where the node will be added (its parent)
     * @param node the node from where we want to start
     * @param key the key of the new node
     * @param value the value of the new node
     * @return the old value of the node (if there was a node with the same key) or null
     */
    private V findInsert(RBNode<Entry<K, V>> node, K key, V value) {
        int compare = key.compareTo(node.getElement().key());
        if (compare == 0) {
            V oldValue = node.getElement().value();
            node.setElement(new Entry<>(key, value));
            return oldValue;
        }
        if (compare < 0) {
            if (node.getLeftChild() == null) {
                RBNode<Entry<K, V>> newNode = new RBNode<>(new Entry<>(key, value), node);
                node.setLeftChild(newNode);
                currentSize++;
                rebalance(newNode);
                return null;
            }
            else return findInsert((RBNode<Entry<K, V>>) node.getLeftChild(), key, value);
        }
        else { // compare > 0
            if (node.getRightChild() == null) {
                RBNode<Entry<K,V>> newNode = new RBNode<>(new Entry<>(key, value), node);
                node.setRightChild(newNode);
                currentSize++;
                rebalance(newNode);
                return null;
            }
            else return findInsert((RBNode<Entry<K, V>>) node.getRightChild(), key, value);
        }
    }

    /**
     * Rebalance de tree according to the rule of the black height
     * @param newNode the node that was recently inserted
     */
    private void rebalance(RBNode<Entry<K,V>> newNode) {
        RBNode<Entry<K,V>> parent = (RBNode<Entry<K, V>>) newNode.getParent();
        RBNode<Entry<K,V>> grandParent = (RBNode<Entry<K, V>>) parent.getParent();

        if (!parent.isBlack()) {
            RBNode<Entry<K,V>> uncle = getUncle(parent,  grandParent);
            if (uncle != null && !uncle.isBlack()) {
                parent.setBlack();
                uncle.setBlack();
                if (root == grandParent) {
                    grandParent.setBlack();
                }
                else {
                    grandParent.setRed();
                    rebalance(grandParent);
                }
            }
            else {
                grandParent.setRed();
                RBNode<Entry<K,V>> newRoot = (RBNode<Entry<K, V>>) restructure(newNode);
                newRoot.setBlack();
            }
        }
    }

    /**
     * Get the uncle of a certain node (the parent's brother of a node)
     * @param parent the parent of the node
     * @param grandParent the grandparent (parent of the parent) of the node
     * @return the uncle's node
     */
    private RBNode<Entry<K,V>> getUncle(RBNode<Entry<K,V>> parent, RBNode<Entry<K,V>> grandParent) {
        RBNode<Entry<K,V>> uncle;

        if (grandParent.getLeftChild() == parent) {
            uncle = (RBNode<Entry<K, V>>) grandParent.getRightChild();
        } else {
            uncle  = (RBNode<Entry<K,V>>) grandParent.getLeftChild();
        }
        return uncle;
    }

    /**
     * Removes the node with the given key from the tree
     * @param key whose entry is to be removed from the map
     * @return the old value, null if there wasn't an element with that key
     */
    public V remove(K key) {
        if (isEmpty()) return null;

        RBNode<Entry<K,V>> node = (RBNode<Entry<K,V>>) getNode((RBNode<Entry<K,V>>)root, key);

        if (key.compareTo(node.getElement().key()) != 0) return null;
        else {
            V oldValue = node.getElement().value();
            removeOptions(node);
            currentSize--;
            return oldValue;
        }
    }

    /**
     * Handles the 4 different options of removing a node
     * @param node the node to be removed
     */
    private void removeOptions(RBNode<Entry<K,V>> node) {
        RBNode<Entry<K,V>> parent = (RBNode<Entry<K,V>>) node.getParent();
        if (node.isLeaf()) {
            removeNode(parent, node, null);
        }
        else if (node.getRightChild()==null) {
            removeNode(parent, node, (RBNode<Entry<K,V>>) node.getLeftChild());
        }
        else if (node.getLeftChild()==null) {
            removeNode(parent, node, (RBNode<Entry<K,V>>) node.getRightChild());
        }
        else {
            RBNode<Entry<K,V>> nodeToRemove = (RBNode<Entry<K,V>>)((RBNode<Entry<K,V>>) node.getLeftChild()).furtherRightElement();
            node.setElement(nodeToRemove.getElement());
            removeOptions(nodeToRemove); //porque ou é leaf ou so tem leftchild
        }
    }

    /**
     * Removes the node and does all the operations needed after the removal
     * @param parent the parent of the node
     * @param remNode the removed node
     * @param newChild the child of the removed node
     */
    private void removeNode(RBNode<Entry<K,V>> parent, RBNode<Entry<K,V>> remNode, RBNode<Entry<K,V>> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.getRightChild() == remNode) {
            parent.setRightChild(newChild);
        } else {
            parent.setLeftChild(newChild);
        }
        if (newChild != null) {
            newChild.setParent(parent);
            if (parent != null){
                rebalance(newChild);
            }
        }

        if (remNode.isBlack()) {
            if (newChild == null)
                doubleBlack(parent, remNode);
            else if (!newChild.isBlack())
                newChild.setBlack();
        }
    }

    /**
     * Handles the case of double black which happens after removing a black node of the tree
     * @param parent the parent of the removed node
     * @param remNode the removed node
     */
    private void doubleBlack(RBNode<Entry<K,V>> parent, RBNode<Entry<K,V>> remNode) {
        RBNode<Entry<K,V>> sibling = getUncle(remNode, parent);

        if (sibling != null && sibling.isBlack()) { // black sibling
            RBNode<Entry<K,V>> redChild = null;

            if (sibling.getLeftChild() != null) { // red left child
                redChild = (RBNode<Entry<K, V>>) sibling.getLeftChild();
            }
            else if (sibling.getRightChild() != null) { // red right child
                redChild = (RBNode<Entry<K, V>>) sibling.getRightChild();
            }
            else { // no child or black child
                if (!parent.isBlack()) { // red parent
                    parent.setBlack();
                    sibling.setRed();
                }
                else { // black parent
                    sibling.setRed();
                    RBNode<Entry<K,V>> newParent = (RBNode<Entry<K, V>>) parent.getParent();
                    if (newParent == null)
                        parent.setBlack(); // reached root
                    else doubleBlack(newParent, parent);
                }
            }
            if (redChild != null) handleRedChild(parent, redChild);

        }
    }

    /**
     * Handles the case of the double black in case of the brother having a red child
     * @param parent the parent of the node being removed
     * @param redChild the red child
     */
    private void handleRedChild(RBNode<Entry<K, V>> parent, RBNode<Entry<K, V>> redChild) {
        redChild.setBlack();
        RBNode<Entry<K,V>> newRoot = (RBNode<Entry<K, V>>) restructure(redChild);

        if (parent.isBlack()) {
            newRoot.setBlack();
        } else newRoot.setRed();
    }
}