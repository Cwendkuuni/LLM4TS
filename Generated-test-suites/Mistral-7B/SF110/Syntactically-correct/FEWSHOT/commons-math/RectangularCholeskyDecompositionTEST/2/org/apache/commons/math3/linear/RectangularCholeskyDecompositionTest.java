package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteException() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, -1.0);
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDiagonalElement() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 0.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, -1.0);
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, root.getData());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testSmallMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 3.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.7071067811865476, 0.408248290463863},}, root.getData());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(0, 2, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(1, 2, 1.0);
        matrix.setEntry(2, 0, 1.0);
        matrix.setEntry(2, 1, 1.0);
        matrix.setEntry(2, 2, 2.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.408248290463863, 1.0, 0.0}, {0.408248290463863, 0.408248290463863, 1.0}}, root.getData());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 0.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 0.0);
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-12);
            assertFalse("NonPositiveDefiniteMatrixException should have been thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertNull(e.getCause());
            assertEquals(0, e.getRank());
            assertEquals(0.0, e.getDeterminant(), Precision.SAFE_EPSILON);
        }
    }

    @Test
    public void testSmallDeterminant() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 7.0);
        matrix.setEntry(2, 1, 8.0);
        matrix.setEntry(2, 2, 9.0);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.3535533905932738, 0.8164965809277262, 0.0}, {0.2777777777777777, -0.4444444444444444, 0.8164965809277262}}, root.getData());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testLargeMatrix() {
        int size = 10;
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(size, size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix.setEntry(i, j, PrecisionUtils.randomDouble(1.0, 10.0));
            }
        }
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(size, root.getRowDimension());
        assertEquals(size, root.getColumnDimension());
        assertEquals(size, decomposition.getRank());
    }

    @Test
    public void testLargeMatrixWithSingularValuesBelowSmallThreshold() {
        int size = 10;
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(size, size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix.setEntry(i, j, PrecisionUtils.randomDouble(1.0, 10.0));
            }
        }
        double smallThreshold = 1e-12;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, smallThreshold);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(size, root.getRowDimension());
        assertEquals(size, root.getColumnDimension());
        assertEquals(size, decomposition.getRank());
    }
}