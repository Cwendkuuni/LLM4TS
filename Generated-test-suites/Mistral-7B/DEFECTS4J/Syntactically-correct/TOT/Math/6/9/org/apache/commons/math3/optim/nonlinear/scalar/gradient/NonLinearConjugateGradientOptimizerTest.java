package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final UnivariateFunction f = x -> Math.pow(x, 2) + 4 * x + 4;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(4.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization of a function with a local minimum and maximum.
     */
    @Test
    public void testFunctionWithMinAndMax() {
        final UnivariateFunction f = x -> Math.pow(x - 2, 3) - Math.log(1 + Math.abs(x - 2));
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @Test
    public void testFunctionWithMultipleMinima() {
        final UnivariateFunction f = x -> Math.pow(x - 3, 2) + Math.pow(x - 1, 2);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        PointValuePair result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(1.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(2.0, result.getValue(), 1e-6);

        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        result = optimizer.optimize(f, -5, 5);
        Assert.assertEquals(3.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(2.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with an incorrect initial step.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIncorrectInitialStep() {
        final UnivariateFunction f = x -> Math.pow(x, 2) + 4 * x + 4;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        optimizer.setInitialStep(-1);
        optimizer.optimize(f, -5, 5);
    }
}