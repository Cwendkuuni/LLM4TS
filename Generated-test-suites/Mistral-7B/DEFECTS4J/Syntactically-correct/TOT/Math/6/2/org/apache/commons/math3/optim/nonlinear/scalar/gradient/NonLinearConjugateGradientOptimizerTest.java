package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.FletcherReevesPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.PolakRibierePreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.IdentityPreconditioner;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleChain;
import org.junit.rules.TestRules;

import java.util.Arrays;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test rule chain.
     */
    @Rule
    public TestRuleChain testRules = new TestRuleChain()
            .around(new TestRules() {
                @Override
                public TestRule[] rules() {
                    return new TestRule[]{new org.apache.commons.math3.util.PrecisionTestRules()};
                }
            });

    /**
     * Test point.
     */
    private double[] testPoint;

    /**
     * Test function.
     */
    private UnivariateFunction testFunction;

    /**
     * Test optimizer.
     */
    private ConjugateGradientOptimizer optimizer;

    /**
     * Initialize test variables.
     */
    @Before
    public void setUp() {
        testPoint = new double[]{1.0, 2.0};
        testFunction = func -> 10.0 * Math.pow(func[0], 2) + Math.pow(func[1], 2);
        optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()) {
            @Override
            public double[] computeObjectiveGradient(double[] point) {
                return new double[]{
                        2.0 * testFunction.value(point, null) * point[0],
                        2.0 * testFunction.value(point, null) * point[1]};
            }
        };
    }

    /**
     * Test optimization with identity preconditioner.
     */
    @Test
    public void testOptimizationIdentityPreconditioner() {
        final double tolerance = 1e-8;
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        PointValuePair result = optimizer.optimize(testFunction, x, null, maxEval);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(1.0, result.getPoint()[0], tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);
    }

    /**
     * Test optimization with Fletcher-Reeves preconditioner.
     */
    @Test
    public void testOptimizationFletcherReevesPreconditioner() {
        final double tolerance = 1e-8;
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        Preconditioner preconditioner = new FletcherReevesPreconditioner();
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner) {
            @Override
            public double[] computeObjectiveGradient(double[] point) {
                return new double[]{
                        2.0 * testFunction.value(point, null) * point[0],
                        2.0 * testFunction.value(point, null) * point[1]};
            }
        };

        PointValuePair result = optimizer.optimize(testFunction, x, null, maxEval);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(1.0, result.getPoint()[0], tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);
    }

    /**
     * Test optimization with Polak-Ribiere preconditioner.
     */
    @Test
    public void testOptimizationPolakRibierePreconditioner() {
        final double tolerance = 1e-8;
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        Preconditioner preconditioner = new PolakRibierePreconditioner();
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner) {
            @Override
            public double[] computeObjectiveGradient(double[] point) {
                return new double[]{
                        2.0 * testFunction.value(point, null) * point[0],
                        2.0 * testFunction.value(point, null) * point[1]};
            }
        };

        PointValuePair result = optimizer.optimize(testFunction, x, null, maxEval);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(1.0, result.getPoint()[0], tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);
    }

    /**
     * Test optimization with invalid preconditioner.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizationInvalidPreconditioner() {
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        Preconditioner preconditioner = x -> new double[x.length];
        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner) {
            @Override
            public double[] computeObjectiveGradient(double[] point) {
                return new double[]{
                        2.0 * testFunction.value(point, null) * point[0],
                        2.0 * testFunction.value(point, null) * point[1]};
            }
        };

        optimizer.optimize(testFunction, x, null, maxEval);
    }

    /**
     * Test optimization with convergence checker.
     */
    @Test
    public void testOptimizationConvergenceChecker() {
        final double tolerance = 1e-8;
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(int iteration, PointValuePair current, PointValuePair previous) {
                return Precision.abs(current.getValue() - previous.getValue()) < tolerance;
            }
        };

        PointValuePair result = optimizer.optimize(testFunction, x, convergenceChecker, maxEval);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertEquals(1.0, result.getPoint()[0], tolerance);
        Assert.assertEquals(0.0, result.getValue(), tolerance);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInvalidGoalType() {
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()) {
            @Override
            public double[] computeObjectiveGradient(double[] point) {
                return new double[]{
                        2.0 * testFunction.value(point, null) * point[0],
                        2.0 * testFunction.value(point, null) * point[1]};
            }
        };

        optimizer.optimize(testFunction, x, null, maxEval, GoalType.MAXIMIZE);
    }

    /**
     * Test optimization with initial step.
     */
    @Test
    public void testOptimizationInitialStep() {
        final double tolerance = 1e-8;
        final double[] x = new double[]{3.0};
        final double initialStep = 0.1;
        final MaxEval maxEval = new MaxEval(100);

        PointValuePair result = optimizer.optimize(testFunction, x, null, initialStep, maxEval);

        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0);
        Assert.assertNotEquals(0.0, result.getValue(), tolerance);
        Assert.assertTrue(result.getValue() > 0);
    }

    /**
     * Test optimization with invalid initial step.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInvalidInitialStep() {
        final double[] x = new double[]{3.0};
        final MaxEval maxEval = new MaxEval(100);

        optimizer.optimize(testFunction, x, null, -1.0, maxEval);
    }
}