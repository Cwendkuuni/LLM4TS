package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorialFunction;
import org.apache.commons.math3.optim.nonlinear.VectorFunction;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.simpleiteratives.LevenbergMarquardtOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimpleLeastSquaresOptimizer();
    }

    /**
     * Test optimization when objective function is a linear regression.
     */
    @Test
    public void testLinearRegression() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 5.0, 6.0, 8.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        SimpleVectorialFunction f = new SimpleVectorialFunction(x, y, w);
        PointValuePair result = optimizer.optimize(f, startPoint);

        double[] expected = {1.0, 1.0, 1.0, 1.0, 1.0};
        Assert.assertArrayEquals(expected, result.getPoint(), 0.001);
        Assert.assertEquals(2.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization when objective function is a linear regression with
     * no solution.
     */
    @Test(expected = NoSolutionException.class)
    public void testLinearRegressionNoSolution() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 3.0, 5.0, 6.0, 8.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        SimpleVectorialFunction f = new SimpleVectorialFunction(x, y, w);
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization when objective function is a quadratic regression.
     */
    @Test
    public void testQuadraticRegression() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 9.0, 16.0, 25.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        VectorFunction g = new VectorFunction(x, 3.0);
        Gradient h = new Gradient(g);

        SimpleLineSearch lineSearch = new SimpleLineSearch(h, 0.01, 1.0, 0.1, 100);
        StoppingCondition stoppingCondition = new StoppingCondition() {
            @Override
            public boolean isSatisfied(int iteration, double fx, double x[]) {
                return fx < 1.0e-12;
            }
        };

        LevenbergMarquardtOptimizer lm = new LevenbergMarquardtOptimizer(g, optimizer.getJacobian(),
                optimizer.getConvergenceChecker(), stoppingCondition, lineSearch);

        PointValuePair result = lm.optimize(new VectorFunction(x, f -> f[0] * f[0] + 2.0 * f[1] + 3.0),
                                            startPoint);

        double[] expected = {1.0, 1.0, 1.0, 1.0, 1.0};
        Assert.assertArrayEquals(expected, result.getPoint(), 0.001);
        Assert.assertEquals(5.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization when maximal number of iterations is exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 9.0, 16.0, 25.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        SimpleVectorialFunction f = new SimpleVectorialFunction(x, y, w);

        optimizer.setMaxIterations(0);
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization when maximal number of evaluations is exceeded.
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 9.0, 16.0, 25.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        SimpleVectorialFunction f = new SimpleVectorialFunction(x, y, w);

        optimizer.setMaxEvaluations(0);
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization when unbounded domain is encountered.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedDomain() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {-Float.POSITIVE_INFINITY, 4.0, 9.0, 16.0, 25.0};
        double[] w = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[x.length];

        SimpleVectorialFunction f = new SimpleVectorialFunction(x, y, w);

        optimizer.optimize(f, startPoint);
    }

}