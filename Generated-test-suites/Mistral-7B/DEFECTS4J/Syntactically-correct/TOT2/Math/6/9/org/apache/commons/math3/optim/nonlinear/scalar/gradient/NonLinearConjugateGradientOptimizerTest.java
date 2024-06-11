package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.SimpleValueCheckerConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test function for optimization. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /**
     * Test the optimizer with default settings.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testDefaultSettings() throws MathIllegalArgumentException {
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(TEST_FUNCTION);
        final double expectedOptimum = 2.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
    }

    /**
     * Test the optimizer with preconditioner.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testPreconditioner() throws MathIllegalArgumentException {
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double[] result = new double[variables.length];
                        for (int i = 0; i < variables.length; ++i) {
                            result[i] = r[i] * Math.sin(Math.PI * i / 4.0);
                        }
                        return result;
                    }
                })
                .optimize(TEST_FUNCTION);
        final double expectedOptimum = 0.5;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
    }

    /**
     * Test the optimizer with line search solver.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testLineSearchSolver() throws MathIllegalArgumentException {
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner())
                .optimize(TEST_FUNCTION);
        final double expectedOptimum = 2.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
    }

    /**
     * Test the optimizer with valid but out of domain initial point.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain or the
     * initial point is out of domain.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testOutOfDomainInitialPoint() throws MathIllegalArgumentException {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(new TestFunctionOutOfDomain());
    }

    /**
     * Test the optimizer with invalid initial point.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain or the
     * initial point is invalid.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialPoint() throws MathIllegalArgumentException {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(new TestFunctionInvalidInitialPoint());
    }

    /**
     * Test the optimizer with null objective function.
     * @throws NullPointerException if the objective function is null.
     */
    @Test(expected = NullPointerException.class)
    public void testNullObjectiveFunction() {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(null);
    }

    /**
     * Test the optimizer with invalid convergence checker.
     * @throws IllegalArgumentException if the convergence checker is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConvergenceChecker() {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                null,
                null,
                null)
                .optimize(TEST_FUNCTION);
    }

    /**
     * Test the optimizer with null preconditioner.
     * @throws NullPointerException if the preconditioner is null.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPreconditioner() {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(TEST_FUNCTION);
    }

    /**
     * Test the optimizer with null line search solver.
     * @throws NullPointerException if the line search solver is null.
     */
    @Test(expected = NullPointerException.class)
    public void testNullLineSearchSolver() {
        new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                new IdentityPreconditioner())
                .optimize(TEST_FUNCTION);
    }

    /**
     * Test the optimizer with simple value checker convergence checker.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testSimpleValueCheckerConvergenceChecker() throws MathIllegalArgumentException {
        final ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueCheckerConvergenceChecker(1e-6);
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker,
                null,
                null)
                .optimize(TEST_FUNCTION);
        final double expectedOptimum = 2.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
        Assert.assertTrue(convergenceChecker.isConverged(1, optimizedPoint, optimizedPoint));
    }

    /**
     * Test the optimizer with custom convergence checker.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testCustomConvergenceChecker() throws MathIllegalArgumentException {
        final ConvergenceChecker<PointValuePair> convergenceChecker = new CustomConvergenceChecker(1e-6);
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker,
                null,
                null)
                .optimize(TEST_FUNCTION);
        final double expectedOptimum = 2.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
        Assert.assertTrue(convergenceChecker.isConverged(1, optimizedPoint, optimizedPoint));
    }

    /**
     * Test the optimizer with zero step size.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testZeroStepSize() throws MathIllegalArgumentException {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null);
        optimizer.setInitialStep(0);
        optimizer.optimize(TEST_FUNCTION);
    }

    /**
     * Test the optimizer with a function with a local minimum at the beginning of its domain.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testMinimumAtBeginningOfDomain() throws MathIllegalArgumentException {
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(new TestFunctionMinimumAtBeginningOfDomain());
        final double expectedOptimum = -1.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
    }

    /**
     * Test the optimizer with a function with a local minimum at the end of its domain.
     * @throws MathIllegalArgumentException if the test function is not defined in the expected domain.
     */
    @Test
    public void testMinimumAtEndOfDomain() throws MathIllegalArgumentException {
        final PointValuePair optimizedPoint = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueCheckerConvergenceChecker(1e-6),
                null,
                null)
                .optimize(new TestFunctionMinimumAtEndOfDomain());
        final double expectedOptimum = 3.0;
        Assert.assertEquals(expectedOptimum, optimizedPoint.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedOptimum, optimizedPoint.getValue(), 1e-6);
    }

    /** Function for optimization testing. */
    private static class TestFunction implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x);
        }
    }

    /** Function for optimization testing with an out of domain initial point. */
    private static class TestFunctionOutOfDomain implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            throw new MathIllegalArgumentException("Test function is not defined for x = " + x);
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    /** Function for optimization testing with an invalid initial point. */
    private static class TestFunctionInvalidInitialPoint implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            throw new IllegalArgumentException("Invalid initial point: " + x);
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    /** Function for optimization testing with a minimum at the beginning of its domain. */
    private static class TestFunctionMinimumAtBeginningOfDomain implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            return Math.pow(Math.sin(x + 0.1), 2);
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            return 2 * Math.sin(x + 0.1) * Math.cos(x + 0.1);
        }
    }

    /** Function for optimization testing with a minimum at the end of its domain. */
    private static class TestFunctionMinimumAtEndOfDomain implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            return Math.pow(Math.sin(x - 0.1), 2);
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            return 2 * Math.sin(x - 0.1) * Math.cos(x - 0.1);
        }
    }

    /** Custom convergence checker for testing purposes. */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        private final double tolerance;

        /** Constructor.
         * @param tolerance tolerance for convergence check.
         */
        public CustomConvergenceChecker(double tolerance) {
            this.tolerance = tolerance;
        }

        /** {@inheritDoc} */
        public boolean isConverged(int iteration, PointValuePair current, PointValuePair previous) {
            if (iteration > 1 && current.getValue() - previous.getValue() < tolerance) {
                return true;
            }
            return false;
        }
    }
}