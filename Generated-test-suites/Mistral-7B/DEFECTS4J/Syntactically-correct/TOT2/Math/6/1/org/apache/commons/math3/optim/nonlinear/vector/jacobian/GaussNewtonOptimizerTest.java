package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[] targetValues;
    private double[] startPoint;
    private RealMatrix weightMatrix;
    private RealMatrix weightedJacobian;
    private double[] currentObjective;
    private double[] currentResiduals;

    /**
     * Initializes the test object with default settings.
     */
    @Before
    public void setUp() {
        targetValues = new double[]{1.0, 2.0, 3.0};
        startPoint = new double[]{1.1, 1.2, 1.3};
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeightMatrix(weightMatrix);
    }

    /**
     * Test constructor without arguments.
     */
    @Test
    public void testConstructorWithoutArguments() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        // This constructor is deprecated, so no further testing is needed.
    }

    /**
     * Test constructor with a null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    /**
     * Test the doOptimize method with a simple example.
     */
    @Test
    public void testDoOptimize() {
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.5, 2.0}, result.getPoint(), 0.001);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getValue(), 0.001);
    }

    /**
     * Test the doOptimize method with an ill-conditioned problem.
     */
    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeWithSingularProblem() {
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, 1.0}, {0.0, 1.0, 0.0}});
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeightMatrix(weightMatrix);
        optimizer.doOptimize();
    }

    /**
     * Test the doOptimize method with a non-converged example.
     */
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithNonConvergedExample() {
        targetValues = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeightMatrix(weightMatrix);
        optimizer.doOptimize();
    }

    /**
     * Test the computeObjectiveValue method.
     */
    @Test
    public void testComputeObjectiveValue() {
        currentObjective = optimizer.computeObjectiveValue(startPoint);
        Assert.assertArrayEquals(new double[]{1.234, 2.345, 3.456}, currentObjective, 0.001);
    }

    /**
     * Test the computeResiduals method.
     */
    @Test
    public void testComputeResiduals() {
        currentResiduals = optimizer.computeResiduals(currentObjective);
        Assert.assertArrayEquals(new double[]{-1.234, -1.345, -1.456}, currentResiduals, 0.001);
    }

    /**
     * Test the computeWeightedJacobian method.
     */
    @Test
    public void testComputeWeightedJacobian() {
        weightedJacobian = optimizer.computeWeightedJacobian(startPoint);
        final double[][] expected = new double[][]{{0.333, 0.666, 1.0}, {1.0, 0.666, 0.333}, {1.5, 0.0, 0.5}};
        Assert.assertArrayEquals(expected, weightedJacobian.getData(), 0.001);
    }
}