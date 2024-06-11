import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.util.ContinuedFraction;

public class ContinuedFractionTest1 {

    @Test
    public void testEvaluateDefaultConstructor() {
        ContinuedFraction continuedFraction = new ContinuedFraction();
        double result = continuedFraction.evaluate(0.0);
        Assert.assertEquals(result, ContinuedFraction.DEFAULT_EPSILON, 0.0);
    }

    @Test
    public void testGetAAndGetBWithValidArguments() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double expectedA = 1.0;
        double expectedB = 1.0;
        double x = 0.0;

        double actualA = continuedFraction.getA(0, x);
        double actualB = continuedFraction.getB(0, x);

        Assert.assertEquals(actualA, expectedA, 0.0);
        Assert.assertEquals(actualB, expectedB, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteValue() {
        ContinuedFraction continuedFraction = new ContinuedFraction();
        double x = Double.POSITIVE_INFINITY;
        continuedFraction.evaluate(x);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNValue() {
        ContinuedFraction continuedFraction = new ContinuedFraction();
        double x = Double.NaN;
        continuedFraction.evaluate(x);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction();
        double x = 0.0;
        continuedFraction.evaluate(x, 0.0, 0);
    }

    static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return n % 2 == 0 ? 1.0 : 1.0 / (x + 2.0 * n);
        }

        @Override
        protected double getB(int n, double x) {
            return -1.0 * ((n + 1) % 2 == 0 ? 1.0 : 1.0 / (x + 2.0 * (n + 1)));
        }
    }
}