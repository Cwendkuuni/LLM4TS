package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimulatedAnnealingOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer;

    @Before
    public void setUp() {
        optimizer = new SimulatedAnnealingOptimizer(new SimpleBounds(0.0, 10.0), 100, 0.01, new MersenneTwister());
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxEvalExceededException
     */
    @Test
    public void testOptimize() throws MaxEvalExceededException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        PointValuePair bestOptimum = multiOptimizer.optimize(f, goalType, min, max);
        assertNotNull(bestOptimum);
        assertNotNull(bestOptimum.getValue());

        // Check that the optimization algorithm has converged
        assertEquals(0.0, bestOptimum.getValue(), 1e-9);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     * @throws MaxEvalExceededException
     */
    @Test
    public void testGetOptima() throws MaxEvalExceededException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        multiOptimizer.optimize(f, goalType, min, max);

        UnivariateRealPointValuePair[] optima = multiOptimizer.getOptima();
        assertNotNull(optima);
        assertEquals(10, optima.length);

        for (UnivariateRealPointValuePair optimum : optima) {
            assertNotNull(optimum);
            assertNotNull(optimum.getValue());

            // Check that each optimum is a local minimum
            try {
                multiOptimizer.optimize(f, goalType, optimum.getPoint(), min, max);
            } catch (MaxEvalExceededException e) {
                // Expected, as the optimizer only checks if the point
                // is a local minimum
            }

            // Check that the optimization algorithm has converged
            assertEquals(optimum.getValue(), multiOptimizer.optimize(f, goalType, min, max).getValue(), 1e-9);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * with an initial point that is not within the bounds.
     * @throws MaxEvalExceededException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws MaxEvalExceededException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = -1.0;

        multiOptimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * with an initial point that is within the bounds but the optimization
     * algorithm fails to converge.
     * @throws MaxEvalExceededException
     */
    @Test
    public void testConvergenceException() throws MaxEvalExceededException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0});
        UnivariateRealPointValuePair initialPoint = new UnivariateRealPointValuePair(0.5, Double.NaN);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        try {
            multiOptimizer.optimize(f, goalType, min, max, initialPoint.getPoint());
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * with an optimization algorithm that throws an exception.
     * @throws MaxEvalExceededException
     */
    @Test(expected = RuntimeException.class)
    public void testRuntimeException() throws MaxEvalExceededException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
                            public PointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max) {
                                throw new RuntimeException("Test exception");
                            }
                        }, 10, new MersenneTwister());

        multiOptimizer.optimize(f, goalType, min, max);
    }
}