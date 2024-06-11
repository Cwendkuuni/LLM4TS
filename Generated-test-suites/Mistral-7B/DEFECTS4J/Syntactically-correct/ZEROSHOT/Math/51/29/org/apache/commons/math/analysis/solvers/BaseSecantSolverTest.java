package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-9;
    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-6;

    @org.junit.Test
    public void testSolver_MinAndMax_AllowedAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(-2.0, result, EPSILON);
        } catch (NoBracketingException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_AllowedLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
            assertEquals(-2.0, result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_AllowedRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(-2.0, result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_AllowedBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
            assertEquals(-2.0, result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_AllowedAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(-2.0, result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_RelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        try {
            double result = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0 / Math.E, result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_AbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        try {
            double result = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(Precision.round(1.0 / Math.E, 6), result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolver_MinAndMax_FunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS, FUNCTION_VALUE_ACCURACY);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        try {
            double result = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(Precision.round(1.0 / Math.E, 6), result, EPSILON);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Expected a solution, but got an exception: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolver_MinAndMax_InvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.CIRCLE);
            fail("Expected an exception, but got a solution: " + result);
        } catch (NoBracketingException e) {
            // Expected exception
        }
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolver_MinAndMax_InvalidMin() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            solver.solve(10, function, Double.NEGATIVE_INFINITY, 5.0, AllowedSolution.ANY_SIDE);
            fail("Expected an exception, but got a solution");
        } catch (NoBracketingException e) {
            // Expected exception
        }
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolver_MinAndMax_InvalidMax() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            solver.solve(10, function, -5.0, Double.POSITIVE_INFINITY, AllowedSolution.ANY_SIDE);
            fail("Expected an exception, but got a solution: " + solver.getRoot());
        } catch (NoBracketingException e) {
            // Expected exception
        }
    }
}