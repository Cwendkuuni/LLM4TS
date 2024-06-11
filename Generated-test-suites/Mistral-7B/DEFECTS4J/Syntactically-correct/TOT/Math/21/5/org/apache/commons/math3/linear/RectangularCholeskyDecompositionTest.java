package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        double[][] data = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            RealVector b = root.getColumnVector(0);
            RealVector c = root.getColumnVector(1);
            RealVector d = root.getColumnVector(2);
            assertEquals(b.getEntry(0), 1.0, 1e-15);
            assertEquals(b.getEntry(1), 0.5, 1e-15);
            assertEquals(b.getEntry(2), 0.5, 1e-15);
            assertEquals(c.getEntry(0), 0.5, 1e-15);
            assertEquals(c.getEntry(1), 0.5, 1e-15);
            assertEquals(c.getEntry(2), 1.0, 1e-15);
            assertEquals(d.getEntry(0), 0.5, 1e-15);
            assertEquals(d.getEntry(1), 0.5, 1e-15);
            assertEquals(d.getEntry(2), 1.0, 1e-15);
            RealMatrix expected = new Array2DRowRealMatrix(data);
            RealMatrix product = root.transpose().multiply(root);
            SingularValueDecomposition svd = new SingularValueDecomposition(expected);
            RealMatrix expectedProduct = svd.getU().transpose().multiply(svd.getU());
            assertTrue(MatrixUtils.isClose(product, expectedProduct, 1e-12));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    /**
     * Test the Cholesky decomposition of a positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testPositiveSemidefiniteWithSmallDiagonal() {
        double[][] data = {
            {1.0, 0.5, 0.5},
            {0.5, 0.1, 0.5},
            {0.5, 0.5, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        double small = 1e-6;
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            RealVector b = root.getColumnVector(0);
            RealVector c = root.getColumnVector(1);
            RealVector d = root.getColumnVector(2);
            assertEquals(b.getEntry(0), 1.0, 1e-15);
            assertEquals(b.getEntry(1), 0.5, 1e-15);
            assertEquals(b.getEntry(2), 0.5, 1e-15);
            assertEquals(c.getEntry(0), 0.5, 1e-15);
            assertEquals(c.getEntry(1), 0.1, 1e-15);
            assertEquals(c.getEntry(2), 0.5, 1e-15);
            assertEquals(d.getEntry(0), 0.5, 1e-15);
            assertEquals(d.getEntry(1), 0.5, 1e-15);
            assertEquals(d.getEntry(2), 0.1, 1e-15);
            RealMatrix expected = new Array2DRowRealMatrix(data);
            RealMatrix product = root.transpose().multiply(root);
            SingularValueDecomposition svd = new SingularValueDecomposition(expected);
            RealMatrix expectedProduct = svd.getU().transpose().multiply(svd.getU());
            assertTrue(MatrixUtils.isClose(product, expectedProduct, 1e-12));
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test the Cholesky decomposition of a non-positive semidefinite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveSemidefinite() {
        double[][] data = {
            {1.0, 0.5, 0.5},
            {0.5, 0.1, -0.5},
            {0.5, 0.5, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        double small = 1e-6;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
    }

}