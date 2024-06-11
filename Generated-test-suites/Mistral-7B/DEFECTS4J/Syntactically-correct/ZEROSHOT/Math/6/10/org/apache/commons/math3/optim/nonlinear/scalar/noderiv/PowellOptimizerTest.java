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
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        Assert.assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, convergenceChecker);
        Assert.assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, null);
    }

    @Test
    public void testOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.sin(FastMath.PI * x[0]) * FastMath.sin(FastMath.PI * x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function);
        Assert.assertArrayEquals(new double[]{0, 0}, result.getPoint(), 0.0);
        Assert.assertEquals(0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(FastMath.PI - x[0], 2) + FastMath.pow(FastMath.PI - x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 100 && current.getValue() < 0.0001);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, convergenceChecker);
        PointValuePair result = optimizer.optimize(function);
        List<Double> expectedX = Arrays.asList(FastMath.PI, FastMath.PI);
        List<Double> expectedValues = Arrays.asList(0.0, 0.0);
        Assert.assertEquals(expectedX.get(0), result.getPoint()[0], 0.001);
        Assert.assertEquals(expectedX.get(1), result.getPoint()[1], 0.001);
        Assert.assertEquals(expectedValues.get(0), result.getValue(), 0.001);
    }
}