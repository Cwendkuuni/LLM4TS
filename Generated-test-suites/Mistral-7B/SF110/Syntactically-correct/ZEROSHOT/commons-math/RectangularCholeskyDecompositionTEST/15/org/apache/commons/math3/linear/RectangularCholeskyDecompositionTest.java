package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.Constant;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0, 4.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        assertNotNull(root);
        assertEquals(root.getRowDimension(), 1);
        assertEquals(root.getColumnDimension(), 1);
        assertEquals(root.getEntry(0, 0), 1.0, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        assertNotNull(root);
        assertEquals(root.getRowDimension(), 2);
        assertEquals(root.getColumnDimension(), 2);
        assertEquals(root.getEntry(0, 0), 1.0, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 1), 1.0, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(0, 1), -0.5, Precision.SAFE_EPSILON);
        assertEquals(root.getEntry(1, 0), -0.5, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSmallMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, Constant.EPSILON.toDouble());
        RealMatrix root = rcd.getRootMatrix();
        assertNotNull(root);
        assertEquals(root.getRowDimension(), 3);
        assertEquals(root.getColumnDimension(), 3);
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testRankDeficientMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testRankDeficientButPositiveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 0.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        assertNotNull(root);
        assertEquals(root.getRowDimension(), 3);
        assertEquals(root.getColumnDimension(), 2);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 4.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        int rank = rcd.getRank();
        assertEquals(rank, 2);
    }
}