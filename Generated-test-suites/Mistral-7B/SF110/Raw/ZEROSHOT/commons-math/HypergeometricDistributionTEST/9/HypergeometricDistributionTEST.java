import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidParameters() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 0);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 // Test lower domain
 double p = dist.cumulativeProbability(0);
 assertEquals(0.0, p, 1e-10);

 // Test valid domain
 p = dist.cumulativeProbability(3);
 assertEquals(0.3472338397226232, p, 1e-10);

 // Test upper domain
 p = dist.cumulativeProbability(7);
 assertEquals(0.6527661602773767, p, 1e-10);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 // Test lower domain
 double p = dist.probability(0);
 assertEquals(0.0, p, 1e-10);

 // Test valid domain
 p = dist.probability(3);
 assertEquals(0.015555555555555555, p, 1e-10);

 // Test upper domain
 p = dist.probability(7);
 assertEquals(0.10869565217391304, p, 1e-10);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 // Test lower domain
 double p = dist.upperCumulativeProbability(0);
 assertEquals(1.0, p, 1e-10);

 // Test valid domain
 p = dist.upperCumulativeProbability(3);
 assertEquals(0.6527661602773767, p, 1e-10);

 // Test upper domain
 p = dist.upperCumulativeProbability(7);
 assertEquals(1.0, p, 1e-10);
 }

 @Test
 public void testCalculateNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double variance = dist.getNumericalVariance();
 assertEquals(0.03343216027263268, variance, 1e-10);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(dist.isSupportConnected());
 }
}