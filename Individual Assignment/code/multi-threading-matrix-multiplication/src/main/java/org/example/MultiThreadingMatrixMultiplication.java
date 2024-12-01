package org.example;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadingMatrixMultiplication {

    private double [][] a;
    private double [][] b;
    private double [][] c;
    private int n;
    private int numThreads;

    public MultiThreadingMatrixMultiplication(int n, int threads) {
        this.a = new double[n][n];
        this.b = new double[n][n];
        this.c = new double[n][n];
        this.n = n;
        this.numThreads = threads;
    }

    public void execute() {

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = random.nextDouble();
                this.b[i][j] = random.nextDouble();
            }
        }

        double[][] c = new double[n][n];

        ExecutorService executorService = Executors.newFixedThreadPool(this.numThreads);


        for (int i = 0; i < n; i++) {
            final int row = i;
            executorService.submit(() -> multiplyRow(row));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private  void multiplyRow(int row) {
        // Print the current thread name
        for (int j = 0; j < this.n; j++) {
            for (int k = 0; k < this.n; k++) {
                this.c[row][j] += a[row][k] * b[k][j];
            }
        }
    }
}