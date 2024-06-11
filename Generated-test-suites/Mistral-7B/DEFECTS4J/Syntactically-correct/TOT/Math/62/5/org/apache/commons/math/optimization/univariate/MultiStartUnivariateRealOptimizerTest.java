package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.MultistartOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SuccessiveParabolicMethodOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction f1;
    private UnivariateRealFunction f2;
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    @Before
    public void setUp() {
        f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x * x + 2;
            }
        };
        f2 = new UnivariateRealFunction() {
            public double value(double x) {
                throw new MathArithmeticException("This function throws MathArithmeticException on purpose");
            }
        };
        optimizer = new SuccessiveParabolicMethodOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
    }

    @Test
    public void testMinimization() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        PointValuePair result = multiStartOptimizer.optimize(f1, goal, min, max);
        assertArrayEquals(new double[]{1.0, f1.value(1.0)}, result.getPointValuePair(), 0.001);

        // Test with fixed starting points
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizerFixed =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        for (int i = 0; i < 3; i++) {
            double startValue = Math.random() * (max - min) + min;
            PointValuePair fixedStartResult = multiStartOptimizerFixed.optimize(f1, goal, min, max, startValue);
            assertEquals(result.getValue(), fixedStartResult.getValue(), 0.001);
            assertEquals(result.getPoint(), fixedStartResult.getPoint(), 0.001);
        }

        // Test with maxEvaluations
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        multiStartOptimizer.setMaxEvaluations(5);
        try {
            multiStartOptimizer.optimize(f1, goal, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }
    }

    @Test
    public void testMaximization() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;

        PointValuePair result = multiStartOptimizer.optimize(f1, goal, min, max);
        assertEquals(new double[]{1.0, f1.value(1.0)}, result.getPointValuePair(), 0.001);

        // Test with fixed starting points
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizerFixed =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        for (int i = 0; i < 3; i++) {
            double startValue = Math.random() * (max - min) + min;
            PointValuePair fixedStartResult = multiStartOptimizerFixed.optimize(f1, goal, min, max, startValue);
            assertEquals(result.getValue(), fixedStartResult.getValue(), 0.001);
            assertEquals(-result.getPoint(), fixedStartResult.getPoint(), 0.001);
        }

        // Test with function throwing MathArithmeticException
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        try {
            multiStartOptimizer.optimize(f2, goal, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }
    }

    @Test
    public void testInitialGuess() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        PointValuePair result = multiStartOptimizer.optimize(f1, goal, min, max, startValue);
        double expected = 2.0;
        assertEquals(expected, result.getValue(), 0.001);
        assertEquals(new InitialGuess(expected), result.getPoint(), 0.001);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        multiStartOptimizer.setMaxEvaluations(3);

        try {
            multiStartOptimizer.optimize(f1, goal, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }
    }
}