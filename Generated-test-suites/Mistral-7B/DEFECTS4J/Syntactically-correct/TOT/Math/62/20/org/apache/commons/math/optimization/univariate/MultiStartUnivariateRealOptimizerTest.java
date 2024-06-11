package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.AroundMaximumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.AbstractRealObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateRealObjectiveFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
    }

    @Test
    public void testEmptyFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };

        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertNull(result);
    }

    @Test
    public void testFunctionWithMinimum() throws Exception {
        UnivariateRealFunction f = new AbstractRealObjectiveFunction("myFunction") {
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };

        GoalType goal = GoalType.MINIMIZE;
        double min = 1.5;
        double max = 2.5;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(2.0, result.getPoint().getValue(), 0.01);
    }

    @Test
    public void testFunctionWithMaximum() throws Exception {
        UnivariateRealFunction f = new AbstractRealObjectiveFunction("myFunction") {
            public double value(double x) {
                return -Math.pow(x - 3.0, 2.0);
            }
        };

        GoalType goal = GoalType.MAXIMIZE;
        double min = 1.5;
        double max = 2.5;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(3.0, result.getPoint().getValue(), 0.01);
    }

    @Test
    public void testFunctionWithMultipleSolutions() throws Exception {
        UnivariateRealFunction f = new AbstractRealObjectiveFunction("myFunction") {
            public double value(double x) {
                return Math.pow(x - 1.0, 2.0);
            }
        };

        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        UnivariateRealPointValuePair[] results = multiStartOptimizer.getOptima();
        assertNotNull(results);
        assertEquals(3, results.length);
        // The results should be in ascending order of their objective function values
        assertEquals(0.0, results[0].getPoint().getValue(), 0.01);
        assertEquals(1.0, results[1].getPoint().getValue(), 0.01);
        assertEquals(1.0, results[2].getPoint().getValue(), 0.01);
    }

    @Test(expected = ConvergenceException.class)
    public void testFunctionWithNoSolution() throws Exception {
        UnivariateRealFunction f = new AbstractRealObjectiveFunction("myFunction") {
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };

        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2.0 * Math.PI;

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testFunctionWithMaxEvaluationsExceeded() throws Exception {
        UnivariateRealFunction f = new AbstractRealObjectiveFunction("myFunction") {
            public double value(double x) {
                return Math.pow(Math.abs(x), 2.0);
            }
        };

        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        int maxEvaluations = 5;

        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.optimize(f, goal, min, max);
    }
}