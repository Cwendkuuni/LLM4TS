package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Test for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test multi-start optimization with a simple function.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testMultiStartOptimizationSimpleFunction() throws Exception {
        RandomGenerator rg = new MersenneTwister(System.currentTimeMillis());

        UnivariateRealFunction f = new PolynomialFunction(2, new double[] { -1., 1., -2. });
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, rg);
        GoalType goalType = GoalType.MINIMIZE;

        double min = -5.;
        double max = 5.;
        double x = multiOptimizer.optimize(f, goalType, min, max);
        PointValuePair pvp = multiOptimizer.getOptima();

        assertEquals(x, pvp.getPoint(), 1e-9);
        assertEquals(f.value(x), pvp.getValue(), 1e-9);
    }

    /**
     * Test multi-start optimization with a function that has multiple local minima.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testMultiStartOptimizationMultipleMinima() throws Exception {
        RandomGenerator rg = new MersenneTwister(System.currentTimeMillis());

        UnivariateRealFunction f = new PolynomialFunction(2, new double[] { -1., 2., 3. });
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, rg);
        GoalType goalType = GoalType.MINIMIZE;

        double min = -5.;
        double max = 5.;
        double[] optima = multiOptimizer.getOptima();
        double[] optimaValues = multiOptimizer.getOptimaValues();

        assertEquals(3, optima.length);
        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1 < 3 ? -(i + 1) * (i + 1) : Double.NaN, optima[i], 1e-9);
            assertEquals(i + 1 < 3 ? -(i + 1) * (i + 1) : Double.NaN, optimaValues[i], 1e-9);
        }
    }

    /**
     * Test multi-start optimization with a function that has no solution.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testMultiStartOptimizationNoSolution() throws Exception {
        RandomGenerator rg = new MersenneTwister(System.currentTimeMillis());

        UnivariateRealFunction f = new PolynomialFunction(1, new double[] { 1. });
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, rg);
        GoalType goalType = GoalType.MINIMIZE;

        double min = -5.;
        double max = 5.;
        assertThrows(OptimizationException.class, () -> multiOptimizer.optimize(f, goalType, min, max));
    }

    /**
     * Test multi-start optimization with a function that throws an exception
     * during evaluation.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testMultiStartOptimizationExceptionEvaluation() throws Exception {
        RandomGenerator rg = new MersenneTwister(System.currentTimeMillis());

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x >= 0) {
                    throw new FunctionEvaluationException("Evaluation failed");
                }
                return Math.sin(Math.PI * x);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, rg);
        GoalType goalType = GoalType.MINIMIZE;

        double min = -5.;
        double max = 5.;
        assertThrows(OptimizationException.class, () -> multiOptimizer.optimize(f, goalType, min, max));
    }

}