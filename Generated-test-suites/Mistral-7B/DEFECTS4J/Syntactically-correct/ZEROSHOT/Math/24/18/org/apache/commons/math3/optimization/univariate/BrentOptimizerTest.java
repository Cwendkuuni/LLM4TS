package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testBrentOptimizer() {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer(null);
        UnivariateFunction function = new SquareFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, new MaxEval(100));
        assertEquals(0.0, result.getPoint(), Precision.SAFE_EPSILON);
        assertEquals(0.0, result.getValue(), Precision.SAFE_EPSILON);

        // Test with custom constructor
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-5, 1e-7, null);
        UnivariatePointValuePair result2 = optimizer2.optimize(function, new MaxEval(100));
        assertEquals(0.0, result2.getPoint(), 1e-5);
        assertEquals(0.0, result2.getValue(), 1e-7);

        // Test with custom convergence checker
        class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                List<UnivariatePointValuePair> points = new ArrayList<>();
                points.add(previous);
                points.add(current);
                return points.size() >= 10;
            }
        }
        BrentOptimizer optimizer3 = new BrentOptimizer(1e-5, 1e-7, new MyConvergenceChecker());
        UnivariatePointValuePair result3 = optimizer3.optimize(function, new MaxEval(100));
        assertNotNull(result3);

        // Test with GoalType.MAXIMIZE
        BrentOptimizer optimizer4 = new BrentOptimizer(new AbsoluteSquareFunction(), GoalType.MAXIMIZE, null);
        UnivariatePointValuePair result4 = optimizer4.optimize(new MaxEval(100));
        assertEquals(1.0, result4.getPoint(), Precision.SAFE_EPSILON);
        assertEquals(1.0, result4.getValue(), Precision.SAFE_EPSILON);
    }

    private static class SquareFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class AbsoluteSquareFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x * x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}