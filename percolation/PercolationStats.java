/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] percolationThresholds;
    private int next = 0;
    private int numTrials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Arguments must be > 0");
        numTrials = trials;
        percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            boolean percolates = false;
            double threshold;
            while (!percolates) {
                int p = StdRandom.uniform(1, n + 1);
                int q = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(p, q)) {
                    perc.open(p, q);
                    if (perc.percolates()) {
                        percolates = true;
                        threshold = (perc.numberOfOpenSites() * 1.0) / (n * n);
                        percolationThresholds[next] = threshold;
                        next += 1;
                    }
                }
            }
        }
    }

    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    public double stddev() {
        if (numTrials == 1) return Double.NaN;
        return StdStats.stddev(percolationThresholds);
    }

    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(numTrials));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(numTrials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo()
                               + ", " + percStats.confidenceHi() + "]");
    }
}
