package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BracketFinder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test a simple function with a unique minimum.
     */
    @Test
    public void testSimpleFunction() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test a simple function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + 3 * y * y;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test a simple function with no solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoSolution() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y - 4.0;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
    }

    /**
     * Test a simple function with an error in the optimizer.
     */
    @Test(expected = ArithmeticException.class)
    public void testError() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0 / 0.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
    }

    /**
     * Test a multivariate function with a unique minimum.
     */
    @Test
    public void testMultivariateFunction() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0, 1.0};
        final MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                return x * x + y * y + z * z;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test a multivariate function with multiple local minima.
     */
    @Test
    public void testMultivariateFunctionMultipleLocalMinima() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0, 1.0};
        final MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                return x * x + 3 * y * y + 2 * z * z;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test a multivariate function with no solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultivariateNoSolution() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0, 1.0};
        final MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                return x * x + y * y + z * z - 4.0;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
    }

    /**
     * Test a multivariate function with an error in the optimizer.
     */
    @Test(expected = ArithmeticException.class)
    public void testMultivariateError() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] startPoint = new double[]{1.0, 1.0, 1.0 / 0.0};
        final MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                return x * x + y * y + z;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
    }

    /**
     * Test the custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8) {
            @Override
            protected ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean converged(int iteration,
                                             PointValuePair previous,
                                             PointValuePair current) {
                        final double delta = FastMath.abs(previous.getValue() - current.getValue());
                        final boolean result = delta < 1e-5;
                        if (result) {
                            System.out.println("Convergence checker triggered.");
                        }
                        return result;
                    }
                };
            }
        };
        final double[] startPoint = new double[]{1.0, 1.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        assertTrue(System.out.toString().contains("Convergence checker triggered."));
    }

    /**
     * Test the custom convergence checker with no convergence.
     */
    @Test
    public void testCustomConvergenceCheckerNoConvergence() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8) {
            @Override
            protected ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean converged(int iteration,
                                             PointValuePair previous,
                                             PointValuePair current) {
                        return false;
                    }
                };
            }
        };
        final double[] startPoint = new double[]{1.0, 1.0};
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.0);
        assertFalse(System.out.toString().contains("Convergence checker triggered."));
    }
}