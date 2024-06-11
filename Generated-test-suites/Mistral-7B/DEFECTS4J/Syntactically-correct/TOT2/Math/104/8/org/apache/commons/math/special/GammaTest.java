import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 // Test case for logGamma method with typical input
 @Test
 public void testLogGammaTypicalInput() {
 double input = 2.0;
 double expected = 1.1821525663506849551;
 double result = Gamma.logGamma(input);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for logGamma method with edge case: negative input
 @Test (expected = MathException.class)
 public void testLogGammaNegativeInput() {
 double input = -1.0;
 Gamma.logGamma(input);
 }

 // Test case for logGamma method with edge case: zero input
 @Test (expected = MathException.class)
 public void testLogGammaZeroInput() {
 double input = 0.0;
 Gamma.logGamma(input);
 }

 // Test case for logGamma method with edge case: very large input
 @Test
 public void testLogGammaVeryLargeInput() {
 double input = 1000.0;
 double expected = 1059.052811421303412;
 double result = Gamma.logGamma(input);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method with typical input
 @Test
 public void testRegularizedGammaPPTypicalInput() {
 double a = 2.0;
 double x = 0.5;
 double expected = 0.8876333827049663785;
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method with edge case: negative a
 @Test (expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 double a = -1.0;
 double x = 0.5;
 Gamma.regularizedGammaP(a, x);
 }

 // Test case for regularizedGammaP method with edge case: non-positive x
 @Test (expected = MathException.class)
 public void testRegularizedGammaPNonPositiveX() {
 double a = 1.0;
 double x = -0.5;
 Gamma.regularizedGammaP(a, x);
 }

 // Test case for regularizedGammaP method with edge case: very large x
 @Test
 public void testRegularizedGammaPVeryLargeX() {
 double a = 1.0;
 double x = 1000.0;
 double expected = 0.1188432499935270973;
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with typical input
 @Test
 public void testRegularizedGammaQTypicalInput() {
 double a = 2.0;
 double x = 0.5;
 double expected = 0.1125226116967785425;
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ method with edge case: negative a
 @Test (expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 double a = -1.0;
 double x = 0.5;
 Gamma.regularizedGammaQ(a, x);
 }

 // Test case for regularizedGammaQ method with edge case: non-positive x
 @Test (expected = MathException.class)
 public void testRegularizedGammaQNonPositiveX() {
 double a = 1.0;
 double x = -0.5;
 Gamma.regularizedGammaQ(a, x);
 }

 // Test case for regularizedGammaQ method with edge case: very large x
 @Test
 public void testRegularizedGammaQVeryLargeX() {
 double a = 1.0;
 double x = 1000.0;
 double expected = 0.8876333827049663785;
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-6);
 }
}