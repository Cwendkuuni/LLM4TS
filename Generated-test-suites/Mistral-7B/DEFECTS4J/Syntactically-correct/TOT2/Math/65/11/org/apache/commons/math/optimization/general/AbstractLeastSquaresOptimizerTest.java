package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.Guess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private ObjectiveFunction function;
    private double[][] data;
    private double[] weights;

    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
        function = new MyObjectiveFunction(data, weights);
    }

    @Test
    public void testOptimizeBasic() {
        double[] startPoint = new double[] {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, null, null, startPoint);
        assertArrayEquals(new double[] {3.0, 4.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeExceptionNullData() {
        data = null;
        weights = new double[0];
        try {
            optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, new double[0]);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeExceptionWrongDataSize() {
        data = new double[1][2];
        weights = new double[3];
        try {
            optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, new double[2]);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testOptimizeExceptionWrongWeightsSize() {
        data = new double[2][2];
        weights = new double[1];
        try {
            optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, new double[2]);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeExceptionMaxIterations() {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(0);
        data = new double[][] {{1.0, 2.0}, {3.0, 4.0}};
        weights = new double[] {1.0, 1.0};
        try {
            optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, new double[2]);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testOptimizeCustomInitialPoint() {
        double[] startPoint = new double[] {5.0, 6.0};
        PointValuePair result = optimizer.optimize(function, null, null, startPoint);
        // The result is not known, just checking that the optimization did not crash
        assertTrue(result != null);
    }

    @Test
    public void testOptimizeCustomInitializer() {
        double[][] data = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setInitializer(new AroundMinimumInitializer(new Guess(data[0], 0.1)));
        PointValuePair result = optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, null);
        // The result is not known, just checking that the optimization did not crash
        assertTrue(result != null);
    }

    @Test
    public void testOptimizeConstraints() {
        double[][] data = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        LinearConstraint constraint = new LinearConstraint(new double[] {1.0, 1.0}, 0.0);
        LinearConstraints constraints = new LinearConstraints();
        constraints.add(constraint);
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setConstraints(constraints);
        PointValuePair result = optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, null);
        // The result is not known, just checking that the optimization did not crash
        assertTrue(result != null);
    }

    @Test
    public void testOptimizeGoalType() {
        double[][] data = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setGoalType(GoalType.ABSOLUTE);
        PointValuePair result = optimizer.optimize(new MyObjectiveFunction(data, weights), null, null, null);
        // The result is not known, just checking that the optimization did not crash
        assertTrue(result != null);
    }

    private static class MyObjectiveFunction implements ObjectiveFunction {

        private final double[][] data;
        private final double[] weights;

        public MyObjectiveFunction(double[][] data, double[] weights) {
            this.data = data;
            this.weights = weights;
        }

        @Override
        public double value(double[] point) throws FunctionEvaluationException {
            double sum = 0.0;
            for (int i = 0; i < data.length; i++) {
                double error = 0.0;
                for (int j = 0; j < data[i].length; j++) {
                    error += Math.pow(data[i][j] - point[j], 2.0);
                }
                sum += Math.pow(error, 0.5) * weights[i];
            }
            return sum;
        }

        @Override
        public double[] gradient(double[] point) throws FunctionEvaluationException {
            double[] result = new double[point.length];
            BFGS bfgs = new BFGS(point.length, point, GoalType.ABSOLUTE);
            for (int i = 0; i < data.length; i++) {
                double[] temp = new double[point.length];
                for (int j = 0; j < point.length; j++) {
                    temp[j] = point[j] - data[i][j];
                }
                double[] tempGradient = bfgs.derivative(temp);
                for (int j = 0; j < point.length; j++) {
                    result[j] += 2 * weights[i] * tempGradient[j];
                }
            }
            return result;
        }
    }
}