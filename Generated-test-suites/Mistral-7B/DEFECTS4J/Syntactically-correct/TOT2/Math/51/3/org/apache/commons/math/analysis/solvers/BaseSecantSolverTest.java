package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private final UnivariateFunction function;
    private final double[] expectedSolutions;
    private final double absoluteAccuracy;
    private final double relativeAccuracy;
    private final double functionValueAccuracy;
    private final BaseSecantSolver.Method method;

    public BaseSecantSolverTest(UnivariateFunction function, double[] expectedSolutions,
                               double absoluteAccuracy, double relativeAccuracy,
                               double functionValueAccuracy, BaseSecantSolver.Method method) {
        this.function = function;
        this.expectedSolutions = expectedSolutions;
        this.absoluteAccuracy = absoluteAccuracy;
        this.relativeAccuracy = relativeAccuracy;
        this.functionValueAccuracy = functionValueAccuracy;
        this.method = method;
    }

    @Parameterized.Parameters(name = "{index}: Testing {0} with tolerance ({1}, {2})")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.ILLINOIS},
                {new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.ILLINOIS},
                {new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.PEGASUS},
                {new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.REGULA_FALSI},
                {new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.PEGASUS},
                {new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.REGULA_FALSI},
                {new PolynomialFunction(new double[]{0.0, 1.0, 1.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.ILLINOIS},
                {new PolynomialFunction(new double[]{0.0, 1.0, 1.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.ILLINOIS},
                {new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.PEGASUS},
                {new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3), new double[]{1.0, 2.0, 3.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.PEGASUS},
                {new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.REGULA_FALSI},
                {new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 1), new double[]{1.0}, 1e-6, 1e-3, 1e-6, BaseSecantSolver.Method.REGULA_FALSI}
        });
    }

    @Test
    public void testSolver() {
        // Test the solver with the expected solutions
        double[] solutions = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method).solve(100, function, getMin(), getMax(), getStartValue());

        // Check if the solutions are correct
        assertArrayEquals("Solver did not find the correct solutions", expectedSolutions, solutions);

        // Test the solver with an incorrect function
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method).solve(100, new UnivariateFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException();
                }
            }, getMin(), getMax(), getStartValue());
            fail("Solver should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test the solver with an incorrect start value
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method).solve(100, function, getMin(), getMax(), 10.0);
            fail("Solver should have thrown a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test the solver with too many function evaluations
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method).solve(0, function, getMin(), getMax(), getStartValue());
            fail("Solver should have thrown a MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    private double getMin() {
        return function.minimize();
    }

    private double getMax() {
        return function.maximize();
    }

    private double getStartValue() {
        return function.value(getMin() + 0.5 * (getMax() - getMin()));
    }

    private double[] expectedSolutionsExpectedSolutions() {
        return expectedSolutions;
    }
}