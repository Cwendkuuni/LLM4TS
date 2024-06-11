package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpace;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.NonLinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.maximization.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.NonLinearConstraints;
import org.apache.commons.math3.optim.nonlinear.vector.linear.NonlinearProgram;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SimpleLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SimpleLinearObjectiveFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testLeastSquaresOptimizer() {
        double[][] xData = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        double[] yData = {2.0, 4.0, 6.0, 8.0};

        AbstractLeastSquaresOptimizer optimizer = new LeastSquaresOptimizer();

        double[] coefficients = optimizer.optimize(new LinearObjectiveFunction(xData, yData), null, null, null).getPoint();

        RealMatrix xM = new Array2DRowRealMatrix(xData);
        RealVector yV = new Array2DRowRealVector(yData);

        LinearObjectiveFunction f = new LeastSquaresProblem(xM, yV);

        RealMatrix xExpected = new Array2DRowRealMatrix(new double[][]{{0.97487572, 0.96831275}, {0.96831275, 0.97487572}});
        RealVector yExpected = new Array2DRowRealVector(new double[]{0.0, 0.0});

        assertEquals(xExpected, coefficients, 0.001);
        assertEquals(yExpected, optimizer.function.value(coefficients), 0.001);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testLeastSquaresOptimizerWrongStartPoint() {
        double[][] xData = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        double[] yData = {2.0, 4.0, 6.0, 8.0};

        AbstractLeastSquaresOptimizer optimizer = new LeastSquaresOptimizer();

        double[] startPoint = new double[2];

        try {
            optimizer.optimize(new LinearObjectiveFunction(xData, yData), null, null, startPoint);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }

        fail("IllegalArgumentException expected");
    }

    @Test
    public void testLeastSquaresOptimizerConstraints() {

        double[][] xData = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {4.0, 4.0}};
        double[] yData = {2.0, 4.0, 6.0, 8.0};

        double lowerBound = 0.0;
        double upperBound = 10.0;

        // 1st constraint: x1 must be positive
        LinearConstraint constraint1 = new SimpleLinearConstraint(new SimpleVector(1.0), 1.0, SimpleConstraints.GREATER_EQUAL, lowerBound);

        // 2nd constraint: x2 must be less than 5
        LinearConstraint constraint2 = new SimpleLinearConstraint(new SimpleVector(1.0), 2.0, SimpleConstraints.LESS_EQUAL, upperBound);

        // Constraints
        NonLinearConstraints constraints = new NonLinearConstraints(new LinearConstraint[]{constraint1, constraint2});

        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        try {
            PointValuePair p = optimizer.optimize(new LinearObjectiveFunction(xData, yData), null, null, null, constraints);
            fail("OptimizationException expected");
        } catch (OptimizationException ex) {
            // Expected
        }

    }

    @Test(expected = NoRootsException.class)
    public void testLeastSquaresOptimizerNoSolution() {
        double[][] xData = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        double[] yData = {1.0, 3.0, 5.0, 7.0};

        AbstractLeastSquaresOptimizer optimizer = new LeastSquaresOptimizer();

        try {
            optimizer.optimize(new LinearObjectiveFunction(xData, yData), null, null, null);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }

        fail("NoRootsException expected");
    }

    @Test
    public void testLeastSquaresOptimizerWithBounds() {
        double[][] xData = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        double[] yData = {2.0, 4.0, 6.0, 8.0};

        double lowerBound = new double[]{-1.0, -1.0};
        double upperBound = new double[]{10.0, 10.0};

        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        PointValuePair solution = optimizer.optimize(new LinearObjectiveFunction(xData, yData), null, null, null, new SimpleBounds(lowerBound, upperBound));

        double[] coefficients = solution.getPoint();

        RealMatrix xM = new Array2DRowRealMatrix(xData);
        RealVector yV = new Array2DRowRealVector(yData);

        LinearObjectiveFunction f = new LeastSquaresProblem(xM, yV);

        RealMatrix xExpected = new Array2DRowRealMatrix(new double[][]{{0.97487572, 0.96831275}, {0.96831275, 0.97487572}});

        assertEquals(xExpected, coefficients, 0.001);
        assertEquals(yExpected, optimizer.function.value(coefficients), 0.001);
    }

}