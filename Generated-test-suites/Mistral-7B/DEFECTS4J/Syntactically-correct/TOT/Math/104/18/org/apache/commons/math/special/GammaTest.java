import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGammaPositiveNumber() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(0.88622692545275801792);
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testLogGammaZero() {
 double result = Gamma.logGamma(0.0);
 double expected = Double.NaN;
 assertEquals(expected, result);
 }

 @Test
 public void testLogGammaNegativeNumber() {
 double result = Gamma.logGamma(-1.5);
 double expected = Double.NaN;
 assertEquals(expected, result);
 }

 @Test
 public void testRegularizedGammaPZeroArgument() {
 double result = Gamma.regularizedGammaP(0.0, 1.0);
 double expected = 0.0;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaPLargeArgument() {
 double result = Gamma.regularizedGammaP(100.0, 100.0);
 double expected = 0.9999999999999965656;
 assertEquals(expected, result, 1e-12);
 }

 @Test
 public void testRegularizedGammaPEpsilon() {
 double result = Gamma.regularizedGammaP(1.5, 1.5, 1e-9, 1000);
 double expected = 0.88622692545275801792;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaPMaxIterations() {
 try {
 double result = Gamma.regularizedGammaP(1.5, 1.5, 1e-9, 1);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 //expected
 }
 }

 @Test
 public void testRegularizedGammaQZeroArgument() {
 double result = Gamma.regularizedGammaQ(0.0, 1.0);
 double expected = 1.0;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaQLargeArgument() {
 double result = Gamma.regularizedGammaQ(100.0, 100.0);
 double expected = 1.2377181932227235528;
 assertEquals(expected, result, 1e-12);
 }

 @Test
 public void testRegularizedGammaQEpsilon() {
 double result = Gamma.regularizedGammaQ(1.5, 1.5, 1e-9, 1000);
 double expected = 0.11377307692307692435;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaQMaxIterations() {
 try {
 double result = Gamma.regularizedGammaQ(1.5, 1.5, 1e-9, 1);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 //expected
 }
 }
}