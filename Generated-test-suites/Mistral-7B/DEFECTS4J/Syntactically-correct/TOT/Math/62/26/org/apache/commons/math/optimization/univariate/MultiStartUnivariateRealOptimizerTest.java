package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.gradient.BroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
    private final double[] x = new double[2];
    private final double[] fx = new double[2];
    private static final RandomValuesGenerator rng = new RandomValuesGenerator();

    @Before
    public void setUp() {
        optimizer = new BroydenFletcherGoldfarbShanno();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, rng);
        convergenceChecker = ConvergenceCheckerFactory.REL_TOL_ABS.setMaxIterations(Integer.MAX_VALUE);
    }

    @Test
    public void testMinimization() throws Exception {
        UnivariateRealFunction functionToOptimize = new PolynomialFunction(2, 1, -1);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionToOptimize, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType(), 0.0);
        Assert.assertArrayEquals(x, result.getPoint(), 5);
        Assert.assertArrayEquals(fx, result.getValue(), 5e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        UnivariateRealFunction functionToOptimize = new PolynomialFunction(2, 1, -1);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionToOptimize, goal, min, max, 0.0);
        Assert.assertNull(result);
    }

    @Test
    public void testMaximization() throws Exception {
        UnivariateRealFunction functionToOptimize = new PolynomialFunction(2, 1, 1);
        GoalType goal = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(functionToOptimize, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(GoalType.MAXIMIZE, result.getGoalType(), 0.0);
        Assert.assertArrayEquals(x, result.getPoint(), 5);
        Assert.assertArrayEquals(fx, result.getValue(), 5e-6);
    }

    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction functionToOptimize = new PolynomialFunction(2, 1, -1);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, rng);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        multiStartOptimizer.optimize(functionToOptimize, goal, min, max);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        List<UnivariateRealPointValuePair> resultList = List.of(optima[0], optima[1], optima[2], optima[3], optima[4], optima[5], optima[6], optima[7], optima[8], optima[9]);
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[10], optima);
        Assert.assertEquals(goal, optima[0].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[1].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[2].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[3].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[4].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[5].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[6].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[7].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[8].getGoalType(), 0.0);
        Assert.assertEquals(goal, optima[9].getGoalType(), 0.0);
        for (int i = 0; i < 10; ++i) {
            Assert.assertNotNull(optima[i]);
            Assert.assertArrayEquals(x, optima[i].getPoint(), 5);
            Assert.assertArrayEquals(fx, optima[i].getValue(), 5e-6);
        }
    }
}