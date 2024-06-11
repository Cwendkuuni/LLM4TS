package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.IdentityPreconditioner;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Simple test for the constructor with default settings,
     * {@link SimpleValueChecker checker}, {@link BrentSolver line search solver}
     * and {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testDefaultConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        assertEquals(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test for the constructor with custom {@link ConvergenceChecker checker}
     * and {@link BrentSolver line search solver}.
     */
    @Test
    public void testCustomConstructorWithBrentSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver());
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        assertNull(optimizer.getPreconditioner());
    }

    /**
     * Test for the constructor with custom {@link UnivariateSolver line search solver}
     * and {@link IdentityPreconditioner preconditioner}.
     */
    @Test
    public void testCustomConstructorWithIdentityPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver(), new IdentityPreconditioner());
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        assertEquals(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test for the constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver(), preconditioner);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        assertEquals(preconditioner.getClass(), optimizer.getPreconditioner().getClass());
    }

    /**
     * Test for setting the initial step.
     */
    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver());
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep());
    }

    /**
     * Test for the optimization method.
     * @throws MathIllegalStateException if the optimizer cannot bracket the optimum.
     */
    @Test
    public void testOptimize() throws MathIllegalStateException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double valuePart1(double x) {
                return Math.sin(x);
            }

            @Override
            public double valuePart2(double x) {
                return Math.sin(x);
            }

            @Override
            public double[] gradient(double x) {
                double[] result = new double[2];
                result[0] = 2 * Math.cos(x) * Math.sin(x);
                result[1] = 2 * Math.cos(x) * Math.sin(x);
                return result;
            }

            @Override
            public double gradientPart1(double x) {
                return 2 * Math.cos(x);
            }

            @Override
            public double gradientPart2(double x) {
                return 2 * Math.cos(x);
            }
        };

        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver());
        double[] startPoint = new double[]{-1.0, 1.0};
        optimizer.optimize(f, startPoint, new PointValuePair[]{new PointValuePair(-1.5, 2.25), new PointValuePair(-0.5, 0.25)}, null, null);

        double[] expectedResult = new double[]{-0.7499999999999999, 0.4999999999999999};
        assertArrayEquals(expectedResult, startPoint, 0.001);
    }

    /**
     * Test for the optimization method with negative initial step.
     * @throws MathIllegalStateException if the optimizer cannot bracket the optimum.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNegativeInitialStep() throws MathIllegalStateException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double valuePart1(double x) {
                return Math.sin(x);
            }

            @Override
            public double valuePart2(double x) {
                return Math.sin(x);
            }

            @Override
            public double[] gradient(double x) {
                double[] result = new double[2];
                result[0] = 2 * Math.cos(x) * Math.sin(x);
                result[1] = 2 * Math.cos(x) * Math.sin(x);
                return result;
            }

            @Override
            public double gradientPart1(double x) {
                return 2 * Math.cos(x);
            }

            @Override
            public double gradientPart2(double x) {
                return 2 * Math.cos(x);
            }
        };

        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, new BrentSolver());
        double[] startPoint = new double[]{-1.0, 1.0};
        optimizer.setInitialStep(-1.0);
        optimizer.optimize(f, startPoint, new PointValuePair[]{new PointValuePair(-1.5, 2.25), new PointValuePair(-0.5, 0.25)}, null, null);
    }
}