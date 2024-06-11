package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValues;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private AbstractSimplex simplex;
    private MultivariateFunction function;
    private double[] startPoint = {-1.0, -1.0};
    private double[] goalPoint = {1.0, 1.0};
    private double maxEval = 100;
    private GoalType goalType = GoalType.MINIMIZE;
    private ConvergenceChecker<PointValuePair> checker;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        simplex = new AmplSimplex(5);
        function = new PolynomialFunction(2, 2, new double[][]{{-1.0, -1.0}, {1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}});
        optimizer.setSimplex(simplex);
        checker = new SimpleValueChecker(1e-6, 1e-6);
    }

    /**
     * Test optimize method with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        PointValues result = optimizer.optimize(maxEval, function, goalType, startPoint);
        PointValuePair optPoint = result.getPointValues().get(0);
        assertEquals(goalPoint[0], optPoint.getPoint()[0], 1e-6);
        assertEquals(goalPoint[1], optPoint.getValue(), 1e-6);
    }

    /**
     * Test optimize method with a non-converged case.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonConverged() {
        optimizer.optimize(maxEval, function, goalType, startPoint);
    }

    /**
     * Test optimize method with a null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(maxEval, null, goalType, startPoint);
    }

    /**
     * Test optimize method with a null start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(maxEval, function, goalType, null);
    }

    /**
     * Test optimize method with a null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        optimizer.optimize(maxEval, function, goalType, (OptimizationData[]) null);
    }

    /**
     * Test setSimplex method with an empty simplex.
     */
    @Test(expected = NullArgumentException.class)
    public void testEmptySimplex() {
        optimizer.setSimplex(null);
    }

    /**
     * Test setSimplex method with a simplex of wrong size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongSizeSimplex() {
        optimizer.setSimplex(new AmplSimplex(3));
    }

}

class AmplSimplex extends AbstractSimplex {

    private int size;

    public AmplSimplex(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void build(double[] points) {
        // Empty implementation.
    }

    @Override
    public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
        // Empty implementation.
    }

    @Override
    public PointValuePair[] getPoints() {
        // Empty implementation.
        return new PointValuePair[size];
    }

    @Override
    public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {
        // Empty implementation.
    }

}