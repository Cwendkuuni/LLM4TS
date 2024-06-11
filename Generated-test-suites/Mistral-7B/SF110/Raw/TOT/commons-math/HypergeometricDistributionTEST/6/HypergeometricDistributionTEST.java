import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstructionWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNegativePopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNonStrictlyPositiveNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNonStrictlyPositiveSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 15, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbabilityWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.3639, distribution.cumulativeProbability(3), 0.0001);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbabilityWithLowerDomain() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.0, distribution.cumulativeProbability(0));
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbabilityWithUpperDomain() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(1.0, distribution.cumulativeProbability(5));
 }

 @Test
 public void testHypergeometricDistributionProbabilityWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.3043, distribution.probability(2), 0.0001);
 }

 @Test
 public void testHypergeometricDistributionProbabilityWithLowerDomain() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.0, distribution.probability(0));
 }

 @Test
 public void testHypergeometricDistributionProbabilityWithUpperDomain() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.0, distribution.probability(6));
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(0.0496, distribution.getNumericalVariance(), 0.0001);
 }

 @Test
 public void testHypergeometricDistributionGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 15, 10, 10);
 Assert.assertEquals(1, distribution.getSupportLowerBound());
 }

 @Test
 public void testHypergeometricDistributionGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 15, 10, 10);
 Assert.assertEquals(10, distribution.getSupportUpperBound());
 }
}