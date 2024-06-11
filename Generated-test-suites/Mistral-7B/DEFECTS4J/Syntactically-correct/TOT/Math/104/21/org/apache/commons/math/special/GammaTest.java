import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGammaPositive() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(0.88622692545275801792) + 0.5 * Math.log(2.0 * Math.PI);
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testLogGammaZero() {
 assertNaN(Gamma.logGamma(0.0));
 }

 @Test
 public void testLogGammaNegative() {
 assertNaN(Gamma.logGamma(-1.0));
 }

 @Test
 public void testRegularizedGammaPPositive() throws MaxIterationsExceededException {
 double result = Gamma.regularizedGammaP(1.5, 0.5);
 double expected = 0.93281326815039648158;
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaPZero() {
 double result = Gamma.regularizedGammaP(0.0, 0.5);
 assertEquals(0.5, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaPNegative() {
 assertNaN(Gamma.regularizedGammaP(-1.0, 0.5));
 }

 @Test
 public void testRegularizedGammaPNonPositiveX() {
 assertNaN(Gamma.regularizedGammaP(1.5, -0.5));
 }

 @Test
 public void testRegularizedGammaPNonPositiveA() {
 assertNaN(Gamma.regularizedGammaP(-1.5, 0.5));
 }

 @Test
 public void testRegularizedGammaQPositive() throws MaxIterationsExceededException {
 double result = Gamma.regularizedGammaQ(1.5, 0.5);
 double expected = 0.06718673184960352212;
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQZero() {
 double result = Gamma.regularizedGammaQ(0.0, 0.5);
 assertEquals(0.5, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQNegative() {
 assertNaN(Gamma.regularizedGammaQ(-1.0, 0.5));
 }

 @Test
 public void testRegularizedGammaQNonPositiveX() {
 assertNaN(Gamma.regularizedGammaQ(1.5, -0.5));
 }

 @Test
 public void testRegularizedGammaQNonPositiveA() {
 assertNaN(Gamma.regularizedGammaQ(-1.5, 0.5));
 }

 @Test
 public void testRegularizedGammaQMaxIterations() throws MaxIterationsExceededException {
 double epsilon = 1e-6;
 double maxIterations = 10;
 double result = Gamma.regularizedGammaQ(1.5, 2.0, epsilon, maxIterations);
 assertFalse(Double.isNaN(result));
 assertEquals(result, 0.66292829485174792395, 1e-6);

 // check that it did not exceed the maximum iterations
 assertEquals(maxIterations, Gamma.getLastCalculatedIteration());
 }
}