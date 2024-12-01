package org.example;

import java.util.Random;
import java.util.stream.IntStream;

public class ParallelStreamMatrixMultiplication {
    public void execute(int n) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        double[][] c = new double[n][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = random.nextDouble();
                b[i][j] = random.nextDouble();
            }
        }

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        });
    }

    public static void main(String[] args) {
        new ParallelStreamMatrixMultiplication().execute(1024);
    }
}