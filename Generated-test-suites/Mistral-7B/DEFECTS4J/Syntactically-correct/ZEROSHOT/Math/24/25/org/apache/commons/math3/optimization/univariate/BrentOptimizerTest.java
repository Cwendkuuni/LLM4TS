package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test constructor with relative and absolute threshold and ConvergenceChecker
        BrentOptimizer brentOptimizer1 = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });

        // Test constructor with relative and absolute threshold
        BrentOptimizer brentOptimizer2 = new BrentOptimizer(0.01, 0.001);

        // Test constructor with only relative threshold
        BrentOptimizer brentOptimizer3 = new BrentOptimizer(0.01);

        // Test constructor with only absolute threshold
        BrentOptimizer brentOptimizer4 = new BrentOptimizer(0.001);

        // Test exception when relative threshold is smaller than machine epsilon
        try {
            new BrentOptimizer(FastMath.ulp(1d), 0.0);
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test exception when absolute threshold is non-positive
        try {
            new BrentOptimizer(0.0, -0.001);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test optimization with simple test function
        BrentFunction function = new BrentFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        UnivariateObjectiveFunctionTestValues expectedValues = new UnivariateObjectiveFunctionTestValues(0.123456789, 0.234567891, 0.345678912);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.5, 1.5);

        assertEquals(expectedValues.getMinimum(), result.getPoint(), 1e-9);
        assertEquals(expectedValues.getValueAtMinimum(), result.getValue(), 1e-9);

        // Test optimization with ConvergenceChecker
        UnivariatePointValuePair convergenceCheckerResult = null;
        int maxIterations = 100;
        BrentFunction convergenceCheckerFunction = new BrentFunction();
        BrentOptimizer convergenceCheckerBrentOptimizer = new BrentOptimizer(0.01, 0.01, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                convergenceCheckerResult = current;
                return iteration >= maxIterations;
            }
        });

        UnivariatePointValuePair optimizationResultWithChecker = convergenceCheckerBrentOptimizer.optimize(convergenceCheckerFunction, GoalType.MINIMIZE, 0.5, 1.5);

        assertEquals(optimizationResultWithChecker.getPoint(), convergenceCheckerResult.getPoint(), 1e-9);
        assertEquals(optimizationResultWithChecker.getValue(), convergenceCheckerResult.getValue(), 1e-9);

        // Test optimization without convergence checker
        UnivariateOptimizer optimizerWithoutChecker = new BrentOptimizer(0.01, 0.01);
        UnivariatePointValuePair optimizationResultWithoutChecker = optimizerWithoutChecker.optimize(function, GoalType.MINIMIZE, 0.5, 1.5);

        assertEquals(expectedValues.getMinimum(), optimizationResultWithoutChecker.getPoint(), 1e-9);
        assertEquals(expectedValues.getValueAtMinimum(), optimizationResultWithoutChecker.getValue(), 1e-9);

        // Test optimization with custom goal type
        BrentGoalTypeCustomFunction customFunction = new BrentGoalTypeCustomFunction();
        UnivariateOptimizer customOptimizer = new BrentOptimizer(0.01, 0.01);
        UnivariatePointValuePair customResult = customOptimizer.optimize(customFunction, new CustomGoalType(), 0.5, 1.5);

        assertEquals(expectedValues.getMinimum(), customResult.getPoint(), 1e-9);
        assertEquals(expectedValues.getValueAtMinimum(), customResult.getValue(), 1e-9);

        // Test optimization with custom goal type and ConvergenceChecker
        BrentGoalTypeCustomFunction convergenceCheckerCustomFunction = new BrentGoalTypeCustomFunction();
        BrentOptimizer convergenceCheckerCustomBrentOptimizer = new BrentOptimizer(0.01, 0.01, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });

        UnivariatePointValuePair convergenceCheckerCustomResult = null;
        UnivariatePointValuePair optimizationResultWithCheckerCustom = convergenceCheckerCustomBrentOptimizer.optimize(convergenceCheckerCustomFunction, new CustomGoalType(), 0.5, 1.5);

        assertEquals(optimizationResultWithCheckerCustom.getPoint(), convergenceCheckerCustomResult.getPoint(), 1e-9);
        assertEquals(optimizationResultWithCheckerCustom.getValue(), convergenceCheckerCustomResult.getValue(), 1e-9);

        // Test optimization with custom goal type without ConvergenceChecker
        UnivariateOptimizer customOptimizerWithoutChecker = new BrentOptimizer(0.01, 0.01);
        UnivariatePointValuePair optimizationResultWithoutCheckerCustom = customOptimizerWithoutChecker.optimize(customFunction, new CustomGoalType(), 0.5, 1.5);

        assertEquals(expectedValues.getMinimum(), optimizationResultWithoutCheckerCustom.getPoint(), 1e-9);
        assertEquals(expectedValues.getValueAtMinimum(), optimizationResultWithoutCheckerCustom.getValue(), 1e-9);

        // Test optimization with function that does not change sign
        BrentFunction signFunction = new BrentFunctionSignFunction();
        UnivariateOptimizer signOptimizer = new BrentOptimizer(0.01, 0.01);

        try {
            signOptimizer.optimize(signFunction, GoalType.MINIMIZE, 0, 1);
            assertFalse(true);
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test optimization with function that has multiple local minima
        BrentFunction multiLocalMinimaFunction = new BrentFunctionMultiLocalMinima();
        UnivariateOptimizer multiLocalMinimaOptimizer = new BrentOptimizer(0.01, 0.01);
        UnivariatePointValuePair multiLocalMinimaResult = multiLocalMinimaOptimizer.optimize(multiLocalMinimaFunction, GoalType.MINIMIZE, 0.5, 1.5);

        double expectedMinimum1 = 0.123456789;
        double expectedMinimum2 = 0.345678912;

        double tolerance = 1e-7;

        assertTrue(Precision.contains(multiLocalMinimaResult.getPoint(), expectedMinimum1, tolerance));
        assertTrue(Precision.contains(multiLocalMinimaResult.getValue(), expectedValues.getValueAtMinimum(), tolerance));

        assertTrue(Precision.contains(multiLocalMinimaResult.getPoint(), expectedMinimum2, tolerance));
        assertTrue(Precision.contains(multiLocalMinimaResult.getValue(), expectedValues.getValueAtMinimum(), tolerance));
    }

    private static class UnivariateObjectiveFunctionTestValues {
        private final double minimum;
        private final double valueAtMinimum;

        public UnivariateObjectiveFunctionTestValues(double minimum, double valueAtMinimum) {
            this.minimum = minimum;
            this.valueAtMinimum = valueAtMinimum;
        }

        public double getMinimum() {
            return minimum;
        }

        public double getValueAtMinimum() {
            return valueAtMinimum;
        }
    }

    private static class BrentFunction implements UnivariateObjectiveFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(Math.PI * x / 2), 2) + 0.1 * Math.sin(2 * Math.PI * x);
        }
    }

    private static class BrentFunctionSignFunction implements UnivariateObjectiveFunction {
        @Override
        public double value(double x) {
            return Math.signum(Math.sin(Math.PI * x / 2));
        }
    }

    private static class BrentFunctionMultiLocalMinima implements UnivariateObjectiveFunction {
        @Override
        public double value(double x) {
            return (Math.pow(Math.sin(Math.PI * x / 2), 2) + 0.1 * Math.sin(2 * Math.PI * x)) * Math.exp(-Math.pow(x - 0.5, 2) / 0.01);
        }
    }

    private static class CustomGoalType implements GoalType {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return true;
        }

        @Override
        public String toString() {
            return "CustomGoalType";
        }
    }
}

class BrentGoalTypeCustomFunction implements UnivariateObjectiveFunction {
    @Override
    public double value(double x) {
        return Math.sin(Math.PI * x / 2) * Math.exp(-Math.pow(x - 0.5, 2) / 0.01);
    }
}