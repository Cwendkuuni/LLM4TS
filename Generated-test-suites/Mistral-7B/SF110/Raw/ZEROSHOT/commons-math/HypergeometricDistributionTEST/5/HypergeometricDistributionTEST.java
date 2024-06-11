import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution dist;

 // Test with valid arguments
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(5, dist.getNumberOfSuccesses());
 assertEquals(10, dist.getSampleSize());

 // Test with invalid arguments (population size)
 try {
 dist = new HypergeometricDistribution(new Well19937c(), -5, 5, 10);
 fail("Should have thrown NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 0, 5, 10);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 11, 10);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 11);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 // Test with invalid arguments (number of successes)
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, -5, 10);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 10);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 // Test with invalid arguments (sample size)
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 int[] domain = dist.getDomain();

 // Test lower bound
 assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 1e-10);

 // Test exact value
 assertEquals(0.5, dist.cumulativeProbability(domain[0]), 1e-10);

 // Test upper bound
 assertEquals(1.0, dist.cumulativeProbability(domain[1]), 1e-10);

 // Test values outside domain
 assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 1e-10);
 assertEquals(0.0, dist.cumulativeProbability(domain[1] + 1), 1e-10);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 int[] domain = dist.getDomain();

 // Test lower bound
 assertEquals(0.0, dist.probability(domain[0] - 1), 1e-10);

 // Test exact value
 assertEquals(0.045, dist.probability(domain[0]), 1e-10);

 // Test upper bound
 assertEquals(0.0, dist.probability(domain[1] + 1), 1e-10);

 // Test values outside domain
 assertEquals(0.0, dist.probability(domain[0] - 1), 1e-10);
 assertEquals(0.0, dist.probability(domain[1] + 1), 1e-10);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 int[] domain = dist.getDomain();

 // Test lower bound
 assertEquals(1.0, dist.upperCumulativeProbability(domain[0] - 1), 1e-10);

 // Test exact value
 assertEquals(0.455, dist.upperCumulativeProbability(domain[0]), 1e-10);

 // Test upper bound
 assertEquals(0.0, dist.upperCumulativeProbability(domain[1] + 1), 1e-10);

 // Test values outside domain
 assertEquals(1.0, dist.upperCumulativeProbability(domain[0] - 1), 1e-10);
 assertEquals(0.0, dist.upperCumulativeProbability(domain[1] + 1), 1e-10);
 }

 @Test
 public void testNumericalMeanAndVariance() {
 HypergemetricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 assertEquals(5.0, dist.getNumericalMean(), 1e-10);
 assertEquals(2.9166666666666665, dist.getNumericalVariance(), 1e-10);
 }

 @Test
 public void testGetSupportLowerAndUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 assertEquals(-5, dist.getSupportLowerBound());
 assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 assertTrue(dist.isSupportConnected());
 }
}