package dataStructures;
/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{
    /**
     *
     * @param key
     * @param value
     * @return
     */


    //TODO: Left as an exercise.
    // If exists a entry with this Key, update the node with new element
    // and return the old value of the entry
    // otherwise, insert the newNode, "rebalance" from the insertion position
    // and return value
    public V put(K key, V value) {
        if (root == null) {
            root = new AVLNode<>(new Entry<>(key, value));
            currentSize++;
            return null;
        }

        BTNode<Entry<K,V>> parent = null;
        BTNode<Entry<K,V>> current = (BTNode<Entry<K, V>>) root;
        int comp = 0;

        while (current != null) {
            comp = key.compareTo(current.getElement().key());
            if (comp == 0) {
                V oldValue = current.getElement().value();
                current.setElement(new Entry<>(key, value));
                return oldValue; // Atualizou, não precisa balancear
            }
            parent = current;
            if (comp < 0) current = (BTNode<Entry<K, V>>) current.getLeftChild();
            else current = (BTNode<Entry<K, V>>) current.getRightChild();
        }

        // Insere o novo nó
        AVLNode<Entry<K,V>> newNode = new AVLNode<>(new Entry<>(key, value));
        newNode.setParent(parent);

        if (comp < 0) parent.setLeftChild(newNode);
        else parent.setRightChild(newNode);

        currentSize++;

        // Agora sim, chamamos o rebalanceamento a partir do novo nó
        insertAndRebalance(newNode);

        return null;
    }


    /**
     *
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        BTNode<Entry<K, V>> nodeToRemove = searchNode(key);
        if (nodeToRemove == null) {
            return null;
        }
        V removedValue = nodeToRemove.getElement().value();

        removeNode(nodeToRemove);

        currentSize--;
        return removedValue;
    }

    private BTNode<Entry<K,V>> searchNode(K key) {
        BTNode<Entry<K, V>> currentNode = (BTNode<Entry<K, V>>) root;
        while (currentNode != null) {
            int cmp = key.compareTo(currentNode.getElement().key());
            if (cmp == 0) {
                return currentNode;
            } else if (cmp < 0) {
                currentNode = (BTNode<Entry<K, V>>) currentNode.getLeftChild();
            } else {
                currentNode = (BTNode<Entry<K, V>>) currentNode.getRightChild();
            }
        }
        return null;
    }

    private void removeNode(BTNode<Entry<K,V>> node) {
        BTNode<Entry<K,V>> nodeToRebalance;
        // Caso 1: É uma folha (Leaf)
        if (node.getLeftChild() == null && node.getRightChild() == null) {
            nodeToRebalance = (BTNode<Entry<K,V>>) node.getParent();
            BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) node.getParent();
            if (parent == null) {
                root = null;
            } else if (parent.getLeftChild() == node) {
                parent.setLeftChild(null);
            } else {
                parent.setRightChild(null);
            }
        }
        // Caso 2: Tem apenas 1 filho
        else if (node.getLeftChild() == null || node.getRightChild() == null) {
            nodeToRebalance = (BTNode<Entry<K,V>>) node.getParent();
            BTNode<Entry<K,V>> child = (node.getLeftChild() != null) ?
                    (BTNode<Entry<K,V>>) node.getLeftChild() :
                    (BTNode<Entry<K,V>>) node.getRightChild();
            BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) node.getParent();
            if (parent == null) {
                root = child;
                child.setParent(null);
            } else if (parent.getLeftChild() == node) {
                parent.setLeftChild(child);
                child.setParent(parent);
            } else {
                parent.setRightChild(child);
                child.setParent(parent);
            }
        }
        // Caso 3: Tem 2 filhos (Caso complexo)
        else {
            BTNode<Entry<K,V>> rightChild = (BTNode<Entry<K,V>>) node.getRightChild();
            BTNode<Entry<K,V>> successor = rightChild.furtherLeftElement();
            node.setElement(successor.getElement());
            removeNode(successor);
            return;
        }
        if (nodeToRebalance != null) {
            insertAndRebalance(nodeToRebalance);
        } else if (root != null) {
            ((AVLNode<Entry<K,V>>)root).updateHeight();
        }
    }


    protected void insertAndRebalance(BTNode<Entry<K,V>> node) {
        AVLNode<Entry<K,V>> current = (AVLNode<Entry<K,V>>) node;
        while (current != null) {
            current.updateHeight();
            if (!isBalanced(current)) {
                AVLNode<Entry<K,V>> z = current;
                AVLNode<Entry<K,V>> y = tallerChild(z);
                AVLNode<Entry<K,V>> x = tallerChild(y);
                BTNode<Entry<K,V>> newSubRoot = restructure(x);
                current = (AVLNode<Entry<K,V>>) newSubRoot;
                x.updateHeight();
                y.updateHeight();
                z.updateHeight();
            }
            current = (AVLNode<Entry<K,V>>) current.getParent();
        }
    }

    /**
     * Verifica se um nó está balanceado (diferença de alturas <= 1).
     */
    private boolean isBalanced(AVLNode<Entry<K,V>> node) {
        if (node == null) return true;

        int leftH = getSafeChildHeight(node.getLeftChild());
        int rightH = getSafeChildHeight(node.getRightChild());

        return Math.abs(leftH - rightH) <= 1;
    }

    /**
     * Devolve o filho com maior altura.
     * Usado para determinar se fazemos rotação à esquerda, direita ou duplas (LL, RR, LR, RL).
     */
    private AVLNode<Entry<K,V>> tallerChild(AVLNode<Entry<K,V>> node) {

        int leftH = getSafeChildHeight(node.getLeftChild());
        int rightH = getSafeChildHeight(node.getRightChild());

        if (leftH > rightH) {
            return (AVLNode<Entry<K,V>>) node.getLeftChild();
        } else if (leftH < rightH) {
            return (AVLNode<Entry<K,V>>) node.getRightChild();
        } else {

            if (node.isRoot()) return (AVLNode<Entry<K,V>>) node.getLeftChild();

            if (node == ((AVLNode<Entry<K,V>>)node.getParent()).getLeftChild()) {
                return (AVLNode<Entry<K,V>>) node.getLeftChild();
            } else {
                return (AVLNode<Entry<K,V>>) node.getRightChild();
            }
        }
    }


    private int getSafeChildHeight(Node<Entry<K, V>> child) {
        if (child == null) return -1;
        if (child instanceof AVLNode) {
            return ((AVLNode<Entry<K, V>>) child).getHeight();
        }
        return ((BTNode<Entry<K, V>>) child).getHeight();
    }


}
