import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 assertEquals(0.0, Gamma.logGamma(0.0), 1e-9);
 assertEquals(0.0, Gamma.logGamma(1.0), 1e-9);
 assertEquals(Math.log(0.5), Gamma.logGamma(0.5), 1e-9);
 assertEquals(Math.log(1.5), Gamma.logGamma(1.5), 1e-9);
 assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-9);

 // Test negative input
 assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-9);
 assertEquals(Double.NaN, Gamma.logGamma(0.0), 1e-9);

 // Test large input
 assertEquals(Math.log(120.0), Gamma.logGamma(3.0), 1e-9);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentInLogGamma() {
 Gamma.logGamma(-1.0);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid inputs
 assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-9);
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-9);
 assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-9);
 assertEquals(0.5, Gamma.regularizedGammaP(0.5, 2.0), 1e-9);

 // Test negative input
 assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 1e-9);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 0.0), 1e-9);

 // Test large input
 assertEquals(0.6666666666666666, Gamma.regularizedGammaP(1.5, 2.0), 1e-6);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid inputs
 assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-9);
 assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-9);
 assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 2.0), 1e-9);

 // Test negative input
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 1e-9);
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 0.0), 1e-9);

 // Test large input
 assertEquals(0.3333333333333333, Gamma.regularizedGammaQ(1.5, 2.0), 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentInRegularizedGammaPQ() {
 Gamma.regularizedGammaP(-1.0, 1.0);
 Gamma.regularizedGammaQ(-1.0, 1.0);
 }

 @Test
 public void testRegularizedGammaPQLargeInputs() {
 double epsilon = 1e-10;
 int maxIterations = 1000;
 assertEquals(0.7071067811865475, Gamma.regularizedGammaP(0.5, 1.0, epsilon, maxIterations), 1e-9);
 assertEquals(0.3183098861837917, Gamma.regularizedGammaQ(0.5, 1.0, epsilon, maxIterations), 1e-9);
 }
}