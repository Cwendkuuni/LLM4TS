package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private static final int MAX_ITERATIONS = 100;
    private static final double MAX_EVALUATIONS = 1000;
    private static final int NUM_STARTS = 3;
    private static final double MIN_BOUND = -5.0;
    private static final double MAX_BOUND = 5.0;
    private static final double ACCURACY = 1e-6;

    @Parameterized.Parameters(name = "{index}: test function {0} with {1} starts")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // polynomial function
                new Object[]{new PolynomialFunction(new double[]{-1, 1, 0.1}), NUM_STARTS},
                // quadratic function
                new Object[]{new PolynomialFunction(new double[]{-1, 1, 1.0}), NUM_STARTS},
                // sin function
                new Object[]{new PolynomialFunction(new double[]{Math.PI, -Math.PI, 1.0}), NUM_STARTS},
                // abs function
                new Object[]{new PolynomialFunction(new double[]{0, 1, 1.0}), NUM_STARTS},
        });
    }

    private final UnivariateRealFunction function;
    private final int starts;

    public MultiStartUnivariateRealOptimizerTest(UnivariateRealFunction function, int starts) {
        this.function = function;
        this.starts = starts;
    }

    @Test
    public void testMultiStartOptimize() throws Exception {
        // initialize
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(new SimpleBoundingMethod(), BrentMethod.BRENT, BrentType.REAL),
                starts, new SimpleRandom());
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setAbsoluteAccuracy(ACCURACY);

        // optimization test
        PointValuePair optimalSolution = optimizer.optimize(function, GoalType.MINIMIZE, MIN_BOUND, MAX_BOUND);
        double x = optimalSolution.getPoint();
        double fx = optimalSolution.getValue();
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        // test optimization result
        assertEquals("Wrong result", function.value(x), fx, ACCURACY);
        assertFalse("No optima found", Arrays.stream(optima).anyMatch(d -> Double.isNaN(d)));
        assertArrayEquals("Wrong optima array", new double[]{x}, optima);
        assertArrayEquals("Wrong optima values array", new double[]{fx}, optimaValues);

        // test multi-start optimization
        int totalIterations = optimizer.getIterationCount();
        int totalEvaluations = optimizer.getEvaluations();
        List<Double> optimaList = Arrays.asList(optima);
        List<Double> optimaValuesList = Arrays.asList(optimaValues);

        assertEquals("Wrong total iterations", MAX_ITERATIONS * (starts + 1), totalIterations);
        assertEquals("Wrong total evaluations", MAX_EVALUATIONS * (starts + 1), totalEvaluations);
        assertEquals("Wrong number of optima", starts + 1, optimaList.size());
        assertEquals("Wrong optima values",
                Arrays.asList(new Double[]{fx, optimaValues[1], optimaValues[2]}),
                optimaValuesList.subList(1, optimaValuesList.size()));

        // test incorrect inputs
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, MIN_BOUND, MAX_BOUND);
            fail("Null function should not pass validation");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer.optimize(function, null, MIN_BOUND, MAX_BOUND);
            fail("Wrong goal type should not pass validation");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, MIN_BOUND, MIN_BOUND);
            fail("Invalid bounds should not pass validation");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}