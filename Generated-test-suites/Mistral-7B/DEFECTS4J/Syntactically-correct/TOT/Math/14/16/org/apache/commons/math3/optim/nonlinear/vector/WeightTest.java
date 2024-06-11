package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.Weight;

public class WeightTest {

    @Test
    public void testDiagonalWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixFactory.identity(3);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testSquareWeight() {
        double[] weights = {1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeight() {
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
    }

    @Test
    public void testCopyWeight() {
        double[] weights = {1.0, 2.0};
        RealMatrix original = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(original);
        RealMatrix expected = original.copy();
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }
}