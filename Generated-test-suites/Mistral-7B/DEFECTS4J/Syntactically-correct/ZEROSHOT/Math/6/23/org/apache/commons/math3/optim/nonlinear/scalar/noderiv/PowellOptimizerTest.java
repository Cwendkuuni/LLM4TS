package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.multivariate.MultivariateFunction;
import org.apache.commons.math3.optimization.multivariate.PowellOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PowellOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNull(optimizer.getStartPoint());
        Assert.assertNull(optimizer.getConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteTolerance(), 0.0);
        assertEquals(2 * Double.ULP, optimizer.getRelativeTolerance(), 0.0);
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        Assert.assertNull(optimizer.getStartPoint());
        Assert.assertNull(optimizer.getConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-6, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(1e-8, optimizer.getAbsoluteTolerance(), 0.0);
        assertEquals(1e-6, optimizer.getRelativeTolerance(), 0.0);
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, customChecker);
        Assert.assertNull(optimizer.getStartPoint());
        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-6, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(1e-8, optimizer.getAbsoluteTolerance(), 0.0);
        assertEquals(1e-6, optimizer.getRelativeTolerance(), 0.0);
        assertEquals(BrentOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testConstructorWithCustomLineSearchTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, 1e-7, 1e-9);
        Assert.assertNull(optimizer.getStartPoint());
        Assert.assertNull(optimizer.getConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-6, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(1e-8, optimizer.getAbsoluteTolerance(), 0.0);
        assertEquals(1e-6, optimizer.getRelativeTolerance(), 0.0);
        assertFalse(optimizer.getLineSearch().isLineSearchEnabled());
        assertEquals(1e-7, optimizer.getLineSearch().getRelativeTolerance(), 0.0);
        assertEquals(1e-9, optimizer.getLineSearch().getAbsoluteTolerance(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, -1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(0, 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithTwoVariables() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair solution = optimizer.optimize(function, startPoint);

        double[] expectedSolution = new double[]{-1.22331223, -1.22331223};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-5);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-5);
        assertEquals(-2.71828183, solution.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeFunctionWithThreeVariables() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]) + Math.sin(x[2]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 3;
            }
        };

        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair solution = optimizer.optimize(function, startPoint);

        double[] expectedSolution = new double[]{-1.22331223, -1.22331223, -1.22331223};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-5);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-5);
        assertEquals(expectedSolution[2], solution.getPoint()[2], 1e-5);
        assertEquals(-8.0 * Math.pow(Math.SIN, 3), solution.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeFunctionWithCustomStartPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{0.5, 0.5};
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair solution = optimizer.optimize(function, startPoint);

        double[] expectedSolution = new double[]{-1.17763624, -1.17763624};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-5);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-5);
        assertEquals(-2.0 * Math.pow(Math.SIN, 2), solution.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeFunctionWithCustomGoalType() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer(GoalType.MAXIMIZE);
        PointValuePair solution = optimizer.optimize(function, startPoint);

        double[] expectedSolution = new double[]{-1.22331223, -1.22331223};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-5);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-5);
        assertEquals(-2.71828183, solution.getValue(), 1e-5);
        assertTrue(solution.isMaximum());
    }

    @Test
    public void testOptimizeFunctionWithCustomChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        double[] startPoint = new double[]{1.0, 1.0};
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1.0;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, customChecker);
        PointValuePair solution = optimizer.optimize(function, startPoint);

        double[] expectedSolution = new double[]{-0.84147098, -0.84147098};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-5);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-5);
        assertEquals(0.6865644, solution.getValue(), 1e-5);
    }
}