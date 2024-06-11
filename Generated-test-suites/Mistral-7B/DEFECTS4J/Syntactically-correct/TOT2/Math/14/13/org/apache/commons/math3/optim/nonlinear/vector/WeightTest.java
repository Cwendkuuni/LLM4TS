package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class WeightTest {
    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = RealMatrixUtils.identityMatrix(3);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix weight = RealMatrixUtils.createMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = RealMatrixUtils.identityMatrix(3);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightConstructorWithNullArray() {
        double[] weights = null;
        new Weight(weights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightConstructorWithEmptyArray() {
        double[] weights = {};
        new Weight(weights);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 3.0}};
        RealMatrix weight = RealMatrixUtils.createMatrix(weights);
        new Weight(weight);
    }

    @Test
    public void testWeightConstructorWithNegativeWeight() {
        double[] weights = {-1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 0.0},
                {0.0, 2.0},
                {0.0, 3.0}
        });
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @AfterClass
    public static void tearDown() {
        // Perform any post-test cleanup here
    }
}