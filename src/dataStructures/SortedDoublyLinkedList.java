package dataStructures;

import dataStructures.exceptions.*;

import java.io.*;


/**
 * Sorted Doubly linked list Implementation
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class SortedDoublyLinkedList<E> implements SortedList<E>, Serializable {

    /**
     *  Node at the head of the list.
     */
    private transient DoublyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private transient DoublyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private transient int currentSize;
    /**
     * Comparator of elements.
     */
    private final Comparator<E> comparator;
    /**
     * Constructor of an empty sorted double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SortedDoublyLinkedList(Comparator<E> comparator) {
        //TODO: Left as an exercise.
        this.comparator = comparator;
        head = null;
        tail = null;
        currentSize = 0;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // escreve os campos normais (não temos aqui, mas é boa prática)
        oos.writeInt(currentSize); // escreve o tamanho
        DoublyListNode node = head;
        while (node != null) {
            oos.writeObject(node.getElement()); // escreve cada elemento
            node = node.getNext();
        }
        oos.flush();
    }


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // lê os campos normais
        int size = ois.readInt(); // lê o tamanho
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) ois.readObject();
            add(element); // recria os nós
        }
    }


    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */

    public int size() {
        return currentSize;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new DoublyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMin( ) {
        //TODO: Left as an exercise.
        if(currentSize == 0) throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMax( ) {
        //TODO: Left as an exercise.
        if(currentSize == 0) throw new NoSuchElementException();
        return tail.getElement();
    }
    /**
     * Returns the first occurrence of the element equals to the given element in the list.
     * @return element in the list or null
     */
    public E get(E element) {
        //TODO: Left as an exercise.
        DoublyListNode<E> nextToCheck = head;
        while(nextToCheck != null){
            int cmp = comparator.compare(nextToCheck.getElement(), element);
            if(cmp == 0) return nextToCheck.getElement();
            if(cmp > 0) return null;
            nextToCheck = nextToCheck.getNext();
        }
        return null;
    }

    /**
     * Returns true iff the element exists in the list.
     *
     * @param element to be found
     * @return true iff the element exists in the list.
     */
    public boolean contains(E element) {
        //TODO: Left as an exercise.
        DoublyListNode<E> current = head;
        while (current != null) {
            int cmp = comparator.compare(current.getElement(), element);
            if (cmp == 0) return true;
            else if (cmp > 0) return false;
            current = current.getNext(); // < 0
        }
        return false;
    }

    /**
     * Inserts the specified element at the list, according to the natural order.
     * If there is an equal element, the new element is inserted after it.
     * @param element to be inserted
     */
    public void add(E element) {
        //TODO: Left as an exercise.
        if (head == null) {
            DoublyListNode<E> newNode = new DoublyListNode<>(element);
            head = newNode;
            tail = newNode;
            currentSize++;
            return;
        }

        DoublyListNode<E> current = head;
        DoublyListNode<E> previous = null;

        while (current != null && comparator.compare(current.getElement(), element) < 0) {
            previous = current;
            current = current.getNext();
        }
        DoublyListNode<E> newNode = new DoublyListNode<>(element);

        // Insert on the Head -
        if (previous == null) {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        // Insert on the Tail
        else if (current == null) {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        // Insert in the middle.
        else {
            previous.setNext(newNode);
            newNode.setPrevious(previous);
            newNode.setNext(current);
            current.setPrevious(newNode);
        }
        currentSize++;
    }

    /**
     * Removes and returns the first occurrence of the element equals to the given element in the list.
     * @return element removed from the list or null if !belongs(element)
     */
    public E remove(E element) {
        //TODO: Left as an exercise.
        DoublyListNode<E> nodeToRemove = head;
        while (nodeToRemove != null) {
            int cmp = comparator.compare(nodeToRemove.getElement(), element);
            if (cmp == 0) {

                DoublyListNode<E> previous = nodeToRemove.getPrevious();
                DoublyListNode<E> next = nodeToRemove.getNext();

                if (previous != null) previous.setNext(next);
                else head = next;

                if (next != null) next.setPrevious(previous);
                else tail = previous;

                currentSize--;
                return nodeToRemove.getElement();
            } else if (cmp > 0) return null;
            nodeToRemove = nodeToRemove.getNext();
        }
        return null;
    }

}
