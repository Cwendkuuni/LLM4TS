package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WeightTest {

    /**
     * Test the constructor with a diagonal weight matrix and an non-diagonal weight matrix.
     */
    @Test
    public void testWeightConstruction() {
        // Test diagonal weight matrix
        double[] diagonalWeight = new double[]{1.0, 2.0, 3.0};
        Weight weightDiagonal = new Weight(diagonalWeight);
        RealMatrix expectedDiagonal = new Array2DRowRealMatrix(3, 3,
                new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualDiagonal = weightDiagonal.getWeight();
        assertEquals(expectedDiagonal, actualDiagonal);

        // Test non-diagonal weight matrix
        double[][] nonDiagonalWeight = {{1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 4.0}};
        RealMatrix inputNonDiagonal = new Array2DRowRealMatrix(3, 3, nonDiagonalWeight);
        Weight weightNonDiagonal = new Weight(inputNonDiagonal);
        RealMatrix expectedNonDiagonal = inputNonDiagonal.copy();
        RealMatrix actualNonDiagonal = weightNonDiagonal.getWeight();
        assertEquals(expectedNonDiagonal, actualNonDiagonal);
    }

    /**
     * Test the copy constructor with a diagonal weight matrix and an non-diagonal weight matrix.
     */
    @Test
    public void testWeightCopyConstruction() {
        // Test diagonal weight matrix
        double[] diagonalWeight = new double[]{1.0, 2.0, 3.0};
        Weight weightDiagonal = new Weight(diagonalWeight);
        Weight weightDiagonalCopy = new Weight(weightDiagonal);
        RealMatrix expectedDiagonal = new Array2DRowRealMatrix(3, 3,
                new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualDiagonal = weightDiagonal.getWeight();
        RealMatrix actualDiagonalCopy = weightDiagonalCopy.getWeight();
        assertEquals(expectedDiagonal, actualDiagonal);
        assertEquals(actualDiagonal, actualDiagonalCopy);

        // Test non-diagonal weight matrix
        double[][] nonDiagonalWeight = {{1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 4.0}};
        RealMatrix inputNonDiagonal = new Array2DRowRealMatrix(3, 3, nonDiagonalWeight);
        Weight weightNonDiagonal = new Weight(inputNonDiagonal);
        Weight weightNonDiagonalCopy = new Weight(weightNonDiagonal);
        RealMatrix expectedNonDiagonal = inputNonDiagonal.copy();
        RealMatrix actualNonDiagonal = weightNonDiagonal.getWeight();
        RealMatrix actualNonDiagonalCopy = weightNonDiagonalCopy.getWeight();
        assertEquals(expectedNonDiagonal, actualNonDiagonal);
        assertEquals(actualNonDiagonal, actualNonDiagonalCopy);
    }

    /**
     * Test the Singular Value Decomposition method on a diagonal weight matrix
     */
    @Test
    public void testWeightSvdDiagonal() {
        double[] diagonalWeight = new double[]{1.0, 2.0, 3.0};
        Weight weightDiagonal = new Weight(diagonalWeight);
        RealMatrix weightMatrixDiagonal = weightDiagonal.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrixDiagonal);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();
        RealMatrix expectedIdentity = MatrixUtils.identityMatrix(3);
        RealMatrix actualIdentity = u.transpose().multiply(vt);
        RealMatrix actualS = s.getMatrix(0, 0, 0, 2);
        assertEquals(expectedIdentity, actualIdentity);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, actualS.getColumnVector(0).toArray(), 0.01);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, actualS.getColumnVector(1).toArray(), 0.01);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, actualS.getColumnVector(2).toArray(), 0.01);
    }

    /**
     * Test the Singular Value Decomposition method on a non-diagonal weight matrix
     */
    @Test
    public void testWeightSvdNonDiagonal() {
        double[][] nonDiagonalWeight = {{1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 4.0}};
        RealMatrix inputNonDiagonal = new Array2DRowRealMatrix(3, 3, nonDiagonalWeight);
        Weight weightNonDiagonal = new Weight(inputNonDiagonal);
        RealMatrix weightMatrixNonDiagonal = weightNonDiagonal.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrixNonDiagonal);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();
        RealMatrix expectedIdentity = MatrixUtils.identityMatrix(3);
        RealMatrix actualIdentity = u.transpose().multiply(vt);
        RealMatrix actualS = s.getMatrix(0, 0, 0, 2);
        assertEquals(expectedIdentity, actualIdentity);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, actualS.getColumnVector(0).toArray(), 0.01);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, actualS.getColumnVector(1).toArray(), 0.01);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, actualS.getColumnVector(2).toArray(), 0.01);
    }

    /**
     * Test the exception thrown when initializing a weight matrix with an incorrect dimension.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightException() {
        double[][] weightMatrix = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        RealMatrix inputWeightMatrix = new Array2DRowRealMatrix(2, 3, weightMatrix);
        Weight weight = new Weight(inputWeightMatrix);
    }
}