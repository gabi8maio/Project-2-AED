package dataStructures;

import dataStructures.exceptions.InvalidPositionException;
import dataStructures.exceptions.NoSuchElementException;
import java.io.*;
/**
 * Doubly Linked List
 *
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic Element
 */
public class DoublyLinkedList<E> implements TwoWayList<E>,Serializable {
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
     * Constructor of an empty double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public DoublyLinkedList() {
        //TODO: Left as an exercise.
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
            addLast(element); // recria os nós
        }
    }


    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    public boolean isEmpty() {
        //TODO: Left as an exercise.
        return currentSize == 0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */

    public int size() {
        //TODO: Left as an exercise.
        return currentSize;
    }

    /**
     * Returns a two-way iterator of the elements in the list.
     *
     * @return Two-Way Iterator of the elements in the list
     */

    public TwoWayIterator<E> twoWayiterator() {
        return new TwoWayDoublyIterator<>(head, tail);
    }
    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new DoublyIterator<>(head);
    }

    /**
     * Inserts the element at the first position in the list.
     * @param element - Element to be inserted
     */
    public void addFirst( E element ) {
        //TODO: Left as an exercise.
        DoublyListNode<E> newNode = new DoublyListNode<E>(element,null,head);
        if(isEmpty()){
            head = newNode;
            tail = newNode;
        }else {
            head.setPrevious(newNode);
            head = newNode;
        }
        currentSize++;
    }

    /**
     * Inserts the element at the last position in the list.
     * @param element - Element to be inserted
     */
    public void addLast( E element ) {
        //TODO: Left as an exercise.
        DoublyListNode<E> newNode = new DoublyListNode<E>(element,tail,null);
        if(isEmpty()) head = newNode;
        else tail.setNext(newNode);
        tail = newNode;
        currentSize++;
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getFirst( ) {
        //TODO: Left as an exercise.
        if(currentSize == 0) throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getLast( ) {
        //TODO: Left as an exercise.
        if(currentSize == 0) throw new NoSuchElementException();
        return tail.getElement();
    }


    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    public E get( int position ) {
        //TODO: Left as an exercise.
        if(position < 0 || position >= currentSize) throw new InvalidPositionException();

        DoublyListNode<E> node = head;
        for(int i = 0; i < position; i++) node = node.getNext();
        return node.getElement();
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    public int indexOf( E element ) {
        //TODO: Left as an exercise.
        DoublyListNode<E> node = head;
        for(int i = 0; i < currentSize; i++){
            if(node.getElement().equals(element)) return i;  // Compare elements
            node = node.getNext();
        }
        return -1;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     * @param position - position where to insert element
     * @param element - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public void add( int position, E element ) {
        //TODO: Left as an exercise.
        if (position < 0 || position > currentSize) throw new InvalidPositionException();
        if (position == 0) {
            addFirst(element);
            return;
        }
        if (position == currentSize) {
            addLast(element);
            return;
        }
        DoublyListNode<E> current = head;
        for (int i = 0; i < position; i++) current = current.getNext();

        DoublyListNode<E> newNode = new DoublyListNode<>(element, current.getPrevious(), current);
        current.getPrevious().setNext(newNode);
        current.setPrevious(newNode);
        currentSize++;
    }

    /**
     * Removes and returns the element at the first position in the list.
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeFirst( ) {
        //TODO: Left as an exercise.
        if (currentSize == 0) throw new NoSuchElementException();
        E element = head.getElement();
        if (currentSize == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
        currentSize--;
        return element;
    }

    /**
     * Removes and returns the element at the last position in the list.
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeLast( ) {
        //TODO: Left as an exercise.
        if (currentSize == 0) throw new NoSuchElementException();
        E element = tail.getElement();
        if (currentSize == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        currentSize--;
        return element;
    }

    /**
     *  Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst.
     * If the specified position is size()-1, remove corresponds to removeLast.
     * @param position - position of element to be removed
     * @return element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public E remove( int position ) {
        if (position < 0 || position >= currentSize) throw new InvalidPositionException();
        if (position == 0) return removeFirst();
        if (position == currentSize - 1) return removeLast();

        DoublyListNode<E> nodeRemove = head;
        for(int i = 0; i < currentSize; i++){
            if(i == position) {
                DoublyListNode<E> previousNode = nodeRemove.getPrevious();
                DoublyListNode<E> nextNode = nodeRemove.getNext();
                previousNode.setNext(nextNode);
                nextNode.setPrevious(previousNode);

                nodeRemove.setPrevious(null);
                nodeRemove.setNext(null);
                currentSize--;
                return nodeRemove.getElement();
            }
            nodeRemove = nodeRemove.getNext();
        }
        return null;
    }
}
