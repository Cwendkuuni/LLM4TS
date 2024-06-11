package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.Complex;
import org.apache.commons.math3.RealArray2D;
import org.apache.commons.math3.RealMatrix;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MatrixUtils;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a simple positive definite matrix.
     */
    @org.junit.Test
    public void testSimpleDecomposition() {
        final double[][] m = { { 1.0, 2.0 }, { 2.0, 3.0 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();
        assertEquals(2, rank);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        RealArray2D a = root.getData();
        assertEquals(1.0, Precision.round(a.getEntry(0, 0), 12), 0.0);
        assertEquals(1.0, Precision.round(a.getEntry(1, 0), 12), 0.0);
        assertEquals(1.0 / Math.sqrt(2.0), Precision.round(a.getEntry(0, 1), 12), 0.0);
        assertEquals(-1.0 / Math.sqrt(2.0), Precision.round(a.getEntry(1, 1), 12), 0.0);
        assertEquals(1.0 / Math.sqrt(2.0), Precision.round(a.getEntry(1, 0), 12), 0.0);
        assertNull(decomp.getRank() > 2 ? decomp.getRootMatrix().getEntry(1, 2) : decomp.getRootMatrix().getEntry(1, 2));
        assertNull(decomp.getRootMatrix().getEntry(2, 0));
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] { { 1.0, 1.0 / Math.sqrt(2.0) },
                { 1.0 / Math.sqrt(2.0), 1.0 } });
        assertEquals(expected, root);
        RealMatrix expectedTranspose = MatrixUtils.createRealMatrix(new double[][] { { 1.0 / Math.sqrt(2.0), 1.0 },
                { 1.0 / Math.sqrt(2.0), 1.0 } });
        assertEquals(expectedTranspose, root.transpose());
        RealMatrix product = root.multiply(root.transpose());
        assertEquals(MatrixUtils.createRealMatrix(m), product, 1e-13);
    }

    /**
     * Test the decomposition of a singular matrix.
     */
    @org.junit.Test
    public void testSingularDecomposition() {
        final double[][] m = { { 1.0, 0.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    /**
     * Test the decomposition of a negative definite matrix.
     */
    @org.junit.Test
    public void testNegativeDefiniteDecomposition() {
        final double[][] m = { { -1.0, 0.0 }, { 0.0, -1.0 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    /**
     * Test the decomposition of a matrix with small diagonal elements.
     */
    @org.junit.Test
    public void testSmallDiagonalDecomposition() {
        final double[][] m = { { 0.1, 0.2 }, { 0.2, 0.3 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RealMatrix decomp = new RectangularCholeskyDecomposition(matrix, 0.01).getRootMatrix();
        int rank = decomp.getRowDimension();
        assertEquals(2, rank);
        RealArray2D a = decomp.getData();
        assertEquals(0.1, Precision.round(a.getEntry(0, 0), 12), 0.0);
        assertEquals(0.1, Precision.round(a.getEntry(1, 0), 12), 0.0);
        assertEquals(0.1, Precision.round(a.getEntry(0, 1), 12), 0.0);
        assertEquals(0.1, Precision.round(a.getEntry(1, 1), 12), 0.0);
        assertEquals(MathUtils.sqrt(0.1) / Math.sqrt(2.0), Precision.round(a.getEntry(1, 0), 12), 0.0);
        assertEquals(-MathUtils.sqrt(0.1) / Math.sqrt(2.0), Precision.round(a.getEntry(0, 1), 12), 0.0);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] { { 1.0 / Math.sqrt(0.1), 1.0 / Math.sqrt(0.1) },
                { MathUtils.sqrt(0.1) / Math.sqrt(2.0), -MathUtils.sqrt(0.1) / Math.sqrt(2.0) } });
        assertEquals(expected, decomp);
        RealMatrix expectedTranspose = MatrixUtils.createRealMatrix(new double[][] { { 1.0 / Math.sqrt(0.1), MathUtils.sqrt(0.1) / Math.sqrt(2.0) },
                { 1.0 / Math.sqrt(0.1), -MathUtils.sqrt(0.1) / Math.sqrt(2.0) } });
        assertEquals(expectedTranspose, decomp.transpose());
        RealMatrix product = decomp.multiply(decomp.transpose());
        RealMatrix expectedProduct = MatrixUtils.createRealMatrix(new double[][] { { 0.1, 0.1 }, { 0.1, 0.1 } });
        assertEquals(expectedProduct, product, 1e-13);
    }

    /**
     * Test the decomposition of a matrix with negative diagonal elements.
     */
    @org.junit.Test
    public void testNegativeDiagonalDecomposition() {
        final double[][] m = { { 1.0, 0.0 }, { -1.0, 1.0 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    /**
     * Test the decomposition of a matrix with complex numbers.
     */
    @org.junit.Test
    public void testComplexDecomposition() {
        final double[][] m = { { 1.0, 2.0 }, { 2.0, new Complex(0.0, 3.0) } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

}