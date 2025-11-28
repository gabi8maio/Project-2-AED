package dataStructures;
import dataStructures.exceptions.NoSuchElementException;

/**
 * Implementation of Two Way Iterator for DLList 
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class DoublyIterator<E> implements Iterator<E> {
    /**
     * Node with the first element in the iteration.
     */
    protected DoublyListNode<E> firstNode; // changed To protected

    /**
     * Node with the next element in the iteration.
     */
    DoublyListNode<E> nextToReturn;


    /**
     * DoublyIterator constructor
     *
     * @param first - Node with the first element of the iteration
     */
    public DoublyIterator(DoublyListNode<E> first) {
        //TODO: Left as an exercise.
        firstNode = first;
        nextToReturn = first;
    }
    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next( ){
        //TODO: Left as an exercise.
        if (!hasNext())
            throw new NoSuchElementException();

        DoublyListNode<E> now = nextToReturn;
        nextToReturn = nextToReturn.getNext();
        return now.getElement();
    }

    /**
     * Restart the iterator
     */
    public void rewind() {
        //TODO: Left as an exercise.
        nextToReturn = firstNode;

    }
    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     * @return true iff the iteration has more elements
     */
    public boolean hasNext( ) {
        //TODO: Left as an exercise.
        return nextToReturn != null;
    }


}
