package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;

import java.util.Random;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @org.junit.Test
    public void testBrentOptimizer() {
        final double epsilon = 1e-8;
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
            }
        };

        final UnivariateOptimizerResult result = optimizer.optimize(f, -10, 10);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertEquals(0.36038197, result.getPoint().getX(), epsilon);
        Assert.assertEquals(-0.47156329, result.getValue(), epsilon);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testBrentOptimizerWithChecker() {
        final double epsilon = 1e-8;
        final double maxIterations = 1000;

        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            private int iter = 0;

            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                ++iter;
                if (iter >= maxIterations) {
                    return true;
                }
                return false;
            }
        });

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
            }
        };

        final UnivariateOptimizerResult result = optimizer.optimize(f, -10, 10);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertNotNull(result.getPoint());
        Assert.assertNotNull(result.getValue());
        Assert.assertEquals(-1, result.getIterations());
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * negative relative tolerance.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testNegativeRelativeThreshold() {
        final BrentOptimizer optimizer = new BrentOptimizer(-1e-5, 1e-5);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * zero absolute threshold.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testZeroAbsoluteThreshold() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * zero relative threshold.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testZeroRelativeThreshold() {
        final BrentOptimizer optimizer = new BrentOptimizer(0, 1e-5);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)} with
     * infinite number of iterations.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testInfiniteIterations() {
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });
        optimizer.optimize(new IdentityFunction(), 0, 1);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * a quadratic function.
     */
    @org.junit.Test
    public void testQuadraticFunction() {
        final double epsilon = 1e-8;
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2 * x - 4;
            }
        };

        final UnivariateOptimizerResult result = optimizer.optimize(f, -10, 10);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertEquals(-2.0, result.getPoint().getX(), epsilon);
        Assert.assertEquals(0.0, result.getValue(), epsilon);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * a sinus function.
     */
    @org.junit.Test
    public void testSinusFunction() {
        final double epsilon = 1e-8;
        final double tolerance = 1e-6;
        final BrentOptimizer optimizer = new BrentOptimizer(tolerance, tolerance);

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        final UnivariateOptimizerResult result = optimizer.optimize(f, -5 * Math.PI, 5 * Math.PI);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertEquals(-Math.PI / 2, result.getPoint().getX(), tolerance);
        Assert.assertEquals(0.0, result.getValue(), epsilon);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * a custom convergence checker.
     */
    @org.junit.Test
    public void testCustomConvergenceChecker() {
        final double epsilon = 1e-8;
        final double tolerance = 0.001;

        final BrentOptimizer optimizer = new BrentOptimizer(tolerance, tolerance, new ConvergenceChecker<UnivariatePointValuePair>() {
            private int iter = 0;
            private final double toleranceSquare = tolerance * tolerance;

            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                ++iter;
                if (iter >= 100 || (current.getValue() - previous.getValue()) <= toleranceSquare) {
                    return true;
                }
                return false;
            }
        });

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x) / (1 + Math.exp(-x));
            }
        };

        final UnivariateOptimizerResult result = optimizer.optimize(f, -10, 10);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertEquals(1.6333228746756332, result.getPoint().getX(), epsilon);
        Assert.assertEquals(0.6454783512133845, result.getValue(), epsilon);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * a custom convergence checker and a random initial point.
     */
    @org.junit.Test
    public void testCustomConvergenceCheckerWithRandomInitialPoint() {
        final double epsilon = 1e-8;
        final double tolerance = 0.01;

        final BrentOptimizer optimizer = new BrentOptimizer(tolerance, tolerance, new ConvergenceChecker<UnivariatePointValuePair>() {
            private int iter = 0;
            private final double toleranceSquare = tolerance * tolerance;

            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                ++iter;
                if (iter >= 100 || (current.getValue() - previous.getValue()) <= toleranceSquare) {
                    return true;
                }
                return false;
            }
        });

        final Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x) / (1 + Math.exp(-x));
            }
        };

        final Random random = new Random();
        double initialPoint = random.nextDouble() * 20;
        UnivariateOptimizerResult result;

        result = optimizer.optimize(f, initialPoint, initialPoint + 1);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
        Assert.assertTrue(result.getPoint().getX() >= initialPoint);
        Assert.assertTrue(result.getPoint().getX() <= initialPoint + 1);
        Assert.assertEquals(1.6333228746756332, result.getPoint().getX(), epsilon);
        Assert.assertEquals(0.6454783512133845, result.getValue(), epsilon);
    }
}

interface Function {
    double value(double x);
}

class IdentityFunction implements Function {
    @Override
    public double value(double x) {
        return x;
    }
}