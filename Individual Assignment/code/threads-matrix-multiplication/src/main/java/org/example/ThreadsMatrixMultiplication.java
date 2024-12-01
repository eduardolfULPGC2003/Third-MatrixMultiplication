package org.example;

import java.util.Random;

public class ThreadsMatrixMultiplication {

    private double [][] a;
    private double [][] b;
    private double [][] c;
    private int n;

    public ThreadsMatrixMultiplication(int n) {
        this.a = new double[n][n];
        this.b = new double[n][n];
        this.c = new double[n][n];
        this.n = n;
    }

    public void execute() {

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = random.nextDouble();
                this.b[i][j] = random.nextDouble();
            }
        }

        // Create and launch threads for each row of the matrix
        Thread[] threads = new Thread[this.n];
        for (int i = 0; i < this.n; i++) {
            final int row = i;
            threads[i] = new Thread(() -> multiplyRow(row));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < this.n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void multiplyRow(int row) {
        for (int j = 0; j < this.n; j++) {
            for (int k = 0; k < this.n; k++) {
                this.c[row][j] += this.a[row][k] * this.b[k][j];
            }
        }
    }

    public static void main(String[] args) {
        new ThreadsMatrixMultiplication(1024).execute();
    }
}