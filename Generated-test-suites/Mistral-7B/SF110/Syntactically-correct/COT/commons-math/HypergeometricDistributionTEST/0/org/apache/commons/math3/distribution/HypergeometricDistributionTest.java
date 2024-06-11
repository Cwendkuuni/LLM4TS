import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSSES = 5;
 private static final int SAMPLE_SIZE = 5;

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
   // Test constructor with negative population size
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE - 1, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // Expected NotPositiveException, so ignore other exceptions
   }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNonStrictlyPositiveNumberOfSuccesses() {
   // Test constructor with non-strictly positive numberOfSuccesses
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES - 1, SAMPLE_SIZE);
   } catch (NotPositiveException | NumberIsTooLargeException e) {
     // Expected NotStrictlyPositiveException, so ignore other exceptions
   }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesGreaterThanPopulationSize() {
   // Test constructor with numberOfSuccesses greater than population size
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES + 1, SAMPLE_SIZE);
   } catch (NotPositiveException | NotStrictlyPositiveException e) {
     // Expected NumberIsTooLargeException, so ignore other exceptions
   }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeGreaterThanPopulationSize() {
   // Test constructor with sample size greater than population size
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE + 1);
   } catch (NotPositiveException | NotStrictlyPositiveException e) {
     // Expected NumberIsTooLargeException, so ignore other exceptions
   }
 }

 @Test
 public void testGetNumberOfSuccesses() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int numberOfSuccesses = distribution.getNumberOfSuccesses();
   Assert.assertEquals(NUMBER_OF_SUCCESSSES, numberOfSuccesses);
 }

 @Test
 public void testGetPopulationSize() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int populationSize = distribution.getPopulationSize();
   Assert.assertEquals(POPULATION_SIZE, populationSize);
 }

 @Test
 public void testGetSampleSize() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int sampleSize = distribution.getSampleSize();
   Assert.assertEquals(SAMPLE_SIZE, sampleSize);
 }

 @Test
 public void testCumulativeProbabilityWithXLessThanLowerDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double cumulativeProbability = distribution.cumulativeProbability(-1);
   Assert.assertEquals(0.0, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithXGreaterThanUpperDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double cumulativeProbability = distribution.cumulativeProbability(POPULATION_SIZE + 1);
   Assert.assertEquals(0.0, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithXInsideDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double cumulativeProbability = distribution.cumulativeProbability(NUMBER_OF_SUCCESSSES);
   Assert.assertTrue(cumulativeProbability > 0.0 && cumulativeProbability < 1.0);
 }

 @Test
 public void testProbabilityWithXLessThanLowerDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double probability = distribution.probability(-1);
   Assert.assertEquals(0.0, probability, 0.001);
 }

 @Test
 public void testProbabilityWithXGreaterThanUpperDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double probability = distribution.probability(POPULATION_SIZE + 1);
   Assert.assertEquals(0.0, probability, 0.001);
 }

 @Test
 public void testProbabilityWithXInsideDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double probability = distribution.probability(NUMBER_OF_SUCCESSSES);
   Assert.assertTrue(probability > 0.0 && probability < 1.0);
 }

 @Test
 public void testUpperCumulativeProbabilityWithXLessThanLowerDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double upperCumulativeProbability = distribution.upperCumulativeProbability(-1);
   Assert.assertEquals(1.0, upperCumulativeProbability, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithXGreaterThanUpperDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double upperCumulativeProbability = distribution.upperCumulativeProbability(POPULATION_SIZE + 1);
   Assert.assertEquals(0.0, upperCumulativeProbability, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithXInsideDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double upperCumulativeProbability = distribution.upperCumulativeProbability(NUMBER_OF_SUCCESSSES + 1);
   Assert.assertTrue(upperCumulativeProbability < 1.0);
 }

 @Test
 public void testNumericalMean() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double numericalMean = distribution.getNumericalMean();
   double expectedMean = SAMPLE_SIZE * NUMBER_OF_SUCCESSSES / POPULATION_SIZE;
   Assert.assertEquals(expectedMean, numericalMean, 0.001);
 }

 @Test
 public void testNumericalVariance() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   double numericalVariance = distribution.getNumericalVariance();
   double expectedVariance = SAMPLE_SIZE * NUMBER_OF_SUCCESSSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSSES) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1.0));
   Assert.assertEquals(expectedVariance, numericalVariance, 0.001);
 }

 @Test
 public void testSupportLowerBound() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int supportLowerBound = distribution.getSupportLowerBound();
   int expectedSupportLowerBound = FastMath.max(0, SAMPLE_SIZE + NUMBER_OF_SUCCESSSES - POPULATION_SIZE);
   Assert.assertEquals(expectedSupportLowerBound, supportLowerBound);
 }

 @Test
 public void testSupportUpperBound() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int supportUpperBound = distribution.getSupportUpperBound();
   int expectedSupportUpperBound = FastMath.min(NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   Assert.assertEquals(expectedSupportUpperBound, supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   boolean isSupportConnected = distribution.isSupportConnected();
   Assert.assertTrue(isSupportConnected);
 }

}