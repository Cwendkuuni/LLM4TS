package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypefactory;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.AroundMaximumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.initializer.ValueBoundInitializer;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testBrentOptimizerConstruction() {
        // Test default constructor
        try {
            new BrentOptimizer(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test constructor with valid arguments
        double relTol = 1e-6;
        double absTol = 1e-3;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleLineSearch(0.1, 1.01);
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, convergenceChecker);
        assertNotNull(optimizer);

        // Test constructor without ConvergenceChecker
        optimizer = new BrentOptimizer(relTol, absTol);
        assertNotNull(optimizer);

        // Test constructor with invalid arguments
        try {
            new BrentOptimizer(-1.0, relTol);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(relTol, -1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(relTol, absTol, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BrentOptimizer(relTol, absTol, convergenceChecker, new ObjectiveFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getNumberOfVariables() {
                    return 1;
                }
            });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDoOptimize() throws Exception {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double start = 1.0;
        double min = 0.0;
        double max = 2.0;
        double expectedResult = 1.0;
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(start);
        PointValuePair result = optimizer.optimize();
        assertEquals(expectedResult, result.getValue(), 1e-7);

        // Test with custom constructor
        double relTol = 1e-6;
        double absTol = 1e-3;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleLineSearch(0.1, 1.01);
        optimizer = new BrentOptimizer(relTol, absTol, convergenceChecker);
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(start);
        result = optimizer.optimize();
        assertEquals(expectedResult, result.getValue(), 1e-7);

        // Test with maximum function evaluation exception
        optimizer = new BrentOptimizer();
        goalType = GoalType.MINIMIZE;
        start = 1.0;
        min = 0.0;
        max = 2.0;
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new MaxEvalExceededException("Maximum function evaluations exceeded");
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setFunction(objectiveFunction);
        try {
            optimizer.optimize();
            fail("Expected Exception");
        } catch (Exception e) {
            // Expected
        }

        // Test with unsupported function
        optimizer = new BrentOptimizer();
        goalType = GoalType.MINIMIZE;
        start = 1.0;
        min = 0.0;
        max = 2.0;
        ObjectiveFunction objectiveFunctionUnsupported = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setFunction(objectiveFunctionUnsupported);
        try {
            optimizer.optimize();
            fail("Expected Exception");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with invalid goal type
        optimizer = new BrentOptimizer();
        optimizer.setGoalType(null);
        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid bounds
        optimizer = new BrentOptimizer();
        goalType = GoalType.MINIMIZE;
        double minInvalid = Double.NEGATIVE_INFINITY;
        double maxInvalid = Double.POSITIVE_INFINITY;
        optimizer.setGoalType(goalType);
        optimizer.setMin(minInvalid);
        optimizer.setMax(maxInvalid);
        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testComputeObjectiveValue() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double x = 0.0;
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.setGoalType(goalType);
        optimizer.setFunction(objectiveFunction);
        double expectedResult = Math.pow(x, 2);
        double result = optimizer.computeObjectiveValue(x);
        assertEquals(expectedResult, result, 1e-7);
    }

    @org.junit.Test
    public void testBest() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariatePointValuePair p1 = new PointValuePair(1.0, 4.0);
        UnivariatePointValuePair p2 = new PointValuePair(2.0, 9.0);
        UnivariatePointValuePair p3 = new PointValuePair(3.0, 16.0);
        List<UnivariatePointValuePair> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        PointValuePairCollection pointValuePairCollection = new PointValuePairCollection(points);
        optimizer.setPoints(pointValuePairCollection);
        boolean isMinim = true;
        UnivariatePointValuePair expectedResult = p1;
        UnivariatePointValuePair result = optimizer.best(null, p2, isMinim);
        assertEquals(expectedResult, result);

        expectedResult = p1;
        result = optimizer.best(p3, null, isMinim);
        assertEquals(expectedResult, result);

        isMinim = false;
        expectedResult = p3;
        result = optimizer.best(p1, p2, isMinim);
        assertEquals(expectedResult, result);

        expectedResult = p2;
        result = optimizer.best(p1, p3, isMinim);
        assertEquals(expectedResult, result);

        expectedResult = p1;
        result = optimizer.best(p1, p1, isMinim);
        assertEquals(expectedResult, result);
    }

    @org.junit.Test
    public void testGoldenSection() {
        double a = 0.0;
        double b = 1.0;
        double expected = 0.5 * (1.0 - Math.sqrt(5.0));
        double result = BrentOptimizer.GOLDEN_SECTION;
        assertEquals(expected, result, 1e-7);
    }
}