import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 } catch (NotPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test
 public void testHypergeometricDistributionWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, distribution.getNumberOfSuccesses());
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getSampleSize());
 int[] domain = distribution.getDomain();
 assertEquals(3, domain[0]);
 assertEquals(5, domain[1]);
 }

 @Test
 public void testCumulativeProbabilityZero() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double cumulativeProbability = distribution.cumulativeProbability(0);
 assertEquals(0.0, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilitySuccess() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double cumulativeProbability = distribution.cumulativeProbability(2);
 assertEquals(0.2734375, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityFailure() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double cumulativeProbability = distribution.cumulativeProbability(6);
 assertEquals(0.515625, cumulativeProbability, 0.001);
 }

 @Test
 public void testProbabilityZero() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double probability = distribution.probability(0);
 assertEquals(0.0, probability, 0.001);
 }

 @Test
 public void testProbabilitySuccess() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double probability = distribution.probability(2);
 assertEquals(0.1265625, probability, 0.001);
 }

 @Test
 public void testProbabilityFailure() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double probability = distribution.probability(6);
 assertEquals(0.0, probability, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double upperCumulativeProbability = distribution.upperCumulativeProbability(0);
 assertEquals(1.0, upperCumulativeProbability, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilitySuccess() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double upperCumulativeProbability = distribution.upperCumulativeProbability(2);
 assertEquals(0.7265625, upperCumulativeProbability, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityFailure() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double upperCumulativeProbability = distribution.upperCumulativeProbability(6);
 assertEquals(0.0, upperCumulativeProbability, 0.001);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double numericalMean = distribution.getNumericalMean();
 assertEquals(3.0, numericalMean, 0.001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double numericalVariance = distribution.getNumericalVariance();
 assertEquals(0.0014583333333333333, numericalVariance, 0.0001);
 }

 @Test
 public void testSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int supportLowerBound = distribution.getSupportLowerBound();
 assertEquals(1, supportLowerBound);
 }

 @Test
 public void testSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int supportUpperBound = distribution.getSupportUpperBound();
 assertEquals(3, supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(distribution.isSupportConnected());
 }
}