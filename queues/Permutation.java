/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if (args[0] != null) {
            RandomizedQueue<String> rq = new RandomizedQueue<String>();
            int k = Integer.parseInt(args[0]);
            int i = 0;
            while (!StdIn.isEmpty()) {
                String inStr = StdIn.readString();
                if (i < k) {
                    rq.enqueue(inStr);
                }
                else {
                    double random = StdRandom.uniform();
                    if (random <= k * 1.0 / i) {
                        rq.dequeue();
                        rq.enqueue(inStr);
                    }
                }
                i++;
            }
            for (int j = 0; j < k; j++) {
                StdOut.println(rq.dequeue() + " ");
            }
        }
    }
}




