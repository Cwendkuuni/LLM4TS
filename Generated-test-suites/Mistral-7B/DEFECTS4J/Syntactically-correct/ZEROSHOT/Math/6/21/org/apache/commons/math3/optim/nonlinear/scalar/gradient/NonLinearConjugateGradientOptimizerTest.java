package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.pointvalues.PointValuePair;
import org.apache.commons.math3.optimization.simplechecker.SimpleValueChecker;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optimization.preconditioner.Preconditioner;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    private UnivariateFunction testFunction;
    private ConjugateGradientFormula updateFormula;
    private Preconditioner preconditioner;
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private UnivariateSolver lineSearchSolver;
    private NonLinearConjugateGradientOptimizer optimizer;
    private double[] startPoint;

    @org.junit.Before
    public void setUp() {
        testFunction = mock(UnivariateFunction.class);
        updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        preconditioner = mock(Preconditioner.class);
        convergenceChecker = mock(ConvergenceChecker.class);
        lineSearchSolver = mock(BrentSolver.class);

        when(testFunction.value(anyDouble())).thenCallRealMethod();
        when(testFunction.value(0)).thenReturn(0);
        when(testFunction.gradient(anyDouble())).thenCallRealMethod();

        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        startPoint = new double[]{1.0, 1.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
    }

    @org.junit.Test
    public void testConstructorWithDefaults() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertEquals("Unexpected number of arguments for constructor", 4, optimizer.getNumberOfArguments());
        assertEquals("Update formula not set correctly", updateFormula, optimizer.getUpdateFormula());
        assertNull("Preconditioner not set to null by default", optimizer.getPreconditioner());
        assertNull("Line search solver not set to null by default", optimizer.getLineSearchSolver());
        assertEquals("Initial step not set to default value of 1.0", 1.0, optimizer.getInitialStep(), 0.001);
    }

    @org.junit.Test
    public void testConstructorWithSimpleValueCheckerAndBrentSolver() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), lineSearchSolver, preconditioner);
        assertEquals("Unexpected number of arguments for constructor", 4, optimizer.getNumberOfArguments());
        assertEquals("Update formula not set correctly", updateFormula, optimizer.getUpdateFormula());
        assertNotNull("Preconditioner not set correctly", optimizer.getPreconditioner());
        assertEquals("Line search solver not set correctly", lineSearchSolver, optimizer.getLineSearchSolver());
        assertEquals("Initial step not set to default value of 1.0", 1.0, optimizer.getInitialStep(), 0.001);
    }

    @org.junit.Test
    public void testSetInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        optimizer.setInitialStep(2.0);
        assertEquals("Initial step not set to specified value", 2.0, optimizer.getInitialStep(), 0.001);
        optimizer.setInitialStep(0.0);
        assertEquals("Initial step not reset to default value of 1.0", 1.0, optimizer.getInitialStep(), 0.001);
    }

    @org.junit.Test
    public void testOptimizeWithIdentityPreconditionerAndBrentSolver() throws Exception {
        when(preconditioner.precondition(anyArray(), anyArray())).then(invocation -> invocation.getArgs()[1]);

        double[] result = optimizer.optimize();
        verify(testFunction, times(3)).value(anyDouble());
        verify(testFunction, times(3)).gradient(anyDouble());
        verify(preconditioner, times(3)).precondition(anyArray(), anyArray());
        verify(lineSearchSolver, times(1)).solve(anyInt(), any(UnivariateFunction.class), anyDouble(), anyDouble(), anyDouble());

        PointValuePair pair = new PointValuePair(Arrays.copyOf(result, result.length), optimizer.getObjectiveValue());
        verify(convergenceChecker, times(1)).checkConvergence(optimizer.getIterations(), pair, any(PointValuePair.class));

        double[] expectedResult = new double[]{0.0, 0.0};
        assertEquals("Optimization did not converge or result is incorrect", expectedResult, result, 0.001);
        verifyNoMoreInteractions();
    }

    @org.junit.Test
    public void testOptimizeWithCustomPreconditionerAndBrentSolver() throws Exception {
        when(preconditioner.precondition(anyArray(), anyArray())).then(invocation -> {
            double[] variables = invocation.getArgs()[0];
            double[] r = invocation.getArgs()[1];
            double[] p = new double[variables.length];
            System.arraycopy(variables, 0, p, 0, variables.length);
            double[] q = new double[variables.length];
            System.arraycopy(r, 0, q, 0, variables.length);

            for (int i = 0; i < variables.length; ++i) {
                p[i] += 2 * r[i];
            }

            return p;
        });

        double[] result = optimizer.optimize();
        verify(testFunction, times(5)).value(anyDouble());
        verify(testFunction, times(5)).gradient(anyDouble());
        verify(preconditioner, times(5)).precondition(anyArray(), anyArray());
        verify(lineSearchSolver, times(2)).solve(anyInt(), any(UnivariateFunction.class), anyDouble(), anyDouble(), anyDouble());

        PointValuePair pair = new PointValuePair(Arrays.copyOf(result, result.length), optimizer.getObjectiveValue());
        verify(convergenceChecker, times(1)).checkConvergence(optimizer.getIterations(), pair, any(PointValuePair.class));

        double[] expectedResult = new double[]{0.5, 0.5};
        assertEquals("Optimization did not converge or result is incorrect", expectedResult, result, 0.001);
        verifyNoMoreInteractions();
    }

    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithDivergingLineSearch() throws Exception {
        when(preconditioner.precondition(anyArray(), anyArray())).then(invocation -> invocation.getArgs()[1]);

        doThrow(new MathIllegalStateException(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH)).when(lineSearchSolver).solve(anyInt(), any(UnivariateFunction.class), anyDouble(), anyDouble(), anyDouble());

        optimizer.optimize();
        verify(testFunction, times(2)).value(anyDouble());
        verify(testFunction, times(2)).gradient(anyDouble());
        verify(preconditioner, times(2)).precondition(anyArray(), anyArray());
        verify(lineSearchSolver, times(1)).solve(anyInt(), any(UnivariateFunction.class), anyDouble(), anyDouble(), anyDouble());
        verify(convergenceChecker, never()).checkConvergence(anyInt(), any(PointValuePair.class), any(PointValuePair.class));
    }

    @org.junit.Test
    public void testFindUpperBound() {
        double a = 1.0;
        double h = 2.0;

        double b = optimizer.findUpperBound(testFunction, a, h);

        double yA = testFunction.value(a);
        double yB = testFunction.value(b);

        assertEquals("Upper bound not found", yA * Math.signum(yB) < 0, yA * yB <= 0, 0.001);
    }

}