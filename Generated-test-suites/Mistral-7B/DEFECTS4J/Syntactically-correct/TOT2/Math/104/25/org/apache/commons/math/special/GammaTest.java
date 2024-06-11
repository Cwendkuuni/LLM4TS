import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGamma() {
        Assert.assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        Assert.assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-10);
        Assert.assertEquals(Math.log(2.0), Gamma.logGamma(1.5), 1e-10);
        Assert.assertEquals(Math.log(Math.PI) + Math.log(2.0), Gamma.logGamma(1.5), 1e-10);
        Assert.assertEquals(Math.log(Math.E) + Math.log(Math.PI), Gamma.logGamma(0.5), 1e-10);

        try {
            Gamma.logGamma(-1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.logGamma(Double.NaN);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        Assert.assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        Assert.assertEquals(0.17132449235827226, Gamma.regularizedGammaP(1.0, 0.5), 1e-10);
        Assert.assertEquals(0.99999999999999929328, Gamma.regularizedGammaP(1.5, 2.0), 1e-10);
        Assert.assertEquals(0.88888888888888875003, Gamma.regularizedGammaP(2.0, 1.5), 1e-10);
        Assert.assertEquals(0.70710678118654752441, Gamma.regularizedGammaP(3.0, 1.5), 1e-10);

        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(Double.NaN, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(1.0, Double.NaN);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        Assert.assertEquals(0.0, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        Assert.assertEquals(1.0 - 0.17132449235827226, Gamma.regularizedGammaQ(1.0, 0.5), 1e-10);
        Assert.assertEquals(1.0 - 0.99999999999999929328, Gamma.regularizedGammaQ(1.5, 2.0), 1e-10);
        Assert.assertEquals(1.0 - 0.88888888888888875003, Gamma.regularizedGammaQ(2.0, 1.5), 1e-10);
        Assert.assertEquals(1.0 - 0.70710678118654752441, Gamma.regularizedGammaQ(3.0, 1.5), 1e-10);

        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(Double.NaN, 1.0);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.0, Double.NaN);
            Assert.fail();
        } catch (MathException e) {
            // expected
        }
    }
}