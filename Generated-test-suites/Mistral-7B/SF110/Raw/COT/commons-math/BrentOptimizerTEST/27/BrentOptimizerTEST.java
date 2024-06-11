package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor_RelAndAbs_NullChecker() {
        expectThrows(IllegalArgumentException.class, () -> new BrentOptimizer(0.0, 0.0, null));
    }

    @Test
    public void testBrentOptimizerConstructor_RelAndAbs() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1);
        assertEquals(1.0, brentOptimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.1, brentOptimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerConstructor_RelAndAbs_Checker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        assertEquals(1.0, brentOptimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.1, brentOptimizer.getAbsoluteThreshold(), 0.0);
        verify(convergenceChecker).converged(0, null, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor_RelIsTooSmall() {
        new BrentOptimizer(0.0001, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor_AbsIsNonPositive() {
        new BrentOptimizer(1.0, -0.01);
    }

    @Test
    public void testDoOptimize_Minimize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(2) - Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(0.0, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Maximize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(2) + Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(0.0, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Minimize_MinIsMax() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return Double.MAX_VALUE;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(Double.MAX_VALUE, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Maximize_MinIsMin() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.1);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return Double.MIN_VALUE;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(Double.MIN_VALUE, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Minimize_MinIsMinAndAbsIsZero() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.0);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(0.0, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Maximize_MinIsMinAndAbsIsZero() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1.0, 0.0);
        Function function = mock(UnivariateFunction.class);
        when(function.value(anyDouble())).thenReturn(Math.pow(Math.E, Math.log(1 + it.nextDouble())));
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateOptimizer brentResult = brentOptimizer.optimize(optimizationProblem, goalType);
        assertEquals(0.0, brentResult.getPoint().getValue(), 0.0);
        verify(function).value(anyDouble());
    }

    @Test
    public void testDoOptimize_Minimize_ConvergenceChecker() {
        BrentOptimizer brentOptimizer = mock(BrentOptimizer.class);
        when(brentOptimizer.optimize(any(), any())).thenReturn(new UnivariateOptimizer() {
            @Override
            public UnivariatePointValuePair getPoint() {
                return new UnivariatePointValuePair(1.0, 1.0);
            }
        });
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(), any()))
                .thenReturn(true);

        BrentOptimizer brentOptimizerWithChecker = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        Function function = mock(UnivariateFunction.class);
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        UnivariateOptimizer brentResult = brentOptimizerWithChecker.optimize(optimizationProblem, goalType);
        assertEquals(1.0, brentResult.getPoint().getValue(), 0.0);
        verify(brentOptimizer).optimize(optimizationProblem, goalType);
        verify(convergenceChecker).converged(anyInt(), any(), any());
    }

    @Test
    public void testDoOptimize_Maximize_ConvergenceChecker() {
        BrentOptimizer brentOptimizer = mock(BrentOptimizer.class);
        when(brentOptimizer.optimize(any(), any())).thenReturn(new UnivariateOptimizer() {
            @Override
            public UnivariatePointValuePair getPoint() {
                return new UnivariatePointValuePair(1.0, 1.0);
            }
        });
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(), any()))
                .thenReturn(true);

        BrentOptimizer brentOptimizerWithChecker = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        Function function = mock(UnivariateFunction.class);
        OptimizationProblem optimizationProblem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getMinimum() {
                return 0.0;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateOptimizer brentResult = brentOptimizerWithChecker.optimize(optimizationProblem, goalType);
        assertEquals(1.0, brentResult.getPoint().getValue(), 0.0);
        verify(brentOptimizer).optimize(optimizationProblem, goalType);
        verify(convergenceChecker).converged(anyInt(), any(), any());
    }

    @Test
    public void testBest_NullA() {
        UnivariatePointValuePair result = new BrentOptimizer().best(null, new UnivariatePointValuePair(1.0, 1.0), GoalType.MINIMIZE);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testBest_NullB() {
        UnivariatePointValuePair result = new BrentOptimizer().best(new UnivariatePointValuePair(1.0, 1.0), null, GoalType.MINIMIZE);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testBest_Minimize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.5, 0.25);
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MINIMIZE);
        assertEquals(0.5, result.getValue(), 0.0);
    }

    @Test
    public void testBest_Maximize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MAXIMIZE);
        assertEquals(2.0, result.getValue(), 0.0);
    }

    @Test
    public void testBest_Minimize_IsMinimize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.5, 2.25);
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MINIMIZE);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testBest_Maximize_IsMaximize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.5, 2.25);
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MAXIMIZE);
        assertEquals(1.5, result.getValue(), 0.0);
    }

    @Test
    public void testBest_Minimize_NullReturn() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MINIMIZE);
        assertNull(result);
    }

    @Test
    public void testBest_Maximize_NullReturn() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = new BrentOptimizer().best(a, b, GoalType.MAXIMIZE);
        assertNull(result);
    }
}