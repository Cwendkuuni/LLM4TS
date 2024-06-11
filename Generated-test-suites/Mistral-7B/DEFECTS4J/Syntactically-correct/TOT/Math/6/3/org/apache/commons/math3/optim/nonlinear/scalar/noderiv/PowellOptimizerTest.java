package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MultivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test for Powell Optimizer with default parameters and simple test function.
     */
    @Test
    public void testPowellOptimizerDefault() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }
        };
        MultivariateOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        double[] x = new double[] {1.0, 1.0};
        PointValuePair solution = optimizer.optimize(f, x, new MaxEval(100));
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertArrayEquals(new double[] {0.0, 0.0}, solution.getPoint(), 1e-6);
    }

    /**
     * Test for Powell Optimizer with custom convergence checker and simple test function.
     */
    @Test
    public void testPowellOptimizerCustomChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }
        };
        double relTol = 1e-4;
        double absTol = 1e-4;

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double relativeDiff = Math.abs(current.getValue() - previous.getValue()) / (Math.abs(current.getValue()) + Math.abs(previous.getValue()) + 1);
                return relativeDiff <= relTol;
            }
        };
        MultivariateOptimizer optimizer = new PowellOptimizer(relTol, absTol, checker);

        double[] x = new double[] {1.0, 1.0};
        PointValuePair solution = optimizer.optimize(f, x, new MaxEval(100));
        assertEquals(0.0, solution.getValue(), 1e-4);
        assertArrayEquals(new double[] {0.0, 0.0}, solution.getPoint(), 1e-4);
    }

    /**
     * Test for Powell Optimizer with non-strictly positive absolute tolerance.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerNegativeAbsoluteTolerance() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }
        };
        MultivariateOptimizer optimizer = new PowellOptimizer(1e-6, -1e-6);
    }

    /**
     * Test for Powell Optimizer with non-strictly positive relative tolerance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPowellOptimizerNonStrictlyPositiveRelativeTolerance() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }
        };
        MultivariateOptimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);
    }
}