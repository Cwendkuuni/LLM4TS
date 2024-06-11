import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionCreation() {
 HypergeometricDistribution dist;
 // Positive cases
 dist = new HypergeometricDistribution(10, 5, 5);
 // Negative cases
 try {
 dist = new HypergeometricDistribution(-1, 5, 5);
 fail("Should throw NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(10, -5, 5);
 fail("Should throw NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(10, 5, -5);
 fail("Should throw NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(10, 5, 11);
 fail("Should throw NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(10, 11, 5);
 fail("Should throw NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(null, 5, 5);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 try {
 dist = new HypergeometricDistribution(new RandomGenerator() {}, 10, 5, 5);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 // Valid cases
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 int[] domain = dist.getDomain();
 // Test lower bound
 assertEquals(0.0, dist.cumulativeProbability(domain[0]), 0.01);
 // Test mean
 assertEquals(3.0 / 5.0, dist.cumulativeProbability(3), 0.01);
 // Test upper bound
 assertEquals(1.0, dist.cumulativeProbability(domain[1]), 0.01);
 // Test values outside domain
 assertEquals(0.0, dist.cumulativeProbability(-1), 0.01);
 assertEquals(0.0, dist.cumulativeProbability(domain[1] + 1), 0.01);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double prob = dist.probability(i);
 // Test values inside domain
 assertEquals(dist.probability(i), dist.probability(i), 0.01);
 }
 // Test values outside domain
 assertEquals(0.0, dist.probability(-1), 0.01);
 assertEquals(0.0, dist.probability(domain[1] + 1), 0.01);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 int[] domain = dist.getDomain();
 // Test lower bound
 assertEquals(1.0, dist.upperCumulativeProbability(domain[0]), 0.01);
 // Test mean
 assertEquals(dist.cumulativeProbability(3), dist.upperCumulativeProbability(3), 0.01);
 // Test upper bound
 assertEquals(0.0, dist.upperCumulativeProbability(domain[1]), 0.01);
 // Test values outside domain
 assertEquals(0.0, dist.upperCumulativeProbability(-1), 0.01);
 assertEquals(1.0, dist.upperCumulativeProbability(domain[1] + 1), 0.01);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertEquals(5, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertEquals(4, dist.getSampleSize());
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertEquals(1, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertEquals(3, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 4);
 assertTrue(dist.isSupportConnected());
 }
}