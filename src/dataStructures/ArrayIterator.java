package dataStructures;
/**
 * Array Iterator
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
class ArrayIterator<E> implements Iterator<E> {
    private final E[] elems;
    private final int counter;
    private int current;


    public ArrayIterator(E[] elems, int counter) {
        this.elems = elems;
        this.counter = counter;
        rewind();
    }

    @Override
    public void rewind() {
        //TODO: Left as an exercise.
        current = 0;
    }

    @Override
    public boolean hasNext() {
        //TODO: Left as an exercise.
        return current < counter;
    }

    @Override
    public E next() {
        //TODO: Left as an exercise.
        return elems[current++];
    }

}
