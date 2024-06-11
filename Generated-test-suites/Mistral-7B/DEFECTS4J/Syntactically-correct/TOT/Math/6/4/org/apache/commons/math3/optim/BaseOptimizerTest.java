package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyOptimizer();
        optimizer.optimize(new DummyFunction(), new DummyInitialGuess(), new MaxEval(10));
        int evaluations = optimizer.getEvaluations();
        Assert.assertNotNull(evaluations);
        Assert.assertEquals(10, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyOptimizer();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
        Assert.assertInstanceOf(SimpleConvergenceChecker.class, convergenceChecker);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerWithCustomChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyOptimizer();
        optimizer.setConvergenceChecker(new CustomConvergenceChecker());
        try {
            ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    private static class DummyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class DummyInitialGuess implements Number {
        private double value;

        public DummyInitialGuess(double value) {
            this.value = value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return (float) doubleValue();
        }

        @Override
        public int intValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long longValue() {
            throw new UnsupportedOperationException();
        }
    }

    private static class DummyOptimizer implements BaseOptimizer<PAIR> {
        private int evaluations;

        @Override
        public int getMaxEvaluations() {
            return 100;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return new SimpleConvergenceChecker();
        }

        @Override
        public void optimize(UnivariateFunction function, Number initialGuess, MaxEval maxEval) {
            evaluations += maxEval.getMaxIterations();
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            throw new UnsupportedOperationException();
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PAIR> {
        @Override
        public boolean isConverged(PAIR currentValue, PAIR previousValue) {
            return currentValue.getValue() < previousValue.getValue();
        }
    }
}