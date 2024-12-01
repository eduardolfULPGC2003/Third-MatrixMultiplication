package org.example;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.Random;
import java.util.Vector;

public class VectorizedMatrixMultiplication {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;
    public void execute(int n) {

        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = random.nextDouble();
                b[i][j] = random.nextDouble();
            }
        }

        double[][] c = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[] columnB = new double[n];
                for (int k = 0; k < n; k++) {
                    columnB[k] = b[k][j]; // Extrae la columna de b
                }
                c[i][j] = vectorizedDotProduct(a[i], columnB);
            }
        }
    }

    private static double vectorizedDotProduct(double[] row, double[] column) {
        DoubleVector sumVector = DoubleVector.zero(SPECIES);

        int i = 0;
        int length = SPECIES.length();

        // Procesa los datos en bloques de longitud igual al vector (e.g., 256 bits / 8 bytes)
        for (; i < row.length - length; i += length) {
            DoubleVector va = DoubleVector.fromArray(SPECIES, row, i);
            DoubleVector vb = DoubleVector.fromArray(SPECIES, column, i);
            sumVector = va.fma(vb, sumVector);  // Fused Multiply-Add: suma (va * vb) a sumVector
        }

        // Resta los elementos sobrantes
        double sum = sumVector.reduceLanes(VectorOperators.ADD);
        for (; i < row.length; i++) {
            sum += row[i] * column[i];
        }

        return sum;
    }

    public static void main(String[] args) {
        new VectorizedMatrixMultiplication().execute(1024);
    }
}