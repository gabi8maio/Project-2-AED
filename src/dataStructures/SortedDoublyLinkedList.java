package dataStructures;

import dataStructures.exceptions.*;


/**
 * Sorted Doubly linked list Implementation
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class SortedDoublyLinkedList<E> implements SortedList<E> {

    /**
     *  Node at the head of the list.
     */
    private DoublyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private DoublyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private int currentSize;
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
        this.comparator = comparator;
        head = null;
        tail = null;
        currentSize = 0;
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
        if (size() == 0)
            throw new NoSuchElementException();
        if (head != null)
            return head.getElement();
        return null;
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMax( ) {
        if (size() == 0)
            throw new NoSuchElementException();
        if (tail != null)
            return tail.getElement();
        return null;
    }
    /**
     * Returns the first occurrence of the element equals to the given element in the list.
     * @return element in the list or null
     */
    public E get(E element) {
        Iterator <E> iterator = iterator();
        while (iterator.hasNext()) {
            E current = iterator.next();
            if (comparator.compare(current, element) == 0)
                return current;
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
        return get(element) != null;
    }

    /**
     * Inserts the specified element at the list, according to the natural order.
     * If there is an equal element, the new element is inserted after it.
     * @param element to be inserted
     */
    public void add(E element) {
        DoublyListNode<E> nodeToCompare = head;
        DoublyListNode<E> newNode = new DoublyListNode<>(element);
        if (this.isEmpty()) {
            head = newNode;
            tail = newNode;
            currentSize++;


        }else if (comparator.compare(element, head.getElement()) < 0 ) {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
            currentSize++;
        }else if (comparator.compare(element, head.getElement()) == 0 ) {
            DoublyListNode<E> next = head.getNext();
            head.setNext(newNode);
            newNode.setNext(next);
            next.setNext(newNode);
            newNode.setPrevious(head);
            currentSize++;
        }else if (comparator.compare(element, tail.getElement()) >= 0) {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
            currentSize++;

        } else {
            while (comparator.compare(nodeToCompare.getElement(), element) <= 0 && nodeToCompare.getNext() != null) {
                nodeToCompare = nodeToCompare.getNext();
            }

            DoublyListNode<E> prev = nodeToCompare.getPrevious();
            prev.setNext(newNode);
            newNode.setPrevious(prev);
            newNode.setNext(nodeToCompare);
            nodeToCompare.setPrevious(newNode);
            currentSize++;
        }
    }

    /**
     * Removes and returns the first occurrence of the element equals to the given element in the list.
     * @return element removed from the list or null if !belongs(element)
     */
    public E remove(E element) {
        if (isEmpty()){
            return null;
        }
        DoublyListNode<E> current = head;
        DoublyListNode<E> nodeToRemove = null;
        while (current != null &&  nodeToRemove == null) {
            if (comparator.compare(current.getElement(), element) == 0) {
                nodeToRemove = current;
            }
            current = current.getNext();
        }

        if (nodeToRemove == null) {
            return null;
        }

        E removedElement = nodeToRemove.getElement();

        if (currentSize==1){
            head=null;
            tail=null;
        }else if (nodeToRemove == head){
            head=head.getNext();
            head.setPrevious(null);
        }else if (nodeToRemove==tail){
            tail=tail.getPrevious();
            tail.setNext(null);
        }else{
            DoublyListNode<E> prev = nodeToRemove.getPrevious();
            DoublyListNode<E> next = nodeToRemove.getNext();
            prev.setNext(next);
            next.setPrevious(prev);
        }
        currentSize--;
        return removedElement;
    }

}
