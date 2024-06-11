package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization of a simple function with a single minimum.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleFunction() throws Exception {
        final UnivariateRealOptimizer optimizer = new FischerFrentzOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwisterFast());
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});

        final double result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals("Optimum not found in the expected range", 0.0, result, 1e-10);
    }

    /**
     * Test optimization of a function with a single maximum.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleFunctionMax() throws Exception {
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwisterFast());
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0}, new double[]{0.0});

        final double result = multiStartOptimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertEquals("Optimum not found in the expected range", 1.0, result, 1e-10);
    }

    /**
     * Test optimization of a function with multiple local minima and maxima.
     * @throws Exception if an error occurs
     */
    @Test
    public void testComplexFunction() throws Exception {
        final UnivariateRealOptimizer optimizer = new HookeJeevesOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwisterFast());
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, -1.0, 2.0}, new double[]{0.0});

        final double[] optima = multiStartOptimizer.getOptima();
        final double[] optimaValues = multiStartOptimizer.getOptimaValues();

        Assert.assertEquals("Unexpected number of optima", 10, optima.length);
        Assert.assertEquals("Unexpected number of optima values", 10, optimaValues.length);

        final double[] expectedOptima = {-2.639128433, -1.86936311, -1.17515935, 0.373977915, 2.276859361, 3.184663424, 3.669271281, 4.630123122, 5.495534639, 6.15413118};
        final double[] expectedOptimaValues = {-3.542256313, -2.483833939, -1.33967438, 0.241381436, 2.250778916, 3.198273972, 3.69525073, 4.620680477, 5.505201843, 5.98340466};

        for (int i = 0; i < optima.length; ++i) {
            Assert.assertEquals("Optima not found in the expected range", expectedOptima[i], optima[i], 1e-10);
            Assert.assertEquals("Optima values not found in the expected range", expectedOptimaValues[i], optimaValues[i], 1e-10);
        }
    }

    /**
     * Test optimization of a function that does not converge.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergentFunction() throws Exception {
        final UnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwisterFast());
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{Double.NEGATIVE_INFINITY});

        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with illegal arguments.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() throws Exception {
        final UnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 0, new MersenneTwisterFast());
    }
}