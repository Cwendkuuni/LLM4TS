package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.*;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test basic functionality of AbstractLeastSquaresOptimizer.
     */
    @Test
    public void testBasicFunctionality() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution = optimizer.optimize(f, target, weights, startPoint);
        Assert.assertEquals(solution.getPoint(), new double[]{1.0, 1.0, 1.0}, 0.001);
        Assert.assertEquals(solution.getValue(), 0.0, 0.001);
    }

    /**
     * Test handling of invalid input dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputDimensions() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of non-differentiable function.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testNonDifferentiableFunction() throws OptimizationException {
        Function f = new NonDifferentiableFunction();
        double[] target = {};
        double[] weights = {};
        double[] startPoint = {};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of maximum iterations exceeded exception.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of maximum evaluations exceeded exception.
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConvergenceChecker() {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of invalid number of rows in the jacobian.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testInvalidNumberOfRowsInJacobian() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.updateJacobian();
        optimizer.setJacobian(new double[target.length][target.length-1][target.length]);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of invalid number of columns in the jacobian.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testInvalidNumberOfColumnsInJacobian() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.updateJacobian();
        optimizer.setJacobian(new double[target.length][target.length+1][target.length]);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of invalid number of degrees of freedom.
     */
    @Test(expected = OptimizationException.class)
    public void testInvalidNumberOfDegreesOfFreedom() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of invalid number of points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfPoints() {
        Function f = new LinearRegressionFunction();
        double[] target = {};
        double[] weights = {};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test handling of null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        double[] target = {};
        double[] weights = {};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(null, target, weights, startPoint);
    }

    /**
     * Test handling of null target.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTarget() {
        Function f = new LinearRegressionFunction();
        double[] weights = {};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, null, weights, startPoint);
    }

    /**
     * Test handling of null weights.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWeights() {
        Function f = new LinearRegressionFunction();
        double[] target = {};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, target, null, startPoint);
    }

    /**
     * Test handling of null start point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullStartPoint() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(f, target, weights, null);
    }

    /**
     * Test handling of non-linear goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearGoalType() throws OptimizationException {
        Function f = new LinearRegressionFunction();
        double[] target = {1.0, 1.0, 1.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setGoalType(GoalType.MINIMIZE_ABSOLUTE);
        optimizer.optimize(f, target, weights, startPoint);
    }

}