package org.apache.commons.math.optimization;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciOptimizer(), 10, new MersenneTwister(17L)) {
            @Override
            public void setMaxIterations(int maxIterations) {
                super.setMaxIterations(Math.min(maxIterations, 100));
            }
        };
    }

    @Test
    public void testSingleStartOptimization() {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double result = optimizer.getResult();
        double expected = Math.PI / 2;
        assertEquals(expected, result, 1e-5);
    }

    @Test
    public void testMultiStartOptimization() {
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        // there should be at least one valid optimum
        assertFalse(Double.isNaN(optima[0]));
        assertTrue(Math.abs(function.value(optima[0]) - Math.PI / 2) < 1e-5);

        // all other optima should be NaN
        for (int i = 1; i < optima.length; i++) {
            assertTrue(Double.isNaN(optima[i]));
            assertTrue(Double.isNaN(optimaValues[i]));
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testNoOptimumFound() {
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI + x);
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Division by zero");
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testMaxEvaluationReached() {
        optimizer.setMaxEvaluations(2);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        // there should be one valid optimum
        assertFalse(Double.isNaN(optima[0]));
        assertTrue(Math.abs(function.value(optima[0]) - Math.PI / 2) < 1e-5);

        // the other optima should be NaN
        for (int i = 1; i < optima.length; i++) {
            assertTrue(Double.isNaN(optima[i]));
            assertTrue(Double.isNaN(optimaValues[i]));
        }

        // the total number of evaluations should match the limit
        assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testIterationCountReached() {
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        // there should be one valid optimum
        assertFalse(Double.isNaN(optima[0]));
        assertTrue(Math.abs(function.value(optima[0]) - Math.PI / 2) < 1e-5);

        // the other optima should be NaN
        for (int i = 1; i < optima.length; i++) {
            assertTrue(Double.isNaN(optima[i]));
            assertTrue(Double.isNaN(optimaValues[i]));
        }

        // the total number of iterations should match the limit
        assertEquals(1, optimizer.getIterationCount());
    }
}