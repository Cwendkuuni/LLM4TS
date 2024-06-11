package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {FastMath.sqrt(2.0), 0.0, -FastMath.sqrt(0.5)},
            {0.0, FastMath.sqrt(2.0), -FastMath.sqrt(0.5)},
            {-FastMath.sqrt(0.5), -FastMath.sqrt(0.5), FastMath.sqrt(0.5)}
        });
        assertEquals("Root matrix is not correct", expected, root);
    }

    /**
     * Test Cholesky decomposition of a positive semidefinite matrix.
     */
    @Test
    public void testPositiveSemidefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 1.0, 0.5},
            {1.0, 0.5, 2.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            assertTrue("Small diagonal element is not small enough", e.getDiagonalValue() < 0.5);
            assertTrue("Row with negative diagonal element is not found", e.getRow() == 2);
        }
    }

    /**
     * Test Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test Cholesky decomposition of an empty matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test Cholesky decomposition of a matrix of different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMatrixOfDifferentDimensions() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        });
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test that decomposition is idempotent.
     */
    @Test
    public void testIdempotent() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix product = MatrixUtils.multiply(root, root.transpose());
        RealMatrix expected = matrix;
        assertArrayEquals("Product is not equal to original matrix", expected.getData(), product.getData());
    }

    /**
     * Test that decomposition is robust against small perturbations.
     */
    @Test
    public void testRobustToSmallPerturbations() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.001, 1.001, 1.0},
            {1.001, 2.001, 1.0},
            {1.0, 1.0, 2.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {FastMath.sqrt(2.001), 0.0, -FastMath.sqrt(0.5)},
            {0.0, FastMath.sqrt(2.001), -FastMath.sqrt(0.5)},
            {-FastMath.sqrt(0.5), -FastMath.sqrt(0.5), FastMath.sqrt(0.5)}
        });
        assertEquals("Root matrix is not correct", expected, root, 1e-8);
    }

    /**
     * Test that decomposition is robust against large perturbations.
     */
    @Test
    public void testRobustToLargePerturbations() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.999, 1.001, 1.0},
            {1.001, 1.999, 1.0},
            {1.0, 1.0, 2.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            assertTrue("Small diagonal element is not small enough", e.getDiagonalValue() < 0.5);
            assertTrue("Row with negative diagonal element is not found", e.getRow() == 2);
        }
    }

}