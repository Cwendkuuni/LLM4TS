package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws SingularValueDecompositionException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                          {4.0, 5.0, 6.0},
                                                                          {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        double[] singularValues = svd.getSingularValues();

        // Check that U is orthogonal
        RealMatrix UTranspose = U.transpose();
        RealMatrix product = U.multiply(UTranspose);
        assertEquals("U.T * U should be the identity matrix", MatrixUtils.createIdentityMatrix(3), product);

        // Check that V is orthogonal
        RealMatrix VTranspose = V.transpose();
        product = V.multiply(VTranspose);
        assertEquals("V.T * V should be the identity matrix", MatrixUtils.createIdentityMatrix(3), product);

        // Check that USV^T is equal to the original matrix
        RealMatrix USV = U.multiply(S).multiply(V.transpose());
        RealMatrix originalMatrix = matrix.copy();
        assertEquals("USV should be equal to the original matrix", originalMatrix, USV);

        // Check singular values are correct
        assertArrayEquals("Singular values should be [14.1421371387, 6.32455532033, 0.0]",
                          singularValues, new double[] {14.1421371387, 6.32455532033, 0.0});

        // Check that we can get the singular values matrix S
        RealMatrix SMatrix = svd.getS();
        assertArrayEquals("Singular values matrix should be correct",
                          new double[][] {{14.14213713866, 0.0, 0.0}, {0.0, 6.32455532033, 0.0},
                                          {0.0, 0.0, 0.0}}, SMatrix.getData());

        // Check that we can get the pseudo-inverse
        RealMatrix pseudoInverse = svd.getSolver().getInverse();
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix result = pseudoInverse.multiply(testMatrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{0.34202016051, 0.65797983949},
                                                                               {-0.65797983949, 0.34202016051}});
        assertEquals("Pseudo-inverse should be correct", expectedResult, result);

        // Check that we can solve equations with a non-singular matrix
        RealMatrix nonSingularMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealVector nonSingularVector = new ArrayRealVector(new double[]{-1.0, 1.0});
        RealVector resultVector = svd.getSolver().solve(nonSingularMatrix, nonSingularVector);
        RealVector expectedResultVector = new ArrayRealVector(new double[]{-1.0, 1.0});
        assertEquals("Solver should return correct result for non-singular matrix",
                     expectedResultVector, resultVector);

        // Check that we can solve equations with a singular matrix
        RealMatrix singularMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RealVector singularVector = new ArrayRealVector(new double[]{1.0});
        RealVector resultVectorSingular = svd.getSolver().solve(singularMatrix, singularVector);
        assertEquals("Solver should return correct result for singular matrix",
                     new ArrayRealVector(new double[]{1.0 / 0.0, 1.0 / 0.0}), resultVectorSingular);
    }

}