package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * A simple test function.
     */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        @Override
        public double value(double[] x) {
            double sum = 0;
            for (int i = 0; i < x.length; i++) {
                sum += x[i] * x[i];
            }
            return sum;
        }

        @Override
        public int getNumberOfVariables() {
            return x.length;
        }
    };

    /**
     * A test point.
     */
    private static final double[] TEST_POINT = new double[]{1.0, 2.0, 3.0};

    /**
     * Tests optimization with the default constructor and default
     * parameters.
     */
    @Test
    public void testDefaultOptimization() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, TEST_POINT);
        double[] expected = new double[]{0.0, 0.0, 0.0};
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expected, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    /**
     * Tests optimization with user-defined parameters.
     */
    @Test
    public void testCustomOptimization() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double[] previousPoint = previous.getPoint();
                double[] currentPoint = current.getPoint();
                double delta = 0;
                for (int i = 0; i < currentPoint.length; i++) {
                    double diff = FastMath.abs(currentPoint[i] - previousPoint[i]);
                    if (diff > delta) {
                        delta = diff;
                    }
                }
                return FastMath.abs(current.getValue() - previous.getValue()) < 0.01 * (FastMath.abs(previous.getValue()) + FastMath.abs(current.getValue())) + 0.001 * delta;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01, checker);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, TEST_POINT);
        double[] expected = new double[]{0.0, 0.0, 0.0};
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expected, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    /**
     * Tests optimization with a custom convergence checker that stops the
     * algorithm earlier.
     */
    @Test
    public void testEarlyStopping() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return iter > 10;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01, checker);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, TEST_POINT);
        double[] expected = new double[]{1.0, 1.5, 2.0};
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertFalse(FastMath.arrayEqual(expected, result.getPoint(), 0.01));
        assertFalse(result.getValue() < 3.0);
    }

    /**
     * Tests optimization with a custom convergence checker that does not
     * stop the algorithm.
     */
    @Test(expected = org.apache.commons.math3.exception.IterationLimitExceededException.class)
    public void testNoStopping() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01, checker);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, TEST_POINT);
        fail("The optimization should have thrown an IterationLimitExceededException.");
    }

    /**
     * Tests optimization with invalid inputs.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(-0.01, 0.01);
    }

    /**
     * Tests optimization with invalid inputs.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidAbsThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -0.01);
    }

    /**
     * Tests optimization with a null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        new PowellOptimizer(0.01, 0.01, null);
    }

    /**
     * Tests optimization with a null multivariate function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullMultivariateFunction() {
        new PowellOptimizer(0.01, 0.01, null, null, null).optimize(null, TEST_POINT);
    }

    /**
     * Tests optimization with a null start point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullStartPoint() {
        new PowellOptimizer(0.01, 0.01, null, null, null).optimize(TEST_FUNCTION, null);
    }
}