package org.apache.commons.math.optimization;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private RandomGenerator random;

    @Before
    public void setUp() {
        function = new TestFunction();
        optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexSearch(), 3, new MersenneTwister());
    }

    @Test
    public void testOptimize() throws Exception {
        optimizer.setMaxEvaluations(50);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        PointValues result = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{0.5, 1.5, 2.5}, result.toArray(), 0.001);
    }

    @Test
    public void testGetIterationCount() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        int iterationCount = optimizer.getIterationCount();
        Assert.assertTrue(iterationCount >= 3 * 50);
    }

    @Test
    public void testGetEvaluations() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        int evaluations = optimizer.getEvaluations();
        Assert.assertTrue(evaluations >= 3 * 50);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() throws Exception {
        optimizer.getOptima();
    }

    @Test
    public void testGetOptimaAfterOptimize() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        double[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(new double[]{0.5, 1.5, 2.5}, optima, 0.001);
    }

    @Test
    public void testGetFunctionValuesAtOptima() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        double[] functionValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{0.25, 1.0, 2.25}, functionValues, 0.001);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexSearch(), 3, new MersenneTwister());
        optimizer.setMaxEvaluations(15);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        PointValues result = optimizer.getOptimaValues();
        List<PointValuePair> pairs = new ArrayList<>();
        for (double optima : result) {
            pairs.add(new PointValuePair(optima, function.value(optima)));
        }
        MaxEval maxEval = new MaxEval(15);
        PointValues reference = new NelderMeadSimplexSearch().optimize(function, maxEval, new SimpleBounds(-10.0, 10.0));
        for (int i = 0; i < result.getNumberPoints(); i++) {
            Assert.assertEquals(reference.getValue(i), pairs.get(i).getValue(), 0.001);
        }
    }

    private static class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            if (x < 0) {
                return x * x;
            } else {
                return Math.pow(x, 2) - 0.5 * Math.pow(Math.E, x);
            }
        }

        @Override
        public double derivative(double x) {
            if (x < 0) {
                return 2.0 * x;
            } else {
                double value = Math.pow(x, 2) - 0.5 * Math.E;
                return Math.pow(x, 2) + Math.pow(Math.E, x);
            }
        }

        @Override
        public double derivative(double x, int direction) {
            return derivative(x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}