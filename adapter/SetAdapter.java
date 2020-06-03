package adapter;

import java.util.Enumeration;
import java.util.Hashtable;

// Non ammette elementi null

public class SetAdapter implements HSet {

    private Hashtable hashtable = new Hashtable();

    /**
     * Adds the specified element to this set if it is not already present (optional operation).
     */
    public boolean add(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        if(contains(o)) {
            return false;
        }
        hashtable.put(o, o);
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this set if they're not already present (optional operation).
     */
    public boolean addAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        HIterator it = c.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            add(o);
        }
        return true;
    }

    /**
     * Removes all of the elements from this set (optional operation).
     */
    public void clear() {
        hashtable.clear();
    }

    /**
     * Returns true if this set contains the specified element.
     */
    public boolean contains(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        return hashtable.containsKey(o);
    }

    /**
     * Returns true if this set contains all of the elements of the specified collection.
     */
    public boolean containsAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        HIterator it = c.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the specified object with this set for equality.
     */
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof HSet)) {
            return false;
        }
        HSet s = (HSet) o;
        if (s.size() != size()) {
            return false;
        }
        try {
            return containsAll(s);
        }
        catch (ClassCastException cce)   {
            return false;
        }
        catch (NullPointerException npe) {
            return false;
        }
    }

    /**
     * Returns the hash code value for this set.
     */
    public int hashCode(){
        int hashCode = 0;
        HIterator it = iterator();
        while (it.hasNext()) {
            hashCode += it.next().hashCode();
        }
        return hashCode;
    }

    /**
     * Returns true if this set contains no elements.
     */
    public boolean isEmpty(){
        return hashtable.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this set.
     */
    public HIterator iterator(){
        return new SetIterator();
    }

    private class SetIterator implements HIterator {
        Enumeration keys = hashtable.keys();
        Object lastRetKey = null;

        public boolean hasNext() {
            return keys.hasMoreElements();
        }

        public Object next() {
            lastRetKey = keys.nextElement(); // Lancia NoSuchElementException
            return lastRetKey;
        }

        public void remove() {
            // Se next non e' mai stato chiamato o remove e' gia' stato
            // chiamato dopo l'ultima chiamata a next.
            if(lastRetKey == null) {
                throw new IllegalStateException();
            }
            SetAdapter.this.remove(lastRetKey);
            lastRetKey = null;  // Reset to null after removing
        }
    }

    /**
     * Removes the specified element from this set if it is present (optional operation).
     */
    public boolean remove(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        if(!contains(o)) {
            return false;
        }
        hashtable.remove(o);
        return true;
    }

    /**
     * Removes from this set all of its elements that are contained in the specified collection (optional operation).
     */
    public boolean removeAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        boolean flag = false;
        HIterator it = c.iterator();
        while(it.hasNext()) {
            remove(it.next());
            flag = true;
        }
        return flag;
    }

    /**
     * Retains only the elements in this set that are contained in the specified collection (optional operation).
     */
    public boolean retainAll(HCollection c){
        if(c == null) {
            throw new NullPointerException();
        }
        boolean flag = false;
        HIterator it = c.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(!contains(o)) {
                remove(o);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     */
    public int size(){
        return hashtable.size();
    }

    /**
     * Returns an array containing all of the elements in this set.
     */
    public Object[] toArray(){
        Object[] v = new Object[size()];
        Enumeration keys = hashtable.elements();
        int i = 0;
        while(keys.hasMoreElements()) {
            v[i] = keys.nextElement();
            i++;
        }
        return v;
    }

    /**
     * Returns an array containing all of the elements in this set; the runtime type of the returned array is that of the specified array.
     */
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException();
    }
}