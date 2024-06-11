package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}.
     */
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Positive definite matrix
        double[][] matrix1 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        };
        RealMatrix inputMatrix1 = RealMatrixUtils.createRealMatrix(matrix1);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(inputMatrix1, small);
            RealMatrix expectedRoot1 = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.0, 1.5}, {0.0, 0.0, 1.5}});
            RealMatrix actualRoot1 = decomp1.getRootMatrix();
            assertEquals("Root matrix is incorrect.", expectedRoot1, actualRoot1);
            assertEquals("Rank is incorrect.", 3, decomp1.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // Should not be thrown for positive definite matrix
            assertFalse("Unexpected exception.", e.isDisplayMessageEnabled());
        }

        // Positive semidefinite matrix with rank 2
        double[][] matrix2 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0},
            {0.0, 0.0, 0.0}
        };
        RealMatrix inputMatrix2 = RealMatrixUtils.createRealMatrix(matrix2);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(inputMatrix2, small);
            RealMatrix expectedRoot2 = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.0, 1.5}, {0.0, 0.0, 1.5}});
            RealMatrix actualRoot2 = decomp2.getRootMatrix();
            assertEquals("Root matrix is incorrect.", expectedRoot2, actualRoot2);
            assertEquals("Rank is incorrect.", 3, decomp2.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected for positive semidefinite matrix
            assertFalse("Unexpected exception.", e.isDisplayMessageEnabled());
            assertEquals("Expected NonPositiveDefiniteMatrixException.", "Non-positive definite matrix", e.getMessage());
        }

        // Non-positive definite matrix
        double[][] matrix3 = {
            {1.0, 2.0, 3.0},
            {2.0, -4.0, 6.0},
            {3.0, 6.0, -9.0}
        };
        RealMatrix inputMatrix3 = RealMatrixUtils.createRealMatrix(matrix3);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(inputMatrix3, small);
            fail("Expected NonPositiveDefiniteMatrixException.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected for non-positive definite matrix
            assertTrue("Unexpected exception.", e.isDisplayMessageEnabled());
            assertEquals("Expected NonPositiveDefiniteMatrixException.", "Non-positive definite matrix", e.getMessage());
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRootMatrix()}.
     */
    @Test
    public void testGetRootMatrix() {
        double[][] matrix1 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        };
        RealMatrix inputMatrix1 = RealMatrixUtils.createRealMatrix(matrix1);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(inputMatrix1, small);
            RealMatrix actualRoot1 = decomp1.getRootMatrix();
            RealMatrix expectedRoot1 = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.0, 1.5}, {0.0, 0.0, 1.5}});
            assertEquals("Root matrix is incorrect.", expectedRoot1, actualRoot1);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Should not be thrown
            fail("Unexpected exception.", e.getMessage());
        }

        double[][] matrix2 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0},
            {0.0, 0.0, 0.0}
        };
        RealMatrix inputMatrix2 = RealMatrixUtils.createRealMatrix(matrix2);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(inputMatrix2, small);
            RealMatrix actualRoot2 = decomp2.getRootMatrix();
            RealMatrix expectedRoot2 = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.0, 1.5}, {0.0, 0.0, 1.5}});
            assertEquals("Root matrix is incorrect.", expectedRoot2, actualRoot2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
            assertFalse("Unexpected exception.", e.isDisplayMessageEnabled());
        }

        double[][] matrix3 = {
            {1.0, 2.0, 3.0},
            {2.0, -4.0, 6.0},
            {3.0, 6.0, -9.0}
        };
        RealMatrix inputMatrix3 = RealMatrixUtils.createRealMatrix(matrix3);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(inputMatrix3, small);
            fail("Expected NonPositiveDefiniteMatrixException.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
            assertTrue("Unexpected exception.", e.isDisplayMessageEnabled());
        }

        // Test null input
        try {
            new RectangularCholeskyDecomposition(null, small);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue("Unexpected exception.", e.isDisplayMessageEnabled());
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRank()}.
     */
    @Test
    public void testGetRank() {
        double[][] matrix1 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        };
        RealMatrix inputMatrix1 = RealMatrixUtils.createRealMatrix(matrix1);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(inputMatrix1, small);
            int actualRank1 = decomp1.getRank();
            int expectedRank1 = 3;
            assertEquals("Rank is incorrect.", expectedRank1, actualRank1);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Should not be thrown
            fail("Unexpected exception.", e.getMessage());
        }

        double[][] matrix2 = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0},
            {0.0, 0.0, 0.0}
        };
        RealMatrix inputMatrix2 = RealMatrixUtils.createRealMatrix(matrix2);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(inputMatrix2, small);
            int actualRank2 = decomp2.getRank();
            int expectedRank2 = 3;
            assertEquals("Rank is incorrect.", expectedRank2, actualRank2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
            assertFalse("Unexpected exception.", e.isDisplayMessageEnabled());
        }

        double[][] matrix3 = {
            {1.0, 2.0, 3.0},
            {2.0, -4.0, 6.0},
            {3.0, 6.0, -9.0}
        };
        RealMatrix inputMatrix3 = RealMatrixUtils.createRealMatrix(matrix3);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(inputMatrix3, small);
            fail("Expected NonPositiveDefiniteMatrixException.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
            assertTrue("Unexpected exception.", e.isDisplayMessageEnabled());
        }

        // Test null input
        try {
            new RectangularCholeskyDecomposition(null, small).getRank();
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue("Unexpected exception.", e.isDisplayMessageEnabled());
        }
    }

}