package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

/**
 * Test for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with valid arguments.
     */
    @Test
    public void testEvaluateValidArguments() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        assertEquals(2.44948974278317, result, 1e-6);

        continuedFraction = new LogarithmBase2ContinuedFraction();
        result = continuedFraction.evaluate(2.0);
        assertEquals(1.5, result, 1e-9);

        continuedFraction = new EContinuedFraction();
        result = continuedFraction.evaluate(1.0);
        assertEquals(e, result, 1e-6);
    }

    /**
     * Test evaluate method with zero argument.
     */
    @Test
    public void testEvaluateZeroArgument() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(ArithmeticException.class, () -> continuedFraction.evaluate(0.0));
    }

    /**
     * Test evaluate method with negative argument.
     */
    @Test
    public void testEvaluateNegativeArgument() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> continuedFraction.evaluate(-1.0));
    }

    /**
     * Test evaluate method with epsilon less than zero.
     */
    @Test
    public void testEvaluateNegativeEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> continuedFraction.evaluate(1.0, -1e-9));
    }

    /**
     * Test evaluate method with maxIterations less than or equal to zero.
     */
    @Test
    public void testEvaluateZeroOrNegativeMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> continuedFraction.evaluate(1.0, 0));
        assertThrows(IllegalArgumentException.class, () -> continuedFraction.evaluate(1.0, -1));
    }

    /**
     * Test evaluate method with infinite argument.
     */
    @Test
    public void testEvaluateInfiniteArgument() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(Double.POSITIVE_INFINITY));
    }

    /**
     * Test evaluate method with NaN argument.
     */
    @Test
    public void testEvaluateNaNArgument() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        assertThrows(ArithmeticException.class, () -> continuedFraction.evaluate(Double.NaN));
    }

    /**
     * Test evaluate method with convergence exception.
     */
    @Test
    public void testEvaluateConvergenceException() {
        ContinuedFraction continuedFraction = new LogarithmBase2ContinuedFractionWithConvergenceException();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(2.0));
    }

    /**
     * Test evaluate method with max count exceeded exception.
     */
    @Test
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction continuedFraction = new LogarithmBase2ContinuedFractionWithMaxCountExceededException();
        assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(2.0, 10));
    }

}