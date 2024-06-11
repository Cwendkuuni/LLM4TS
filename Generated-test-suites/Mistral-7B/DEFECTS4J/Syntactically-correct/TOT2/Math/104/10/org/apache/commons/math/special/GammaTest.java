import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

 // Test case for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + 0.5 * Math.log(1.5) - 0.1767766963254875;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for logGamma with negative input
 @Test(expected = MathException.class)
 public void testLogGammaNegativeInput() {
 Gamma.logGamma(-1.0);
 }

 // Test case for logGamma with zero input
 @Test(expected = MathException.class)
 public void testLogGammaZeroInput() {
 Gamma.logGamma(0.0);
 }

 // Test case for logGamma with NaN input
 @Test
 public void testLogGammaNaNInput() {
 double result = Gamma.logGamma(Double.NaN);
 assertNaN(result);
 }

 // Test case for regularizedGammaP method
 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(3.0, 2.0);
 double expected = 0.621320343741261;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaP with negative input
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeInput() {
 Gamma.regularizedGammaP(-2.0, 2.0);
 }

 // Test case for regularizedGammaP with zero input
 @Test
 public void testRegularizedGammaPZeroInput() {
 double result = Gamma.regularizedGammaP(0.0, 0.0);
 double expected = 1.0;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaP with x < a
 @Test
 public void testRegularizedGammaPCasesDontMatch() {
 double result = Gamma.regularizedGammaP(2.0, 1.0);
 double expected = 0.88888888888888885;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaP with large a and x
 @Test
 public void testRegularizedGammaPLargeArgs() {
 double result = Gamma.regularizedGammaP(100.0, 100.0);
 double expected = 0.738245188388062;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ method
 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(3.0, 2.0);
 double expected = 0.378679656258825;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ with negative input
 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeInput() {
 Gamma.regularizedGammaQ(-2.0, 2.0);
 }

 // Test case for regularizedGammaQ with zero input
 @Test
 public void testRegularizedGammaQZeroInput() {
 double result = Gamma.regularizedGammaQ(0.0, 0.0);
 double expected = 1.0 - 1.0;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ with x > a
 @Test
 public void testRegularizedGammaQCasesDontMatch() {
 double result = Gamma.regularizedGammaQ(2.0, 3.0);
 double expected = 0.120151939102158;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ with large a and x
 @Test
 public void testRegularizedGammaQLargeArgs() {
 double result = Gamma.regularizedGammaQ(100.0, 100.0);
 double expected = 0.261799385366141;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGamma method with valid input
 @Test
 public void testRegularizedGamma() {
 double result = Gamma.regularizedGamma(3.0, 2.0);
 double expected = 0.621320343741261;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGamma method with negative input
 @Test(expected = MathException.class)
 public void testRegularizedGammaNegativeInput() {
 Gamma.regularizedGamma(-2.0, 2.0);
 }

 // Test case for regularizedGamma method with zero input
 @Test
 public void testRegularizedGammaZeroInput() {
 double result = Gamma.regularizedGamma(0.0, 0.0);
 double expected = 1.0;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGamma method with x < a
 @Test
 public void testRegularizedGammaCasesDontMatch() {
 double result = Gamma.regularizedGamma(2.0, 1.0);
 double expected = 1.0 - 0.88888888888888885;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGamma method with large a and x
 @Test
 public void testRegularizedGammaLargeArgs() {
 double result = Gamma.regularizedGamma(100.0, 100.0);
 double expected = 1.0 - 0.738245188388062;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGamma method with exception
 @Test(expected = MathException.class)
 public void testRegularizedGammaException() {
 Gamma.regularizedGamma(1.0, 0.0);
 }
}