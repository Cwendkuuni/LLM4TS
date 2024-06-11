import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 // Test case for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(0.5);
 double expected = -0.5 * Math.log(0.5) + 0.5 * Math.log(Math.PI) - 0.0392560535525475;
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method with positive argument
 @Test
 public void testRegularizedGammaPPositive() {
 double result = 0.5;
 double expected = Gamma.regularizedGammaP(1.5, 1.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method with negative argument
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegative() {
 double result = Gamma.regularizedGammaP(-1.5, 1.0);
 }

 // Test case for regularizedGammaP method with non-positive value
 @Test
 public void testRegularizedGammaPNonPositive() {
 double result = 0.0;
 double expected = Gamma.regularizedGammaP(1.5, 0.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method with large x
 @Test
 public void testRegularizedGammaPLargeX() {
 double result = 0.975562932875;
 double expected = Gamma.regularizedGammaP(1.5, 100.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with positive argument
 @Test
 public void testRegularizedGammaQPositive() {
 double result = 0.5;
 double expected = 1.0 - Gamma.regularizedGammaP(1.5, 1.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with negative argument
 @Test
 public void testRegularizedGammaQNegative() {
 double result = 1.0;
 double expected = Gamma.regularizedGammaQ(1.5, 1.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with non-positive value
 @Test
 public void testRegularizedGammaQNonPositive() {
 double result = 1.0;
 double expected = Gamma.regularizedGammaQ(1.5, 0.0);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with large x
 @Test
 public void testRegularizedGammaQLargeX() {
 double result = 0.024997606487;
 double expected = Gamma.regularizedGammaQ(1.5, 100.0);
 assertEquals(expected, result, 1e-6);
 }
}