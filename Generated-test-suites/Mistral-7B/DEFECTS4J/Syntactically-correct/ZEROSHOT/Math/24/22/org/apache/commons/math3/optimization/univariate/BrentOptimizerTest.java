package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-8;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;
    private static final double MINIMUM = 3.0;
    private static final double MAXIMUM = 5.0;
    private static final double FUNCTION_MINIMUM = 2.0;
    private static final double FUNCTION_MAXIMUM = 4.0;

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }
        };

        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @After
    public void tearDown() {
        optimizer = null;
        function = null;
    }

    @Test
    public void testBrentOptimizerMinimization() {
        optimizer.setStartValue(3.5);
        optimizer.setMinimum(MINIMUM);
        optimizer.setMaximum(MAXIMUM);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair foundMin = optimizer.optimize(function);

        assertEquals(MINIMUM, foundMin.getPoint(), 0.0);
        assertEquals(FUNCTION_MINIMUM, foundMin.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        optimizer.setStartValue(3.5);
        optimizer.setMinimum(MINIMUM);
        optimizer.setMaximum(MAXIMUM);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair foundMax = optimizer.optimize(function);

        assertEquals(MAXIMUM, foundMax.getPoint(), 0.0);
        assertEquals(FUNCTION_MAXIMUM, foundMax.getValue(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, -ABSOLUTE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        optimizer = new BrentOptimizer(-RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testBrentOptimizerSimpleConvergenceChecker() {
        optimizer.setStartValue(3.5);
        optimizer.setMinimum(MINIMUM);
        optimizer.setMaximum(MAXIMUM);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration,
                                     UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                List<UnivariatePointValuePair> points = new ArrayList<>();
                points.add(previous);
                points.add(current);
                double error = current.getValue() - foundMin(points).getValue();
                return error < RELATIVE_TOLERANCE * Math.max(Math.abs(MINIMUM), Math.abs(MAXIMUM))
                       || iteration > 100;
            }
        });

        UnivariatePointValuePair result = optimizer.optimize(function);

        assertEquals(MINIMUM, result.getPoint(), 0.0);
        assertEquals(FUNCTION_MINIMUM, result.getValue(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullFunction() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        optimizer.optimize(null);
    }

    @Test
    public void testBrentOptimizerInterpolationStep() {
        optimizer.setStartValue(3.5);
        optimizer.setMinimum(MINIMUM);
        optimizer.setMaximum(MAXIMUM);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        double x = 3.5;
        double a = MINIMUM;
        double b = MAXIMUM;

        UnivariatePointValuePair result;
        while ((result = optimizer.doOptimize()).getPoint() != null) {
            x = result.getPoint();
            double fx = function.value(x);

            if ((x - (a + b) / 2) < FastMath.abs(b - a) / 2) {
                break;
            }

            if (fx < function.value(a)) {
                a = x;
            } else if (fx < function.value(b)) {
                b = x;
            }
        }

        assertEquals(MINIMUM, x, 0.0);
        assertEquals(FUNCTION_MINIMUM, function.value(x), 0.0);
    }

    @Test
    public void testBrentOptimizerGoldenSectionStep() {
        optimizer.setStartValue(3.5);
        optimizer.setMinimum(MINIMUM);
        optimizer.setMaximum(MAXIMUM);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        double x = 3.5;
        double a = MINIMUM;
        double b = MAXIMUM;

        UnivariatePointValuePair result;
        while ((result = optimizer.doOptimize()).getPoint() != null) {
            x = result.getPoint();
            double fx = function.value(x);

            if (Math.abs(x - (a + b) / 2) > (b - a) * 0.382) {
                continue;
            }

            if (fx < function.value(a)) {
                b = x;
            } else if (fx < function.value(b)) {
                a = x;
            }
        }

        assertEquals(MINIMUM, x, 0.0);
        assertEquals(FUNCTION_MINIMUM, function.value(x), 0.0);
    }
}