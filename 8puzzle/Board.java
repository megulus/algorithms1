/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private final int dimension;
    private final int total;
    private final int[][] tiles;
    private int blankX;
    private int blankY;
    private final int hamming;
    private final int manhattan;
    private Board cachedTwin;

    public Board(int[][] tiles) {
        if (tiles.length != tiles[0].length)
            throw new IllegalArgumentException("board must be n x n");
        dimension = tiles.length;
        total = dimension * dimension;
        this.tiles = new int[dimension][dimension];
        int count = 1;
        int ham = 0;
        int man = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
                else {
                    if (tiles[i][j] != count) ham++;
                    int goalX = (tiles[i][j] - 1) / dimension;
                    int goalY = (tiles[i][j] - 1) % dimension;
                    man += Math.abs(goalX - i) + Math.abs(goalY - j);
                }
                count++;
            }
        }
        this.hamming = ham;
        this.manhattan = man;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                str.append(String.format("%2d ", this.tiles[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        int count = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boolean isGoal = count == total ? this.tiles[i][j] == 0 : this.tiles[i][j] == count;
                if (!isGoal) return false;
                count++;
            }
        }
        return true;
    }

    public boolean equals(Object that) {
        if (that == null) return false;
        if (this == that) return true;
        if (that.getClass() != this.getClass()) return false;
        Board b = (Board) that;
        if (b.dimension() != this.dimension()) return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.tiles[i][j] != b.tiles[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        if (blankX - 1 >= 0) neighbors.add(this.swap(blankX, blankY, blankX - 1, blankY));
        if (blankX + 1 < dimension)
            neighbors.add(this.swap(blankX, blankY, blankX + 1, blankY));
        if (blankY - 1 >= 0) neighbors.add(this.swap(blankX, blankY, blankX, blankY - 1));
        if (blankY + 1 < dimension)
            neighbors.add(this.swap(blankX, blankY, blankX, blankY + 1));
        return neighbors;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.cachedTwin != null) return this.cachedTwin;
        else {
            int randomAi = StdRandom.uniform(dimension);
            int randomAj = StdRandom.uniform(dimension);
            int randomBi = StdRandom.uniform(dimension);
            int randomBj = StdRandom.uniform(dimension);
            int a = this.tiles[randomAi][randomAj];
            while (a == 0) {
                randomAi = StdRandom.uniform(dimension);
                randomAj = StdRandom.uniform(dimension);
                a = this.tiles[randomAi][randomAj];
            }
            int b = this.tiles[randomBi][randomBj];
            while (b == 0 || b == a) {
                randomBi = StdRandom.uniform(dimension);
                randomBj = StdRandom.uniform(dimension);
                b = this.tiles[randomBi][randomBj];
            }
            this.cachedTwin = this.swap(randomAi, randomAj, randomBi, randomBj);
            return this.cachedTwin;
        }
    }

    private Board swap(int ai, int aj, int bi, int bj) {
        int[][] copyTiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copyTiles[i][j] = this.tiles[i][j];
            }
        }
        int a = copyTiles[ai][aj];
        copyTiles[ai][aj] = copyTiles[bi][bj];
        copyTiles[bi][bj] = a;
        return new Board(copyTiles);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println("dimension " + board.dimension());
        StdOut.println(board.toString());
        StdOut.println("isGoal " + board.isGoal());
        // Board twin = board.twin();
        // StdOut.println(twin.toString());
        // StdOut.println("isGoal " + twin.isGoal());
        StdOut.println("board hamming " + board.hamming());
        // StdOut.println("twin hamming " + twin.hamming());
        // StdOut.println("board.equals(board)? " + board.equals(board));
        // StdOut.println("board.equals(twin)? " + board.equals(twin));
        Iterable<Board> neighbors = board.neighbors();
        for (Board b : neighbors) {
            StdOut.println(
                    "neighbor: " + "blankX, blankY (" + b.blankX + ", " + b.blankY + ") \n" + b
                            .toString());
        }
        // board.manhattan();
    }
}
