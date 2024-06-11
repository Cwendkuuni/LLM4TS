package org.apache.commons.math.optimization.mock; // add a mock package for testing

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private MultiStartUnivariateRealOptimizer testOptimizer;

    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(-x) - E.E;
            }
        };
        testOptimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(), 5, new MersenneTwisterFastRandom());
    }

    @Test
    public void testOptimize() throws Exception {
        double optimaSum = 0.0;
        double optimaValuesSum = 0.0;

        for (int i = 0; i < testOptimizer.getStarts(); i++) {
            double result = testOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 10.0);
            double optima = testOptimizer.getOptima()[i];
            double optimaValue = testOptimizer.getOptimaValues()[i];

            assertEquals(GoalType.MINIMIZE, testOptimizer.getGoalType(), 0.0);
            assertEquals(0.0, testOptimizer.getMin(), 0.0);
            assertEquals(10.0, testOptimizer.getMax(), 0.0);
            assertEquals(result, optima, 0.001);
            assertEquals(-E.E, optimaValue, 0.001);

            optimaSum += optima;
            optimaValuesSum += optimaValue;
        }

        double expectedOptimaSum = 0.0;
        double expectedOptimaValuesSum = -5.0 * E.E;
        for (int i = 0; i < testOptimizer.getStarts(); i++) {
            expectedOptimaSum += Math.exp(-i / 5.0);
        }

        assertEquals(expectedOptimaSum, optimaSum, 0.001);
        assertEquals(expectedOptimaValuesSum, optimaValuesSum, 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithoutCall() throws Exception {
        testOptimizer.getOptima();
    }
}