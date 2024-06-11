import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid input
        double result = Gamma.logGamma(1.5);
        Assert.assertEquals(Math.log(Gamma.EPSILON * Gamma.PI * Math.pow(2.71828, 1.5)), result, 0.001);

        // Test with edge cases
        result = Gamma.logGamma(0.0);
        Assert.assertEquals(Double.NaN, result);

        result = Gamma.logGamma(-1.0);
        Assert.assertEquals(Double.NaN, result);

        result = Gamma.logGamma(Integer.MAX_VALUE);
        Assert.assertEquals(Double.NaN, result);

        // Test with error scenarios
        try {
            result = Gamma.logGamma(Double.NEGATIVE_INFINITY);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.logGamma(Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (MathException e) {
            // Expected
        }
    }

    // Repeat the same pattern for each method (regularizedGammaP, regularizedGammaQ)

}