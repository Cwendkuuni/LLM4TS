package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import static org.apache.commons.math3.optim.GoalType.*;
import static org.apache.commons.math3.optim.nonlinear.scalar.GoalType.*;
import static org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction.*;
import static org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair.*;
import static org.apache.commons.math3.optim.nonlinear.scalar.univariate.*;
import static org.apache.commons.math3.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class BrentOptimizerTest {
    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;

    @Parameterized.Parameters(name = "{index}: ({0}, {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new PolynomialFunction(x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x), 0.5, 2.0},
                {new PolynomialFunction(x -> Math.pow(x, 3) - 3 * Math.pow(x, 2) + 3 * x - 1), 1.0, 3.0},
                {new PolynomialFunction(x -> Math.exp(x) - 2), 1.5, 5.0},
                {new PolynomialFunction(x -> Math.sin(x) / x), 0.1, 1.0},
                {new PolynomialFunction(x -> Math.log(1 + x) / x), 0.001, 0.5},
                {new PolynomialFunction(x -> Math.sqrt(Math.abs(x) + 1) - 1), 0.01, 1.0},
                {new PolynomialFunction(x -> Math.log(Math.abs(x) + 1) / Math.abs(x) + 1), 0.001, 1.0}
        });
    }

    private final ObjectiveFunction function;
    private final double min;
    private final double max;

    public BrentOptimizerTest(ObjectiveFunction function, double min, double max) {
        this.function = function;
        this.min = min;
        this.max = max;
    }

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, null);
        UnivariatePointValuePair result = optimizer.optimize(function, min, max);

        double actual = result.getValue();
        double expected;

        switch (getGoalType(MINIMIZE)) {
            case MINIMIZE:
                expected = min;
                break;
            case MAXIMIZE:
                expected = max;
                break;
            default:
                throw new AssertionError("Invalid goal type: " + getGoalType(MINIMIZE));
        }

        assertEquals(expected, actual, RELATIVE_TOLERANCE + ABSOLUTE_TOLERANCE);
    }
}