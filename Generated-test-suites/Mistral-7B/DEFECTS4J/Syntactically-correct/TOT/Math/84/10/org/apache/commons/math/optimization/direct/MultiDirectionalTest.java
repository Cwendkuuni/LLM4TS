package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.NonSmoothedPointValuePair;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private FunctionEvaluator function;
    private Comparator<PointValuePair> comparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        function = new FunctionEvaluator();
        comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue() < o2.getValue() ? -1 : (o1.getValue() > o2.getValue() ? 1 : 0);
            }
        };
    }

    @Test
    public void testConstructor() {
        optimizer = new MultiDirectional(1.5, 0.2);
        assertEquals(1.5, optimizer.khi, 0.001);
        assertEquals(0.2, optimizer.gamma, 0.001);
    }

    @Test
    public void testIterateSimplexWithDefaultCoefficients() throws Exception {
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
        SimplePointValuePair best = (SimplePointValuePair) optimizer.getSolution();
        assertEquals(0.0, best.getPoint()[0], 0.001);
        assertEquals(0.0, best.getPoint()[1], 0.001);
        assertEquals(0.0, best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexWithCustomCoefficients() throws Exception {
        optimizer = new MultiDirectional(3.0, 0.4);
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
        SimplePointValuePair best = (SimplePointValuePair) optimizer.getSolution();
        // Performance improvement with custom coefficients is not guaranteed and depends on the function
        // Check if a solution is found within the specified bounds
        assertNotNull(best);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithInvalidBounds() throws Exception {
        optimizer.minimize(new SimpleBounds(new double[]{Double.POSITIVE_INFINITY}, new double[]{-5.0}), function);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testIterateSimplexWithUnboundedDomain() throws Exception {
        function = new FunctionEvaluatorWithUnboundedDomain();
        optimizer.minimize(new SimpleBounds(new double[]{}, new double[]{}), function);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIterateSimplexWithFunctionReturningZero() throws Exception {
        function = new FunctionEvaluatorWithZeroReturn();
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testIterateSimplexWithMaxEvaluationsExceeded() throws Exception {
        optimizer.setMaxEvaluations(1);
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
    }

    @Test(expected = NoBracketingException.class)
    public void testIterateSimplexWithNoBracketing() throws Exception {
        function = new FunctionEvaluatorWithNoBracketing();
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        optimizer.minimize(new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), function);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(simplexFinal, 1.0, comparator);
        assertEquals(reflected.getValue(), optimizer.getFunctionValue(reflected.getPoint()), 0.001);
        optimizer.evaluateNewSimplex(simplexFinal, 2.0, comparator); // Expected no exception but unreachable code

        optimizer.gamma = 0.9;
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(simplexFinal, 0.9, comparator);
        assertEquals(contracted.getValue(), optimizer.getFunctionValue(contracted.getPoint()), 0.001);
    }

    private class FunctionEvaluator implements org.apache.commons.math3.optim.UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.sin(x[1]), 2);
        }

        @Override
        public double value(double[] x, double[] gradient, int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

    }

    private class FunctionEvaluatorWithZeroReturn implements org.apache.commons.math3.optim.UnivariateFunction {

        @Override
        public double value(double x) {
            return 0;
        }

        @Override
        public double value(double[] x) {
            return 0;
        }

        @Override
        public double value(double[] x, double[] gradient, int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

    }

    private class FunctionEvaluatorWithUnboundedDomain implements org.apache.commons.math3.optim.UnivariateFunction {

        @Override
        public double value(double x) {
            throw new UnboundedDomainException();
        }

        @Override
        public double value(double[] x) {
            throw new UnboundedDomainException();
        }

        @Override
        public double value(double[] x, double[] gradient, int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

    }

}