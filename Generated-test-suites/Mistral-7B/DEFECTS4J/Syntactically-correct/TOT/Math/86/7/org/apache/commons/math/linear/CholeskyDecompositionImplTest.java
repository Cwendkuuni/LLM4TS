package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.SingularValueException;
import org.apache.commons.math3.exception.NonSquareMatrixException;

import org.junit.Test;

/**
 * Tests for {@link CholeskyDecompositionImpl} class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl constructor with positive definite
     * real symmetric matrix.
     */
    @Test
    public void testDecompositionOfPositiveDefiniteRealSymmetricMatrix() {
        final double[][] data = new double[][] {
            { 1.0, 2.0, 2.0 },
            { 2.0, 5.0, 3.0 },
            { 2.0, 3.0, 5.0 }
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();

        // Check L is lower triangular
        for (int i = 0; i < l.getRowDimension(); ++i) {
            for (int j = i + 1; j < l.getColumnDimension(); ++j) {
                assertEquals(0.0, l.getEntry(i, j), 1e-14);
            }
        }

        // Check LT is the transpose of L
        for (int i = 0; i < l.getRowDimension(); ++i) {
            for (int j = i; j < l.getColumnDimension(); ++j) {
                assertEquals(l.getEntry(i, j), lt.getEntry(j, i), 1e-14);
            }
        }

        // Check LT is the inverse of L
        RealMatrix lInv = l.inverse();
        RealMatrix lTInv = lt.transpose();

        RealMatrix product = lInv.multiply(lt);
        assertEquals(MatrixUtils.createIdentityMatrix(3), product, 1e-14);

        // Test solve using L
        RealVector b = new Array2DRowRealVector(new double[][] { { 1.0 }, { 3.0 }, { 4.0 } });
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[] { 0.34652753, -0.54322475, 1.13105648 });
        assertEquals(expected, x, 1e-14);

        // Test solve using LT
        RealMatrix m = lt.multiply(b);
        RealVector y = cholesky.getSolver().solve(m);
        RealVector expectedY = new RealVectorImpl(new double[] { 1.0, 0.0, 0.0 });
        assertEquals(expectedY, y, 1e-14);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testDecompositionOfNonSymmetricMatrix() {
        final double[][] data = new double[][] {
            { 1.0, 2.0, 2.0 },
            { 2.0, 5.0, 3.0 },
            { 2.0, 3.0, 4.0 }
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testDecompositionOfNonPositiveDefiniteMatrix() {
        final double[][] data = new double[][] {
            { -1.0, 2.0, 2.0 },
            { 2.0, -1.0, 3.0 },
            { 2.0, 3.0, -1.0 }
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testDecompositionOfNonSquareMatrix() {
        final double[][] data = new double[][] {
            { 1.0, 2.0 },
            { 3.0, 4.0, 5.0 }
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

}