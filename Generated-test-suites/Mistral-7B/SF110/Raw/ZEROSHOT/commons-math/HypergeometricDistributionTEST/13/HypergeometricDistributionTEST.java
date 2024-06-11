import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionNegativePopulationSize() {
 new HypergeometricDistribution(new Well19937c(), -1, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionNegativeNumberOfSuccesses() {
 new HypergeometricDistribution(new Well19937c(), 10, -2, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionNegativeSampleSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 5, -2);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionNumberOfSuccessesLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionSampleSizeLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testGetters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(5, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 assertEquals(5, domain[0]);
 assertEquals(5, domain[1]);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.cumulativeProbability(0);
 assertEquals(0.0, p, 1e-8);
 p = dist.cumulativeProbability(1);
 assertEquals(0.1, p, 1e-8);
 p = dist.cumulativeProbability(2);
 assertEquals(0.202020202020202, p, 1e-8);
 p = dist.cumulativeProbability(5);
 assertEquals(0.5, p, 1e-8);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.probability(0);
 assertEquals(0.0, p, 1e-8);
 p = dist.probability(1);
 assertEquals(0.05, p, 1e-8);
 p = dist.probability(2);
 assertEquals(0.0769230769230769, p, 1e-8);
 p = dist.probability(5);
 assertEquals(0.1, p, 1e-8);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.upperCumulativeProbability(0);
 assertEquals(1.0, p, 1e-8);
 p = dist.upperCumulativeProbability(1);
 assertEquals(0.95, p, 1e-8);
 p = dist.upperCumulativeProbability(2);
 assertEquals(0.923076923076923, p, 1e-8);
 p = dist.upperCumulativeProbability(5);
 assertEquals(0.5, p, 1e-8);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double mean = dist.getNumericalMean();
 assertEquals(5.0, mean, 1e-8);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double variance = dist.getNumericalVariance();
 assertEquals(3.5, variance, 1e-8);
 }

 @Test
 public void testSupportBounds() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int lowerBound = dist.getSupportLowerBound();
 int upperBound = dist.getSupportUpperBound();
 assertEquals(0, lowerBound);
 assertEquals(5, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertTrue(dist.isSupportConnected());
 }
}