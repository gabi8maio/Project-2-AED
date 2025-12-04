package dataStructures;

import dataStructures.exceptions.EmptyMapException;
/**
 * Binary Search Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class BSTSortedMap<K extends Comparable<K>,V> extends BTree<Map.Entry<K,V>> implements SortedMap<K,V>{

    /**
     * Constructor
     */
    public BSTSortedMap(){
        super();
    }
    /**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
    }


    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V get(K key) {
        Node<Entry<K,V>> node=getNode((BTNode<Entry<K,V>>)root,key);
        if (node!=null)
            return node.getElement().value();
        return null;
    }

    protected BTNode<Entry<K,V>> getNode(BTNode<Entry<K,V>> node, K key) {
        if (node == null)
            return null;

        K current = node.getElement().key();
        int compare = key.compareTo(current); // ← CORRIGIDO: key.compareTo(current)

        if (compare == 0)
            return node;
        else if (compare < 0) // key < current → vai para esquerda
            return getNode((BTNode<Entry<K, V>>) node.getLeftChild(), key);
        else // compare > 0 → key > current → vai para direita
            return getNode((BTNode<Entry<K, V>>) node.getRightChild(), key);
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     *
     * @param key   with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V put(K key, V value) {
        if (isEmpty()) {
            root = new BTNode<>(new Map.Entry<>(key, value));
            currentSize++; // FALTAVA ESTA LINHA
            return null;
        }
        return findInsert((BTNode<Entry<K, V>>) root, key, value);
    }

    private V findInsert(BTNode<Entry<K,V>> node, K key, V value) {
        int compare = key.compareTo(node.getElement().key());
        if (compare == 0) {
            V oldValue = node.getElement().value();
            node.setElement(new Map.Entry<>(key, value));
            return oldValue;
        }
        if (compare < 0) {
            if (node.getLeftChild() == null) {
                BTNode<Entry<K, V>> newNode = new BTNode<>(new Map.Entry<>(key, value), node);
                node.setLeftChild(newNode);
                currentSize++; // FALTAVA ESTA LINHA
                return null;
            }
            else return findInsert((BTNode<Entry<K, V>>) node.getLeftChild(), key, value);
        }
        else { // compare > 0
            if (node.getRightChild() == null) {
                BTNode<Entry<K,V>> newNode = new BTNode<>(new Map.Entry<>(key, value), node);
                node.setRightChild(newNode);
                currentSize++; // FALTAVA ESTA LINHA
                return null;
            }
            else return findInsert((BTNode<Entry<K, V>>) node.getRightChild(), key, value);
        }
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    @Override
    public V remove(K key) {
        if (isEmpty())
            return null;

        BTNode<Entry<K, V>> node = getNode((BTNode<Entry<K, V>>) root, key);
        if (node == null)
            return null;
        else {
            V oldValue = node.getElement().value();
            recursiveRemove(node);
            currentSize--;
            return oldValue;
        }
    }

    private void replaceChild(BTNode<Entry<K, V>> parent, BTNode<Entry<K, V>> child, BTNode<Entry<K, V>> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.getLeftChild() == child) {
            parent.setLeftChild(newChild);
        } else {
            parent.setRightChild(newChild);
        }

        if (newChild != null)
            newChild.setParent(parent);
    }

    private void recursiveRemove(BTNode<Entry<K, V>> node) {
        BTNode<Entry<K, V>> parent = (BTNode<Entry<K, V>>) node.getParent();
        if (node.isLeaf())
            replaceChild(parent, node, null);
        else if (node.getLeftChild() == null)
            replaceChild(parent, node, (BTNode<Entry<K, V>>) node.getRightChild());
        else if (node.getRightChild() == null)
            replaceChild(parent, node, (BTNode<Entry<K, V>>) node.getLeftChild());
        else {//opçao A: substituir pelo mais a direita do left Child.
            BTNode<Entry<K, V>> replace = ((BTNode<Entry<K, V>>) node.getLeftChild()).furtherRightElement();
            node.setElement(replace.getElement());
            recursiveRemove(replace);
        }
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new InOrderIterator<>((BTNode<Entry<K,V>>) root);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}
