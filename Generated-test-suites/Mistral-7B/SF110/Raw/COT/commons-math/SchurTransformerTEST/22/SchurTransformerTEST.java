package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, 0.85705888, 0.0}, {-0.85705888, 0.53846931, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, -0.85705888, 0.0}, {0.85705888, 0.53846931, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 4.0}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransformWithDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransformWithSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 2.0}, {2.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.74175237, 0.45236233, 0.0}, {-0.45236233, 0.74175237, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{0.74175237, 0.45236233, 2.0}, {0.45236233, 0.74175237, 2.0}, {-2.0, -2.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransformWithMatrixCloseToSingular() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.001, 1.0}, {1.0, 0.001}});
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
        assertNull(transformer.getP());
        assertNull(transformer.getT());
    }

    @Test
    public void testTransformWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
        assertNull(transformer.getP());
        assertNull(transformer.getT());
    }

    @Test
    public void testFindSmallSubDiagonalElementWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer transformer = new SchurTransformer(matrix.transpose().add(matrix).divide(2.0));
        int actualIndex = transformer.findSmallSubDiagonalElement(0, 0.0);
        assertEquals(0, actualIndex);
    }

    @Test
    public void testFindSmallSubDiagonalElementWithDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int actualIndex = transformer.findSmallSubDiagonalElement(0, 0.0);
        assertEquals(0, actualIndex);
    }

    @Test
    public void testFindSmallSubDiagonalElementWithZeroDiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int actualIndex = transformer.findSmallSubDiagonalElement(0, 0.0);
        assertEquals(1, actualIndex);
    }

    @Test
    public void testFindSmallSubDiagonalElementWithLargeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 4.0, 5.0}, {3.0, 4.0, 1.0, 6.0}, {4.0, 5.0, 6.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix.transpose().add(matrix).divide(2.0));
        int actualIndex = transformer.findSmallSubDiagonalElement(0, 0.0);
        int expectedIndex = 0;
        assertEquals(expectedIndex, actualIndex);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMatrixNotConverging() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
    }

    @Test
    public void testTransformWithMatrixConverging() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, 0.85705888, 0.0}, {-0.85705888, 0.53846931, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = Math.pow(3.0, 2.0) + Math.pow(4.0, 2.0);
        double actualNorm = transformer.getNorm();
        assertEquals(expectedNorm, actualNorm, 0.0);
    }
}