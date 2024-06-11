package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomDataGenerators;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    private static final double EPSILON = 1.0e-5;
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new MyFunction();
        optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(), 10, RandomDataGenerators.getDefaultRandom());
    }

    @Test
    public void testMinimizationOneEval() throws Exception {
        optimizer.setMaxEvaluations(1);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.5, result.getPoint().doubleValue(), EPSILON);
        assertEquals(-1.648435059473268e-12, result.getValue(), EPSILON);
    }

    @Test
    public void testMinimizationTenEval() throws Exception {
        optimizer.setMaxEvaluations(10);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.5, result.getPoint().doubleValue(), EPSILON);
        assertEquals(-1.648435059473268e-12, result.getValue(), EPSILON);

        UnivariateRealPointValuePair[] allResults = optimizer.getOptima();
        assertEquals(10, allResults.length);
        for (UnivariateRealPointValuePair p : allResults) {
            assertNotNull(p);
            assertEquals(0.5, p.getPoint().doubleValue(), EPSILON);
            assertEquals(-1.648435059473268e-12, p.getValue(), EPSILON);
        }
    }

    @Test
    public void testMinimizationMaxEval() throws Exception {
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.5, result.getPoint().doubleValue(), EPSILON);
        assertEquals(-1.648435059473268e-12, result.getValue(), EPSILON);

        UnivariateRealPointValuePair[] allResults = optimizer.getOptima();
        assertEquals(10, allResults.length); // Maximum of 10, but may be less
        for (UnivariateRealPointValuePair p : allResults) {
            assertNotNull(p);
            assertEquals(0.5, p.getPoint().doubleValue(), EPSILON);
            assertEquals(-1.648435059473268e-12, p.getValue(), EPSILON);
        }
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMinimizationTooManyEval() throws Exception {
        optimizer.setMaxEvaluations(5);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        fail("Did not throw MaxEvalExceededException");
    }

    @Test
    public void testMaximizationOneEval() throws Exception {
        optimizer.setMaxEvaluations(1);
        PointValuePair result = optimizer.optimize(new MyMaxFunction(), GoalType.MAXIMIZE, 0.0, 1.0);
        assertEquals(1.0, result.getPoint().doubleValue(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testMaximizationTenEval() throws Exception {
        optimizer.setMaxEvaluations(10);
        PointValuePair result = optimizer.optimize(new MyMaxFunction(), GoalType.MAXIMIZE, 0.0, 1.0);
        assertEquals(1.0, result.getPoint().doubleValue(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);

        UnivariateRealPointValuePair[] allResults = optimizer.getOptima();
        assertEquals(10, allResults.length);
        for (UnivariateRealPointValuePair p : allResults) {
            assertNotNull(p);
            assertEquals(1.0, p.getPoint().doubleValue(), EPSILON);
            assertEquals(1.0, p.getValue(), EPSILON);
        }
    }

    @Test
    public void testMaximizationMaxEval() throws Exception {
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        PointValuePair result = optimizer.optimize(new MyMaxFunction(), GoalType.MAXIMIZE, 0.0, 1.0);
        assertEquals(1.0, result.getPoint().doubleValue(), EPSILON);
        assertEquals(1.0, result.getValue(), EPSILON);

        UnivariateRealPointValuePair[] allResults = optimizer.getOptima();
        assertEquals(10, allResults.length); // Maximum of 10, but may be less
        for (UnivariateRealPointValuePair p : allResults) {
            assertNotNull(p);
            assertEquals(1.0, p.getPoint().doubleValue(), EPSILON);
            assertEquals(1.0, p.getValue(), EPSILON);
        }
    }

    @Test
    public void testMinimizationBounds() throws Exception {
        optimizer.setMaxEvaluations(10);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 0.0);
        assertEquals(-1.0, result.getPoint().doubleValue(), EPSILON);
        assertEquals(1.648435059473268e-12, result.getValue(), EPSILON);

        UnivariateRealPointValuePair[] allResults = optimizer.getOptima();
        assertEquals(10, allResults.length);
        for (UnivariateRealPointValuePair p : allResults) {
            assertNotNull(p);
            if (p.getValue() >= 0.0) {
                assertEquals(0.5, p.getPoint().doubleValue(), EPSILON);
                assertEquals(-1.648435059473268e-12, p.getValue(), EPSILON);
            } else {
                assertNotNull(p.getPoint());
                assertTrue(p.getPoint().doubleValue() < 0.0);
            }
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() throws Exception {
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
        fail("Did not throw NullArgumentException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonUnivariateFunction() throws Exception {
        optimizer.optimize(new TwoVariableFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        fail("Did not throw UnsupportedOperationException");
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOutOfBounds() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        fail("Did not throw NumberIsTooLargeException");
    }

    private static class MyFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.pow(Math.sin(Math.PI * x), 2);
        }

        public double derivative(double x) {
            return 2.0 * Math.sin(Math.PI * x) * Math.cos(Math.PI * x);
        }

        public double[] gradient(double[] x) {
            return new double[]{2.0 * Math.sin(Math.PI * x[0]) * Math.cos(Math.PI * x[0])};
        }

        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MyMaxFunction implements UnivariateFunction {
        public double value(double x) {
            return x;
        }

        public double derivative(double x) {
            return 1.0;
        }

        public double[] gradient(double[] x) {
            return new double[]{1.0};
        }

        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FibonacciSearchOptimizer
            extends BaseUnivariateRealOptimizer<UnivariateFunction> {

        public PointValuePair optimize(UnivariateFunction f, GoalType goal, double min, double max)
                throws FunctionEvaluationException {
            return new SimpleBounds(min, max)
                    .optimize(f, goal, new InitialGuess(max), new MaxEval(10));
        }
    }
}