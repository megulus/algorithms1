/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private boolean boardSolved = false;
    private int moves = -1;
    private LinkedList<Board> solution = new LinkedList<>();


    // find a solution to the initial board using the A* algorithm
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("must supply a board to the Solver");
        boolean twinSolved = false;
        SearchNode initNode = new SearchNode(null, initial);
        SearchNode twinNode = new SearchNode(null, initial.twin());
        MinPQ<SearchNode> solver = new MinPQ<SearchNode>(initNode.priorityOrder());
        MinPQ<SearchNode> twinSolver = new MinPQ<SearchNode>(twinNode.priorityOrder());
        solver.insert(initNode);
        twinSolver.insert(twinNode);
        while (!boardSolved && !twinSolved) {
            SearchNode searchNode = solver.delMin();
            if (!searchNode.board().isGoal()) {
                Iterable<Board> neighbors = searchNode.board().neighbors();
                for (Board a : neighbors) {
                    SearchNode newNode = new SearchNode(searchNode, a);
                    if (!newNode.equalsPrevious()) {
                        solver.insert(newNode);
                    }
                }
            }
            else if (searchNode.board().isGoal()) {
                boardSolved = true;
                solution.add(searchNode.board());
                buildSolution(searchNode);
            }
            SearchNode twinSearchNode = twinSolver.delMin();
            if (!twinSearchNode.board().isGoal()) {
                Iterable<Board> twinNeighbors = twinSearchNode.board().neighbors();
                for (Board b : twinNeighbors) {
                    SearchNode newTwinNode = new SearchNode(twinSearchNode, b);
                    if (!newTwinNode.equalsPrevious()) {
                        twinSolver.insert(newTwinNode);
                    }
                }
            }
            else if (twinSearchNode.board().isGoal()) {
                twinSolved = true;
            }
        }
    }

    private void buildSolution(SearchNode lastNode) {
        while (lastNode.previous() != null) {
            solution.addFirst(lastNode.previous().board());
            lastNode = lastNode.previous();
        }
        this.moves = solution.size() - 1;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return boardSolved;
    }

    // min number of moves to solve the initial board
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) return this.solution;
        return null;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode {
        private final Board board;
        private final SearchNode previous;
        private final int moves;
        private final int priority;

        public SearchNode(SearchNode previous, Board board) {
            this.board = board;
            this.previous = previous;
            this.moves = this.previous == null ? 0 : previous.moves() + 1;
            this.priority = this.board.manhattan() + this.moves;
        }

        public Comparator<SearchNode> priorityOrder() {
            return new NodePriorityComparator();
        }

        private class NodePriorityComparator implements Comparator<SearchNode> {
            public int compare(SearchNode a, SearchNode b) {
                return Integer.compare(a.priority(), b.priority());
            }
        }

        public boolean equalsPrevious() {
            if (this.previous() == null || this.previous().previous() == null) return false;
            // if (this.previous().previous() == null)
            //     return this.board.equals(this.previous().board());
            return this.board.equals(this.previous().previous().board());
        }

        public SearchNode previous() {
            return this.previous;
        }

        public int moves() {
            return this.moves;
        }

        public Board board() {
            return this.board;
        }

        public int priority() {
            return priority;
        }
    }
}
