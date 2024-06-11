package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 4.0}, {3.0, 4.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix); //should throw NonPositiveDefiniteMatrixException
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.4495, 0.8165, 0.0}, {0.4495, 0.0, 0.8165}});
        RealMatrix actual = rcd.getRootMatrix();
        assertTrue(PrecisionUtils.isClose(expected, actual, 1e-12));
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        int expected = 3;
        int actual = rcd.getRank();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition rcd = null;
        try {
            rcd = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            //expected since the matrix is not positive definite
        }
        assertTrue(rcd == null);

        rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.7071, 0.7071}});
        RealMatrix actual = rcd.getRootMatrix();
        assertTrue(PrecisionUtils.isClose(expected, actual, 1e-12));
        int rank = rcd.getRank();
        assertEquals(2, rank);
    }

    @Test
    public void testSwappingElements() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        int[] expectedIndex = {1, 0};
        int[] actualIndex = new int[2];
        System.arraycopy(rcd.getIndex(), 0, actualIndex, 0, 2);
        assertArrayEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testNegativeElements() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
            fail("NonPositiveDefiniteMatrixException should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            //expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.4495, 0.8165, 0.0}, {0.4495, 0.0, 0.8165}});
        RealMatrix actual = rcd.getRootMatrix();
        assertTrue(PrecisionUtils.isClose(expected, actual, 1e-12));
        int rank = rcd.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testCalculateRootMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.4495, 0.8165, 0.0}, {0.4495, 0.0, 0.8165}});
        RealMatrix actual = rcd.getRootMatrix();
        assertTrue(PrecisionUtils.isClose(expected, actual, 1e-12));
    }

    @Test
    public void testCalculateRank() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Precision.EPSILON);
        int expected = 3;
        int actual = rcd.getRank();
        assertEquals(expected, actual);
    }
}