package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.Gradient;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.StartPointGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private SimpleOptimizationProblem problem;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(false);
        problem = new SimpleOptimizationProblem(new LinearFunction(new double[]{-1.0, 1.0}),
                new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2),
                new SimpleBounds(-10.0, 10.0), new Gradient() {
                    @Override
                    public double value(RealVector x) {
                        return -x.getEntry(0) * x.getEntry(0) - 2 * x.getEntry(0) * x.getEntry(1) + x.getEntry(1) * x.getEntry(1);
                    }

                    @Override
                    public double[] gradient(RealVector x) {
                        double[] grad = new double[x.getDimension()];
                        grad[0] = -2 * x.getEntry(0) - 2 * x.getEntry(1);
                        grad[1] = -2 * x.getEntry(0) + 2 * x.getEntry(1);
                        return grad;
                    }
                });
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
    }

    /**
     * Test the optimization of a simple linear regression problem with
     * identity weight matrix.
     */
    @Test
    public void testOptimizeLinearRegression() {
        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.001);
    }

    /**
     * Test the optimization of a simple non-linear regression problem with
     * identity weight matrix.
     */
    @Test
    public void testOptimizeNonlinearRegression() {
        optimizer = new GaussNewtonOptimizer(false);
        problem = new SimpleOptimizationProblem(new LinearFunction(new double[]{-1.0, 1.0}),
                new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2),
                new SimpleBounds(-10.0, 10.0), new Gradient() {
                    @Override
                    public double value(RealVector x) {
                        return Math.pow(Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4, 2);
                    }

                    @Override
                    public double[] gradient(RealVector x) {
                        double[] grad = new double[x.getDimension()];
                        grad[0] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(0);
                        grad[1] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(1);
                        return grad;
                    }
                });

        PointValuePair solution = optimizer.optimize(problem);
        assertEquals(new double[]{-2.0, 2.0}, solution.getPoint(), 0.001);
    }

    /**
     * Test the optimization of a simple non-linear regression problem with
     * unequal weights.
     */
    @Test
    public void testOptimizeNonlinearRegressionWithUnequalWeights() {
        optimizer = new GaussNewtonOptimizer(false);
        problem = new SimpleOptimizationProblem(new LinearFunction(new double[]{-1.0, 1.0}),
                new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2),
                new SimpleBounds(-10.0, 10.0), new Gradient() {
                    @Override
                    public double value(RealVector x) {
                        return Math.pow(Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4, 2);
                    }

                    @Override
                    public double[] gradient(RealVector x) {
                        double[] grad = new double[x.getDimension()];
                        grad[0] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(0);
                        grad[1] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(1);
                        return grad;
                    }
                });
        weightMatrix = new BlockRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 3.0}});

        PointValuePair solution = optimizer.optimize(problem, weightMatrix);
        assertEquals(new double[]{-1.6, 1.6}, solution.getPoint(), 0.001);
    }

    /**
     * Test the optimization with an invalid weight matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidWeightMatrix() {
        optimizer = new GaussNewtonOptimizer(false);
        problem = new SimpleOptimizationProblem(new LinearFunction(new double[]{-1.0, 1.0}),
                new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2),
                new SimpleBounds(-10.0, 10.0), new Gradient() {
                    @Override
                    public double value(RealVector x) {
                        return Math.pow(Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4, 2);
                    }

                    @Override
                    public double[] gradient(RealVector x) {
                        double[] grad = new double[x.getDimension()];
                        grad[0] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(0);
                        grad[1] = 4 * (Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) - 4) * 2 * x.getEntry(1);
                        return grad;
                    }
                });
        weightMatrix = new BlockRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        optimizer.optimize(problem, weightMatrix);
    }
}