package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerThrowsExceptionForNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetPMatrix() {
        double[][] inputMatrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pMatrix = transformer.getP();
        assertNotNull(pMatrix);
        assertEquals(inputMatrix.length, pMatrix.getRowDimension());
        assertEquals(inputMatrix[0].length, pMatrix.getColumnDimension());
    }

    @Test
    public void testGetTMatrix() {
        double[][] inputMatrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix tMatrix = transformer.getT();
        assertNotNull(tMatrix);
        assertEquals(inputMatrix.length, tMatrix.getRowDimension());
        assertEquals(inputMatrix[0].length, tMatrix.getColumnDimension());
    }

    @Test
    public void testTransformMethod() {
        double[][] inputMatrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPMatrix = MatrixUtils.createRealMatrix(new double[][]{{0.743205483923884, 0.135079551701884, 0.631223983568484},
                {0.612801536313224, 0.743205483923884, -0.394653635095661},
                {-0.612801536313224, 0.135079551701884, 0.743205483923884}});
        RealMatrix expectedTMatrix = MatrixUtils.createRealMatrix(new double[][]{{5.49123733641842, 3.03186261605703, -1.14078201660802},
                {3.03186261605703, 2.33138559261106, 0.612801536313224},
                {-1.14078201660802, 0.612801536313224, 0.394653635095661}});

        RealMatrix actualPMatrix = transformer.getP();
        RealMatrix actualTMatrix = transformer.getT();

        assertArrayEquals(expectedPMatrix.getData(), actualPMatrix.getData(), 0.0001);
        assertArrayEquals(expectedTMatrix.getData(), actualTMatrix.getData(), 0.0001);
    }

    @Test
    public void testTransformWithSmallMatrices() {
        double[][] inputMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPMatrix = MatrixUtils.createRealMatrix(new double[][]{{0.707106781186548, -0.707106781186548},
                {0.707106781186548, 0.707106781186548}});
        RealMatrix expectedTMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.61803398875, 0.55470023729},
                {0.55470023729, 1.61803398875}});

        RealMatrix actualPMatrix = transformer.getP();
        RealMatrix actualTMatrix = transformer.getT();

        assertArrayEquals(expectedPMatrix.getData(), actualPMatrix.getData(), 0.0001);
        assertArrayEquals(expectedTMatrix.getData(), actualTMatrix.getData(), 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformThrowsMaxCountExceededException() {
        double[][] inputMatrix = new double[1000][1000];
        for (int i = 0; i < inputMatrix.length; ++i) {
            for (int j = 0; j < inputMatrix[i].length; ++j) {
                inputMatrix[i][j] = Math.random();
            }
        }
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}