package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonlinearObjectiveFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMaximizer;
import org.apache.commons.math3.special.FastMath;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimizer() throws Exception {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        double minValue = -3.5;
        double maxValue = 2.5;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return 2 * Math.pow(x, 2) + Math.sin(x) - 2;
            }
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        brentOptimizer.optimize(function, new InitialGuess(1.0), new GoalTypeMinimizer() {
            @Override
            public double getGoalValue() {
                return minValue;
            }
        });
        PointValuePair optimizedPointValue = brentOptimizer.getOptimum();
        assertEquals(goalType, brentOptimizer.getGoalType());
        assertEquals(minValue, optimizedPointValue.getValue(), 1e-9);
        assertEquals(-1.131199383582165, optimizedPointValue.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerMaximizer() throws Exception {
        GoalType goalType = GoalType.MAXIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        double maxValue = 3.5;
        double minValue = -2.5;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return 2 * Math.pow(x, 2) + Math.sin(x) - 2;
            }
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        brentOptimizer.optimize(function, new InitialGuess(1.0), new GoalTypeMaximizer() {
            @Override
            public double getGoalValue() {
                return maxValue;
            }
        });
        PointValuePair optimizedPointValue = brentOptimizer.getOptimum();
        assertEquals(goalType, brentOptimizer.getGoalType());
        assertEquals(maxValue, optimizedPointValue.getValue(), 1e-9);
        assertEquals(1.131199383582165, optimizedPointValue.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerInvalidInputs() {
        try {
            new BrentOptimizer(-1.1, 1.0, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, -1.0, null);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, 1.0, null);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, 1.0, null);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, 0.0, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1.0, 1.0, new GoalTypeMaximizer() {
                @Override
                public double getGoalValue() {
                    return -1.0;
                }
            });
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerNoConvergenceChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        try {
            brentOptimizer.optimize(function, new InitialGuess(1.0));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerNullInput() {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        ScalarFunction function = null;
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        try {
            brentOptimizer.optimize(function, new InitialGuess(1.0));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        double lo = 0.0;
        double hi = 1.0;
        double initialGuess = 0.5;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = (iter, current, best) -> {
            double currentDiff = Math.abs(current.getPoint() - best.getPoint());
            double bestDiff = Math.abs(best.getPoint() - lo);
            return currentDiff < bestDiff / 10;
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, convergenceChecker) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        brentOptimizer.optimize(function, new InitialGuess(initialGuess));
        UnivariatePointValuePair optimizedPointValue = brentOptimizer.getOptimum();
        assertEquals(goalType, brentOptimizer.getGoalType());
        assertEquals(lo, optimizedPointValue.getPoint(), 1e-8);
    }

    @Test
    public void testBrentOptimizerConvergenceCheckerNullInput() {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = null;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, convergenceChecker) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        try {
            brentOptimizer.optimize(function, new InitialGuess(1.0));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerRelativeAndAbsoluteThresholds() {
        GoalType goalType = GoalType.MINIMIZE;
        double minRelThreshold = 1e-10;
        double minAbsThreshold = 1e-8;
        double maxValue = 1.0;
        ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer brentOptimizerMinRelThreshold = new BrentOptimizer(minRelThreshold, null, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        BrentOptimizer brentOptimizerMinAbsThreshold = new BrentOptimizer(null, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        BrentOptimizer brentOptimizerNoThreshold = new BrentOptimizer(null, null, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        try {
            brentOptimizerMinRelThreshold.optimize(function, new InitialGuess(1.0), new GoalTypeMinimizer() {
                @Override
                public double getGoalValue() {
                    return maxValue;
                }
            });
            fail();
        } catch (AssertionError e) {
            // Expected
        }
        try {
            brentOptimizerMinAbsThreshold.optimize(function, new InitialGuess(1.0), new GoalTypeMinimizer() {
                @Override
                public double getGoalValue() {
                    return maxValue;
                }
            });
            fail();
        } catch (AssertionError e) {
            // Expected
        }
        try {
            brentOptimizerNoThreshold.optimize(function, new InitialGuess(1.0), new GoalTypeMinimizer() {
                @Override
                public double getGoalValue() {
                    return maxValue;
                }
            });
            fail();
        } catch (AssertionError e) {
            // Expected
        }
        BrentOptimizer brentOptimizer = new BrentOptimizer(minRelThreshold, minAbsThreshold, null) {
            @Override
            public GoalType getGoalType() {
                return goalType;
            }
        };
        brentOptimizer.optimize(function, new InitialGuess(1.0), new GoalTypeMinimizer() {
            @Override
            public double getGoalValue() {
                return maxValue;
            }
        });
        UnivariatePointValuePair optimizedPointValue = brentOptimizer.getOptimum();
        assertEquals(goalType, brentOptimizer.getGoalType());
        assertNull(optimizedPointValue);
    }

}