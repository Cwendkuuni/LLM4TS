import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Positive integers
        Assert.assertEquals(0.0, Gamma.logGamma(0), 1e-12);
        Assert.assertEquals(0.0, Gamma.logGamma(1), 1e-12);
        Assert.assertEquals(1.0, Gamma.logGamma(2), 1e-12);
        Assert.assertEquals(1.4020588012019305, Gamma.logGamma(3), 1e-12);
        Assert.assertEquals(1.702731745329261, Gamma.logGamma(4), 1e-12);
        Assert.assertEquals(2.090111261083668, Gamma.logGamma(5), 1e-12);
        Assert.assertEquals(2.584982802461639, Gamma.logGamma(6), 1e-12);
        Assert.assertEquals(3.1780105490658605, Gamma.logGamma(7), 1e-12);

        // Negative integers
        Assert.assertEquals(-0.0, Gamma.logGamma(0), 1e-12);
        Assert.assertEquals(0.0, Gamma.logGamma(-1), 1e-12);
        Assert.assertEquals(0.9182968850561828, Gamma.logGamma(-0.5), 1e-12);
        Assert.assertEquals(1.5707963267948966, Gamma.logGamma(-1.5), 1e-12);
        Assert.assertEquals(2.401292631653847, Gamma.logGamma(-2.5), 1e-12);

        // Large values
        Assert.assertEquals(1972.0, Gamma.logGamma(1000), 1e-12);

        // NaN and negative numbers
        Assert.assertEquals(Double.NaN, Gamma.logGamma(-2), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.logGamma(NaN), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.logGamma(0.5), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Positive values of 'a' and 'x'
        Assert.assertEquals(0.0, Gamma.regularizedGammaP(0, 0), 1e-12);
        Assert.assertEquals(1.0, Gamma.regularizedGammaP(1, 1), 1e-12);
        Assert.assertEquals(0.9999999999999963, Gamma.regularizedGammaP(1, 2), 1e-12);
        Assert.assertEquals(0.968575124238193, Gamma.regularizedGammaP(1.5, 2), 1e-12);
        Assert.assertEquals(0.9313225706995745, Gamma.regularizedGammaP(2.5, 2), 1e-12);

        // Negative values of 'x'
        Assert.assertEquals(1.0, Gamma.regularizedGammaP(1, -1), 1e-12);
        Assert.assertEquals(0.865063596252114, Gamma.regularizedGammaP(1.5, -1), 1e-12);
        Assert.assertEquals(0.8418006953226295, Gamma.regularizedGammaP(2.5, -1), 1e-12);

        // Large values
        Assert.assertEquals(1.0, Gamma.regularizedGammaP(100, 1000), 1e-12);

        // NaN and negative numbers
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(2, -1), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(2.5, NaN), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 1), 0.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() {
        Gamma.regularizedGammaP(1, 0);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Positive values of 'a' and 'x'
        Assert.assertEquals(1.0, Gamma.regularizedGammaQ(0, 0), 1e-12);
        Assert.assertEquals(0.0, Gamma.regularizedGammaQ(1, 1), 1e-12);
        Assert.assertEquals(0.9999999999999963, Gamma.regularizedGammaQ(1, 2), 1e-12);
        Assert.assertEquals(0.968575124238193, Gamma.regularizedGammaQ(1.5, 2), 1e-12);
        Assert.assertEquals(0.9313225706995745, Gamma.regularizedGammaQ(2.5, 2), 1e-12);

        // Negative values of 'x'
        Assert.assertEquals(1.0, Gamma.regularizedGammaQ(1, -1), 1e-12);
        Assert.assertEquals(0.865063596252114, Gamma.regularizedGammaQ(1.5, -1), 1e-12);
        Assert.assertEquals(0.8418006953226295, Gamma.regularizedGammaQ(2.5, -1), 1e-12);

        // Large values
        Assert.assertEquals(0.0, Gamma.regularizedGammaQ(100, 1000), 1e-12);

        // NaN and negative numbers
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(2, -1), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(2.5, NaN), 0.0);
        Assert.assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 1), 0.0);
    }

    @Test
    public void testRegularizedGammaQLargeA() {
        // Large values of 'a'
        Assert.assertEquals(0.999997820531616, Gamma.regularizedGammaQ(50, 2), 1e-12);
        Assert.assertEquals(0.999999999843093, Gamma.regularizedGammaQ(100, 2), 1e-12);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() {
        Gamma.regularizedGammaQ(1, 0);
    }
}