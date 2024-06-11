package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new MyLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
    }

    /**
     * Test optimization with a simple quadratic function.
     * @throws Exception if optimization fails
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        double[] x = new double[] {1.0, 2.0};
        double[] y = new double[] {5.0, 11.0};
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public int getNumberOfVariables() {
                return x.length;
            }

            @Override
            public double[] getVariables() {
                return x;
            }

            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                double[] grad = new double[x.length];
                grad[0] = 4.0 * (x[0] - 1.0);
                grad[1] = 4.0 * (x[1] - 2.0);
                return grad;
            }
        };

        PointValuePair opt = optimizer.optimize(problem.getValueFunction(), problem.getTarget(), null, x);

        Assert.assertArrayEquals(new double[] {1.0, 2.0}, opt.getPoint(), 0.001);
        Assert.assertEquals(0.25, opt.getValue(), 0.01);
    }

    /**
     * Test optimization with a simple linear function.
     * @throws Exception if optimization fails
     */
    @Test
    public void testLinearFunction() throws Exception {
        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {1.0, 2.0, 3.0};
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public int getNumberOfVariables() {
                return x.length;
            }

            @Override
            public double[] getVariables() {
                return x;
            }

            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1.0, 2.0);
                }
                return sum - Math.pow(1.0, 2.0) - Math.pow(2.0, 2.0) - Math.pow(3.0, 2.0);
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0);
                }
                return grad;
            }
        };

        PointValuePair opt = optimizer.optimize(problem.getValueFunction(), y, null, x);

        Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, opt.getPoint(), 0.001);
        Assert.assertEquals(-0.0, opt.getValue(), 0.01);
    }

    /**
     * Test optimization when maximum number of iterations is exceeded.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        double[] x = new double[] {1.0, 2.0};
        double[] y = new double[] {5.0, 11.0};
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public int getNumberOfVariables() {
                return x.length;
            }

            @Override
            public double[] getVariables() {
                return x;
            }

            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                double[] grad = new double[x.length];
                grad[0] = 4.0 * (x[0] - 1.0);
                grad[1] = 4.0 * (x[1] - 2.0);
                return grad;
            }
        };

        optimizer.setMaxIterations(2);
        optimizer.optimize(problem.getValueFunction(), y, null, x);
    }

    /**
     * Test optimization when maximum number of evaluations is exceeded.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws Exception {
        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {1.0, 2.0, 3.0};
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public int getNumberOfVariables() {
                return x.length;
            }

            @Override
            public double[] getVariables() {
                return x;
            }

            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1.0, 2.0);
                }
                return sum - Math.pow(1.0, 2.0) - Math.pow(2.0, 2.0) - Math.pow(3.0, 2.0);
            }

            @Override
            public double[] gradient(double[] x) throws FunctionEvaluationException {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0);
                }
                return grad;
            }
        };

        optimizer.setMaxEvaluations(1);
        optimizer.optimize(problem.getValueFunction(), y, null, x);
    }

}