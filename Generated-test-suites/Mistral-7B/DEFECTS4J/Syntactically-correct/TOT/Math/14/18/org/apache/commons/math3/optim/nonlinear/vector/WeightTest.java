package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.WeightedSumSquaresFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeight() {
        // Test creating a diagonal weight matrix
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testNonDiagonalWeight() {
        // Test creating a non-diagonal weight matrix
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeight() {
        // Test creating a non-square weight matrix (should throw an exception)
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
    }

    @Test
    public void testSingularValueDecomposition() {
        // Test if the weight matrix can be used for Singular Value Decomposition
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix actualWeight = weight.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(actualWeight);
        RealMatrix U = svd.getU();

        // Test if the original weight matrix can be reconstructed from U and S
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedUVT = U.transpose().multiply(V);
        RealMatrix actualUVT = actualWeight.transpose().multiply(actualWeight);
        assertEquals(expectedUVT, actualUVT);
    }

    @Test
    public void testWeightedSumSquaresFunction() {
        // Test using a weight matrix with a WeightedSumSquaresFunction
        double[] weights = new double[]{1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
        RealMatrix actualWeight = weight.getWeight();

        WeightedSumSquaresFunction function = new WeightedSumSquaresFunction(new RealVector[]{
                new ArrayRealVector(new double[]{1.0, 2.0, 3.0}),
                new ArrayRealVector(new double[]{4.0, 5.0, 6.0})},
                actualWeight);

        PointValuePair result = function.minimize();
        double[] expectedResult = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testWeightInInitialGuess() {
        // Test if the weight matrix is used for the initial guess
        double[] weights = new double[]{1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
        RealMatrix actualWeight = weight.getWeight();

        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public RealVector getInitialPoint(RealVector dimensions) {
                RealVector vector = new ArrayRealVector(dimensions.getDimension());
                for (int i = 0; i < dimensions.getDimension(); i++) {
                    vector.setEntry(i, actualWeight.getEntry(i, i) / weights[i]);
                }
                return vector;
            }
        };

        WeightedSumSquaresFunction function = new WeightedSumSquaresFunction(new RealVector[]{
                new ArrayRealVector(new double[]{1.0, 2.0, 3.0}),
                new ArrayRealVector(new double[]{4.0, 5.0, 6.0})},
                actualWeight);
        PointValuePair result = function.minimize(initialGuess);

        double[] expectedResult = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }
}