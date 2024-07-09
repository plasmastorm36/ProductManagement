import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A generic HashTable in java uses an initial array size of 16, however the key must be
 * comparable
 * @author Noah Rouse <my_email@noahrouse36@gmail.com>
 */
public final class HashTable<K extends Comparable<K> & Serializable, V extends Serializable>
      implements Serializable {
   /**
    * A class dedicated to entering things into the hash set
    */
   public static final class Entry <K extends Comparable<K> & Serializable, 
         V extends Serializable> implements Serializable {
      private final K key;
      private V value;
      /**
       * 
       * @param key key to search for entry
       * @param value the value that the entry holds
       */
      private Entry (final K key, final V value) {
         this.key = key;
         this.value = value;
      }

      /**
       * 
       * @return key of entry
       */
      private K key () {
         return this.key;
      }

      /**
       * 
       * @return value of entry
       */
      private V value () {
         return this.value;
      }
   }

   private final ArrayList<ArrayList<Entry<K, V>>> table = new ArrayList<>(17);
   private int size = 0;

   /**
    * Creates all of the buckets for the HashSet
    */
   public HashTable() {
      for (int i = 0; i < 17; i++) {
         table.add(new ArrayList<Entry<K, V>>());
      }
   }
   /**
    * Size of the HashTable
    * @return returns the size of the HashTable
    */
   public int size () {
      return size;
   }

   /**
    * 
    * @return Returns true if HashTable is empty
    */
   public boolean isEmpty () {
      return size == 0;
   }

   /**
    * Retrieves the value associated with the key
    * @param key associated value for entry
    * @return returns value associated with key
    * @throws NoSuchElementException if the key does not exist
    */
   public V get (final K key) throws NoSuchElementException {
      final int i = key.hashCode() % 17;
      final ArrayList<Entry<K, V>> bucket = table.get(i);
      if (bucket.isEmpty()) {
         throw new NoSuchElementException("Empty Bucket");
      }
      int index = binarySearch(key, bucket, 0, bucket.size() - 1);
      if (index == -1) {
         throw new NoSuchElementException("Key not added");
      }

      return bucket.get(index).value();
   }

   /**
    * Insets the entry into the HashTable
    * @param key index to grab the value
    * @param value element to be contained
    */
   public void put (final K key, final V value) {
      final Entry<K, V> entry = new Entry<>(key, value);
      final ArrayList<Entry<K,V>> bucket = table.get(key.hashCode() % 17);
      // checking sorted order
      for (int i = 0; i < bucket.size(); i++) {
         if (key.compareTo(bucket.get(i).key()) < 0) {
            bucket.add(i, entry);
            return;
         }

         // case where key already exists
         if (key.compareTo(bucket.get(i).key()) == 0) {
            bucket.set(i, entry);
            return;
         }
      }

      // case is last
      bucket.add(entry);
      return;
   }

   /**
    * Removes entry from HashSet
    * @param key index of the value to be removed
    * @throws NoSuchElementException when key does not exist
    */
   public V remove (final K key) throws NoSuchElementException {
      final ArrayList<Entry<K, V>> bucket = table.get(key.hashCode() % 17);
      final int index = binarySearch(key, bucket, 0, bucket.size() - 1);
      if (index == -1) {
         throw new NoSuchElementException("Key does not exist");
      }
      return bucket.remove(index).value();
   }

   /**
    * 
    * @return an iterable set of all entries
    */
   public ArrayList<K> keySet () {
      final ArrayList<K> keySet = new ArrayList<K>();
      for (final ArrayList<Entry<K, V>> i: table) {
         for (final Entry<K, V> j: i) {
            keySet.add(j.key());
         }
      }

      return keySet;
   }

   /**
    * 
    * @return an iterable set of all values
    */
   public ArrayList<V> values () {
      final ArrayList<V> values = new ArrayList<V>();
      for (final ArrayList<Entry<K, V>> i: table) {
         for (final Entry<K, V> j: i) {
            values.add(j.value());
         }
      }
      return values;
   }

   /**
    * 
    * @return an iterable set of key-value entries
    */
   public ArrayList<Entry<K, V>> entrySet () {
      final ArrayList<Entry<K, V>> entrySet = new ArrayList<Entry<K, V>>();
      for (final ArrayList<Entry<K, V>> i: table) {
         entrySet.addAll(i);
      }
      return entrySet;
   }

   /**
    * Binary search algorithm
    * @param key item being search for
    * @param bucket arrayList holding all of the entries
    * @param low lowest index
    * @param high highest index
    * @return value if in the array list or null otherwise
    */
   private int binarySearch (final K key, final ArrayList<Entry<K, V>> bucket, int low,
         int high) {

      final int mid = (high + low) / 2;
      final Entry<K, V> entry = bucket.get(mid);
      // null base case
      if (high < low) {
         return -1;
      }

      // return base case
      if (entry.key().compareTo(key) == 0) {
         return mid;
      }

      // case mid key comes before target key
      if (entry.key().compareTo(key) > 0) {
         return binarySearch(key, bucket, mid, high);
      }

      // case mid key comes after target key
      if (entry.key().compareTo(key) < 0) {
         return binarySearch(key, bucket, low, mid);
      }

      return -1;
   }
}
