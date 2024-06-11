package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.Simplex;
import org.junit.Test;

/**
 * Test suite for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** A simple quadratic function for testing. */
    private static final MultivariateFunction FUNCTION = new MultivariateFunction() {
        @Override
        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < x.length; ++i) {
                sum += Math.pow(x[i], 2);
            }
            return sum;
        }
    };

    /** A simplex for testing. */
    private static final Simplex SIMPLEX = new Simplex(3, new ObjectiveFunction() {
        @Override
        public double value(double[] point) {
            return FUNCTION.value(point);
        }
    }, new GoalType(GoalType.MINIMIZE), new SimpleValueChecker(1e-7, 1e-8));

    /**
     * Test optimization of a simple quadratic function.
     * @throws MaxEvalException if optimization is not complete.
     */
    @Test
    public void testOptimization() throws MaxEvalException {
        final int maxEvals = 50;
        final PointValuePair result = new SimplexOptimizer(new SimpleValueChecker(1e-7, 1e-8)).optimize(
                maxEvals, FUNCTION, GoalType.MINIMIZE, new Object[] { SIMPLEX });
        final double[] expected = new double[] { 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0),
                1.0 / Math.sqrt(2.0) };
        assertEquals("Unexpected optimization result.", expected, result.getPoint(), 1e-7);
        assertEquals("Unexpected minimum value.", 0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with an empty simplex.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationEmptySimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(50, FUNCTION, GoalType.MINIMIZE, new Object[0]);
    }

    /**
     * Test optimization of a simple quadratic function with a null simplex.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationNullSimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(50, FUNCTION, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization of a simple quadratic function with a simplex of
     * wrong size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWrongSimplexSize() {
        final Simplex simplex = new Simplex(2, new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return FUNCTION.value(point);
            }
        }, new GoalType(GoalType.MINIMIZE), new SimpleValueChecker(1e-7, 1e-8));
        new SimplexOptimizer().optimize(50, FUNCTION, GoalType.MINIMIZE, new Object[] { simplex });
    }

    /**
     * Test optimization of a simple quadratic function with a convergence
     * checker that does not allow convergence.
     */
    @Test
    public void testOptimizationNonConvergingChecker() {
        final ConvergenceChecker<PointValuePair> nonConvergingChecker
            = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous,
                                    PointValuePair current) {
                return false;
            }
        };
        final SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-7, 1e-8),
                new SimpleValueChecker(1e-7, 1e-8), nonConvergingChecker);
        assertThrows(MaxEvalException.class, () -> optimizer.optimize(50, FUNCTION,
                GoalType.MINIMIZE, new Object[] { SIMPLEX }));
    }

    /**
     * Test optimization of a simple quadratic function with a convergence
     * checker that does not check for convergence.
     */
    @Test
    public void testOptimizationNoCheckingChecker() {
        final ConvergenceChecker<PointValuePair> noCheckingChecker
            = new ConvergenceChecker<PointValuePair>() {
                @Override
                public boolean converged(int iteration, PointValuePair previous,
                                         PointValuePair current) {
                    return true;
                }
            };
        final SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-7, 1e-8),
                noCheckingChecker);
        final PointValuePair result = optimizer.optimize(50, FUNCTION, GoalType.MINIMIZE,
                new Object[] { SIMPLEX });
        final double[] expected = new double[] { 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0),
                1.0 / Math.sqrt(2.0) };
        assertEquals("Unexpected optimization result.", expected, result.getPoint(), 1e-7);
        assertEquals("Unexpected minimum value.", 0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a simple quadratic function with a convergence
     * checker that checks for convergence too early.
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizationTooEarlyChecker() {
        final ConvergenceChecker<PointValuePair> tooEarlyChecker
            = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous,
                                    PointValuePair current) {
                return iteration > 0;
            }
        };
        final SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-7, 1e-8),
                tooEarlyChecker);
        optimizer.optimize(50, FUNCTION, GoalType.MINIMIZE, new Object[] { SIMPLEX });
    }

    /**
     * Test optimization of a noisy function.
     * @throws MaxEvalException if optimization is not complete.
     */
    @Test
    public void testOptimizationNoisyFunction() throws MaxEvalException {
        final MultivariateFunction noisyFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                final double v = FUNCTION.value(x);
                final double noise = Math.random() * 1e-5;
                return v + noise;
            }
        };
        final PointValuePair result = new SimplexOptimizer(new SimpleValueChecker(1e-7, 1e-8))
                .optimize(50, noisyFunction, GoalType.MINIMIZE, new Object[] { SIMPLEX });
        final double[] expected = new double[] { 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0),
                1.0 / Math.sqrt(2.0) };
        assertEquals("Unexpected optimization result.", expected, result.getPoint(), 1e-7);
        assertEquals("Unexpected minimum value.", 0.0, result.getValue(), 1e-8);
    }

}