package dataStructures;

import java.util.HashMap;

/**
 * SepChain Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class SepChainHashTable<K,V> extends HashTable<K,V> {

    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.75f;
    static final float MAX_LOAD_FACTOR =0.9f;

    // The array of Map with singly linked list.
    private Map<K,V>[] table;

    public SepChainHashTable( ){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public SepChainHashTable( int capacity ){
        super(capacity);
        int arraySize = nextPrime( (int) Math.ceil( capacity / IDEAL_LOAD_FACTOR ) );
        table = (MapSinglyList<K,V>[]) new MapSinglyList[arraySize];
        for (int i=0; i<arraySize; i++){
            table[i]= new MapSinglyList<>();
        }
        maxSize = (int) (arraySize * MAX_LOAD_FACTOR);
    }

    // Returns the hash value of the specified key.
    protected int hash( K key ){
        return Math.abs( key.hashCode() ) % table.length;
    }
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    public V get(K key) {
        if (key == null) return null;
        int index = hash(key);
        if (table[index] != null) {
            return table[index].get(key);
        }
        return null;
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
    public V put(K key, V value) {
        if (isFull())
            rehash();
        int index;
        if (table == null)
            index = 0;
        else index = hash(key);
        assert table != null;
        if (table[index] == null) {
            table[index] = new MapSinglyList<>();
        }
        currentSize++;
        return table[index].put(key, value);
    }


    private void rehash() {
        Map<K,V>[] oldTable = table;
        int newSize = nextPrime( oldTable.length * 2 );
        if (newSize == 0)
            newSize = nextPrime( DEFAULT_CAPACITY );

        @SuppressWarnings("unchecked")
        Map<K,V>[] newTable = (Map<K,V>[]) new MapSinglyList[newSize];
        table = newTable;
        maxSize = (int) (newSize * MAX_LOAD_FACTOR);
        currentSize = 0;


        for ( Map<K,V> map : oldTable ) {
            if ( map != null ) {
                Iterator<Entry<K,V>> it = map.iterator();
                while ( it.hasNext() ) {
                    Entry<K,V> entry = it.next();
                    this.put( entry.key(), entry.value() );
                }
            }
        }
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
    public V remove(K key) {
        int index = hash(key);
        if (table[index] != null) {
            V value = table[index].remove(key);
            if (value != null) {
                currentSize--;
            }
            return value;
        }
        return null;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }


}
