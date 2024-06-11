package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test multi start optimizer on a simple polynomial function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiStartOptimizerPolynomialFunction() throws Exception {
        final int numStarts = 10;
        final int maxIterations = 50;
        final int maxEvaluations = 100;
        final int numPointsToCheck = 5;

        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, false);
        final RandomDataGenerator generator = new RandomDataGenerator();
        final UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, numStarts, generator);

        // Set optimization parameters
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.setMaximalIterationCount(maxIterations);

        // Test optimization with given bounds
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        final double x = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
        PointsValuePair pvp = optimizer.getLastOptimizationResult();

        // Check results
        final double expectedMinimum = -2.0;
        assertEquals(expectedMinimum, x, 1e-6);
        assertEquals(expectedMinimum, pvp.getValue(), 1e-6);
        assertEquals(GoalType.MINIMIZE, pvp.getGoalType());

        // Check number of iterations and evaluations
        assertEquals(numStarts * maxIterations, multiStartOptimizer.getIterationCount());
        assertEquals(numStarts * maxEvaluations, multiStartOptimizer.getEvaluations());

        // Test optimization with random start points
        for (int i = 0; i < numStarts; ++i) {
            bounds = new SimpleBounds(0.0, 1.0);
            x = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
            pvp = optimizer.getLastOptimizationResult();
            assertArrayEquals(new double[]{x}, new double[]{pvp.getPoint()}, 1e-6);
        }

        // Test optimization with random start points and convergence check
        final double tolerance = 1e-6;
        boolean convergenceFound = false;
        for (int i = 0; i < numStarts && !convergenceFound; ++i) {
            double xi = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound()).getPoint();
            double xiPrev = i > 0 ? multiStartOptimizer.getOptima()[i - 1] : Double.NaN;
            double delta = Math.abs(xi - xiPrev);
            if (delta < tolerance) {
                convergenceFound = true;
            } else {
                System.out.println("Convergence not found with start " + i + ". Current point: " + xi + " Previous point: " + xiPrev +
                                   ". Difference: " + delta + " Tolerance: " + tolerance);
            }
        }
        assertTrue(convergenceFound);

        // Check optimization results
        double[] results = multiStartOptimizer.getOptima();
        double[] values = multiStartOptimizer.getOptimaValues();
        double[] expectedResults = new double[numStarts];
        double[] expectedValues = new double[numStarts];
        for (int i = 0; i < numStarts; ++i) {
            expectedResults[i] = StatUtils.sampleMinimum(new double[]{}, new UnivariateRealFunction() {

                @Override
                public double value(double x) throws FunctionEvaluationException {
                    return f.value(x);
                }
            }, numPointsToCheck)[0];
            expectedValues[i] = f.value(expectedResults[i]);
            assertEquals(expectedResults[i], results[i], 1e-6);
            assertEquals(expectedValues[i], values[i], 1e-6);
        }

    }
}