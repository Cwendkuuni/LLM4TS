import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testNegativePopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), -3, 2, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testZeroPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 0, 2, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 2, 0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesGreaterThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeGreaterThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 2, 6);
 }

 @Test
 public void testCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.cumulativeProbability(0);
 assertEquals(0.0, prob, 0.0);
 }

 @Test
 public void testCumulativeProbabilityEdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.cumulativeProbability(3);
 assertEquals(0.2734375, prob, 0.0001);
 }

 @Test
 public void testCumulativeProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.cumulativeProbability(10);
 assertEquals(1.0, prob, 0.0);
 }

 @Test
 public void testProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.probability(0);
 assertEquals(0.01041667, prob, 0.0001);
 }

 @Test
 public void testProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.probability(10);
 assertEquals(0.01041667, prob, 0.0001);
 }

 @Test
 public void testProbabilityEdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.probability(3);
 assertEquals(0.2734375, prob, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.upperCumulativeProbability(0);
 assertEquals(1.0, prob, 0.0);
 }

 @Test
 public void testUpperCumulativeProbabilityMax() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 int x = 10;
 double prob = dist.upperCumulativeProbability(x);
 assertEquals(0.0, prob, 0.0);
 }

 @Test
 public void testUpperCumulativeProbabilityEdgeCase() {
 HypergemetricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double prob = dist.upperCumulativeProbability(3);
 assertEquals(0.7265625, prob, 0.0001);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double mean = dist.getNumericalMean();
 assertEquals(2.5, mean, 0.0);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 double variance = dist.getNumericalVariance();
 assertEquals(0.3264, variance, 0.01);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 int lowerBound = dist.getSupportLowerBound();
 assertEquals(3, lowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 int upperBound = dist.getSupportUpperBound();
 assertEquals(3, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
 boolean connected = dist.isSupportConnected();
 assertTrue(connected);
 }
}