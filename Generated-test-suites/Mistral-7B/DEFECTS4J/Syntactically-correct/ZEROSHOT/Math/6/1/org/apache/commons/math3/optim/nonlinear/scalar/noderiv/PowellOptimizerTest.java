package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testConstructors() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);

        PowellOptimizer optimizer1 = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-4, 1e-4));
        assertNotNull(optimizer1);

        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6, 1e-4, 1e-4, new SimpleUnivariateValueChecker(1e-4, 1e-4));
        assertNotNull(optimizer2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        new PowellOptimizer(0.5 * FastMath.ulp(1d), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbsoluteThresholdNegative() {
        new PowellOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testOptimize() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(f);

        assertEquals(new PointValuePair(new double[]{0, 0}, 0), solution);
    }

    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.sin(x[0]) * FastMath.sin(x[1]);
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = (iter, previous, current) -> {
            double difference = FastMath.abs(current.getValue() - previous.getValue());
            return difference < 1e-4;
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, convergenceChecker);
        PointValuePair solution = optimizer.optimize(f);

        double[] expectedSolution = {1.23456789, 2.34567890};
        assertEquals(new PointValuePair(expectedSolution, FastMath.sin(expectedSolution[0]) * FastMath.sin(expectedSolution[1])), solution);
    }

    @Test
    public void testOptimizeWithBracketing() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(f);

        assertEquals(new PointValuePair(new double[]{0, 0}, 0), solution);

        double[] point = new double[]{3, 4};
        double[] bracketingResult = optimizer.optimize(f, point);

        PointValuePair expectedBracketingSolution = new PointValuePair(new double[]{1, 1}, -2);
        assertEquals(expectedBracketingSolution, bracketingResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(null);
    }

    @Test
    public void testGetGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = optimizer.getGoalType();
        assertEquals(GoalType.MINIMIZE, goalType);
    }

    @Test
    public void testGetStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = optimizer.getStartPoint();
        assertArrayEquals(new double[]{0, 0}, startPoint, 0.0);
    }
}