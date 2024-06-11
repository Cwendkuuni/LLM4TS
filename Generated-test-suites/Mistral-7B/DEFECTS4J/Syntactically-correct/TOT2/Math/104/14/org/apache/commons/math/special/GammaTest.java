import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 // Test case for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + (Math.log(1.5) - 0.5) - Math.log(2) + 0.32659122326591226;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for logGamma with negative input
 @Test(expected = MathException.class)
 public void testLogGammaNegativeInput() {
 Gamma.logGamma(-1.0);
 }

 // Test case for logGamma with non-numeric input
 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 Gamma.logGamma(Double.NaN);
 }

 // Test case for regularizedGammaP method with valid input
 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = 0.9063024357694806127;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaP with negative a input
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 Gamma.regularizedGammaP(-1.0, 1.5);
 }

 // Test case for regularizedGammaP with negative x input
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeX() {
 Gamma.regularizedGammaP(1.5, -1.0);
 }

 // Test case for regularizedGammaP with x = a
 @Test
 public void testRegularizedGammaPXEqualsA() {
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = 0.9063024357694806127;
 assertEquals(expected, result, 1e-8);

 result = Gamma.regularizedGammaP(2.5, 2.5);
 expected = 0.9189385332046727498;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaP with max iterations exceeded
 @Test(expected = Gamma.MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterations() {
 Gamma.regularizedGammaP(0.5, 0.5, 1e-10, 10);
 }

 // Test case for regularizedGammaQ method with valid input
 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 0.0936975642305193873;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ with negative a input
 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 Gamma.regularizedGammaQ(-1.0, 1.5);
 }

 // Test case for regularizedGammaQ with negative x input
 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeX() {
 Gamma.regularizedGammaQ(1.5, -1.0);
 }

 // Test case for regularizedGammaQ with x > a
 @Test
 public void testRegularizedGammaQXGreaterThanA() {
 double result = Gamma.regularizedGammaQ(1.5, 2.5);
 double expected = 0.0571298293575811917;
 assertEquals(expected, result, 1e-8);
 }

 // Test case for regularizedGammaQ with a = 1
 @Test
 public void testRegularizedGammaQAEquals1() {
 double result = Gamma.regularizedGammaQ(1.0, 1.5);
 double expected = 0.5;
 assertEquals(expected, result, 1e-8);
 }
}