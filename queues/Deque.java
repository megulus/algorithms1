/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int numNodes;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        numNodes = 0;
    }

    public boolean isEmpty() {
        return numNodes == 0;
    }

    public int size() {
        return numNodes;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null values cannot be added to the deque");
        boolean isFirstItem = isEmpty();
        Node newfirst = new Node();
        newfirst.item = item;
        newfirst.prev = null;
        Node oldfirst = first;
        first = newfirst;
        first.next = oldfirst;
        if (isFirstItem) {
            last = first;
        }
        else {
            oldfirst.prev = first;
        }
        numNodes += 1;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null values cannot be added to the deque");
        boolean isFirstItem = isEmpty();
        Node newlast = new Node();
        newlast.item = item;
        newlast.next = null;
        Node oldlast = last;
        last = newlast;
        last.prev = oldlast;
        if (isFirstItem) {
            first = last;
        }
        else {
            oldlast.next = last;
        }
        numNodes += 1;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Cannot remove an item from an empty deque");
        Item item = first.item;
        if (numNodes == 1) {
            last = null;
            first = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        numNodes -= 1;
        return item;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Cannot remove an item from an empty deque");
        Item item = last.item;
        if (numNodes == 1) {
            first = null;
            last = null;
        }
        else {
            Node newlast = last.prev;
            newlast.next = null;
            last = newlast;
        }
        numNodes -= 1;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("The deque is empty");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "The remove() method is not supported on this iterator implementation.");
        }
    }

    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();
        StdOut.println("d.size() " + d.size() + " d.isEmpty() " + d.isEmpty());
        d.addFirst(4);
        StdOut.println("d.addFirst(4)");
        StdOut.println("d.size() " + d.size());
        int last = d.removeLast();
        StdOut.println("d.removeLast()");
        StdOut.println("last " + last + " d.isEmpty() " + d.isEmpty() + " d.size() " + d.size());
        d.addLast(6);
        StdOut.println("d.addLast(6)");
        StdOut.println("d.size() " + d.size());
        int first = d.removeFirst();
        StdOut.println("d.removeFirst()");
        StdOut.println("first " + first + " d.isEmpty() " + d.isEmpty());
        d.addFirst(8);
        StdOut.println("d.addFirst(8)");
        d.addFirst(10);
        StdOut.println("d.addFirst(10)");
        StdOut.println("d.size() " + d.size());
        int newlast = d.removeLast();
        StdOut.println("d.removeLast()");
        StdOut.println(
                "newlast " + newlast + " d.isEmpty() " + d.isEmpty() + " d.size() " + d.size());
        Iterator<Integer> it = d.iterator();
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.addFirst(17) ");
        d.addFirst(17);
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("d.size() " + d.size());
        StdOut.println("it.hasNext() " + it.hasNext());
    }
}
