package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi_Function1() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, TestConstants.SQRT2, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_RegulaFalsi_Function2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, Math.log(2.0) / Math.E, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_RegulaFalsi_Function3() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -TestConstants.PI, TestConstants.PI);
            assertTrue(Precision.isClose(root, 0.0, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Illinois_Function1() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1e-6, 1e-6);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, TestConstants.SQRT2, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Illinois_Function2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1e-6, 1e-6);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, Math.log(2.0) / Math.E, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Illinois_Function3() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1e-6, 1e-6);
        try {
            double root = solver.solve(100, function, -TestConstants.PI, TestConstants.PI);
            assertTrue(Precision.isClose(root, 0.0, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Pegasus_Function1() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS, 1e-6, 1e-6,
                1e-6);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, TestConstants.SQRT2, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Pegasus_Function2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS, 1e-6, 1e-6,
                1e-6);
        try {
            double root = solver.solve(100, function, -3.0, 3.0);
            assertTrue(Precision.isClose(root, Math.log(2.0) / Math.E, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test
    public void testSolver_Pegasus_Function3() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS, 1e-6, 1e-6,
                1e-6);
        try {
            double root = solver.solve(100, function, -TestConstants.PI, TestConstants.PI);
            assertTrue(Precision.isClose(root, 0.0, 1e-6));
        } catch (MaxEvalExceededException e) {
            fail("MaxEvalExceededException should not be thrown.");
        } catch (RootFinderException e) {
            fail("RootFinderException should not be thrown.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_SolverConstructor_WrongMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1e-6, 1e-6,
                1e-6, Method.REGULA_FALSI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_SolverConstructor_NullFunction() {
        UnivariateRealFunction function = null;
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI, function,
                -1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_SolverConstructor_NullMin() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = null;
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI, function,
                min, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_SolverConstructor_NullMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double max = null;
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI, function,
                -1.0, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_SolverConstructor_WrongAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI, 1e-6,
                1e-6, 1e-6, AllowedSolution.ABOVE_SIDE);
    }

    @Test
    public void testVerification_Bracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        try {
            solver.solve(1, function, 0, 1);
            fail("Expected RootFinderException.");
        } catch (RootFinderException e) {
            // Expected.
        }
    }
}