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

    private final Map<K,V>[] table;
    private int currentBucket;
    private Iterator<Map.Entry<K,V>> currentIterator;

    public SepChainHashTableIterator(Map<K,V>[] table) {
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
        if (currentIterator != null && currentIterator.hasNext())
            return true;
        for (int i = currentBucket + 1; i < table.length; i++)
            if (table[i] != null && !table[i].isEmpty())
                return true;
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public Map.Entry<K,V> next() {
        if (!hasNext()) throw new NoSuchElementException();
        //DESCER ATE ENCONTRAR A 1º LISTA N VAZIA
        //DESCER NOVAMENTE ATÉ A LISTA SEGUINTE QUE N SEJA VAZIA
        // ITERAR AS LISTAS NÃO VAZIAS

        if(currentIterator == null || !currentIterator.hasNext()) {
            currentBucket++;
            while (currentBucket < table.length && (table[currentBucket] == null || table[currentBucket].isEmpty())) {
                currentBucket++;
            }
            currentIterator = table[currentBucket].iterator();
        }
        return currentIterator.next();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        currentBucket = -1;
        currentIterator = null;
    }
}

