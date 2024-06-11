package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() <= 1e-8;
            }
        });

        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 2;
            }
        };

        PointValuePair result = optimizer.optimize(f, new InitialGuess(0.5), GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-8);
        assertEquals(1.0, result.getValue(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerIllegalArgument1() {
        new BrentOptimizer(-0.001, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerIllegalArgument2() {
        new BrentOptimizer(1e-8, 0.0, null);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() >= 1e-8;
            }
        });

        Function f = new Function() {
            @Override
            public double value(double x) {
                return -x * x + 2 * x - 1;
            }

            @Override
            public double derivative(double x) {
                return -2 * x + 2;
            }
        };

        PointValuePair result = optimizer.optimize(f, new InitialGuess(0.5), GoalType.MAXIMIZE);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(-1.0, result.getValue(), 1e-8);
    }

    @FunctionalInterface
    interface Function {
        double value(double x);

        double derivative(double x) throws UnsupportedOperationException;
    }
}