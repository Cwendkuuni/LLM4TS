package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.WeightedSumFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with diagonal weight matrix.
     */
    @Test
    public void testConstructorDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    /**
     * Test constructor with square weight matrix.
     */
    @Test
    public void testConstructorSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix;
        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test constructor with non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }

    /**
     * Test getWeight method with diagonal weight matrix.
     */
    @Test
    public void testGetWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    /**
     * Test getWeight method with square weight matrix.
     */
    @Test
    public void testGetWeightSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix;
        RealMatrix actual = weightObj.getWeight();
        Assert.assertEquals(expected, actual);
    }
}