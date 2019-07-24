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
            for (int j = 0; j < len; j++) {
                // make sure indices are distinct
                if (i != j) {
                    // the sum of integers at i and j in a will be the key
                    int key = this.a[i] + this.a[j];
                    // value will be the indices themselves
                    int[] value = new int[] { i, j };
                    // sort the array so that we will not be comparing [1,2] to [2,1]
                    Arrays.sort(value);
                    // check whether key already in the hashmap
                    if (hm.containsKey(key)) {
                        int[] existing = hm.get(key);
                        // check whether they are the same
                        if (!existing.equals(value)) {
                            // still have to check whether all 4 indices distinct - better way to do this?
                            if ((existing[0] != value[0]) && (existing[0] != value[1]) && (
                                    existing[1] != value[0]) && (existing[1] != value[1])) {
                                // indices are distinct -- add to results
                                int[] newresult = new int[] {
                                        value[0], value[1], existing[0], existing[1]
                                };
                                results.add(newresult);
                            }
                            // indices are not distinct, add new value to hashmap
                            hm.put(key, value);
                        }
                    }
                    // key does not exist in hashmap, add new value to hashmap
                    hm.put(key, value);
                }
                // i == j - ignore this pair
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
