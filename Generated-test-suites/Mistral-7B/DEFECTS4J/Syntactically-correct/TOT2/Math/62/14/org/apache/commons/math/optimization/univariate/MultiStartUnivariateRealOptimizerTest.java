package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximize;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionWithGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundsMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundsMaximizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.InitialGuessNumerical;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.StoppingCondition;
import org.apache.commons.math3.optim.convergence.RelativeChange;
import org.apache.commons.math3.optim.convergence.ValueChange;
import org.apache.commons.math3.optim.convergence.IterationNumber;
import org.apache.commons.math3.optim.convergence.IterationNumberAboveThreshold;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    @Test
    public void testMultiStartMinimizeFunction() throws Exception {
        Random rand = new Random();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateOptimizer optimizer = new FibonacciSearchOptimizer(1, 1.0e-6, 1.0e-5);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rand);

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalTypeMinimize.INSTANCE;
        PointValuePair result = multiStartOptimizer.optimize(function, goal, bounds);

        verify(optimizer, times(5)).optimize(function, goal, bounds);
        assertArrayEquals(new double[] { 1.189205805367313, 3.022635589274792, 6.330525783434393, 3.559516536326221, 0.6254437741927448 }, result.getPoint(), 0.001);
    }

    @Test
    public void testMultiStartMinimizeFunctionWithGradient() throws Exception {
        Random rand = new Random();
        UnivariateRealFunction function = mock(ObjectiveFunctionWithGradient.class);
        UnivariateOptimizer optimizer = new SimpleBoundsMinimizer(1.0e-6, 1.0e-5);
        MultiStartUnivariateRealOptimizer<ObjectiveFunctionWithGradient> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rand);

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalTypeMinimize.INSTANCE;
        InitialGuess initialGuess = new InitialGuessNumerical(0.5);
        ConvergenceChecker<PointValuePair> convergenceChecker = new StoppingCondition(new RelativeChange(1.0e-6), new IterationNumberAboveThreshold(100));
        multiStartOptimizer.setInitialGuess(initialGuess);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);

        PointValuePair result = multiStartOptimizer.optimize(function, goal, bounds);

        verify(optimizer, times(5)).optimize(function, goal, bounds, initialGuess);
        assertArrayEquals(new double[] { 1.189205805367313, 3.022635589274792, 6.330525783434393, 3.559516536326221, 0.6254437741927448 }, result.getPoint(), 0.001);
    }

    @Test
    public void testMultiStartMaximizeFunction() throws Exception {
        Random rand = new Random();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateOptimizer optimizer = new SimpleBoundsMaximizer(1.0e-6, 1.0e-5);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rand);

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalTypeMaximize.INSTANCE;
        PointValuePair result = multiStartOptimizer.optimize(function, goal, bounds);

        verify(optimizer, times(5)).optimize(function, goal, bounds);
        assertArrayEquals(new double[] { 9.810794963268688, 7.013986151202163, 4.933133631013586, 8.440887887439989, 5.618302568977652 }, result.getPoint(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartNoConvergence() throws Exception {
        Random rand = new Random();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateOptimizer optimizer = new FibonacciSearchOptimizer(1, 1.0e-6, 1.0e-5);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rand);

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = new GoalTypeWeightedSum(new double[] { 1.0 }, new double[] { 1.0 });
        multiStartOptimizer.optimize(function, goal, bounds);

        assertNull(multiStartOptimizer.getOptima());
    }
}