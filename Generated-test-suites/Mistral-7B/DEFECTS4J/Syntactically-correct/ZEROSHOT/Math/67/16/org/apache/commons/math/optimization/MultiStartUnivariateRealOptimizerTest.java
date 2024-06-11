package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwisterFast;

/**
 * Test for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}
     * constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwisterFast());
        assertEquals(optimizer, optimizerMS.getOptimizer());
        assertEquals(10, optimizerMS.getStarts());
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}
     * method.
     */
    @org.junit.Test
    public void testGetIterationCount() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        optimizerMS.optimize(new PolynomialFunction(1.0), GoalType.MINIMIZE, 1.0, 2.0);
        assertEquals(11 * 5, optimizerMS.getIterationCount());
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}
     * method.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
        optimizerMS.optimize(new PolynomialFunction(1.0), GoalType.MINIMIZE, 1.0, 2.0);
        assertEquals(11 * 5, optimizerMS.getEvaluations());
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#getOptima()}
     * method.
     */
    @org.junit.Test
    public void testGetOptima() throws Exception {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());

        UnivariateRealFunction f = new PolynomialFunction(1.0);
        double[] optimaExpected = {1.61803398875, 1.61796519563, 1.61800333267, 1.61803172229, 1.61803311927};
        double[] optimaValuesExpected = {-0.61803398875, -0.61803398875, -0.61803427593, -0.61803350763, -0.61803311927};

        optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 2.0);
        double[] optima = optimizerMS.getOptima();
        double[] optimaValues = optimizerMS.getOptimaValues();

        assertArrayEquals(optimaExpected, optima);
        assertArrayEquals(optimaValuesExpected, optimaValues);
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}
     * method.
     */
    @org.junit.Test
    public void testGetOptimaValues() throws Exception {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());

        UnivariateRealFunction f = new PolynomialFunction(1.0);
        double[] optimaValuesExpected = {-0.61803398875, -0.61803398875, -0.61803427593, -0.61803350763, -0.61803311927};

        optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 2.0);
        double[] optimaValues = optimizerMS.getOptimaValues();

        assertArrayEquals(optimaValuesExpected, optimaValues);
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * method.
     */
    @org.junit.Test
    public void testOptimize() throws Exception {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());

        UnivariateRealFunction f = new PolynomialFunction(1.0);
        double min = 0.0;
        double max = 2.0;
        double result = optimizerMS.optimize(f, GoalType.MINIMIZE, min, max);
        double expected = 1.61803398875;
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * method.
     */
    @org.junit.Test
    public void testOptimizeStartValue() throws Exception {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());

        UnivariateRealFunction f = new PolynomialFunction(1.0);
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        double result = optimizerMS.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        double expected = 1.61803398875;
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test for {@link MultiStartUnivariateRealOptimizer#getOptima()} method
     * when optimization did not converge.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testNoOptima() throws Exception {
        UnivariateRealOptimizer optimizer = new GoldenSectionSearch();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());

        UnivariateRealFunction f = new PolynomialFunction(-1.0);
        optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 2.0);

        double[] optima = optimizerMS.getOptima();
        assertNull(optima);

        double[] optimaValues = optimizerMS.getOptimaValues();
        assertNull(optimaValues);
    }

}