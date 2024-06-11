import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionImplTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSES = 5;
 private static final int SAMPLE_SIZE = 3;

 @Test
 public void testValidConstruction() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 Assert.assertEquals(NUMBER_OF_SUCCESSES, dist.getNumberOfSuccesses());
 Assert.assertEquals(POPULATION_SIZE, dist.getPopulationSize());
 Assert.assertEquals(SAMPLE_SIZE, dist.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testInvalidPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), -1, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 } catch (NotPositiveException e) {
 // Expected
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, 0, SAMPLE_SIZE);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, POPULATION_SIZE + 1, SAMPLE_SIZE);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, POPULATION_SIZE + 1);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 Assert.assertNull(dist);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int expected[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 for (int i = 0; i < expected.length; i++) {
 double result = dist.cumulativeProbability(i);
 Assert.assertEquals("Cumulative probability for value " + i + " is incorrect.", expected[i], result, 0.0001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int expected[] = new int[11];
 for (int i = 0; i < expected.length; i++) {
 expected[i] = HypergeometricDistributionImplTest.hypergeometricProbability(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE, i);
 }
 for (int i = 0; i < expected.length; i++) {
 double result = dist.probability(i);
 Assert.assertEquals("Probability for value " + i + " is incorrect.", expected[i], result, 0.0001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int expected[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 for (int i = 0; i < expected.length; i++) {
 double result = dist.upperCumulativeProbability(i);
 Assert.assertEquals("Upper cumulative probability for value " + i + " is incorrect.", 1.0 - expected[expected.length - i - 1], result, 0.0001);
 }
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int lowerBound = dist.getSupportLowerBound();
 int expected = POPULATION_SIZE + NUMBER_OF_SUCCESSES - SAMPLE_SIZE;
 Assert.assertEquals("Lower bound of support is incorrect.", expected, lowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 int upperBound = dist.getSupportUpperBound();
 int expected = Math.min(NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 Assert.assertEquals("Upper bound of support is incorrect.", expected, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 boolean result = dist.isSupportConnected();
 boolean expected = true;
 Assert.assertEquals("Support is not connected.", expected, result);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 double result = dist.getNumericalMean();
 double expected = (SAMPLE_SIZE * NUMBER_OF_SUCCESSES) / POPULATION_SIZE;
 Assert.assertEquals("Numerical mean is incorrect.", expected, result, 0.0001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 double result = dist.getNumericalVariance();
 double expected = (SAMPLE_SIZE * NUMBER_OF_SUCCESSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSES)) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1));
 Assert.assertEquals("Numerical variance is incorrect.", expected, result, 0.0001);
 }

}