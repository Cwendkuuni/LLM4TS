package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.5, 0.5}, {-0.5, 0.5}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.5, -0.5}, {0.5, 0.5}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.5, -0.5, 1.5}, {-0.5, 2.5, -0.5}, {1.5, -0.5, 1.5}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformerWithNullMatrix() {
        new SchurTransformer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testTransformerWithAlreadyTransformedMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.5, -0.5, 1.5}, {-0.5, 2.5, -0.5}, {1.5, -0.5, 1.5}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{10, 1}, {1, 10}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = 11 * Math.sqrt(2);
        double actualNorm = transformer.getNorm();
        assertEquals(expectedNorm, actualNorm, 0.001);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = {{1, 0, 1}, {0, 2, -1}, {1, -1, 3}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        int expectedIl = 1;
        int actualIl = transformer.findSmallSubDiagonalElement(0, transformer.getNorm());
        assertEquals(expectedIl, actualIl);
    }

    @Test
    public void testComputeShift() {
        double[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        ShiftInfo expectedShift = new ShiftInfo();
        expectedShift.x = 1.732;
        expectedShift.y = 1.732;
        expectedShift.w = -1.732 * 1.732;
        ShiftInfo actualShift = transformer.computeShift(1, 2, 10, new ShiftInfo());
        assertEquals(expectedShift.x, actualShift.x, 0.001);
        assertEquals(expectedShift.y, actualShift.y, 0.001);
        assertEquals(expectedShift.w, actualShift.w, 0.001);
    }

    @Test
    public void testInitQRStep() {
        double[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        int expectedIm = 1;
        int actualIm = transformer.initQRStep(1, 2, new ShiftInfo(), new double[3]);
        assertEquals(expectedIm, actualIm);
    }
}