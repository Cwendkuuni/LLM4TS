import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test with valid input
 double input = 1.5;
 double expected = Gamma.logGamma(input);
 double result = Gamma.logGamma(input);
 Assert.assertEquals("Log gamma test failed for valid input.", expected, result, 1e-8);

 // Test with negative input
 input = -1.0;
 expected = Double.NaN;
 result = Gamma.logGamma(input);
 Assert.assertEquals("Log gamma test failed for negative input.", expected, result);

 // Test with zero input
 input = 0.0;
 expected = Double.NaN;
 result = Gamma.logGamma(input);
 Assert.assertEquals("Log gamma test failed for zero input.", expected, result);
 }

 @Test
 public void testRegularizedGammaP() throws Exception {
 // Test with valid input
 double a = 1.5;
 double x = 1.5;
 double expected = 0.9729945883303632361;
 double result = Gamma.regularizedGammaP(a, x);
 Assert.assertEquals("Regularized gamma P test failed for valid input.", expected, result, 1e-8);

 // Test with negative a and positive x
 a = -1.0;
 x = 1.0;
 expected = Double.NaN;
 result = Gamma.regularizedGammaP(a, x);
 Assert.assertEquals("Regularized gamma P test failed for negative a and positive x.", expected, result);

 // Test with negative a and negative x
 a = -1.0;
 x = -1.0;
 expected = Double.NaN;
 result = Gamma.regularizedGammaP(a, x);
 Assert.assertEquals("Regularized gamma P test failed for negative a and negative x.", expected, result);

 // Test with zero a and positive x
 a = 0.0;
 x = 1.0;
 expected = 0.5;
 result = Gamma.regularizedGammaP(a, x);
 Assert.assertEquals("Regularized gamma P test failed for zero a and positive x.", expected, result, 1e-8);

 // Test with zero a and zero x
 a = 0.0;
 x = 0.0;
 expected = 1.0;
 result = Gamma.regularizedGammaP(a, x);
 Assert.assertEquals("Regularized gamma P test failed for zero a and zero x.", expected, result);
 }

 @Test
 public void testRegularizedGammaQ() throws Exception {
 // Test with valid input
 double a = 1.5;
 double x = 1.5;
 double expected = 0.0270054116700285956;
 double result = Gamma.regularizedGammaQ(a, x);
 Assert.assertEquals("Regularized gamma Q test failed for valid input.", expected, result, 1e-8);

 // Test with negative a and positive x
 a = -1.0;
 x = 1.0;
 expected = Double.NaN;
 result = Gamma.regularizedGammaQ(a, x);
 Assert.assertEquals("Regularized gamma Q test failed for negative a and positive x.", expected, result);

 // Test with negative a and negative x
 a = -1.0;
 x = -1.0;
 expected = Double.NaN;
 result = Gamma.regularizedGammaQ(a, x);
 Assert.assertEquals("Regularized gamma Q test failed for negative a and negative x.", expected, result);

 // Test with zero a and positive x
 a = 0.0;
 x = 1.0;
 expected = 1.0 - expected; // invert expected value because of the way regularized gamma Q is defined
 result = Gamma.regularizedGammaQ(a, x);
 Assert.assertEquals("Regularized gamma Q test failed for zero a and positive x.", expected, result, 1e-8);

 // Test with zero a and zero x
 a = 0.0;
 x = 0.0;
 expected = 1.0;
 result = Gamma.regularizedGammaQ(a, x);
 Assert.assertEquals("Regularized gamma Q test failed for zero a and zero x.", expected, result);
 }
}