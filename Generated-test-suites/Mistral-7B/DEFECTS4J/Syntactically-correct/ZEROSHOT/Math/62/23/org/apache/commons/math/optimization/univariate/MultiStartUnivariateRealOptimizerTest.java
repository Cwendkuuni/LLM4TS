package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        // Create a test function.
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 0, 1});

        // Create a classical optimizer.
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new SimpleBoundingMethod(f, new InitialGuess(0.5), new MaxEval(100), null);

        // Create a multi-start optimizer.
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);

        // Test the number of starts and evaluations.
        assertTrue(multiStartOptimizer.getStarts() == 3);
        assertTrue(multiStartOptimizer.getMaxEvaluations() == optimizer.getMaxEvaluations());
        assertTrue(multiStartOptimizer.getEvaluations() == 0);

        // Test optimization with a given boundary.
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);

        // Test the convergence checker.
        ConvergenceChecker<PointValuePair> convergenceChecker = multiStartOptimizer.getConvergenceChecker();
        assertTrue(result != null);
        assertTrue(convergenceChecker.isConverged(result));

        // Test the number of evaluations.
        assertTrue(multiStartOptimizer.getEvaluations() > optimizer.getMaxEvaluations());

        // Test the optima.
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{result}, optima);

        // Test optimization with a given boundary and initial point.
        double startValue = 5.0;
        result = multiStartOptimizer.optimize(f, goal, min, max, startValue);

        // Test the convergence checker.
        assertTrue(result != null);
        assertTrue(convergenceChecker.isConverged(result));

        // Test the optima.
        optima = multiStartOptimizer.getOptima();
        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[]{result, optima[1], optima[2]};
        assertArrayEquals(expectedOptima, optima);

        // Test optimization with a given boundary and a convergence exception.
        optimizer = new SimpleBoundingMethod(f, new InitialGuess(2.0), new MaxEval(1), null);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        try {
            result = multiStartOptimizer.optimize(f, goal, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Ok: expected ConvergenceException.
        }

        // Test the optima.
        optima = multiStartOptimizer.getOptima();
        assertNull(optima[0]);
        assertArrayEquals(new UnivariateRealPointValuePair[]{}, Arrays.copyOfRange(optima, 1, optima.length));

        // Test optimization with a given boundary and a FunctionEvaluationException.
        optimizer = new SimpleBoundingMethod(f, new InitialGuess(3.0), new MaxEval(1), null);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        try {
            result = multiStartOptimizer.optimize(f, goal, min, max);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Ok: expected FunctionEvaluationException.
        }

        // Test the optima.
        optima = multiStartOptimizer.getOptima();
        assertNull(optima[0]);
        assertArrayEquals(new UnivariateRealPointValuePair[]{}, optima);

        // Test optimization with a given boundary and a MaxEvalExceededException.
        optimizer = new SimpleBoundingMethod(f, new InitialGuess(4.0), new MaxEval(0), null);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        try {
            multiStartOptimizer.optimize(f, goal, min, max);
            fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Ok: expected MaxEvalExceededException.
        }

        // Test the optima.
        optima = multiStartOptimizer.getOptima();
        assertNull(optima[0]);
        assertArrayEquals(new UnivariateRealPointValuePair[]{}, optima);
    }
}