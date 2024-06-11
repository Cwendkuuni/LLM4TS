package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Weight class.
 */
public class WeightTest {

    /**
     * Test diagonal weight matrix construction.
     */
    @Test
    public void testDiagonalWeightMatrixConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = weightObj.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    /**
     * Test non-square matrix error when constructing weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrixConstruction() {
        double[] weight = {1.0, 2.0};
        Weight weightObj = new Weight(weight);
    }

    /**
     * Test weight matrix copy.
     */
    @Test
    public void testWeightMatrixCopy() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = weightObj.getWeight().copy();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }
}