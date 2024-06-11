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
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test the optimizer with a simple linear problem.
     */
    @Test
    public void testLinearProblem() {
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] startPoint = {1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        LinearObjectiveFunction objective = new LinearObjectiveFunction(targetValues, weightMatrix);
        optimizer.setObjectiveFunction(objective);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{2.0, 2.0}, result.getPoint(), 0.0);
        Assert.assertArrayEquals(new double[]{-1.0e-10, -1.0e-10}, result.getValue(), 1.0e-10);
    }

    /**
     * Test the optimizer with a quadratic problem.
     */
    @Test
    public void testQuadraticProblem() {
        double[] targetValues = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        LinearObjectiveFunction objective = new LinearObjectiveFunction(targetValues, weightMatrix);
        optimizer.setObjectiveFunction(objective);
        optimizer.setStartPoint(startPoint);
        optimizer.setMaxIterations(100);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize();
        double[] expected = {1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)};
        Assert.assertArrayEquals(expected, optimizer.getPoint(), 1.0e-8);
    }

    /**
     * Test the optimizer with a nonlinear problem.
     */
    @Test
    public void testNonlinearProblem() {
        double[] targetValues = {Math.exp(1.0), Math.exp(2.0), Math.exp(3.0)};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        LinearObjectiveFunction linearObjective = new LinearObjectiveFunction(targetValues, weightMatrix);
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new PointValuePair(new double[]{1.0, 1.0, 1.0}, 0.0)),
                new LinearConstraint(new PointValuePair(new double[]{2.0, 2.0, 2.0}, 1.0))
        };
        LinearOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setObjectiveFunction(new MaxEval(new NonlinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.exp(linearObjective.value(point)) - 1.0;
            }

            @Override
            public double[] gradient(double[] point) {
                RealMatrix jacobian = optimizer.getObjectiveFunction().getValue(point);
                double[] grad = new double[jacobian.getDimension()];
                for (int i = 0; i < jacobian.getDimension(); ++i) {
                    grad[i] = jacobian.getEntry(i, 0);
                }
                return grad;
            }

            @Override
            public int getDimension() {
                return linearObjective.getDimension();
            }
        }));
        optimizer.setConstraints(constraints);
        optimizer.setStartPoint(startPoint);
        optimizer.setMaxIterations(100);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize();
        double[] expected = {Math.E / Math.sqrt(3.0), Math.E / Math.sqrt(3.0), Math.E / Math.sqrt(3.0)};
        Assert.assertArrayEquals(expected, optimizer.getPoint(), 1.0e-8);
    }

    /**
     * Test the optimizer with a problem for which the normal equations are
     * singular.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingularProblem() {
        double[] targetValues = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        LinearObjectiveFunction objective = new LinearObjectiveFunction(targetValues, weightMatrix);
        optimizer.setObjectiveFunction(objective);
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
    }

    /**
     * Test the optimizer with a problem for which the starting point is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        double[] targetValues = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        LinearObjectiveFunction objective = new LinearObjectiveFunction(targetValues, weightMatrix);
        optimizer.setObjectiveFunction(objective);
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Test the optimizer with a problem for which the target values are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullTargetValues() {
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test the optimizer with a problem for which the weight matrix is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        double[] targetValues = {1.0, 1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setObjectiveFunction(new LinearObjectiveFunction(targetValues, null));
        optimizer.optimize();
    }
}

class NonlinearObjectiveFunction {
    @Override
    public double value(double[] point) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] gradient(double[] point) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDimension() {
        throw new UnsupportedOperationException();
    }
}