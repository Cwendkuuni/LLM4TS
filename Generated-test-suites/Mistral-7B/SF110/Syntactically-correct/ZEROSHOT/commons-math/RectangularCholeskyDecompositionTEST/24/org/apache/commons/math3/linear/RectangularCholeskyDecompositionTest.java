package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            new RectangularCholeskyDecomposition(matrix);
            assertFalse("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(root.getRowDimension(), root.getColumnDimension(), 2);
        assertEquals(root.getEntry(0, 0), 1.0, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 1), 1.0, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(0, 1), 0.0, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 0), 0.0, Precision.SAFE_EPSILON);
        assertEquals(decomposition.getRank(), 2);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            assertFalse("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(root.getRowDimension(), root.getColumnDimension(), 2);
        assertEquals(root.getEntry(0, 0), Precision.doubleValue(Math.sqrt(1.0)), Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 1), Precision.doubleValue(Math.sqrt(4.0)), Precision.SAFE_EPSILON);
        assertEquals(decomposition.getRank(), 2);
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            assertFalse("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
        assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-15);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(new RectangularCholeskyDecomposition(matrix, 1.0E-15).getRootMatrix());
        assertNull(new RectangularCholeskyDecomposition(matrix, 1.0E-15).getRootMatrix());
        assertNull(new RectangularCholeskyDecomposition(matrix, 1.0E-15).getRootMatrix());
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0E-15);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(root.getRowDimension(), root.getColumnDimension(), 2);
        assertEquals(root.getEntry(0, 0), Precision.doubleValue(Math.sqrt(1.0 + 4.0 / 3.0)), Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 1), Precision.doubleValue(Math.sqrt(5.0 / 3.0)), Precision.SAFE_EPSILON);
        assertEquals(decomposition.getRank(), 2);

        RealVector vector = matrix.operate(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}).transpose());
        RealVector result = root.operate(vector);
        assertEquals(result.getEntry(0), PrecisionUtils.round(1.0 + 4.0 / 3.0, 3), 3);
        assertEquals(result.getEntry(1), PrecisionUtils.round(1.0, 3), 3);
    }

}