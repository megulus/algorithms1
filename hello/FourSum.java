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
import java.util.HashMap;

public class FourSum {

    private int[] a;
    private ArrayList<int[]> results = new ArrayList<>();
    private int len;
    private HashMap<Integer, int[]> hm = new HashMap<>();

    public FourSum(int[] a) {
        if (a == null) throw new IllegalArgumentException("no null arrays!");
        this.a = a;
        this.len = this.a.length;
        findFourSums();
    }

    public void findFourSums() {
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                int key = this.a[i] + this.a[j];
                int[] value = new int[] { i, j };
                if (hm.containsKey(key)) {
                    int[] existing = hm.get(key);
                    if ((existing[0] != value[0]) && (existing[0] != value[1]) && (
                            existing[1] != value[0]) && (existing[1] != value[1])) {
                        int[] newresult = new int[] {
                                existing[0], existing[1], value[0], value[1]
                        };
                        results.add(newresult);
                    }
                }
                hm.put(key, value);
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
