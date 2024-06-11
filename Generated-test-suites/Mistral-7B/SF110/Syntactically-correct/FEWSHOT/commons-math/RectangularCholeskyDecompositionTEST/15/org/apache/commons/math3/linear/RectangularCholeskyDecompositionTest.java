package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.449489742783, 0.816496580925, 0.0}, {0.0, 1.61803398875, 3.0}});
        assertArrayEquals(expected.getData(), rcd.getRootMatrix().getData(), 0.00001);
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testDecompositionSmallMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDeterminant(), Precision.SAFE_EPSILON);
            assertEquals(1, e.getRow());
        }
    }

    @Test
    public void testDecompositionNegativeMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDeterminant(), Precision.SAFE_EPSILON);
            assertEquals(0, e.getRow());
        }
    }

    @Test
    public void testDecompositionIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertArrayEquals(expected.getData(), rcd.getRootMatrix().getData(), 0.00001);
        assertEquals(2, rcd.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNegativeDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.449489742783, 0.816496580925, 0.0}, {0.0, 1.61803398875, 3.0}});
        assertNotNull(rcd.getRootMatrix());
        assertArrayEquals(expected.getData(), rcd.getRootMatrix().getData(), 0.00001);
    }
}