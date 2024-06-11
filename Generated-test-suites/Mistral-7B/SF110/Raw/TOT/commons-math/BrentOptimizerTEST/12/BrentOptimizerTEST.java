package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.commons.math3.optim.univariate.BrentOptimizer.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10);
        try {
            PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
            assertEquals(-1.0, result.getPoint(), 1e-6);
            assertEquals(0.0, result.getValue(), 1e-6);
        } catch (NumberIsTooSmallException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithNullChecker() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10);
        PointValuePair result = optimizer.optimize(function, null, null);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithGoalTypeNull() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10);
        try {
            PointValuePair result = optimizer.optimize(function, null, null);
            fail("Unexpected result with null goal type.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithInvalidMin() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, -1.0, null);
        try {
            PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
            fail("Unexpected result with invalid minimum.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithInvalidAbsThreshold() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, -0.1, null);
        try {
            PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
            fail("Unexpected result with invalid absolute threshold.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithInvalidRelativeThreshold() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-0.1, 1.0e-10, null);
        try {
            PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
            fail("Unexpected result with invalid relative threshold.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizationWithConvergenceChecker() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return current.getValue() - previous.getValue() < 1e-8;
            }
        });
        PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithCustomConvergenceChecker() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return current.getValue() < 1e-8;
            }
        });
        PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MINIMIZE), null);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10, new GoalTypeChecker(GoalType.MAXIMIZE));
        try {
            PointValuePair result = optimizer.optimize(function, null, null);
            assertEquals(1.0, result.getPoint(), 1e-6);
            assertEquals(0.0, result.getValue(), 1e-6);
        } catch (NumberIsTooSmallException | NotStrictlyPositiveException | IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerMaximizationWithInvalidGoalType() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0, 1.0e-10);
        try {
            PointValuePair result = optimizer.optimize(function, new GoalTypeChecker(GoalType.MAXIMIZE), null);
            fail("Unexpected result with invalid goal type.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}