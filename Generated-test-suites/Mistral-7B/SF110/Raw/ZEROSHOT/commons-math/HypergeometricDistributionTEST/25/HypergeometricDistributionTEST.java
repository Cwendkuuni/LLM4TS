import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected, population size should be strictly positive
 }
 // population size is not strictly positive, test should throw NotPositiveException
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotPositiveException e) {
 Assert.assertEquals("Population size must be positive.", e.getMessage());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected, number of successes should be strictly positive
 }
 // number of successes is not strictly positive, test should throw NotPositiveException
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
 } catch (NotPositiveException e) {
 Assert.assertEquals("Number of successes must be non-negative.", e.getMessage());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 } catch (NotStrictlyPositiveException e) {
 // expected, sample size should be strictly positive
 }
 // sample size is not strictly positive, test should throw NotPositiveException
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 } catch (NotPositiveException e) {
 Assert.assertEquals("Number of samples must be non-negative.", e.getMessage());
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
 } catch (NumberIsTooLargeException e) {
 Assert.assertEquals("Number of successes cannot be larger than the population size.", e.getMessage());
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 } catch (NumberIsTooLargeException e) {
 Assert.assertEquals("Sample size cannot be larger than the population size.", e.getMessage());
 }
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 int[] expectedDomains = distribution.getDomain();
 for (int x = expectedDomains[0]; x <= expectedDomains[1]; x++) {
 double prob = distribution.cumulativeProbability(x);
 double expectedProb = distribution.innerCumulativeProbability(x, x + 1, 1);
 Assert.assertEquals("Cumulative probability does not match expected.", expectedProb, prob, 1e-10);
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 int[] expectedDomains = distribution.getDomain();
 for (int x = expectedDomains[0]; x <= expectedDomains[1]; x++) {
 double prob = distribution.probability(x);
 double expectedProb = distribution.innerCumulativeProbability(x, x + 1, 1) - distribution.innerCumulativeProbability(x, x, 1);
 Assert.assertEquals("Probability does not match expected.", expectedProb, prob, 1e-10);
 }
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 double expectedVariance = distribution.calculateNumericalVariance();
 double numericalVariance = distribution.getNumericalVariance();
 Assert.assertEquals("Numerical variance does not match expected.", expectedVariance, numericalVariance, 1e-10);
 }
}