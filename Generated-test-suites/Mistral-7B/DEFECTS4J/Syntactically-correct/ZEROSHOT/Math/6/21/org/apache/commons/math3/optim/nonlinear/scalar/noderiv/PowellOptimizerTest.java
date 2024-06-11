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
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        // Test constructor with default values
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);

        // Test constructor with user-defined convergence checker
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double previousValue = previous.getValue();
                double currentValue = current.getValue();
                double maxError = FastMath.max(FastMath.abs(previousValue - currentValue), 1);
                return maxError <= 0.0001;
            }
        };
        PowellOptimizer optimizerWithChecker = new PowellOptimizer(0.01, 0.001, checker);

        // Test optimization
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -Math.pow(Math.sin(Math.sqrt(x[0])), 2) - Math.pow(Math.sin(Math.sqrt(x[1])), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PointValuePair result = optimizer.optimize(function);
        PointValuePair expectedResult = new PointValuePair(new double[]{1.5708, 1.5708}, -1.0);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), 0.001);
        Assert.assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.001);

        result = optimizerWithChecker.optimize(function);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), 0.001);
        Assert.assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.001);

        // Test optimization with custom stopping criteria
        checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double previousValue = previous.getValue();
                double currentValue = current.getValue();
                return FastMath.abs(previousValue - currentValue) <= 0.01;
            }
        };
        PowellOptimizer optimizerWithCustomCriteria = new PowellOptimizer(0.01, 0.001, checker);
        result = optimizerWithCustomCriteria.optimize(function);
        Assert.assertEquals(-1.0, result.getValue(), 0.01);
    }
}