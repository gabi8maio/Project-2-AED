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

    private BTNode<Entry<K,V>> getNode(BTNode<Entry<K,V>> node, K key) {
        //TODO: Left as an exercise.

        if (node == null) return null;

        int comparison = key.compareTo(node.getElement().key());
        if (comparison == 0)
            return node;
        else if (comparison < 0)
            return getNode((BTNode<Entry<K,V>>) node.getLeftChild(), key);
        else
            return getNode((BTNode<Entry<K,V>>) node.getRightChild(), key);
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
        //TODO: Left as an exercise.

        Entry<K,V> newEntry = new Entry<>(key, value);

        if (isEmpty()) {
            root = new BTNode<>(newEntry, null, null, null);
            return null;
        }

        BTNode<Entry<K,V>> current = (BTNode<Entry<K,V>>) root;
        BTNode<Entry<K,V>> parent = null;

        while (current != null) {
            parent = current;
            int comparison = key.compareTo(current.getElement().key());

            if (comparison == 0) {
                V oldValue = current.getElement().value();
                current.setElement(newEntry);
                return oldValue;
            }

            current = (comparison < 0) ?
                    (BTNode<Entry<K,V>>) current.getLeftChild() :
                    (BTNode<Entry<K,V>>) current.getRightChild();
        }


        BTNode<Entry<K,V>> newNode = new BTNode<>(newEntry, parent, null, null);
        if (key.compareTo(parent.getElement().key()) < 0) {
            parent.setLeftChild(newNode);
        } else
            parent.setRightChild(newNode);


        return null;
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
        //TODO: Left as an exercise.
        BTNode<Entry<K,V>> current = (BTNode<Entry<K,V>>) root;
        BTNode<Entry<K,V>> parent = null;

        while (current != null) {
            int comparison = key.compareTo(current.getElement().key());

            if (comparison == 0) {
                V oldValue = current.getElement().value();

                if (current.getLeftChild() != null && current.getRightChild() != null) {
                    // Node with two children - find successor
                    BTNode<Entry<K,V>> successor = (BTNode<Entry<K,V>>) current.getRightChild();
                    BTNode<Entry<K,V>> successorParent = current;

                    while (successor.getLeftChild() != null) {
                        successorParent = successor;
                        successor = (BTNode<Entry<K,V>>) successor.getLeftChild();
                    }

                    // Replace current with successor's data
                    current.setElement(successor.getElement());

                    // Remove the successor (which has at most one right child)
                    if (successorParent.getLeftChild() == successor) {
                        successorParent.setLeftChild(successor.getRightChild());
                    } else
                        successorParent.setRightChild(successor.getRightChild());


                    if (successor.getRightChild() != null)
                        ((BTNode<Entry<K,V>>) successor.getRightChild()).setParent(successorParent);

                } else {
                    BTNode<Entry<K,V>> child = (current.getLeftChild() != null) ? (BTNode<Entry<K,V>>) current.getLeftChild() : (BTNode<Entry<K,V>>) current.getRightChild();

                    if (parent == null) root = child;
                    else if (parent.getLeftChild() == current) parent.setLeftChild(child);
                     else parent.setRightChild(child);


                    if (child != null) child.setParent(parent);

                }

                return oldValue;
            }

            parent = current;
            current = (comparison < 0) ?
                    (BTNode<Entry<K,V>>) current.getLeftChild() :
                    (BTNode<Entry<K,V>>) current.getRightChild();
        }
        return null;
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
