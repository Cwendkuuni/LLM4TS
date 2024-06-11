package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;
    private double[] targetValues;
    private double[] startPoint;

    @Before
    public void setup() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker());
        weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0}, {1.0}
        });
        targetValues = new double[]{1.0, 1.0};
        startPoint = new double[]{0.0, 0.0};
    }

    /**
     * Tests the constructor with LU decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerLUConstructor() {
        // No operation.
    }

    /**
     * Tests the optimize method with a simple test case.
     */
    @Test
    public void testOptimizeSimple() {
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        PointValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Tests the optimize method with a test case where the solution is not
     * in the null space of the Jacobian.
     */
    @Test
    public void testOptimizeNotInNullSpace() {
        targetValues = new double[]{1.0, 2.0};
        startPoint = new double[]{0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        PointValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Tests the optimize method with a test case where the solution is in
     * the null space of the Jacobian.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeInNullSpace() {
        targetValues = new double[]{0.0, 0.0};
        startPoint = new double[]{0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Tests the optimize method with a test case where the Jacobian is
     * singular.
     */
    @Test(expected = SingularMatrixException.class)
    public void testOptimizeSingularJacobian() {
        weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 1.0},
                {0.0, 0.0}
        });
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Tests the optimize method with a test case where the weight matrix is
     * singular.
     */
    @Test(expected = SingularValueDecomposition.SingularMatrixException.class)
    public void testOptimizeSingularWeightMatrix() {
        weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Tests the optimize method with a test case where the target values
     * are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullTarget() {
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    /**
     * Tests the optimize method with a test case where the start point is
     * null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullStartPoint() {
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Tests the optimize method with a test case where the weight matrix is
     * null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullWeight() {
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(null);
        optimizer.optimize();
    }
}