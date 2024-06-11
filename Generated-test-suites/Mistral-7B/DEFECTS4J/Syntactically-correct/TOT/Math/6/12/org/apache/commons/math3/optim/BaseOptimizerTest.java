package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingBoxMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.UnivariateOptimizer;
import org.apache.commons.math3.optim.initialization.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SufficientDecreaseChecker;
import org.apache.commons.math3.special.Fibonacci;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.BaseOptimizer.PAIR;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    private ObjectiveFunction testFunction;
    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Before
    public void setUp() {
        testFunction = new ObjectiveFunctionGenerator(new TestObjectiveFunction()).generateFunction();
        optimizer = new SimpleBoundingBoxMinimizer(testFunction, new SimpleBoundingBox(-10, 10, -10, 10),
                new InitialGuess(-5), GoalType.MINIMIZE);
        convergenceChecker = new SufficientDecreaseChecker(0.1);
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    /**
     * Test getMaxEvaluations() method.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEval = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of function evaluations should be set", 100, maxEval);
    }

    /**
     * Test getEvaluations() method.
     */
    @Test
    public void testGetEvaluations() {
        int numEval = optimizer.getEvaluations();
        assertEquals("Number of function evaluations should be zero at start", 0, numEval);
        
        optimizer.optimize();
        numEval = optimizer.getEvaluations();
        assert(numEval > 0);
    }

    /**
     * Test getConvergenceChecker() method.
     */
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> checker = optimizer.getConvergenceChecker();
        assertNotNull("Convergence checker should not be null", checker);
        assertEquals("Convergence checker should be the one set", convergenceChecker, checker);
    }

    /**
     * Test optimization with a simple optimizer.
     */
    @Test
    public void testOptimizeSimple() {
        optimizer.optimize();
        
        PAIR result = optimizer.getOptimum();
        assertNotNull("Optimization should have found an optimum", result);
        
        double optimum = result.getPoint().getComponent(0);
        double objective = result.getValue();
        
        // Check that the result is correct
        double expectedOptimum = Fibonacci. golden(new TestObjectiveFunction().getValue(new double[] { -5.0 }),
                1.61803398875);
        double expectedObjective = testFunction.value(new double[] { expectedOptimum });
        assertEquals("Optimum should match expected value", Precision.round(expectedOptimum, 6), optimum, 1E-6);
        assertEquals("Objective value should match expected value", Precision.round(expectedObjective, 6), objective, 1E-6);
    }

    /**
     * Test optimization with a Fibonacci search optimizer.
     */
    @Test
    public void testOptimizeFibonacci() {
        optimizer = new FibonacciSearchOptimizer(testFunction, GoalType.MINIMIZE, new MaxEval(1000));
        optimizer.optimize();
        
        PAIR result = optimizer.getOptimum();
        assertNotNull("Optimization should have found an optimum", result);
        
        double optimum = result.getPoint().getComponent(0);
        double objective = result.getValue();
        
        // Check that the result is correct
        double expectedOptimum = Fibonacci. golden(new TestObjectiveFunction().getValue(new double[] { -5.0 }),
                1.61803398875);
        double expectedObjective = testFunction.value(new double[] { expectedOptimum });
        assertEquals("Optimum should match expected value", Precision.round(expectedOptimum, 6), optimum, 1E-6);
        assertEquals("Objective value should match expected value", Precision.round(expectedObjective, 6), objective, 1E-6);
    }

    /**
     * Test optimization with a non-converging objective function.
     */
    @Test(expected = IllegalStateException.class)
    public void testOptimizeNonConverging() {
        optimizer = new SimpleBoundingBoxMinimizer(new TestObjectiveFunctionNonConverging(), new SimpleBoundingBox(-10, 10, -10, 10),
                new InitialGuess(-5), GoalType.MINIMIZE);
        optimizer.optimize();
    }

    /**
     * Objective function for testing.
     */
    private static class TestObjectiveFunction implements ObjectiveFunction {

        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.cos(x[0]), 2);
        }

        @Override
        public double value(double[] x, double[] gradient, int index, double step) {
            double value = value(x);
            if (gradient != null) {
                gradient[index] = 2 * (Math.cos(x[0]) * 2 * Math.sin(x[0]) - Math.sin(x[0]) * 2 * Math.cos(x[0]));
            }
            return value;
        }

    }

    /**
     * Objective function for testing with non-converging behavior.
     */
    private static class TestObjectiveFunctionNonConverging implements ObjectiveFunction {

        @Override
        public double value(double[] x) {
            if (Arrays.stream(x).anyMatch(d -> d > 0)) {
                throw new IllegalStateException("Testing non-converging objective function");
            }
            return Math.pow(Math.sin(Math.abs(x[0])), 2) + Math.pow(Math.cos(Math.abs(x[0])), 2);
        }

        @Override
        public double value(double[] x, double[] gradient, int index, double step) {
            double value = value(x);
            if (gradient != null) {
                gradient[index] = 2 * (Math.cos(Math.abs(x[0])) * 2 * Math.sin(Math.abs(x[0])) - Math.sin(Math.abs(x[0])) * 2 * Math.cos(Math.abs(x[0])));
            }
            return value;
        }

    }

}