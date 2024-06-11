import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSES = 5;
 private static final int SAMPLE_SIZE = 5;

 @Test
 public void testHypergeometricDistributionConstructionWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 Assert.assertEquals(POPULATION_SIZE, distribution.getPopulationSize());
 Assert.assertEquals(NUMBER_OF_SUCCESSES, distribution.getNumberOfSuccesses());
 Assert.assertEquals(SAMPLE_SIZE, distribution.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionWithInvalidPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), -5, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithInvalidNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, 0, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithInvalidSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, POPULATION_SIZE + 1, SAMPLE_SIZE);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, POPULATION_SIZE + 1);
 }

 @Test
 public void testGetDomain() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int[] domain = distribution.getDomain();
 int lowerBound = domain[0];
 int upperBound = domain[1];
 Assert.assertEquals(FastMath.max(0, POPULATION_SIZE - (SAMPLE_SIZE - NUMBER_OF_SUCCESSES)), lowerBound);
 Assert.assertEquals(FastMath.min(NUMBER_OF_SUCCESSES, SAMPLE_SIZE), upperBound);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 for (int i = 0; i <= SAMPLE_SIZE; i++) {
 double cumulativeProbability = distribution.cumulativeProbability(i);
 double expectedCumulativeProbability = FastMath.cumulDist(NUMBER_OF_SUCCESSES, POPULATION_SIZE, SAMPLE_SIZE, i);
 Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 for (int i = FastMath.max(0, POPULATION_SIZE - (SAMPLE_SIZE - NUMBER_OF_SUCCESSES)); i <= SAMPLE_SIZE; i++) {
 double probability = distribution.probability(i);
 double expectedProbability = FastMath.binomProbability(SAMPLE_SIZE, POPULATION_SIZE, NUMBER_OF_SUCCESSES, i) / Math.pow(2, SAMPLE_SIZE);
 Assert.assertEquals(expectedProbability, probability, 0.001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 for (int i = 0; i <= SAMPLE_SIZE; i++) {
 double upperCumulativeProbability = distribution.upperCumulativeProbability(i);
 double expectedUpperCumulativeProbability = 1.0 - FastMath.cumulDist(NUMBER_OF_SUCCESSES, POPULATION_SIZE, SAMPLE_SIZE, i);
 Assert.assertEquals(expectedUpperCumulativeProbability, upperCumulativeProbability, 0.001);
 }
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 double numericalMean = distribution.getNumericalMean();
 double expectedNumericalMean = SAMPLE_SIZE * NUMBER_OF_SUCCESSES / POPULATION_SIZE;
 Assert.assertEquals(expectedNumericalMean, numericalMean, 0.001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 double numericalVariance = distribution.getNumericalVariance();
 double expectedNumericalVariance = SAMPLE_SIZE * NUMBER_OF_SUCCESSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSES) / (Math.pow(POPULATION_SIZE, 2) * (POPULATION_SIZE - 1.0));
 Assert.assertEquals(expectedNumericalVariance, numericalVariance, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int supportLowerBound = distribution.getSupportLowerBound();
 int expectedSupportLowerBound = FastMath.max(0, SAMPLE_SIZE + NUMBER_OF_SUCCESSES - POPULATION_SIZE);
 Assert.assertEquals(expectedSupportLowerBound, supportLowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int supportUpperBound = distribution.getSupportUpperBound();
 int expectedSupportUpperBound = FastMath.min(NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 Assert.assertEquals(expectedSupportUpperBound, supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 boolean isSupportConnected = distribution.isSupportConnected();
 boolean expectedIsSupportConnected = true;
 Assert.assertEquals(expectedIsSupportConnected, isSupportConnected);
 }
}