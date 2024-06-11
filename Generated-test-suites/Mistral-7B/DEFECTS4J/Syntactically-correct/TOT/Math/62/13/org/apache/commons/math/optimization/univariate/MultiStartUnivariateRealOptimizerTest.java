package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalings.LinearScaling;
import org.apache.commons.math3.optim.nonlinear.scalings.SquareRootScaling;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    /**
     * Test optimization with a simple function.
     * @throws Exception if an error occurs during optimization.
     */
    @Test
    public void testSimpleFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchUnivariateOptimizer(new LinearScaling()), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(-3.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);

        Assert.assertArrayEquals(new PointValuePair[] {
            new PointValuePair(-3.0, 0.0), new PointValuePair(2.0, 4.0), new PointValuePair(5.0, 25.0),
            new PointValuePair(7.0, 49.0), new PointValuePair(8.0, 64.0), new PointValuePair(9.0, 81.0)},
            optimizer.getOptima());
    }

    /**
     * Test optimization with a function having multiple roots.
     * @throws Exception if an error occurs during optimization.
     */
    @Test
    public void testMultipleRoots() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x * x + 1;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionSearch(), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertEquals(-1.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.25, result.getValue(), 1e-6);

        Assert.assertArrayEquals(new PointValuePair[] {
            new PointValuePair(-1.0, 0.25), new PointValuePair(1.0, 0.25), new PointValuePair(2.0, 2.0)},
            optimizer.getOptima());
    }

    /**
     * Test optimization with a function having no roots.
     * @throws Exception if an error occurs during optimization.
     */
    @Test(expected = NoRootsException.class)
    public void testNoRoots() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2.0) - 1.0;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with a function having a bracketed interval.
     * @throws Exception if an error occurs during optimization.
     */
    @Test
    public void testBracketedInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x);
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean checkConvergence(PointValuePair pair) {
                return Math.abs(pair.getValue()) < 0.001;
            }
        });

        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
        Assert.assertEquals(-1.7121, result.getPoint(), 1e-6);
        Assert.assertEquals(-1.7121, result.getValue(), 1e-6);

        Assert.assertArrayEquals(new PointValuePair[] {new PointValuePair(-1.7121, -1.7121)}, optimizer.getOptima());
    }

    /**
     * Test optimization with a function having no bracketed interval.
     * @throws Exception if an error occurs during optimization.
     */
    @Test(expected = NoBracketException.class)
    public void testNoBracketedInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x);
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });

        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 0.0);
    }

    /**
     * Test optimization with a function having a maximum number of evaluations.
     * @throws Exception if an error occurs during optimization.
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / Math.sqrt(Math.abs(x));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new RandomGenerator() {
                    @Override
                    public double nextGaussian() {
                        return 0;
                    }
                });
        optimizer.setMaxEvaluations(10);

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }
}