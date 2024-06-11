import static org.junit.Assert.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.ConvergenceException;

import org.apache.commons.math3.util.ContinuedFraction;

public class ContinuedFractionTest {

    @SuppressWarnings("unchecked")
    private static <T extends ContinuedFraction<T, Double>> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.Test
    public void testEvaluateSqrt2() {
        ContinuedFraction sqrt2 = newInstance(ContinuedFraction2Sqrt.class);
        double epsilon = 1e-12;
        double x = 1.0;
        double result = sqrt2.evaluate(x, epsilon);
        double expected = 1.41421356237309552860109936;
        assertEquals(expected, result, epsilon);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateSqrt2WithMaxIterations() {
        ContinuedFraction sqrt2 = newInstance(ContinuedFraction2Sqrt.class);
        double epsilon = 1e-12;
        double x = 1.0;
        sqrt2.evaluate(x, epsilon, 10);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateSqrt2WithMaxIterationsThrowsException() {
        ContinuedFraction sqrt2 = newInstance(ContinuedFraction2Sqrt.class);
        double epsilon = 1e-12;
        double x = 1.0;
        sqrt2.evaluate(x, epsilon, 5);
        sqrt2.evaluate(x, epsilon, 5);
    }

    @org.junit.Test
    public void testEvaluateE() {
        ContinuedFraction e = newInstance(ContinuedFractionE.class);
        double epsilon = 1e-12;
        double x = 1.0;
        double result = e.evaluate(x, epsilon);
        double expected = 2.718281828459045108203064;
        assertEquals(expected, result, epsilon);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateEWithMaxIterations() {
        ContinuedFraction e = newInstance(ContinuedFractionE.class);
        double epsilon = 1e-12;
        double x = 1.0;
        e.evaluate(x, epsilon, 10);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateEWithMaxIterationsThrowsException() {
        ContinuedFraction e = newInstance(ContinuedFractionE.class);
        double epsilon = 1e-12;
        double x = 1.0;
        e.evaluate(x, epsilon, 5);
        e.evaluate(x, epsilon, 5);
    }

    // Add tests for other subclasses of ContinuedFraction as needed

    @org.junit.Test
    public void testEvaluateWithDefaultEpsilon() {
        ContinuedFraction sqrt2 = newInstance(ContinuedFraction2Sqrt.class);
        double x = 1.0;
        double result = sqrt2.evaluate(x);
        double expected = 1.41421356237309552860109936;
        assertEquals(expected, result, ContinuedFraction.DEFAULT_EPSILON);
    }

    @org.junit.Test
    public void testEvaluateWithDefaultMaxIterations() {
        ContinuedFraction sqrt2 = newInstance(ContinuedFraction2Sqrt.class);
        double x = 1.0;
        double result = sqrt2.evaluate(x, ContinuedFraction.DEFAULT_EPSILON);
        double expected = 1.41421356237309552860109936;
        assertEquals(expected, result, ContinuedFraction.DEFAULT_EPSILON);
    }
}