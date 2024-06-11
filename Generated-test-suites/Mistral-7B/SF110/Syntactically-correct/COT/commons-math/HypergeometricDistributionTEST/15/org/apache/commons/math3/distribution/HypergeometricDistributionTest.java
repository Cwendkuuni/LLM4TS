import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSSES = 3;
 private static final int SAMPLE_SIZE = 5;

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE - 1, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  } catch (NotPositiveException e) {
   fail("Expected NotStrictlyPositiveException");
  }
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES - 1, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroNumberOfSuccesses() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  } catch (NotPositiveException e) {
   fail("Expected NotStrictlyPositiveException");
  }
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeSampleSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE - 1);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithZeroSampleSize() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  } catch (NotStrictlyPositiveException e) {
   fail("Expected NotPositiveException");
  }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, POPULATION_SIZE, SAMPLE_SIZE);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, POPULATION_SIZE);
 }

 @Test
 public void testCumulativeProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();
  for (int x = domain[0]; x <= domain[1]; x++) {
   double expected = (x <= numberOfSuccesses) ? x * 1.0 / sampleSize : 1.0 - ((numberOfSuccesses + populationSize - sampleSize - x) * 1.0 / populationSize);
   double actual = distribution.cumulativeProbability(x);
   assertEquals(expected, actual, 0.0001);
  }
 }

 @Test
 public void testProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  for (int x = 0; x <= populationSize; x++) {
   double expected = (x <= numberOfSuccesses) ? (double) numberOfSuccesses / populationSize : (double) (populationSize - numberOfSuccesses) / populationSize;
   double actual = distribution.probability(x);
   assertEquals(expected, actual, 0.0001);
  }
 }

 @Test
 public void testUpperCumulativeProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();
  for (int x = domain[1]; x >= domain[0]; x--) {
   double expected = (x <= numberOfSuccesses) ? 1.0 : 1.0 - ((numberOfSuccesses + populationSize - sampleSize - x + 1) * 1.0 / (populationSize + 1));
   double actual = distribution.upperCumulativeProbability(x);
   assertEquals(expected, actual, 0.0001);
  }
 }

 @Test
 public void testGetNumberOfSuccesses() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(NUMBER_OF_SUCCESSSES, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(POPULATION_SIZE, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(SAMPLE_SIZE, distribution.getSampleSize());
 }

 @Test
 public void testNumericalMean() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double actual = distribution.getNumericalMean();
  double expected = ((double) NUMBER_OF_SUCCESSSES * SAMPLE_SIZE + POPULATION_SIZE * (NUMBER_OF_SUCCESSSES - SAMPLE_SIZE / 2)) / POPULATION_SIZE;
  assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testNumericalVariance() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double actual = distribution.getNumericalVariance();
  double expected = ((double) NUMBER_OF_SUCCESSSES * SAMPLE_SIZE * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSSES) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1)));
  assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int actual = distribution.getSupportLowerBound();
  int expected = FastMath.max(0, POPULATION_SIZE + NUMBER_OF_SUCCESSSES - SAMPLE_SIZE);
  assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int actual = distribution.getSupportUpperBound();
  int expected = FastMath.min(NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(expected, actual);
 }
}