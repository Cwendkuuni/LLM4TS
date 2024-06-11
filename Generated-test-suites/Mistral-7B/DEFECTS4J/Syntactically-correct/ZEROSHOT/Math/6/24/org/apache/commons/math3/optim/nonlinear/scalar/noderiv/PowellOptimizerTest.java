package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
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
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testOptimize() {
        // Test with default constructor and default convergence checker.
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0] - 2, 2) + FastMath.pow(x[1] - 3, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(new PointValuePair(new double[]{1.9999999999999998D, 2.9999999999999998D}, 1.2324999999999999E-6d), result, 1e-9);

        // Test with user-defined convergence checker.
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(previous.getValue() - current.getValue()) < 1e-9;
            }
        };
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6, checker);
        PointValuePair result2 = optimizer2.optimize(function);
        Assert.assertEquals(new PointValuePair(new double[]{1.9999999999999998D, 2.9999999999999998D}, 1.2324999999999999E-6d), result2, 1e-9);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        new PowellOptimizer(1e-10, 1e-6);
    }
}