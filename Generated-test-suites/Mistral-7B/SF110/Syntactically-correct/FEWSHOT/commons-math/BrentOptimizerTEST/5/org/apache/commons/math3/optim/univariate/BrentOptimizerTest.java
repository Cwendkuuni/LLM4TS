package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        assertNotNull(optimizer);

        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizerMin = new BrentOptimizer(0.001, 0.0, goalType);
        BrentOptimizer optimizerMax = new BrentOptimizer(0.001, 0.0, GoalType.MAXIMIZE);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };

        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizerMin.optimize(problem);
            assertEquals(-1.0, result.getPoint(), 0.001);
            assertEquals(-1.0, result.getValue(), 0.001);

            result = optimizerMax.optimize(problem);
            assertEquals(1.0, result.getPoint(), 0.001);
            assertEquals(1.0, result.getValue(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_min() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.5;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizer.optimize(problem);
            double expectedResult = -1.5708; // Approximate solution using numerical methods or a graphic calculator
            assertEquals(expectedResult, result.getPoint(), 0.001);
            assertEquals(-1.5708, result.getValue(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_max() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - Math.sin(x);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizer.optimize(problem);
            double expectedResult = 1.0; // Exact solution
            assertEquals(expectedResult, result.getPoint(), 0.001);
            assertEquals(1.0, result.getValue(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_threshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            optimizer = new BrentOptimizer(0.0, 0.0, null);
            UnivariatePointValuePair result = optimizer.optimize(problem);
            fail("Expected an IllegalArgumentException for relative threshold = 0.0");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        optimizer = new BrentOptimizer(0.0001, 0.0);
        problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizer.optimize(problem);
            assertEquals(-1.0, result.getPoint(), 0.0001);
            assertEquals(-1.0, result.getValue(), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        optimizer = new BrentOptimizer(0.0, -0.0);
        problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizer.optimize(problem);
            fail("Expected an IllegalArgumentException for absolute threshold < 0.0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizer_convergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue() - previous.getValue() < 0.0001;
            }
        };

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            UnivariatePointValuePair result = optimizer.optimize(problem, convergenceChecker);
            assertEquals(-1.0, result.getPoint(), 0.001);
            assertEquals(-1.0, result.getValue(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_notStrictlyPositiveAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, -0.01);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            optimizer.optimize(problem);
            fail("Expected an IllegalArgumentException for an absolute threshold less than 0.0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizer_minRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            optimizer.optimize(problem, 0.0);
            fail("Expected an IllegalArgumentException for a relative threshold <= 0.0");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizer_nullInput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        UnivariateFunction function = null;
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }
        };

        try {
            optimizer.optimize(problem);
            fail("Expected an IllegalArgumentException for a null objective function");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return 1.0;
            }

            @Override
            public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker() {
                return null;
            }
        };

        try {
            optimizer.optimize(problem);
            fail("Expected an IllegalArgumentException for a null convergence checker");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizer_maximumBeyondBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }

            @Override
            public UnivariateFunction getObjectiveFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return -1.0;
            }

            @Override
            public double getMaximum() {
                return -1.0; // This value is beyond the upper bound
            }
        };

        try {
            optimizer.optimize(problem);
            fail("Expected an IllegalStateException for a maximum beyond the upper bound");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}