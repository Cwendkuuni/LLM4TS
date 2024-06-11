package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import org.junit.Test;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(3, rank);
        assertNull(assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, -1.0)));
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive semidefinite matrix.
     */
    @Test
    public void testPositiveSemidefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 0.0}});
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(2, rank);
        assertFalse(assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, -1.0)).isInstanceOf(NonPositiveDefiniteMatrixException.class));
        assertFalse(assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 1.0)).isInstanceOf(NonPositiveDefiniteMatrixException.class));
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with non-positive definite matrix.
     */
    @Test
    public void testNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 5.0, -6.0}, {3.0, 6.0, -10.0}});
        double small = 0.0;
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with non-square matrix.
     */
    @Test
    public void testNonSquare() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        double small = 0.0;
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

}