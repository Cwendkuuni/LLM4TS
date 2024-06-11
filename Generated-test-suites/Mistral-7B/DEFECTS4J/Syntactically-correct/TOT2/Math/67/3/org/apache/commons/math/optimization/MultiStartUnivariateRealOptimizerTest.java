package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optim.initializers.GaussianRandomVectorGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealFunction;
import org.apache.commons.math3.optim.univariate.UnivariateRealIterator;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.univariate.nonlinear.LocalMinimizer;
import org.apache.commons.math3.optim.univariate.nonlinear.SimpleBoundingMethod;
import org.apache.commons.math3.optim.univariate.nonlinear.diffev.DiffEvMultiStart;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() {

        // Test with a simple function
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        UnivariateRealOptimizer optimizer = new DiffEvMultiStart(new SimpleBoundingMethod(), new MaxEval(1000));
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new RandomValuesGenerator());
        GoalType goalType = GoalType.MINIMIZE;

        double[] xOptima = multiStartOptimizer.optimize(f, goalType, -10.0, 10.0, null);
        double[] xExpectedOptima = {-2.44965324131253, -2.44963838782547, -2.44964119020973, -2.44964002184841, -2.44963999821386, -2.44963999895834, -2.44963999898762, -2.44963999900163, -2.44963999900613, -2.44963999901118};
        assertArrayEquals("Optima are not correct", xExpectedOptima, xOptima, 0.001);

        // Test with a function with multiple local minima
        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, false);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new LocalMinimizer(new SimpleBoundingMethod(), new MaxEval(1000)), 10, new RandomValuesGenerator());
        xOptima = multiStartOptimizer.optimize(f2, goalType, -10.0, 10.0, null);
        double[] xExpectedOptima2 = {-3.43113727719227, -2.14275928385471, -0.948432327547348, -0.347138603622527, 0.346349790424334, 0.94656207979975, 1.94587532113043, 2.94543681229743, 3.94513533516128, 4.9449473555263};
        assertArrayEquals("Optima are not correct", xExpectedOptima2, xOptima, 0.001);

        // Test with a function with a single global maximum
        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{-1.0, -1.0, 1.0}, false);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new LocalMinimizer(new SimpleBoundingMethod(), new MaxEval(1000)), 10, new RandomValuesGenerator());
        goalType = GoalType.MAXIMIZE;
        xOptima = multiStartOptimizer.optimize(f3, goalType, -10.0, 10.0, null);
        double[] xExpectedOptima3 = {9.44995273280748, 9.44994566860656, 9.44994570021527, 9.44994570109131, 9.44994570115364, 9.44994570116892, 9.4499457011742, 9.44994570117857, 9.44994570118085, 9.44994570118233};
        assertArrayEquals("Optima are not correct", xExpectedOptima3, xOptima, 0.001);

        // Test with a function that does not converge
        UnivariateRealFunction f4 = new PolynomialFunction(new double[]{0.0, 1.0, 0.0}, false);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new LocalMinimizer(new SimpleBoundingMethod(), new MaxEval(1000)), 10, new RandomValuesGenerator());
        xOptima = multiStartOptimizer.optimize(f4, goalType, -10.0, 10.0, null);
        assertTrue("Optima found even with a non-converging function", xOptima[0] == Double.NaN);

        // Test with a function that throws an exception
        UnivariateRealFunction f5 = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("An exception occurred!");
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new LocalMinimizer(new SimpleBoundingMethod(), new MaxEval(1000)), 10, new RandomValuesGenerator());
        try {
            xOptima = multiStartOptimizer.optimize(f5, goalType, -10.0, 10.0, null);
            fail("A FunctionEvaluationException should have been thrown!");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test the getOptima() and getOptimaValues() methods
        multiStartOptimizer.optimize(f3, goalType, -10.0, 10.0, null);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals("Optima values are not correct", xExpectedOptima3, multiStartOptimizer.getOptima(), 0.001);
        assertArrayEquals("Optima values are not correct", optimaValues, new double[]{9.44994570118233, 9.44994570117857, 9.4499457011742, 9.44994570116892, 9.44994570115364, 9.44994570113924, 9.44994570111945, 9.44994570109957, 9.4499457010796, 9.44994570105963}, 0.001);

    }
}