package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization of a simple function, using the default constructor.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[] {5.0, 5.0};
        double functionValue = optimizer.optimize(new QuadraticFunction(2.0), startPoint)[1];
        assertEquals(3.1622776601683795e-14, functionValue, 1e-12);
    }

    /**
     * Test optimization of a simple function, using a custom constructor with
     * custom tolerances.
     */
    @Test
    public void testOptimizeSimpleFunctionCustomTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] {5.0, 5.0};
        double functionValue = optimizer.optimize(new QuadraticFunction(2.0), startPoint)[1];
        assertEquals(1.224744871391595e-11, functionValue, 1e-10);
    }

    /**
     * Test optimization of a simple function with a custom convergence checker.
     */
    @Test
    public void testOptimizeSimpleFunctionCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        double[] startPoint = new double[] {5.0, 5.0};
        double functionValue = optimizer.optimize(new QuadraticFunction(2.0), startPoint)[1];
        assertEquals(1.224744871391595e-11, functionValue, 1e-10);
    }

    /**
     * Test optimization of a simple function with a custom line search.
     */
    @Test
    public void testOptimizeSimpleFunctionCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null, null, new CustomLineSearch());
        double[] startPoint = new double[] {5.0, 5.0};
        double functionValue = optimizer.optimize(new QuadraticFunction(2.0), startPoint)[1];
        assertEquals(1.224744871391595e-11, functionValue, 1e-10);
    }

    /**
     * Test optimization of a multivariate function with multiple extremes.
     */
    @Test
    public void testOptimizeMultiExtremeFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[] {1.0, 1.0};
        double[] expectedOptimum = new double[] {1.0, 2.0};
        double functionValue = optimizer.optimize(new MultiExtremeFunction(), startPoint)[1];
        assertArrayEquals(expectedOptimum, optimizer.getLastIterate(), 1e-6);
        assertEquals(0.0, functionValue, 1e-6);
    }

    /**
     * Test optimization of a multivariate function with a singularity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeSingularFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[] {0.0, 0.0};
        optimizer.optimize(new SingularFunction(), startPoint);
    }

    /**
     * Test optimization of a multivariate function with a non-continuous point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNonContinuousFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[] {0.0, 0.0};
        optimizer.optimize(new NonContinuousFunction(), startPoint);
    }

    private static class QuadraticFunction implements UnivariateFunction {
        private final double constant;

        public QuadraticFunction(double constant) {
            this.constant = constant;
        }

        public double value(double x) {
            return Math.pow(x, 2) + constant;
        }
    }

    private static class MultiExtremeFunction implements MultivariateFunction {
        public double value(double[] x) {
            return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2) + 1;
        }

        public double[] gradient(double[] x) {
            double[] grad = new double[2];
            grad[0] = 2 * Math.cos(x[0]) * Math.sin(x[0]) * (1 + Math.sin(x[1]) * Math.sin(x[1]));
            grad[1] = 2 * Math.cos(x[1]) * Math.sin(x[1]) * (1 + Math.sin(x[0]) * Math.sin(x[0]));
            return grad;
        }
    }

    private static class SingularFunction implements MultivariateFunction {
        public double value(double[] x) {
            if (x[0] * x[0] + x[1] * x[1] <= 1) {
                return Math.log(Math.abs(x[0] * x[0] + x[1] * x[1] - 1));
            } else {
                throw new NumberIsTooSmallException(1, 0.0);
            }
        }

        public double[] gradient(double[] x) {
            if (x[0] * x[0] + x[1] * x[1] <= 1) {
                double[] grad = new double[2];
                grad[0] = 2 * x[0] / (Math.abs(x[0] * x[0] + x[1] * x[1] - 1));
                grad[1] = 2 * x[1] / (Math.abs(x[0] * x[0] + x[1] * x[1] - 1));
                return grad;
            } else {
                throw new NumberIsTooSmallException(1, 0.0);
            }
        }
    }

    private static class NonContinuousFunction implements MultivariateFunction {
        public double value(double[] x) {
            if (x[0] >= 0 && x[1] >= 0) {
                return x[0] * x[1];
            } else {
                throw new IllegalArgumentException("Invalid input.");
            }
        }

        public double[] gradient(double[] x) {
            if (x[0] >= 0 && x[1] >= 0) {
                double[] grad = new double[2];
                grad[0] = x[1];
                grad[1] = x[0];
                return grad;
            } else {
                throw new IllegalArgumentException("Invalid input.");
            }
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double[] p1 = previous.getPoint();
            double[] p2 = current.getPoint();
            double[] x1 = previous.getValue();
            double[] x2 = current.getValue();
            double f1 = previous.getValue();
            double f2 = current.getValue();

            double deltaX = 0.0;
            for (int i = 0; i < p1.length; i++) {
                deltaX += Math.pow(p2[i] - p1[i], 2);
            }
            deltaX = Math.sqrt(deltaX);

            double deltaF = Math.abs(f1 - f2);

            return (deltaX < 1e-6 && deltaF < 1e-6);
        }
    }

    private static class CustomLineSearch implements LineSearch {
        @Override
        public UnivariatePointValuePair search(double[] p, double[] d) {
            double alpha = 1.0;
            double[] x = new double[2];
            double fx = PowellOptimizer.this.computeObjectiveValue(p);

            while (true) {
                x = new double[] {p[0] + alpha * d[0], p[1] + alpha * d[1]};
                double fxNew = PowellOptimizer.this.computeObjectiveValue(x);

                if (fxNew < fx) {
                    fx = fxNew;
                    alpha *= 2.0;
                } else {
                    break;
                }
            }

            return new UnivariatePointValuePair(alpha, fx);
        }
    }
}