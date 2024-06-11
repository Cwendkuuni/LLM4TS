package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.functions.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightMatrixConstruction() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareConstruction() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3).transpose();
        new Weight(weight);
    }

    @Test
    public void testDiagonalMatrixMultiplication() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix x = RealMatrixUtils.randomMatrix(3, 1);
        RealMatrix y = weight.getWeight().multiply(x);
        RealMatrix expected = RealMatrixUtils.identityMatrix(3).multiply(x);
        assertEquals(expected, y);
    }

    @Test
    public void testSingularValueDecomposition() {
        double[] weights = {1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
        RealMatrix matrix = RealMatrixUtils.randomMatrix(4, 4);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix.multiply(weight.getWeight()));
        assertEquals(4, svd.getS().getRowDimension());
        assertEquals(4, svd.getS().getColumnDimension());
        for (int i = 0; i < 4; i++) {
            assertEquals(0.0, svd.getS().getEntry(i, i) - 1.0, 1e-9);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeWeight() {
        double[] weights = {1.0, -1.0, 1.0};
        new Weight(weights);
    }

    @Test
    public void testOptimizationWithWeightMatrix() {
        double[] weights = {1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);
        LinearFunction f = new LinearFunction(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 3.0}}, new double[]{-5.0, -5.0, -5.0});
        PointValuePair result = new MaxEval(f, new SimpleBounds(-10.0, 10.0), GoalType.MAXIMIZE, new Gradient(), new InitialGuess(new double[3]), 1000, weight).optimize();
        assertEquals(-5.0, result.getPoint()[0], 1e-9);
        assertEquals(-5.0, result.getValue(), 1e-9);
    }
}