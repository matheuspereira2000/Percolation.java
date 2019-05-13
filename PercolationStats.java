import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int N;
    private int T;
    private double[] p;
    // Perform T independent experiments (Monte Carlo simulations) on an 
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N or T cannot equal 0");
        }
        this.N = N;
        this.T = T;
        this.p = new double[T];
        for (int i = 0; i < T; i++) { 
            Percolation a = new Percolation(N);
            while (!a.percolates()) { 
                a.open(StdRandom.uniform(0, N), StdRandom.uniform(0, N));
            }
            p[i] = ((double) a.numberOfOpenSites() / ((double) N * N));
        }
    }
    
    // Sample mean of percolation threshold.
    public double mean() {
        double x = 0;
        for (int i = 0; i < this.T; i++) {
            x += p[i];
        }
        x /= (double) this.T;
        return x;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        double x = 0;
        for (int i = 0; i < T; i++) {
            x += (p[i] - this.mean()) * (p[i] - this.mean());
        }
        x /= (this.T - 1);
        x = Math.sqrt(x);
        return x;
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        double quotient = 1.96 * this.stddev();
        double divisor = (double) Math.sqrt(this.T);
        double x = this.mean() - (quotient / divisor);
        return x;
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        double quotient = 1.96 * this.stddev();
        double divisor = (double) Math.sqrt(this.T);
        double x = this.mean() + (quotient / divisor);
        return x;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
