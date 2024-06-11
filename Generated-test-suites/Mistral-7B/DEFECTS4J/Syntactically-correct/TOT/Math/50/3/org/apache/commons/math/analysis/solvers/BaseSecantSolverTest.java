package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiMethod() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double result = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals("Regula Falsi should find a root around x = 1.5", Precision.round(result, 4), 1.5, 1e-6);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("Regula Falsi should not find a root in an unbracketed interval");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, Double.POSITIVE_INFINITY);
            fail("Regula Falsi should not accept an infinite upper bound");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, null);
            fail("Regula Falsi should not accept a null function");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIllinoisMethod() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x) - 1;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, new IllinoisSolver.IllinoisMethod());
        double result = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals("Illinois should find a root around x = 0.5", Precision.round(result, 4), 0.5, 1e-6);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("Illinois should not find a root in an unbracketed interval");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, Double.POSITIVE_INFINITY);
            fail("Illinois should not accept an infinite upper bound");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, null);
            fail("Illinois should not accept a null function");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPegasusMethod() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - x * x;
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6, new PegasusSolver.PegasusMethod());
        double result = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals("Pegasus should find a root around x = 0.524", Precision.round(result, 5), 0.524356887, 1e-6);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("Pegasus should not find a root in an unbracketed interval");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, Double.POSITIVE_INFINITY);
            fail("Pegasus should not accept an infinite upper bound");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, null);
            fail("Pegasus should not accept a null function");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -5.0, 5.0, new double[] { 1.0, 2.0 });
            fail("Pegasus should not accept an array as starting point");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        solver.setMaxEvaluations(0);
        solver.solve(10, function, -5.0, 5.0, 0.0);
    }
}