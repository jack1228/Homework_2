package adapter;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Adapter class from CLDC 1.1 Vector to JSE 1.4.2 List (interface HList). This class implements an Object Adapter, therefore it stores a Vector instance which is used by the List's methods. This implementation does not allow null elements.
 */

public class ListAdapter implements HList {

    private Vector v = new Vector();

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's insertElementAt(Object, int) method.
     * @throws NullPointerException {@inheritDoc}
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, Object element) {
        if(element == null) {
            throw new NullPointerException();
        }
        try {
            v.insertElementAt(element, index);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's addElement(Object) method.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean add(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        v.addElement(o);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the collection and adds its elements to the List using add(Object).
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean addAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        boolean flag = false;
        HIterator cit = c.iterator();
        while(cit.hasNext()) {
            Object o = cit.next();
            add(o); // Contiene controllo null
            flag = true;
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the collection and adds its elements to the List at the right index using add(int, Object).
     * @throws NullPointerException {@inheritDoc}
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        if(index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        boolean flag = false;
        HIterator cit = c.iterator();
        while(cit.hasNext()) {
            Object o = cit.next();
            add(index, o); // Contiene controllo null
            flag = true;
            index++;
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's removeAllElements() method.
     */
    @Override
    public void clear() {
        v.removeAllElements();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's contains(Object) method.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        return v.contains(o);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the collection and checks if its elements are all contained in the List using contains(Object).
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean containsAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        HIterator cit = c.iterator();
        while(cit.hasNext()) {
            if(!contains(cit.next()))
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation first checks if the specified object is this list. If so, it returns true; if not, it checks if the specified object is a list. If not, it returns false; if so, it iterates over both lists, comparing corresponding pairs of elements. If any comparison returns false, this method returns false. If either iterator runs out of elements before the other it returns false (as the lists are of unequal length); otherwise it returns true when the iterations complete.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if (!(o instanceof HList)) {
            return false;
        }

        HListIterator it1 = listIterator();
        HListIterator it2 = ((HList) o).listIterator();
        while (it1.hasNext() && it2.hasNext()) {
            Object o1 = it1.next();
            Object o2 = it2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's ElementAt(int) method.
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Object get(int index) {
        try {
            return v.elementAt(index);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * {@inheritDoc}
     * <p>This implementation uses exactly the code that is used to define the list hash function in the documentation for the List.hashCode method.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        HIterator it = iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's indexOf(Object) method.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        return v.indexOf(o);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's isEmpty() method.
     */
    @Override
    public boolean isEmpty() {
        return v.isEmpty();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation uses an integer cursor to iterate over the list. If the list is modified in any way other than through the iterator's remove() method while an iteration is in progress, the changes won't be reflected in the iterator but the latter will not be invalidated. The iterator's cursor, in fact, isn't updated by the methods of the list. Therefore, if structural changes are made to the list when an iteration is in progress, the iterator's behavior won't be consistent with the list.
     */
    @Override
    public HIterator iterator() {
        return new Iterator();
    }

    private class Iterator implements HIterator {

        protected int cursor = 0;
        protected int lastRet = -1;

        public boolean hasNext() {
            return cursor != size();
        }

        public Object next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Object o = get(cursor);
            lastRet = cursor;
            cursor++;
            return o;
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListAdapter.this.remove(lastRet);
            if (lastRet < cursor)
                cursor--;
            lastRet = -1;
        }
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's lastIndexOf(Object) method.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        return v.lastIndexOf(o);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the listIterator(int) method.
     */
    @Override
    public HListIterator listIterator() {
        return new ListIterator(0);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation uses an Internal listIterator class which extends the Iterator class. If the list is modified in any way other than through the listIterator's methods while an iteration is in progress, the changes won't be reflected in the listIterator but the latter will not be invalidated. The listIterator's cursor, in fact, isn't updated by the methods of the list. Therefore, if structural changes are made to the list when an iteration is in progress, the listIterator's behavior won't be consistent with the list.
     */
    @Override
    public HListIterator listIterator(int index) {
        return new ListIterator(index);
    }

    private class ListIterator extends Iterator implements HListIterator {
        
        ListIterator(int index) {
            cursor = index;
        }

        public void add(Object o) {
            ListAdapter.this.add(cursor, o);
            cursor++;
            lastRet = -1;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public Object previous() {
            if(!hasPrevious()) {
                throw new NoSuchElementException();
            }
            cursor--;
            Object o = get(cursor);
            lastRet = cursor;
            return o;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void set(Object o) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListAdapter.this.set(lastRet, o); //Da testare indici limite
        }

    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the get(int) method and the vector's removeElementAt(int) method and then returns the object returned by get(int).
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Object remove(int index) {
        Object o = get(index);  // Contiene controllo bounds
        v.removeElementAt(index);
        return o;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's removeElement(Object) method.
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }
        return v.removeElement(o);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the collection and removes its elements from the set using remove(Object).
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean removeAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        boolean flag = false;
        HIterator cit = c.iterator();
        while(cit.hasNext()) {
            if(remove(cit.next())) { // Contiene controllo null
                flag = true;
            }
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the list and removes from the list the elements which are not contained in the collection (using the iterator's remove() method).
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean retainAll(HCollection c) {
        if(c == null) {
            throw new NullPointerException();
        }
        boolean flag = false;
        HIterator it = iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(!c.contains(o)) {
                it.remove(); // Contiene controllo null
                flag = true;
            }
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the get(int) method and the vector's setElementAt(Object, int) method and then returns the object returned by get(int).
     * @throws NullPointerException {@inheritDoc}
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Object set(int index, Object element) {
        if(element == null) {
            throw new NullPointerException();
        }
        Object o = get(index); // Throws IndexOutOfBoundsException
        v.setElementAt(element, index);
        return o;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation calls the vector's size() method.
     */
    @Override
    public int size() {
        return v.size();    //Integer.maxValue??
    }

    /**
     * {@inheritDoc}
     * <p>This implementation returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive. The returned list is backed by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa. The behavior of the list returned by this method becomes inconsistent with the backing list (i.e., this list) if the latter is structurally modified in any way other than via the returned list. The sublist is, in fact, implemented with an inner class wich stores the offset and the size of the sublist, which are not updated by the methods of the backing list. The sublists won't be invalidated when structural changes are made to the backing list.
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public HList subList(int fromIndex, int toIndex) {
        return new SubList(this, fromIndex, toIndex);
    }

    private class SubList extends ListAdapter {
        private int offset = -1;
        private int size = -1;
        private ListAdapter l = null;
        
        public SubList(ListAdapter list, int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > list.size() || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException();
            }
            l = list;
            offset = fromIndex;
            size = toIndex - fromIndex;
        }
    
        private void boundCheck(int index) {
            if(index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
        }
    
        private void boundCheckForAdd(int index) {
            if(index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
        }
    
        public void add(int index, Object element) {
            boundCheckForAdd(index);
            l.add(offset + index, element);
            size++;
        }
    
        public boolean add(Object o) {
            l.add(offset + size, o);
            size++;
            return true;
        }
    
        public boolean addAll(HCollection c) {
            return addAll(size, c);
        }
    
        public boolean addAll(int index, HCollection c) {
            boundCheckForAdd(index);
            int cSize = c.size();
            if(cSize == 0) {
                return false;
            }
            l.addAll(offset + index, c);
            size += cSize;
            return true;
        }
    
        public void clear() {
            HIterator it = iterator();
            while(it.hasNext()) {
                it.next();
                it.remove();
            }
        }

        public boolean contains(Object o) {
            return indexOf(o) != -1;
        }
    
        public Object get(int index) {
            boundCheck(index);
            return l.get(offset + index);
        }
    
        public int indexOf(Object o) {
            int index = l.indexOf(o);
            if(index < offset || index >= (offset + size)) {
                return -1;
            }
            return index - offset;
        }
    
        public boolean isEmpty() {
            return size == 0;
        }
    
        public HIterator iterator() {
            return new SubListIterator(0);
        }
    
        public int lastIndexOf(Object o) {
            int index = l.lastIndexOf(o);
            if(index < offset || index >= (offset + size)) {
                return -1;
            }
            return index - offset;
        }
    
        public HListIterator listIterator() {
            return new SubListIterator(0);
        }
    
        public HListIterator listIterator(int index) {
            return new SubListIterator(index);
        }
    
        private class SubListIterator implements HListIterator {
            private HListIterator it = null;
    
            SubListIterator(int index) {
                it = ListAdapter.this.listIterator(index+offset);
            }
    
            public boolean hasNext() {
                return nextIndex() < size;
            }
    
            public Object next() {
                if(hasNext()) { // hasNext() di subList, che controlla indici
                    return it.next(); // next usa l'hasNext() di ListAdapter
                }
                else {
                    throw new NoSuchElementException();
                }
            }
    
            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }
    
            public Object previous() {
                if(hasPrevious()) {
                    return it.previous();
                }
                else {
                    throw new NoSuchElementException();
                }
            }
    
            public int nextIndex() {
                return it.nextIndex() - offset;
            }
    
            public int previousIndex() {
                return it.previousIndex() - offset;
            }
    
            public void remove() {
                it.remove();
                size--;
            }
    
            public void set(Object o) {
                it.set(o);
            }
    
            public void add(Object o) {
                it.add(o);
                size++;
            }
        }
    
        public Object remove(int index) {
            boundCheck(index);
            Object o = l.remove(offset + index);
            size--;
            return o;
        }
    
        public boolean remove(Object o) {
            if(o == null) {
                throw new NullPointerException();
            }
            HIterator it = iterator();
            while(it.hasNext()) {
                if(it.next().equals(o)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
    
        public boolean removeAll(HCollection c) {
            if(c == null) {
                throw new NullPointerException();
            }
            boolean flag = false;
            HIterator cit = c.iterator();
            while(cit.hasNext()) {
                if(remove(cit.next())) {
                    flag = true;
                }
            }
            return flag;
        }
    
        public boolean retainAll(HCollection c) {
            if(c == null) {
                throw new NullPointerException();
            }
            boolean flag = false;
            HIterator it = iterator();
            while(it.hasNext()) {
                Object o = it.next();
                if(!c.contains(o)) {
                    it.remove();
                    flag = true;
                }
            }
            return flag;
        }
    
        public Object set(int index, Object element) {
            if(element == null) {
                throw new NullPointerException();
            }
            boundCheck(index);
            return l.set(offset + index, element);
        }
    
        public int size() {
            return size;
        }
    }
    
    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the list and adds the elements returned by next() to the array. The length of the array is equal to the size of the list.
     */
    public Object[] toArray(){
        Object[] v = new Object[size()];
        HIterator it = iterator();
        for(int i = 0; it.hasNext(); i++) {
            v[i] = it.next();
        }
        return v;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the list and adds the elements returned by next() to the array. The length of the array is equal to the one of the array passed as the parameter. If it's length is greater than the list's size the elements whose index is greater or equal to the list's size are set to null.
     * @throws NullPointerException {@inheritDoc}
     */
    public Object[] toArray(Object[] a) {
        if(a == null) {
            throw new NullPointerException();
        }
        Object[] v;
        HIterator it = iterator();
        if(a.length >= size()) {
            v = new Object[a.length];
            int i = 0;
            while(it.hasNext()) {
                v[i++] = it.next();
            }
        }
        else {
            v = new Object[size()];
            for(int i = 0; i < size() && it.hasNext(); i++) {
                v[i] = it.next();
            }
        }
        return v;
    }

}