package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.StartingPointGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** A simple test function for optimization. */
    static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x);
        }
    }

    /** A function to generate random starting points. */
    static class RandomStartingPointGenerator implements StartingPointGenerator<Double> {
        private final RandomDataGenerator generator;

        public RandomStartingPointGenerator() {
            this.generator = new RandomDataGenerator();
        }

        public Double generateStartingPoint(Double minimum, Double maximum) {
            return minimum + generator.nextDouble() * (maximum - minimum);
        }
    }

    /** The optimizer to test. */
    private UnivariateOptimizer<TestFunction> optimizer;

    /** The multi-start optimizer to test. */
    private MultiStartUnivariateRealOptimizer<TestFunction> multiOptimizer;

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiStartMinimization() throws Exception {
        // Initialize the optimizer and the multi-start optimizer.
        TestFunction f = new TestFunction();
        optimizer = new Fibonacci16GoldenSectionSearch(f);
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomStartingPointGenerator());

        // Set the maximum number of evaluations.
        maxEvaluations(multiOptimizer, 100);

        // Perform optimization.
        PointValuePair result = multiOptimizer.optimize(GoalType.MINIMIZE, -5, 5);

        // Check the result.
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertEquals(-3.14159265359, result.getPoint(), 1e-6);

        // Check the optima.
        PointValuePair[] optima = multiOptimizer.getOptima();
        Assert.assertEquals(10, optima.length);
        for (PointValuePair p : optima) {
            Assert.assertNotNull(p);
            if (p != result) {
                Assert.assertNull(p.getPoint());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MathIllegalStateException.class)
    public void testMultiStartNoConvergence() throws Exception {
        // Initialize the optimizer and the multi-start optimizer.
        TestFunction f = new TestFunction();
        optimizer = new Fibonacci16GoldenSectionSearch(f);
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomStartingPointGenerator());

        // Set the maximum number of evaluations.
        maxEvaluations(multiOptimizer, 10);

        // Perform optimization.
        multiOptimizer.optimize(GoalType.MINIMIZE, -5, 5);

        // This should throw an exception.
        multiOptimizer.optimize(GoalType.MINIMIZE, -5, 5);
    }

    @Test
    public void testMultiStartMaximization() throws Exception {
        // Initialize the optimizer and the multi-start optimizer.
        TestFunction f = new TestFunction();
        optimizer = new Fibonacci16GoldenSectionSearch(f);
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomStartingPointGenerator());

        // Set the optimizer to maximize.
        multiOptimizer.setGoalType(GoalType.MAXIMIZE);

        // Set the maximum number of evaluations.
        maxEvaluations(multiOptimizer, 100);

        // Perform optimization.
        PointValuePair result = multiOptimizer.optimize(GoalType.MAXIMIZE, -5, 5);

        // Check the result.
        Assert.assertNotNull(result);
        Assert.assertEquals(Math.PI / 2, result.getValue(), 1e-6);
        Assert.assertEquals(3.14159265359, result.getPoint(), 1e-6);

        // Check the optima.
        PointValuePair[] optima = multiOptimizer.getOptima();
        Assert.assertEquals(10, optima.length);
        for (PointValuePair p : optima) {
            Assert.assertNotNull(p);
            if (p != result) {
                Assert.assertNull(p.getPoint());
            }
        }
    }

    /**
     * Sets the maximum number of evaluations for the optimizer.
     *
     * @param optimizer The optimizer.
     * @param evaluations The maximum number of evaluations.
     */
    private void maxEvaluations(MultiStartUnivariateRealOptimizer<?> optimizer, int evaluations) {
        optimizer.setMaxEvaluations(evaluations);
        BaseOptimizer<?> baseOptimizer = optimizer.getOptimizer();
        baseOptimizer.setMaxEvaluations(evaluations);
    }
}