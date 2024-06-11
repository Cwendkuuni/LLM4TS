package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializers.AverageStartingPointInitializer;
import org.apache.commons.math3.optim.initializers.ExtendedBrentInitializer;
import org.apache.commons.math3.optim.initializers.NelderMeadSimplexInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with a simple polynomial function and
     * different optimizers.
     */
    @Test
    public void testMultiStartOptimization() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1.0, -1.0, 1.0);
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexInitializer(3), 10, null);

        // Test with default settings
        int maxIterations = optimizer.getMaxIterations();
        double absoluteAccuracy = optimizer.getAbsoluteAccuracy();
        double relativeAccuracy = optimizer.getRelativeAccuracy();
        PointValuePair pp = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertNotEquals(Double.NaN, pp.getPoint());
        Assert.assertNotEquals(Double.NaN, pp.getValue());
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertEquals(maxIterations, optimizer.getIterationCount());
        Assert.assertEquals(pp.getValue(), f.value(pp.getPoint()), absoluteAccuracy);

        // Test with custom convergence criteria
        optimizer.setIterationLimit(50);
        optimizer.setRelativeStopCondition(1.0e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(50, optimizer.getIterationCount());
        Assert.assertTrue(Math.abs(optimizer.getResult() - f.value(optimizer.getPoint())) < 1.0e-6);

        // Test optimization with different optimizers
        optimizer = new MultiStartUnivariateRealOptimizer(
                new ExtendedBrentInitializer(), 10, null);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(optimizer.getResult(), f.value(optimizer.getPoint()), absoluteAccuracy);

        optimizer = new MultiStartUnivariateRealOptimizer(
                new AverageStartingPointInitializer(3), 10, null);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(optimizer.getResult(), f.value(optimizer.getPoint()), absoluteAccuracy);

        // Test optimization with function evaluation exception
        UnivariateRealFunction fWithException = new ExceptionThrowingFunction();
        UnivariateRealOptimizer optimizerWithException = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexInitializer(3), 10, null);
        try {
            optimizerWithException.optimize(fWithException, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail();
        } catch (FunctionEvaluationException expected) {
            // expected
        }

        // Test optimization with convergence exception
        UnivariateRealFunction fWithConvergenceException = new ConvergenceFunction();
        UnivariateRealOptimizer optimizerWithConvergenceException = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexInitializer(3), 10, null);
        try {
            optimizerWithConvergenceException.optimize(fWithConvergenceException, GoalType.MINIMIZE,
                    -10.0, 10.0);
            Assert.fail();
        } catch (ConvergenceException expected) {
            // expected
        }

        // Test optimization with illegal argument exception
        UnivariateRealFunction fWithIllegalArgumentException = new IllegalArgumentFunction();
        UnivariateRealOptimizer optimizerWithIllegalArgumentException = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexInitializer(3), 10, null);
        try {
            optimizerWithIllegalArgumentException.optimize(fWithIllegalArgumentException, null,
                    -10.0, 10.0);
            Assert.fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}

class ConvergenceFunction extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new ConvergenceException("Convergence exception.");
    }
}

class ExceptionThrowingFunction extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new FunctionEvaluationException("Function evaluation exception.");
    }
}

class IllegalArgumentFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new IllegalArgumentException("Illegal argument exception.");
    }
}