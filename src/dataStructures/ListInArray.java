package dataStructures;
import dataStructures.exceptions.*;
/**
 * List in Array
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class ListInArray<E> implements List<E> {

    private static final int FACTOR = 2;
    /**
     * Array of generic elements E.
     */
    private E[] elems;

    /**
     * Number of elements in array.
     */
    private int counter;


    /**
     * Construtor with capacity.
     * @param dimension - initial capacity of array.
     */
    @SuppressWarnings("unchecked")
    public ListInArray(int dimension) {
        elems = (E[]) new Object[dimension];
        counter = 0;
    }
    /**
     * Returns true iff the list contains no elements.
     *
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return counter==0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return number of elements in the list
     */
    public int size() {
        return counter;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     *
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elems,counter);
    }

    /**
     * Returns the first element of the list.
     *
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getFirst() {
        //TODO: Left as an exercise.
        if(size() == 0) throw new NoSuchElementException();
        return elems[0];
    }

    /**
     * Returns the last element of the list.
     *
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getLast() {
        //TODO: Left as an exercise.
        if(size() == 0) throw new NoSuchElementException();
        return elems[counter - 1];
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     *
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    public E get(int position) {
        //TODO: Left as an exercise.
        if(position < 0 || position >= counter) throw new InvalidPositionException();

        return elems[position];
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    public int indexOf(E element) {
        //TODO: Left as an exercise.
        for(int i = 0; i < counter; i++){
            if(elems[i].equals(element)) return i;
        }
        return -1;
    }

    /**
     * Inserts the specified element at the first position in the list.
     *
     * @param element to be inserted
     */
    @SuppressWarnings("unchecked")
    public void addFirst(E element) {
        //TODO: Left as an exercise.

        if(size() == elems.length){
            E[] newOne = (E[]) new Object[elems.length * 2];
            for (int i = 0; i < counter; i++) newOne[i] = elems[i];

            elems = newOne;
        }
        for(int i = counter; i > 0; i--){
            elems[i] = elems[i - 1];
        }
        elems[0] = element;
        counter++;
    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @param element to be inserted
     */
    @SuppressWarnings("unchecked")
    public void addLast(E element) {
        //TODO: Left as an exercise.
        if(size() == elems.length){
            E[] newOne = (E[]) new Object[elems.length * 2];
            for (int i = 0; i < counter; i++) newOne[i] = elems[i];

            elems = newOne;
        }
        elems[counter] = element;
        counter++;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     *
     * @param position - position where to insert element
     * @param element  - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    @SuppressWarnings("unchecked")
    public void add(int position, E element) {
        //TODO: Left as an exercise.
        if(position < 0 || position > size()) throw new InvalidPositionException();
        if(position == 0) {addFirst(element); return;}
        if (position == counter){ addLast(element); return;}

        if(size() == elems.length){
            E[] newOne = (E[]) new Object[elems.length * 2];
            if (counter >= 0) System.arraycopy(elems, 0, newOne, 0, counter);
            elems = newOne;
        }
        for(int i = counter; i > position; i--)elems[i] = elems[i - 1];
        elems[position] = element;
        counter++;

    }

    /**
     * Removes and returns the element at the first position in the list.
     *
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeFirst() {
        //TODO: Left as an exercise.
        if(size() == 0) throw new NoSuchElementException();

        E removed = elems[0];
        for(int i = 0; i < counter - 1; i++) elems[i] = elems[i + 1];
        elems[--counter] = null;
        return removed;
    }

    /**
     * Removes and returns the element at the last position in the list.
     *
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeLast() {
        //TODO: Left as an exercise.
        if(size() == 0) throw new NoSuchElementException();

        E lastEle = elems[size() - 1];
        elems[size() - 1] = null;
        counter--;
        return lastEle;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst.
     * If the specified position is size()-1, remove corresponds to removeLast.
     *
     * @param position - position of element to be removed
     * @return element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public E remove(int position) {
        //TODO: Left as an exercise.
        if(position < 0 || position >= counter) throw new InvalidPositionException();
        E removed = elems[position];
        for(int i = position; i < counter - 1; i++) elems[i] = elems[i + 1];
        elems[--counter] = null;
        return removed;
    }
}
