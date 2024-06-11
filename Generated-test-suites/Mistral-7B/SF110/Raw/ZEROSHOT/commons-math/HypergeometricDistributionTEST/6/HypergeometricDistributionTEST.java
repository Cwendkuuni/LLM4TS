import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 10);
 fail("Should not allow negative population size");
 } catch (NotStrictlyPositiveException e) {
 // Pass
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 10);
 fail("Should not allow negative number of successes");
 } catch (NotStrictlyPositiveException e) {
 // Pass
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithZeroNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 0, 10);
 fail("Should not allow zero number of successes");
 } catch (NotPositiveException e) {
 // Pass
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithZeroPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 0, 3, 10);
 fail("Should not allow zero population size");
 } catch (NotPositiveException e) {
 // Pass
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithZeroSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 0);
 fail("Should not allow zero sample size");
 } catch (NotPositiveException e) {
 // Pass
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 10);
 fail("Should not allow number of successes larger than population size");
 } catch (NumberIsTooLargeException e) {
 // Pass
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 fail("Should not allow sample size larger than population size");
 } catch (NumberIsTooLargeException e) {
 // Pass
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals("Number of successes should be 3", 3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals("Population size should be 10", 10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals("Sample size should be 5", 5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double[] probabilities = new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 1.0 };
 for (double p : probabilities) {
 assertEquals("Cumulative probability for x = " + p * dist.getSampleSize(), p, dist.cumulativeProbability((int)(p * dist.getSampleSize())));
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double[] probabilities = new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 1.0 };
 for (double p : probabilities) {
 assertEquals("Probability for x = " + p * dist.getSampleSize(), p, dist.probability((int)(p * dist.getSampleSize())), 1e-8);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double[] probabilities = new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 1.0 };
 for (double p : probabilities) {
 assertEquals("Upper cumulative probability for x = " + p * dist.getSampleSize(), 1.0 - p, dist.upperCumulativeProbability((int)(p * dist.getSampleSize())), 1e-8);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double expectedMean = 3.0 * 5.0 / 10.0;
 assertEquals("Numerical mean should be " + expectedMean, expectedMean, dist.getNumericalMean(), 1e-8);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double expectedVariance = 5.0 * 3.0 * (10.0 - 5.0) * (10.0 - 3.0) / (10.0 * 10.0 * (10.0 - 1.0));
 assertEquals("Numerical variance should be " + expectedVariance, expectedVariance, dist.getNumericalVariance(), 1e-8);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int expectedLowerBound = 5 + 3 - 10;
 assertEquals("Support lower bound should be " + expectedLowerBound, expectedLowerBound, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int expectedUpperBound = 3;
 assertEquals("Support upper bound should be " + expectedUpperBound, expectedUpperBound, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue("Support should be connected", dist.isSupportConnected());
 }
}