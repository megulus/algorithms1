/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridDimension;
    private int gridSize;
    private boolean[] openSites;
    private int numberOfOpenSites = 0;
    private WeightedQuickUnionUF wquf;
    private int top;
    private int bottom;


    public Percolation(int n) {
        gridDimension = n;
        gridSize = n * n;
        openSites = new boolean[gridSize + 2];
        top = 0;
        bottom = (n * n) + 1;
        openSites[top] = true;
        openSites[bottom] = true;
        wquf = new WeightedQuickUnionUF(gridSize + 2);
    }

    private int xyToID(int row, int col) {
        return (gridDimension * (row - 1)) + col;
    }

    private boolean indicesValid(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridDimension || col > gridDimension) return false;
        return true;
    }

    private void addToOpenSites(int row, int col) {
        int translatedSite = xyToID(row, col);
        if (!isOpen(row, col)) {
            openSites[translatedSite] = true;
            numberOfOpenSites += 1;
        }
    }

    private int[][] findNeighbors(int row, int col) {
        int count = 0;
        boolean[] validNeighbors = { false, false, false, false };
        int[][] possibleNeighbors = { { row, col - 1 }, { row, col + 1 }, { row - 1, col }, { row + 1, col } };
        for (int i = 0; i < possibleNeighbors.length; i++) {
            int[] indices = possibleNeighbors[i];
            if (indicesValid(indices[0], indices[1])) {
                validNeighbors[i] = true;
                count += 1;
            }
        }
        int[][] actualNeighbors = new int[count][2];
        int next = 0;
        for (int i = 0; i < validNeighbors.length; i++) {
            if (validNeighbors[i]) {
                actualNeighbors[next] = possibleNeighbors[i];
                next += 1;
            }
        }
        return actualNeighbors;
    }

    public void open(int row, int col) {
        if (!indicesValid(row, col))
            throw new IllegalArgumentException("Error: coordinates cannot be <= 0 or > n");
        addToOpenSites(row, col);
        int[][] neighbors = findNeighbors(row, col);
        int p = xyToID(row, col);
        if (row == 1) wquf.union(p, top);
        if (row == gridDimension) wquf.union(p, bottom);
        for (int[] ar : neighbors) {
            if (isOpen(ar[0], ar[1])) {
                int q = xyToID(ar[0], ar[1]);
                wquf.union(p, q);
            }
        }
    }


    public boolean isOpen(int row, int col) {
        if (!indicesValid(row, col)) {
            throw new IllegalArgumentException("Error: coordinates cannot be <= 0 or > n");
        }
        int translatedSite = xyToID(row, col);
        return openSites[translatedSite];
    }

    public boolean isFull(int row, int col) {
        if (!indicesValid(row, col)) {
            throw new IllegalArgumentException("Error: coordinates cannot be <= 0");
        }
        int translatedsite = xyToID(row, col);
        return wquf.connected(top, translatedsite);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return wquf.connected(top, bottom);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            int n = Integer.parseInt(args[0]);
            Percolation perc = new Percolation(n);
            perc.open(1, 1);
            StdOut.println("percolates? " + perc.percolates());
            perc.open(1, 2);
            StdOut.println("1,1 & 1,2 connected? " + perc.wquf.connected(perc.xyToID(1, 1), perc.xyToID(1, 2)));
            StdOut.println("percolates? " + perc.percolates());
            perc.open(2, 2);
            StdOut.println("1,2 & 2,2 connected? " + perc.wquf.connected(perc.xyToID(2, 2), perc.xyToID(1, 2)));
            StdOut.println("percolates? " + perc.percolates());
            perc.open(3, 2);
            StdOut.println("percolates? " + perc.percolates());
            perc.open(3, 3);
            StdOut.println("percolates? " + perc.percolates());
            perc.open(4, 3);
            StdOut.println("percolates? " + perc.percolates());
            StdOut.println("1,1 connected to top? " + perc.wquf.connected(perc.xyToID(1, 1), perc.top));
            StdOut.println("4, 3 connected to bottom? " + perc.wquf.connected(perc.xyToID(4, 3), perc.bottom));
        }
    }
}
