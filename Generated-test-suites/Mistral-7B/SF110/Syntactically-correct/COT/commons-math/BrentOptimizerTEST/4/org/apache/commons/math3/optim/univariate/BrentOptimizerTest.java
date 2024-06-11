package org.apache.commons.math3.optim.univariate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.BrentOptimizerException;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.special.Fresnel;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateFunction function;
    private UnivariateOptimizer optimizer;

    @Before
    public void setup() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Fresnel.sinc(x);
            }
        };
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
    }

    @Test
    public void testConstructorWithArguments() {
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelativeThresholdOnly() {
        optimizer = new BrentOptimizer(1.0e-6, null);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testThrowNumberIsTooSmallExceptionWithRelativeThresholdLessThanMinimum() {
        optimizer = new BrentOptimizer(1.0e-8, 1.0e-6, null);
        optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testThrowNotStrictlyPositiveExceptionWithAbsoluteThresholdLessThanZero() {
        optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
        optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100));
    }

    @Test
    public void testOptimizeWithValidInput() {
        PointValuePair result = optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100));
        assertEquals(0.0, result.getPoint(), 1.0e-9);
    }

    @Test
    public void testOptimizeWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() == previous.getValue();
            }
        };
        PointValuePair result = optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100), convergenceChecker);
        assertEquals(0.0, result.getPoint(), 1.0e-9);
    }

    @Test(expected = BrentOptimizerException.class)
    public void testThrowExceptionWhenFailsToConverge() {
        optimizer = new BrentOptimizer(1.0e-12, 1.0e-12, null);
        optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100));
    }

}