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

import java.util.ArrayList;
import java.util.Arrays;

public class FourSum {

    private int[] a;
    private ArrayList<int[]> results = new ArrayList<>();
    private int len;

    public FourSum(int[] a) {
        if (a == null) throw new IllegalArgumentException("no null arrays!");
        this.a = a;
        this.len = this.a.length;
        findFourSums();
    }

    public void findFourSums() {
        for (int i = 0; i <= len - 4; i++) {
            if (Integer.hashCode(this.a[i] + this.a[i + 1]) == Integer
                    .hashCode(this.a[i + 2] + this.a[i + 3])) {
                int[] sum = new int[] { i, i + 1, i + 2, i + 3 };
                results.add(sum);
            }
        }
    }

    public ArrayList<int[]> getResults() {
        return results;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int[] a = in.readAllInts();
        FourSum fs = new FourSum(a);
        ArrayList<int[]> results = fs.getResults();
        StdOut.println("results length " + results.size());
        for (int[] res : results) {
            StdOut.println("results array " + Arrays.toString(res));
            for (int ea : res) {
                StdOut.print("values: " + a[ea] + " ");
            }
            StdOut.println();
        }
    }
}
