import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSSES = 5;
 private static final int SAMPLE_SIZE = 5;

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE - 1, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
   fail("Expected NotPositiveException for negative population size");
  }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonStrictlyPositiveNumberOfSuccesses() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES - 1, SAMPLE_SIZE);
  } catch (NotPositiveException | NumberIsTooLargeException | NotStrictlyPositiveException e) {
   fail("Expected NotStrictlyPositiveException for non-strictly positive number of successes");
  }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES + 1, SAMPLE_SIZE);
  } catch (NotPositiveException | NumberIsTooLargeException | NotStrictlyPositiveException e) {
   fail("Expected NumberIsTooLargeException for number of successes greater than population size");
  }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
  try {
   new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE + 1);
  } catch (NotPositiveException | NumberIsTooLargeException | NotStrictlyPositiveException e) {
   fail("Expected NumberIsTooLargeException for sample size greater than population size");
  }
 }

 @Test
 public void testCumulativeProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();
  for (int x = domain[0]; x <= domain[1]; x++) {
   double expected = (x <= NUMBER_OF_SUCCESSSES) ? x * 1.0 / SAMPLE_SIZE : 1.0 - (NumberOfSuccesses - x) * 1.0 / SAMPLE_SIZE;
   double actual = distribution.cumulativeProbability(x);
   assertEquals(expected, actual, 0.0001);
  }
 }

 @Test
 public void testProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double sum = 0.0;
  for (int x = 0; x <= SAMPLE_SIZE; x++) {
   double actual = distribution.probability(x);
   sum += actual;
   assertEquals(distribution.cumulativeProbability(x), sum, 0.0001);
  }
 }

 @Test
 public void testGetNumberOfSuccesses() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(NUMBER_OF_SUCCESSSES, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(POPULATION_SIZE, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertEquals(SAMPLE_SIZE, distribution.getSampleSize());
 }

 @Test
 public void testGetSupportLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = POPULATION_SIZE + NUMBER_OF_SUCCESSSES - SAMPLE_SIZE;
  int actual = distribution.getSupportLowerBound();
  assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = Math.min(NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int actual = distribution.getSupportUpperBound();
  assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  assertTrue(distribution.isSupportConnected());
 }

 @Test
 public void testGetNumericalMean() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = SAMPLE_SIZE * NUMBER_OF_SUCCESSSES / POPULATION_SIZE;
  double actual = distribution.getNumericalMean();
  assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testGetNumericalVariance() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = SAMPLE_SIZE * NUMBER_OF_SUCCESSSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSSES) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1));
  double actual = distribution.getNumericalVariance();
  assertEquals(expected, actual, 0.0001);
 }
}