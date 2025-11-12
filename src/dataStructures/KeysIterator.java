package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
/**
 * Iterator of keys
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
class KeysIterator<E> implements Iterator<E> {

    
     //TODO: Left as an exercise.
     private final Iterator<Map.Entry<E, ?>> entryIterator;

    public KeysIterator(Iterator<Map.Entry<E,?>> it) {
       //TODO: Left as an exercise.
        this.entryIterator = it;
    }

    @Override
    public boolean hasNext() {
	//TODO: Left as an exercise.
        return entryIterator.hasNext();
    }

    @Override
    public E next() {
	//TODO: Left as an exercise.
        if (!hasNext()) throw new NoSuchElementException();

        Map.Entry<E, ?> entry = entryIterator.next();
        return entry.key();
    }

    @Override
    public void rewind() {
        //TODO: Left as an exercise.
        entryIterator.rewind();
    }
}
