/* *****************************************************************************
 *  Name:    Alan Turing
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description:  Prints 'Hello, World' to the terminal window.
 *                By tradition, this is everyone's first program.
 *                Prof. Brian Kernighan initiated this tradition in 1974.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class SocialNetwork {

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        boolean connected = false;

        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);
        while (!in.isEmpty() && !connected) {
            int friend1 = in.readInt();
            int friend2 = in.readInt();
            int timestamp = in.readInt();
            if (!uf.connected(friend1, friend2)) {
                uf.union(friend1, friend2);
            }
            if (uf.count() == 1) {
                StdOut.println("connected at " + timestamp);
                connected = true;
            }
        }
    }
}
