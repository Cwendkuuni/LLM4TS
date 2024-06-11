import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSES = 3;
 private static final int SAMPLE_SIZE = 5;

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE - 1, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositivePopulationSize() {
  new HypergeometricDistribution(0, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES - 1, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveSampleSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE - 1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, POPULATION_SIZE, SAMPLE_SIZE);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, POPULATION_SIZE);
 }

 @Test
 public void testGetNumberOfSuccesses() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(NUMBER_OF_SUCCESSES, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(POPULATION_SIZE, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(SAMPLE_SIZE, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = dist.getDomain();
  for (int x = domain[0]; x <= domain[1]; x++) {
   double cp = dist.cumulativeProbability(x);
   double p = dist.probability(x);
   double expectedCp = 0.0;
   for (int i = domain[0]; i < x; i++) {
    expectedCp += p;
   }
   assertEquals(expectedCp, cp, 0.001);
  }
 }

 @Test
 public void testProbability() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = dist.getDomain();
  for (int x = domain[0]; x <= domain[1]; x++) {
   double p = dist.probability(x);
   double q = 1.0 - p;
   double expectedP = SaddlePointExpansion.logBinomialProbability(x, NUMBER_OF_SUCCESSES, dist.getSampleSize() / POPULATION_SIZE, q);
   double expectedQ = SaddlePointExpansion.logBinomialProbability(dist.getSampleSize() - x, POPULATION_SIZE - NUMBER_OF_SUCCESSES, dist.getSampleSize() / POPULATION_SIZE, q);
   double expectedProbability = Math.exp(expectedP + expectedQ);
   assertEquals(expectedProbability, p, 0.001);
  }
 }

 @Test
 public void testUpperCumulativeProbability() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = dist.getDomain();
  for (int x = domain[0]; x <= domain[1]; x++) {
   double ucp = dist.upperCumulativeProbability(x);
   double cp = dist.cumulativeProbability(x);
   assertEquals(1.0 - cp, ucp, 0.001);
  }
 }

 @Test
 public void testInnerCumulativeProbability() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int x0 = domain[0];
  int x1 = domain[1];
  double p = dist.probability(x0);
  double expectedCumulativeProbability = p;
  for (int i = 1; i <= x1 - x0; i++) {
   expectedCumulativeProbability += dist.probability(x0 + i);
  }
  double actualCumulativeProbability = dist.innerCumulativeProbability(x0, x1, 1);
  assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test
 public void testGetNumericalMean() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  double numericalMean = dist.getNumericalMean();
  double expectedNumericalMean = (double) SAMPLE_SIZE * (double) NUMBER_OF_SUCCESSES / (double) POPULATION_SIZE;
  assertEquals(expectedNumericalMean, numericalMean, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  double numericalVariance = dist.getNumericalVariance();
  double expectedNumericalVariance = dist.calculateNumericalVariance();
  assertEquals(expectedNumericalVariance, numericalVariance, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int supportLowerBound = dist.getSupportLowerBound();
  int expectedSupportLowerBound = POPULATION_SIZE + NUMBER_OF_SUCCESSES - SAMPLE_SIZE;
  assertEquals(expectedSupportLowerBound, supportLowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int supportUpperBound = dist.getSupportUpperBound();
  int expectedSupportUpperBound = Math.min(NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(expectedSupportUpperBound, supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
  HypergeometricDistribution dist = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertTrue(dist.isSupportConnected());
 }

}