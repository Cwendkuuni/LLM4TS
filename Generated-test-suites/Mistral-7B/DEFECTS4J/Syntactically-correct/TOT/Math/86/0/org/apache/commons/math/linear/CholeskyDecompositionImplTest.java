package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        // Test the identity matrix
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(5);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lT = decomposition.getLT();
        RealMatrix expectedL = MatrixUtils.createRealIdentityMatrix(5);
        assertEquals(expectedL, l);
        assertEquals(expectedL.transpose(), lT);

        // Test the symmetry of the matrix
        double[][] inputMatrix = {{1.0, 1.0, 0.0, 0.0, 0.0},
                                   {1.0, 1.0, 0.0, 0.0, 0.0},
                                   {0.0, 0.0, 2.0, 1.0, 1.0},
                                   {0.0, 0.0, 1.0, 2.0, 1.0},
                                   {0.0, 0.0, 1.0, 1.0, 3.0}};

        RealMatrix symmetricMatrix = new RealMatrixImpl(inputMatrix);
        CholeskyDecompositionImpl decompositionSymmetric = null;
        try {
            decompositionSymmetric = new CholeskyDecompositionImpl(symmetricMatrix, 0.0001, 0.0001);
        } catch (NotSymmetricMatrixException e) {
            fail("Expected symmetric matrix to be decomposed, but it was not.");
        }
        RealMatrix lSymmetric = decompositionSymmetric.getL();
        for (int i = 0; i < lSymmetric.getRowDimension(); i++) {
            for (int j = i + 1; j < lSymmetric.getColumnDimension(); j++) {
                assertEquals("Off-diagonal elements of the Cholesky decomposition of a symmetric matrix must be zero.",
                            0.0, lSymmetric.getEntry(i, j), 0.0001);
            }
        }

        // Test the positive definiteness of the matrix
        double[][] inputMatrixPD = {{1.0, 1.0, 1.0, 0.0, 0.0},
                                     {1.0, 2.0, 2.0, 2.0, 0.0},
                                     {1.0, 2.0, 3.0, 2.0, 1.0},
                                     {0.0, 2.0, 2.0, 4.0, 2.0},
                                     {0.0, 0.0, 1.0, 2.0, 3.0}};

        RealMatrix pdMatrix = new RealMatrixImpl(inputMatrixPD);
        CholeskyDecompositionImpl decompositionPD = null;
        try {
            decompositionPD = new CholeskyDecompositionImpl(pdMatrix, 0.0001, 0.0001);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix to be decomposed, but it was not.");
        }

        // Test the determinant of the matrix
        RealMatrix determinantTestMatrix = new RealMatrixImpl(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decompositionDeterminant = null;
        try {
            decompositionDeterminant = new CholeskyDecompositionImpl(determinantTestMatrix, 0.0001, 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        double determinant = decompositionDeterminant.getDeterminant();
        assertEquals("The determinant of a 2x2 matrix should be ((a*d)-(b*c))", 1.0 * 3.0 - 2.0 * 2.0, determinant, 0.0001);

        // Test the solver
        double[] b = {1.0, 2.0, 3.0, 4.0, 5.0};
        RealVector solution = new RealVectorImpl(pdMatrix.operate(b));
        RealVector expectedSolution = new RealVectorImpl(new double[]{1.0, 0.9487, -0.2731, 0.3791, 0.4817});
        assertEquals(expectedSolution, decompositionPD.getSolver().solve(solution));

        // Test the exception handling
        double[][] inputMatrixSingular = {{1.0, 1.0}, {1.0, 0.0}};
        RealMatrix singularMatrix = new RealMatrixImpl(inputMatrixSingular);
        try {
            new CholeskyDecompositionImpl(singularMatrix, 0.0001, 0.0001);
            fail("Expected NotPositiveDefiniteMatrixException to be thrown.");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

        double[][] inputMatrixNonSquare = {{1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix nonSquareMatrix = new RealMatrixImpl(inputMatrixNonSquare);
        try {
            new CholeskyDecompositionImpl(nonSquareMatrix, 0.0001, 0.0001);
            fail("Expected NonSquareMatrixException to be thrown.");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

}