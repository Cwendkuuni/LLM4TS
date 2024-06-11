import static org.apache.commons.math3.MathConstants.EPSILON;
import static org.apache.commons.math3.MathConstants.MAX_ITERATIONS;
import static org.apache.commons.math3.SpecialFunctions.logGamma;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

 @Test
 public void testLogGamma() {
 int i;
 for (i = 0; i <= 5; i++) {
 double x = i + 1.0;
 double result = Gamma.logGamma(x);
 double expected = Math.log(factorial(x)) - (x * Math.log(x)) + (x * Math.log(Math.PI) / 2.0) - Math.log(Math.SQRT2);
 assertEquals(expected, result, EPSILON);
 }
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 double NaN = Double.NaN;
 double result = Gamma.logGamma(NaN);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 double result = Gamma.logGamma(-1.0);
 }

 @Test(expected = MathException.class)
 public void testLogGammaZero() {
 double result = Gamma.logGamma(0.0);
 }

 @Test
 public void testRegularizedGammaP() {
 int i;
 for (i = 0; i <= 5; i++) {
 double a = i + 1.0;
 double x = a + 1.0;
 double result = Gamma.regularizedGammaP(a, x);
 double expected = (Math.exp(-x + (a * Math.log(x)) - logGamma(a)) * factorialRecursive(a)) / (Math.pow(Math.E, x));
 assertEquals(expected, result, EPSILON);
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 double a = -1.0;
 double x = 1.0;
 try {
 Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 assertEquals("a must be non-negative", e.getMessage());
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeX() {
 double a = 1.0;
 double x = -1.0;
 try {
 Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 assertEquals("x must be non-negative", e.getMessage());
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPZeroX() {
 double a = 1.0;
 double x = 0.0;
 try {
 Gamma.regularizedGammaP(a, x);
 } catch (MathException e) {
 assertEquals("x cannot be zero", e.getMessage());
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 int i;
 for (i = 0; i <= 5; i++) {
 double a = i + 1.0;
 double x = a + 1.0;
 double result = Gamma.regularizedGammaQ(a, x);
 double expected = 1.0 - ((Math.exp(-x + (a * Math.log(x)) - logGamma(a)) * factorialRecursive(a)) / (Math.pow(Math.E, x)));
 assertEquals(expected, result, EPSILON);
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 double a = -1.0;
 double x = 1.0;
 try {
 Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 assertEquals("a must be non-negative", e.getMessage());
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeX() {
 double a = 1.0;
 double x = -1.0;
 try {
 Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 assertEquals("x must be non-negative", e.getMessage());
 }
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQZeroX() {
 double a = 1.0;
 double x = 0.0;
 try {
 Gamma.regularizedGammaQ(a, x);
 } catch (MathException e) {
 assertEquals("x cannot be zero", e.getMessage());
 }
 }

 @Test
 public void testRegularizedGammaQLargeX() {
 double a = 10.0;
 double x = 20.0;
 double result = Gamma.regularizedGammaQ(a, x);
 double expected = 1.0 - ((Math.exp(-x + (a * Math.log(x)) - logGamma(a)) * factorialRecursive(a)) / (Math.pow(Math.E, x)));
 assertEquals(expected, result, EPSILON);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testRegularizedGammaQIterations() {
 double a = 0.5;
 double x = 1.5;
 try {
 Gamma.regularizedGammaQ(a, x, EPSILON, MAX_ITERATIONS);
 } catch (MaxIterationsExceededException e) {
 // Expected
 }
 }

 private long factorial(double n) {
 long result = 1;
 for (long i = 1; i <= (long) n; i++) {
 result *= i;
 }
 return result;
 }

 private long factorialRecursive(double n) {
 if (n == 0.0) {
 return 1;
 } else {
 return (long) n * factorialRecursive(n - 1.0);
 }
 }
}