package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Tests the default constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultConstructor() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

    /**
     * Test for valid input with small tolerance.
     */
    @Test
    public void testValidInputWithSmallTolerance() {
        final double tolerance = 1e-10;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, 1.2, 0.3},
                {0.1, 0.3, 0.4}
        });
        final RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, tolerance);
        final RealMatrix root = decomposer.getRootMatrix();
        final int rank = decomposer.getRank();
        assertNotNull(root);
        assertEquals(3, rank);
        assertFalse(root.isSymmetric());
        assertEquals(0.5, root.getEntry(0, 0), tolerance);
        assertEquals(-0.13533528323661274, root.getEntry(1, 0), tolerance);
        assertEquals(0.23562287605006283, root.getEntry(2, 0), tolerance);
        assertEquals(0.5, root.getEntry(0, 1), tolerance);
        assertEquals(0.17320508075688772, root.getEntry(1, 1), tolerance);
        assertEquals(0.0291522759333126, root.getEntry(2, 1), tolerance);
        assertEquals(0.23562287605006283, root.getEntry(1, 2), tolerance);
        assertEquals(0.0291522759333126, root.getEntry(2, 2), tolerance);
    }

    /**
     * Test for valid input with zero tolerance.
     */
    @Test
    public void testValidInputWithZeroTolerance() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, 1.2, 0.3},
                {0.1, 0.3, 0.4}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Tests the decomposition of a singular matrix with small tolerance.
     */
    @Test
    public void testSingularMatrixWithSmallTolerance() {
        final double tolerance = 1e-10;
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, 1.2, 0.3},
                {0.1, 0.3, 0.0}
        });
        final RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, tolerance);
        final RealMatrix root = decomposer.getRootMatrix();
        final int rank = decomposer.getRank();
        assertNotNull(root);
        assertEquals(2, rank);
        assertEquals(0.5, root.getEntry(0, 0), tolerance);
        assertEquals(-0.13533528323661274, root.getEntry(1, 0), tolerance);
        assertEquals(0.23562287605006283, root.getEntry(1, 1), tolerance);
    }

    /**
     * Tests the decomposition of a singular matrix with zero tolerance.
     */
    @Test
    public void testSingularMatrixWithZeroTolerance() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, 1.2, 0.3},
                {0.1, 0.3, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Tests the decomposition of a negative semi-definite matrix.
     */
    @Test
    public void testNegativeSemiDefiniteMatrix() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, -1.2, 0.3},
                {0.1, 0.3, 0.4}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}