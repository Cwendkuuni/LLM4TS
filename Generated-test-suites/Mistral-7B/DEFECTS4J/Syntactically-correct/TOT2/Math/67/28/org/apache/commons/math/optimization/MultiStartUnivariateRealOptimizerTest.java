package org.apache.commons.math.optimization;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Test cases for the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealOptimizer optimizer = new org.apache.commons.math3.optim.SimpleXOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        assertEquals("Wrong number of starts", 3, multiStartOptimizer.getStarts());
        assertNotNull("Underlying optimizer is null", multiStartOptimizer.getOptimizer());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double expected = function.value(2.0);
        assertEquals("Wrong result", expected, result, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double[] optima = multiStartOptimizer.getOptima();
        assertEquals("Unexpected number of optima", 3, optima.length);
        for (int i = 0; i < 3; ++i) {
            assertTrue("Optima is not valid", multiStartOptimizer.isValidPoint(optima[i]));
            double value = multiStartOptimizer.value(optima[i]);
            assertEquals("Unexpected value at optima[" + i + "]", function.value(optima[i]), value, 0.001);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}.
     */
    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertEquals("Unexpected number of optima values", 3, optimaValues.length);
        for (int i = 0; i < 3; ++i) {
            assertEquals("Unexpected value at optimaValues[" + i + "]", function.value(optima[i]), optimaValues[i], 0.001);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        double startValue = 5.0;
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        double expected = function.value(startValue);
        assertEquals("Wrong result", expected, result, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#isValidPoint(double)}.
     */
    @Test
    public void testIsValidPoint() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double point = 2.0;
        assertTrue("Invalid point", multiStartOptimizer.isValidPoint(point));
        point = Double.NaN;
        assertFalse("Invalid point", multiStartOptimizer.isValidPoint(point));
        point = -1.0;
        assertFalse("Invalid point", multiStartOptimizer.isValidPoint(point));
        point = max + 1.0;
        assertFalse("Invalid point", multiStartOptimizer.isValidPoint(point));
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#value(double)}.
     */
    @Test
    public void testValue() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 2, -1}, 3);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(new org.apache.commons.math3.optim.SimpleXOptimizer(), 3, new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator());
        double min = -10;
        double max = 10;
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double point = 2.0;
        double value = multiStartOptimizer.value(point);
        assertEquals("Unexpected value", function.value(point), value, 0.001);
    }

}