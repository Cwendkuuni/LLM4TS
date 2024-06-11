import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 // Test for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 double expected = Math.log(Math.Gamma(1.0)); // Expected value from Math library
 assertEquals(expected, result, 1e-9); // Comparing with an absolute tolerance of 1e-9

 result = Gamma.logGamma(0.5);
 expected = Math.log(Math.Gamma(0.5)) + Math.log(Math.SQRT_E) / 2; // Calculated value using Math library and SQRT_E constant
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(-1.0);
 assertNaN(result);
 }

 // Test for regularizedGammaP method
 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 double expected = Math.exp(1.0) / Math.sqrt(Math.PI); // Expected value from Math library
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(2.0, 1.0);
 expected = 1.0; // Expected value for int argument
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(0.5, 1.0);
 expected = Math.sqrt(Math.PI) / (2 * Math.sqrt(Math.E)); // Calculated value
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 0.0);
 assertNaN(result);
 }

 // Test for regularizedGammaQ method
 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 double expected = 0.0; // Expected value for int argument
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(2.0, 1.0);
 expected = 1.0;
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(0.5, 1.0);
 expected = Math.sqrt(Math.PI) / (2 * Math.sqrt(Math.E));
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 0.0);
 assertNaN(result);
 }

 // Test for edge cases and exception handling
 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 result = Gamma.regularizedGammaP(1.0, -1.0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 result = Gamma.regularizedGammaQ(1.0, -1.0);
 }

 @Test(expected = MathException.class)
 public void testLogGammaException() {
 result = Gamma.logGamma(-1.0);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 result = Gamma.logGamma(Double.NaN);
 }

 @Test(expected = MathException.class)
 public void testLogGammaInf() {
 result = Gamma.logGamma(Double.POSITIVE_INFINITY);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegInf() {
 result = Gamma.logGamma(Double.NEGATIVE_INFINITY);
 }
}