package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private static final int MAX_EVALS = 10;
    private static final int NUM_STARTS = 5;
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private UnivariateRealFunction function;
    private RandomGenerator random;

    @Before
    public void setUp() {
        optimizer = new FibonacciMinimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new int[]{3, 2, 1});
    }

    @Test
    public void testOptimize() {
        try {
            PointValuePair best = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            assertNotNull(best);
            assertEquals(0.0, best.getPoint().doubleValue(), 1e-6);
            assertEquals(-1.0, best.getValue().doubleValue(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        try {
            multiStartOptimizer.optimize(new PointValuePairFunction() {
                @Override
                public double value(double x) {
                    return x * x;
                }

                @Override
                public double derivative(double x) {
                    return 2.0 * x;
                }
            }, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOptima() {
        try {
            PointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0);
            assertArrayEquals(new PointValuePair[]{new PointValuePair(0.0, -1.0), null, null, null, null}, optima);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMaxEvaluations() {
        optimizer = new DifferingSimpleBrentMethod();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());

        try {
            multiStartOptimizer.setMaxEvaluations(MAX_EVALS);
            PointValuePair best = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            assertEquals(MAX_EVALS, multiStartOptimizer.getEvaluations());
            assertNotNull(best);
            assertEquals(0.0, best.getPoint().doubleValue(), 1e-6);
            assertEquals(-1.0, best.getValue().doubleValue(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEmptyOptimaIfNoConvergence() {
        try {
            optimizer = new DifferingSimpleBrentMethod();
            multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
            multiStartOptimizer.setMaxEvaluations(MAX_EVALS);

            PointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 10 * MAX_EVALS);
            assertArrayEquals(new PointValuePair[NUM_STARTS], optima);
            for (PointValuePair pointValuePair : optima) {
                assertNull(pointValuePair);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSortOptima() {
        try {
            optimizer = new FibonacciMinimizer();
            multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());

            PointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0);
            assertArrayEquals(new PointValuePair[]{new PointValuePair(0.0, -1.0), null, null, null, null}, optima);

            multiStartOptimizer.sortOptima(GoalType.MINIMIZE);

            PointValuePair[] sortedOptima = multiStartOptimizer.getOptima();
            assertArrayEquals(new PointValuePair[]{new PointValuePair(0.0, -1.0), null, null, null, null}, sortedOptima);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSortOptimaMaximizing() {
        try {
            optimizer = new FibonacciMaximizer();
            multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());

            PointValuePair[] optima = multiStartOptimizer.optimize(function.negate(), GoalType.MAXIMIZE, -5.0, 5.0, 0);
            assertArrayEquals(new PointValuePair[]{new PointValuePair(0.0, 1.0), null, null, null, null}, optima);

            multiStartOptimizer.sortOptima(GoalType.MAXIMIZE);

            PointValuePair[] sortedOptima = multiStartOptimizer.getOptima();
            assertArrayEquals(new PointValuePair[]{new PointValuePair(0.0, 1.0), null, null, null, null}, sortedOptima);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer = new DifferingSimpleBrentMethod();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(PointValuePair pointValuePair) {
                return false;
            }

            @Override
            public String getFailureMessage() {
                return "convergence checker failure";
            }
        };

        multiStartOptimizer.setConvergenceChecker(convergenceChecker);

        try {
            PointValuePair best = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            assertNull(best);
            assertTrue(multiStartOptimizer.isConverged());
            assertEquals("convergence checker failure", multiStartOptimizer.getConvergenceChecker().getFailureMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetConvergenceChecker() {
        optimizer = new DifferingSimpleBrentMethod();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(PointValuePair pointValuePair) {
                return false;
            }

            @Override
            public String getFailureMessage() {
                return "convergence checker failure";
            }
        };
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);

        ConvergenceChecker<PointValuePair> checker = multiStartOptimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertEquals(convergenceChecker, checker);
    }

    @Test
    public void testSingleStartOptimizer() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> single = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, new MersenneTwister());
        try {
            PointValuePair best = single.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            assertEquals(0.0, best.getPoint().doubleValue(), 1e-6);
            assertEquals(-1.0, best.getValue().doubleValue(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testZeroStarts() {
        try {
            MultiStartUnivariateRealOptimizer<UnivariateRealFunction> zero = new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new MersenneTwister());
            zero.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            assertFalse(true);
        } catch (Exception e) {
            // Expected
        }
    }
}

class FibonacciMinimizer extends DifferencingMinimizer {
    @Override
    protected double bracketingInterval(UnivariateRealFunction f, double x, double xPrev, double xNext) {
        return Math.abs(xNext - xPrev);
    }

    @Override
    protected double initialInterval(UnivariateRealFunction f, double x) {
        return 1.0;
    }
}

class FibonacciMaximizer extends DifferencingMaximizer {
    @Override
    protected double bracketingInterval(UnivariateRealFunction f, double x, double xPrev, double xNext) {
        return Math.abs(xNext - xPrev);
    }

    @Override
    protected double initialInterval(UnivariateRealFunction f, double x) {
        return 1.0;
    }
}