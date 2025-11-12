package dataStructures;
/**
 * SepChain Hash Table Iterator
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
import dataStructures.exceptions.NoSuchElementException;

class SepChainHashTableIterator<K,V> implements Iterator<Map.Entry<K,V>> {

    //TODO: Left as exercise
    private final Map<K,V>[] table;
    private int currentBucket;
    private Iterator<Map.Entry<K,V>> currentIterator;

    public SepChainHashTableIterator(Map<K,V>[] table) {
        //TODO: Left as exercise
        this.table = table;
        currentBucket = -1;
        currentIterator = null;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
	//TODO: Left as exercise
        if (currentIterator != null && currentIterator.hasNext()) return true;

        for (int i = currentBucket + 1; i < table.length; i++)
            if (table[i] != null && !table[i].isEmpty()) return true;
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public Map.Entry<K,V> next() {
        //TODO: Left as exercise
        if (!hasNext()) throw new NoSuchElementException();

        if (currentIterator != null && currentIterator.hasNext()) return currentIterator.next();

        currentBucket++;
        while (currentBucket < table.length) {
            if (table[currentBucket] != null && !table[currentBucket].isEmpty()) {
                currentIterator = table[currentBucket].iterator();
                return currentIterator.next();
            }
            currentBucket++;
        }
        throw new NoSuchElementException();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        //TODO: Left as exercise
        currentBucket = -1;
        currentIterator = null;
    }
}