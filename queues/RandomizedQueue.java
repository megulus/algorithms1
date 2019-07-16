/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int numElements;
    private int first;
    private int last;

    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        numElements = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return numElements == 0;
    }

    public int size() {
        return numElements;
    }

    private void resizeQueue(int newLength) {
        Item[] temp = (Item[]) new Object[newLength];
        Iterator<Item> it = iterator();
        int i = 0;
        while (it.hasNext()) {
            temp[i++] = it.next();
        }
        queue = temp;
        first = 0;
        last = numElements;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null to the queue");
        if (numElements == queue.length) resizeQueue(2 * queue.length);
        queue[last++] = item;
        numElements++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("The queue is empty");
        int randomIndex = StdRandom.uniform(last);
        Item item = queue[randomIndex];
        queue[randomIndex] = queue[last - 1];
        last--;
        numElements--;
        if (numElements > 0 && numElements == (queue.length / 4)) {
            resizeQueue((queue.length / 2));
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("The queue is empty");
        int randomIndex = StdRandom.uniform(last);
        return queue[randomIndex];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> rqCopy = new RandomizedQueue<Item>();

        private RandomizedQueueIterator() {
            for (int i = first; i < numElements; i++) {
                rqCopy.enqueue(queue[i]);
            }
        }

        public boolean hasNext() {
            return !rqCopy.isEmpty();
        }

        public Item next() {
            if (isEmpty()) throw new NoSuchElementException("The queue is empty");
            return rqCopy.dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "The remove() method is not supported on this iterator implementation.");
        }
    }


    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        StdOut.println("rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size());
        StdOut.println("rq.enqueue(3)");
        rq.enqueue(3);
        StdOut.println("rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size());
        StdOut.println("rq.enqueue(5)");
        rq.enqueue(5);
        StdOut.println(
                "rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size() + " rq.sample() " + rq
                        .sample());
        StdOut.println("rq.enqueue(8)");
        rq.enqueue(8);
        StdOut.println("rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size());
        StdOut.println("rq.enqueue(11)");
        rq.enqueue(11);
        StdOut.println("rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size());
        StdOut.println("rq.enqueue(6)");
        rq.enqueue(6);
        StdOut.println("rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size());
        StdOut.println("rq.enqueue(7)");
        rq.enqueue(7);
        StdOut.println(
                "rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size() + " rq.sample() " + rq
                        .sample());
        StdOut.println("rq.dequeue()");
        int item = rq.dequeue();
        StdOut.println("item " + item + " rq.isEmpty " + rq.isEmpty() + " rq.size() " + rq.size()
                               + " rq.sample() " + rq.sample());
        Iterator<Integer> it = rq.iterator();
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
        StdOut.println("it.next() " + it.next());
        StdOut.println("rq.size() " + rq.size());
        StdOut.println("it.hasNext() " + it.hasNext());
    }
}
