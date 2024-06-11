package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test method for {@link GaussNewtonOptimizer#GaussNewtonOptimizer()}.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#GaussNewtonOptimizer(boolean)}.
     */
    @Test
    public void testConstructorWithUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#GaussNewtonOptimizer(boolean, ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithUseLUAndChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertEquals(true, optimizer.isUseLU());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#GaussNewtonOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertEquals(false, optimizer.isUseLU());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(double[] x) {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)
                        + Math.pow(x[2] - 3.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0) * (x[i] - 1.0) + 2.0 * (x[i] - 2.0) * (x[i] - 2.0)
                            + 2.0 * (x[i] - 3.0) * (x[i] - 3.0);
                }
                return grad;
            }
        };
        optimizer.init(targetValues, weightMatrix, function);
        PointValuePair solution = optimizer.optimize();
        double[] point = solution.getPoint();
        double cost = optimizer.getFunctionValue(point);
        assertEquals(1.0, point[0], 1e-6);
        assertEquals(1.0, point[1], 1e-6);
        assertEquals(1.0, point[2], 1e-6);
        assertEquals(1.0, cost, 1e-6);
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-5, 1e-5);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(double[] x) {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)
                        + Math.pow(x[2] - 3.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0) * (x[i] - 1.0) + 2.0 * (x[i] - 2.0) * (x[i] - 2.0)
                            + 2.0 * (x[i] - 3.0) * (x[i] - 3.0);
                }
                return grad;
            }
        };
        optimizer.init(targetValues, weightMatrix, function);
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null));
        optimizer.optimize();
        PointValuePair solution = optimizer.getLastOptimizationResult();
        double[] point = solution.getPoint();
        double cost = optimizer.getFunctionValue(point);
        assertEquals(1.0, point[0], 1e-6);
        assertEquals(1.0, point[1], 1e-6);
        assertEquals(1.0, point[2], 1e-6);
        assertEquals(1.0, cost, 1e-6);
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(double[] x) {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)
                        + Math.pow(x[2] - 3.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0) * (x[i] - 1.0) + 2.0 * (x[i] - 2.0) * (x[i] - 2.0)
                            + 2.0 * (x[i] - 3.0) * (x[i] - 3.0);
                }
                return grad;
            }
        };
        optimizer.init(targetValues, weightMatrix, function);
        PointVectorValuePair solution = optimizer.optimize();
        double[] point = solution.getPoint();
        double cost = optimizer.getFunctionValue(point);
        assertEquals(1.0, point[0], 1e-6);
        assertEquals(1.0, point[1], 1e-6);
        assertEquals(1.0, point[2], 1e-6);
        assertEquals(1.0, cost, 1e-6);
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithUseLUAndChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-5, 1e-5);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(double[] x) {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)
                        + Math.pow(x[2] - 3.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0) * (x[i] - 1.0) + 2.0 * (x[i] - 2.0) * (x[i] - 2.0)
                            + 2.0 * (x[i] - 3.0) * (x[i] - 3.0);
                }
                return grad;
            }
        };
        optimizer.init(targetValues, weightMatrix, function);
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null));
        optimizer.optimize();
        PointVectorValuePair solution = optimizer.getLastOptimizationResult();
        double[] point = solution.getPoint();
        double cost = optimizer.getFunctionValue(point);
        assertEquals(1.0, point[0], 1e-6);
        assertEquals(1.0, point[1], 1e-6);
        assertEquals(1.0, point[2], 1e-6);
        assertEquals(1.0, cost, 1e-6);
    }

    /**
     * Test method for {@link GaussNewtonOptimizer#optimize()}.
     */
    @Test
    public void testOptimizeWithSingularWeightMatrix() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-5, 1e-5);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(double[] x) {
                return 0.5 * Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)
                        + Math.pow(x[2] - 3.0, 2.0), 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    grad[i] = 2.0 * (x[i] - 1.0) * (x[i] - 1.0) + 2.0 * (x[i] - 2.0) * (x[i] - 2.0)
                            + 2.0 * (x[i] - 3.0) * (x[i] - 3.0);
                }
                return grad;
            }
        };
        optimizer.init(targetValues, weightMatrix, function);
        assertThrows(SingularMatrixException.class, optimizer::optimize);
    }
}