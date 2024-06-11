import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.special.Gamma;

public class GammaTest {

 @Test
 public void testLogGamma() {
 int i;
 for (i = 0; i < 10; i++) {
 double x = i + 1.0;
 double expected = Math.log(factorial(i)) + EulerConstant.EULER_MASCHERONI;
 double actual = Gamma.logGamma(x);
 assertEquals(expected, actual, 1e-12);
 }
 }

 @Test
 public void testLogGammaNaN() {
 double x = Double.NaN;
 double actual = Gamma.logGamma(x);
 assertEquals(Double.NaN, actual);
 }

 @Test
 public void testLogGammaNegative() {
 double x = -1.0;
 double actual = Gamma.logGamma(x);
 assertEquals(Double.NaN, actual);
 }

 @Test
 public void testLogGammaZero() {
 double x = 0.0;
 double actual = Gamma.logGamma(x);
 assertEquals(Double.NaN, actual);
 }

 @Test
 public void testRegularizedGammaP() {
 int i;
 for (i = 1; i < 10; i++) {
 double a = i + 0.5;
 double x = i + 1.0;
 double expected = Math.pow(Math.PI, 0.5) / Math.sqrt(Math.pow(2.0, i)) * factorial(i) / factorial(i + 0.5);
 double actual = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, actual, 1e-12);
 }
 }

 @Test
 public void testRegularizedGammaPZero() {
 double x = 0.0;
 double actual = Gamma.regularizedGammaP(0.5, x);
 assertEquals(0.5, actual, 1e-12);
 }

 @Test
 public void testRegularizedGammaPNegative() {
 double a = -1.0;
 double x = 1.0;
 try {
 Gamma.regularizedGammaP(a, x);
 fail("Should have thrown an exception");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaPNotPositive() {
 double a = 0.5;
 double x = -1.0;
 try {
 Gamma.regularizedGammaP(a, x);
 fail("Should have thrown an exception");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 int i;
 for (i = 1; i < 10; i++) {
 double a = i + 0.5;
 double x = i + 1.0;
 double expected = 1.0 - Math.pow(Math.PI, 0.5) / Math.sqrt(Math.pow(2.0, i)) * factorial(i) / factorial(i + 0.5);
 double actual = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, actual, 1e-12);
 }
 }

 @Test
 public void testRegularizedGammaQZero() {
 double x = 0.0;
 double actual = Gamma.regularizedGammaQ(0.5, x);
 assertEquals(1.0 - 0.5, actual, 1e-12);
 }

 @Test
 public void testRegularizedGammaQNegative() {
 double a = -1.0;
 double x = 1.0;
 try {
 Gamma.regularizedGammaQ(a, x);
 fail("Should have thrown an exception");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testRegularizedGammaQNotPositive() {
 double a = 0.5;
 double x = -1.0;
 try {
 Gamma.regularizedGammaQ(a, x);
 fail("Should have thrown an exception");
 } catch (MathException e) {
 // Expected
 }
 }

 private static long factorial(int n) {
 long result = 1;
 for (int i = 2; i <= n; i++) {
 result *= i;
 }
 return result;
 }
}