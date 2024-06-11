import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testEvaluateSquareRootOfTwo() {
        ContinuedFraction sqrt2 = new SqrtTwoContinuedFraction();
        double result = sqrt2.evaluate(2.0);
        assertEquals(2.0, result, 1e-9);
    }

    @Test
    public void testEvaluateSquareRootOfTwoEpsilon() {
        ContinuedFraction sqrt2 = new SqrtTwoContinuedFraction();
        double result = sqrt2.evaluate(2.0, 1e-12);
        assertEquals(2.0, result, 1e-12);
    }

    @Test
    public void testEvaluateSquareRootOfTwoMaxIterations() {
        ContinuedFraction sqrt2 = new SqrtTwoContinuedFraction();
        try {
            sqrt2.evaluate(2.0, 2);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(0.0);
        assertEquals(1.618051232076661, result, 1e-12);
    }

    @Test
    public void testEvaluateGoldenRatioEpsilon() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(0.0, 1e-15);
        assertEquals(1.618051232076661, result, 1e-15);
    }

    @Test
    public void testEvaluateGoldenRatioMaxIterations() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        try {
            goldenRatio.evaluate(0.0, 5);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateE() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(0.0);
        assertEquals(2.718281828459045, result, 1e-12);
    }

    @Test
    public void testEvaluateEpsilon() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(0.0, 1e-15);
        assertEquals(2.718281828459045, result, 1e-15);
    }

    @Test
    public void testEvaluateEMaxIterations() {
        ContinuedFraction e = new EContinuedFraction();
        try {
            e.evaluate(0.0, 5);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluatePi() {
        ContinuedFraction pi = new PiContinuedFraction();
        double result = pi.evaluate(0.0);
        assertEquals(3.141592653589793, result, 1e-12);
    }

    @Test
    public void testEvaluatePiEpsilon() {
        ContinuedFraction pi = new PiContinuedFraction();
        double result = pi.evaluate(0.0, 1e-15);
        assertEquals(3.141592653589793, result, 1e-15);
    }

    @Test
    public void testEvaluatePiMaxIterations() {
        ContinuedFraction pi = new PiContinuedFraction();
        try {
            pi.evaluate(0.0, 5);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    private static class SqrtTwoContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return 1.0;
            }
            return FastMath.sqrt(2.0);
        }

        @Override
        protected double getB(int n, double x) {
            if (n % 2 == 1) {
                return -1.0;
            }
            return 2.0;
        }
    }

    private static class GoldenRatioContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n > 0) ? (1.0 + 1.0 / getA(n - 1, x)) : 0.0;
        }

        @Override
        protected double getB(int n, double x) {
            return (n > 0) ? (1.0) : 0.0;
        }
    }

    private static class EContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n > 0) ? (1.0 + 1.0 / getA(n - 1, x)) : 2.5;
        }

        @Override
        protected double getB(int n, double x) {
            return (n > 0) ? (n) : 1.0;
        }
    }

    private static class PiContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if ((n % 2) == 0) {
                return 1.0;
            }
            return -1.0 * FastMath.signum(Math.sin(FastMath.PI * (double) n / 2.0));
        }

        @Override
        protected double getB(int n, double x) {
            return (n > 0) ? (2.0 * FastMath.PI * (double) n) : 0.0;
        }
    }
}