package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static final int MAX_EVALUATIONS = 100;
    private static final int NUM_STARTS = 3;

    private UnivariateOptimizer<PolynomialFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        randomGenerator = multiStartOptimizer.getGenerator();
    }

    @After
    public void tearDown() {
        randomGenerator = null; // to free the random number generator
    }

    /**
     * Test optimization of a quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;

        try {
            final PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
            assertEquals("The result is not correct", -2.0, result.getPoint(), 0.001);
            assertEquals("The value is not correct", 2.0, result.getValue(), 0.001);
            assertArrayEquals("The optima are not correct", new double[]{0.0, -2.0}, getOptima(f, goal, min, max)[0].getPoint(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test optimization of a function with a local minimum and a global minimum.
     */
    @Test
    public void testFunctionWithLocalAndGlobalMinima() {
        final PolynomialFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0, -2.0, 1.0});
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        try {
            final PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
            assertEquals("The result is not correct", -4.0, result.getPoint(), 0.001);
            assertEquals("The value is not correct", 4.0, result.getValue(), 0.001);
            assertArrayEquals("The optima are not correct", new double[]{3.0, -2.0, -3.0, 2.0}, getOptima(f, goal, min, max));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test optimization of a function that does not converge.
     */
    @Test
    public void testFunctionThatDoesNotConverge() {
        final PolynomialFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        try {
            multiStartOptimizer.optimize(f, goal, min, max);
            fail();
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test optimization of a function with a singularity.
     */
    @Test
    public void testFunctionWithSingularity() {
        final PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, -1.0});
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;

        try {
            multiStartOptimizer.optimize(f, goal, min, max);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Returns the optima found by the optimizer.
     *
     * @param f The objective function.
     * @param goal The optimization goal.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return An array containing the optima found by the optimizer.
     * @throws Exception If optimization fails.
     */
    private PointValuePair[] getOptima(PolynomialFunction f, GoalType goal, double min, double max) throws Exception {
        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS);
        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, min, max);
        return multiStartOptimizer.getOptima();
    }

}