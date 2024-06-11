package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] input = {{3.0, 2.0, 1.0}, {2.0, 5.0, 4.0}, {1.0, 4.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(input);

        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        assertArrayEquals("Cholesky L is not equal to LT^T", l.transpose().getData(), lt.getData(), 0.0);
        assertArrayEquals("Cholesky L^T is not equal to L^(-1)", l.transpose().multiply(l), MatrixUtils.createRealIdentityMatrix(3), 0.0);

        RealVector b = new RealVectorImpl(new double[]{-1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1.0 / 3.0, 1.0 / 5.0, -1.0 / 6.0});
        assertArrayEquals("Solution is not correct", expectedX.getData(), x.getData(), 0.0);

        RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 4.0}});
        try {
            new CholeskyDecompositionImpl(A);
            assertEquals("Should have thrown exception", "Failed to initialize CholeskyDecompositionImpl", null);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {1.0, 2.0}});
        try {
            new CholeskyDecompositionImpl(nonSquareMatrix);
            assertEquals("Should have thrown exception", "Failed to initialize CholeskyDecompositionImpl", null);
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        RealMatrix notSymmetricMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        try {
            new CholeskyDecompositionImpl(notSymmetricMatrix, 1e-14, 1e-14);
            assertEquals("Should have thrown exception", "Failed to initialize CholeskyDecompositionImpl", null);
        } catch (NotSymmetricMatrixException e) {
            // Expected
        }

    }

}