import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {
    private int N;
    private boolean[][] a;
    private WeightedQuickUnionUF uf;
    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N cannot equal 0");
        }
        this.N = N;
        this.a = new boolean[N][N];
        //Initializing everything in array to False = Blocked
        for (int i = 0; i < N; i++) { 
            for (int j = 0; j < N; j++) { 
                this.a[i][j] = false;
            }
        }
        this.uf = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < N; i++) { 
            this.uf.union(0, i);
        }
    } 

    // Open site (i, j) if it is not open already.
    public void open(int i, int j) {
        this.a[i][j] = true;
        //checking surrounding area if
        // goes out of bounds and if its open
        if ((i - 1) >= 0 && isOpen(i - 1, j)) { 
            this.uf.union(this.encode(i, j), this.encode(i - 1, j));
        }
        if ((i + 1) < N && isOpen(i + 1, j)) { 
            this.uf.union(this.encode(i, j), this.encode(i + 1, j));
        }
        if ((j + 1) < N && isOpen(i, j + 1)) { 
            this.uf.union(this.encode(i, j), this.encode(i, j + 1));
        }
        if ((j - 1) >= 0 && isOpen(i, j - 1)) { 
            this.uf.union(this.encode(i, j), this.encode(i, j - 1));
        }
        //Connecting to source
        if (i == 0) { 
            this.uf.union(0, this.encode(i, j));
        }
        //Connecting to sink
        if (i == N - 1) {
            if (isFull(i - 1, j)) {
                this.uf.union(this.encode(i, j), N * N + 1);
            }
            if (isFull(i, j + 1)) {
                this.uf.union(this.encode(i, j), N * N + 1);
            }
            if (isFull(i, j - 1)) {
                this.uf.union(this.encode(i, j), N * N + 1);
            }
        }
        
        
    }

    // Is site (i, j) open?
    public boolean isOpen(int i, int j) {
        return this.a[i][j];
    }

    // Is site (i, j) full?
    public boolean isFull(int i, int j) {
        return this.uf.connected(0, this.encode(i, j));
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < this.a.length; i++) { 
            for (int j = 0; j < this.a.length; j++) {
                if (isOpen(i, j)) { 
                    count++;
                }
            }
        }
        return count;
    }

    // Does the system percolate?
    public boolean percolates() {
        for (int i = 0; i < this.a.length; i++) {
            if (isFull(this.N - 1, i)) { 
                return true;
            }
        }
        return false;
    }

    // An integer ID (1...N) for site (i, j).
    private int encode(int i, int j) {
        return (i * this.N) + j + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }
        
        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
