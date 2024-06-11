package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.WeightedSumSquaresProblem;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with a diagonal matrix.
     */
    @Test
    public void testDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = RealMatrixFactory.identity(3);
        for (int i = 0; i < 3; i++) {
            expectedWeight.setEntry(i, i, weights[i]);
        }
        RealMatrix actualWeight = weight.getWeight();
        assertEquals("Weight matrix is not as expected.", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with a square non-diagonal matrix.
     */
    @Test
    public void testSquareNonDiagonalConstructor() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals("Weight matrix is not as expected.", expectedWeight, actualWeight);
    }

    /**
     * Test constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareConstructor() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}, {1.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        new Weight(weightMatrix);
    }

    /**
     * Test getWeight() method.
     */
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = RealMatrixFactory.identity(3);
        for (int i = 0; i < 3; i++) {
            expectedWeight.setEntry(i, i, weights[i]);
        }
        RealMatrix actualWeight = weight.getWeight();
        assertEquals("Weight matrix is not as expected.", expectedWeight, actualWeight);
    }
}