package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Tests the positive semidefinite case.
     */
    @Test
    public void testPositiveSemidefinite() {
        // create a positive semidefinite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1, 2, 2},
                {2, 4, 2},
                {2, 2, 3}
        });

        // compute the decomposition
        RectangularCholeskyDecomposition decomp;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix, 1e-9);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("The matrix should be positive semidefinite");
            return;
        }

        // check the result
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {FastMath.SQRT(1), 0, FastMath.SQRT(2)},
                {FastMath.SQRT(2), FastMath.SQRT(2), 0},
                {FastMath.SQRT(2), 0, FastMath.SQRT(3)}
        });

        Assert.assertEquals(expected, decomp.getRootMatrix());
        Assert.assertEquals(3, decomp.getRank());

        // check that A is almost equal to BB<sup>T</sup>
        RealMatrix b = new Array2DRowRealMatrix(decomp.root.getColumnVector(0),
                                              decomp.root.getColumnVector(1),
                                              decomp.root.getColumnVector(2));
        RealMatrix bT = MatrixUtils.transpose(b);
        RealMatrix product = MatrixUtils.multiply(b, bT);
        RealVector difference = MatrixUtils.subtract(matrix, product);
        double maxError = Precision.getMaximumAbsoluteRelativeError(matrix.getData(),
                                                                   product.getData());
        Assert.assertTrue("Matrix is not almost equal to BB<sup>T</sup>: max error = " + maxError,
                         maxError < 1e-9);
    }

    /**
     * Tests the case where the matrix is not positive semidefinite.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNotPositiveSemidefinite() {
        // create a non-positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1, 2, 2},
                {2, -4, 2},
                {2, 2, 3}
        });

        new RectangularCholeskyDecomposition(matrix, 1e-9);
    }

}