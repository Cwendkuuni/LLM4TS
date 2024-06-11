package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.ArrayInitializer;
import org.apache.commons.math3.optim.point.PointValuePairArray;
import org.apache.commons.math3.optim.simple.SimpleValueChecker.Throw;
import org.apache.commons.math3.optim.simple.SimpleValueChecker.Throw.THROW_EXCEPTION;
import org.apache.commons.math3.optim.simple.SimpleValueChecker.Warning.WARN_MESSAGE;
import org.apache.commons.math3.optim.simple.SimpleValueChecker.Warning.WARN_WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
@Category(org.apache.commons.math3.optim.Category.REGRESSION_TEST)
public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-9;
    private static final double EPSILON_SQUARE = EPSILON * EPSILON;

    @Parameterized.Parameters(name = "{index}: {0} optimization with {1} goal type")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {new PolynomialFunction(new double[]{-4, 4, 4}, new double[]{1, 2, 3}), GoalType.MINIMIZE},
            {new PolynomialFunction(new double[]{-4, 4, 4}, new double[]{1, 2, 3}), GoalType.MAXIMIZE},
            {new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{1, 1, 1, 1}), GoalType.MINIMIZE},
            {new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{1, 1, 1, 1}), GoalType.MAXIMIZE},
            {new UnivariateFunction() {
                public double value(double x) {
                    return Math.sin(x) - x;
                }
            }, GoalType.MINIMIZE},
            {new UnivariateFunction() {
                public double value(double x) {
                    return Math.sin(x) - x;
                }
            }, GoalType.MAXIMIZE},
            {new UnivariateFunction() {
                public double value(double[] x) {
                    return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
                }
            }, GoalType.MINIMIZE},
            {new UnivariateFunction() {
                public double value(double[] x) {
                    return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
                }
            }, GoalType.MAXIMIZE},
            {new UnivariateFunction() {
                public double value(double[] x) {
                    return Math.exp(-Math.sum(x)) * Math.pow(Math.sin(Math.PI * Math.max(x[0], x[1])), 2);
                }
            }, GoalType.MINIMIZE},
            {new UnivariateFunction() {
                public double value(double[] x) {
                    return Math.exp(-Math.sum(x)) * Math.pow(Math.sin(Math.PI * Math.max(x[0], x[1])), 2);
                }
            }, GoalType.MAXIMIZE}
        });
    }

    private MultivariateFunction function;
    private GoalType goalType;

    public SimplexOptimizerTest(MultivariateFunction function, GoalType goalType) {
        this.function = function;
        this.goalType = goalType;
    }

    private AbstractSimplex simplex;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() throws Exception {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON_SQUARE, WARN_WARNING, WARN_MESSAGE, THROW_EXCEPTION));
        optimizer.setSimplex(new Simplex(new ArrayInitializer(new int[] {function.getNumberOfParameters()})));
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(100, function, goalType, new double[function.getNumberOfParameters()]);
        double[] x = result.getPoint();
        double fmin = result.getValue();
        PointValuePair expected = function.findMinimum(EPSILON);
        double[] expectedX = expected.getPoint();

        assertEquals(expectedX.length, x.length);
        assertArrayEquals(expectedX, x, EPSILON);
        assertEquals(expected.getValue(), fmin, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(100, null, goalType, new double[function.getNumberOfParameters()]);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGoalType() {
        optimizer.optimize(100, function, null, new double[function.getNumberOfParameters()]);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullInitialPoint() {
        optimizer.optimize(100, function, goalType, null);
    }
}