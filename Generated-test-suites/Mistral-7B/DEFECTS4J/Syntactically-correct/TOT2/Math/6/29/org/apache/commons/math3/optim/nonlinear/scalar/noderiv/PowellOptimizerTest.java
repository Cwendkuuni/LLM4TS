package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.DirectOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.PolynomialFunction;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}
     * with default stopping criterion.
     * @throws Exception
     */
    @org.junit.Test
    public void testDefaultStoppingCriterion() throws Exception {
        double[] startPoint = {2.0, 3.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * Math.sin(x[1]) + 1;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-11);
        assertEquals(0.0, result.getValue(), 1e-11);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * custom stopping criterion.
     * @throws Exception
     */
    @org.junit.Test
    public void testCustomStoppingCriterion() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(1e-5, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, checker);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}
     * with custom stopping criterion and custom line search tolerances.
     * @throws Exception
     */
    @org.junit.Test
    public void testCustomStoppingAndLineSearchTolerances() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(1e-5, 1e-5);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, 1e-3, 1e-3, checker);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     * with custom stopping criterion and custom line search tolerances.
     * @throws Exception
     */
    @org.junit.Test
    public void testCustomStoppingAndLineSearchTolerancesWithCustomChecker() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new CustomConvergenceChecker();
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, 1e-3, 1e-3, checker);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), Precision.EPSILON);
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    /**
     * Test method for empty constructor.
     * @throws Exception
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyConstructorNotStrictlyPositive() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer();
    }

    /**
     * Test method for empty constructor with custom stopping criterion.
     * @throws Exception
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyConstructorCustomStoppingCriterionNotPositive() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new CustomConvergenceChecker(-1.0);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, checker);
    }

    /**
     * Test method for empty constructor with custom stopping criterion and custom line search tolerances.
     * @throws Exception
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyConstructorCustomStoppingAndLineSearchTolerancesNotPositive() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new CustomConvergenceChecker(-1.0);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, -1.0, -1.0, checker);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * with negative absolute tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testCustomStoppingCriterionNegativeAbsoluteTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(-1.0, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, checker);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}
     * with negative absolute tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testDefaultStoppingCriterionNegativeAbsoluteTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, -1.0);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}
     * with negative absolute tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testCustomStoppingAndLineSearchTolerancesNegativeAbsoluteTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, -1.0, -1.0);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     * with negative relative tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testCustomStoppingCriterionNegativeRelativeTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new SimpleUnivariateValueChecker(0.0, -1.0);
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, checker);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}
     * with negative relative tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testDefaultStoppingCriterionNegativeRelativeTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, -1.0);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     * with negative relative tolerance.
     * @throws Exception
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testCustomStoppingAndLineSearchTolerancesNegativeRelativeTolerance() throws Exception {
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2) + 1;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(function, startPoint, -1.0, -1.0, new SimpleUnivariateValueChecker(0.0, -1.0));
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return current.getValue() < 0.01;
        }
    }
}