import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSES = 3;
 private static final int SAMPLE_SIZE = 5;

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE - 1, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveNumberOfSuccesses() {
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, 0, SAMPLE_SIZE);
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, -1, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveSampleSize() {
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE - 1);
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, 0);
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, -1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES + 1, SAMPLE_SIZE);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
  new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE + 1);
 }

 @Test
 public void testCumulativeProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();

  for (int x = domain[0]; x <= domain[1]; x++) {
   double expected = (x < domain[0]) ? 0.0 : ((x >= domain[1]) ? 1.0 : distribution.cumulativeProbability(x));
   double actual = distribution.cumulativeProbability(x);
   assertEquals(expected, actual, 0.0);
  }
 }

 @Test
 public void testGetNumberOfSuccesses() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(NUMBER_OF_SUCCESSES, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(POPULATION_SIZE, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertEquals(SAMPLE_SIZE, distribution.getSampleSize());
 }

 @Test
 public void testProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();

  for (int x = domain[0]; x <= domain[1]; x++) {
   double expected = (x < domain[0]) ? 0.0 : ((x >= domain[1]) ? 1.0 : distribution.probability(x));
   double actual = distribution.probability(x);
   assertEquals(expected, actual, 0.0);
  }
 }

 @Test
 public void testUpperCumulativeProbability() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();

  for (int x = domain[0]; x <= domain[1]; x++) {
   double expected = (x <= domain[0]) ? 1.0 : ((x > domain[1]) ? 0.0 : distribution.upperCumulativeProbability(x));
   double actual = distribution.upperCumulativeProbability(x);
   assertEquals(expected, actual, 0.0);
  }
 }

 @Test
 public void testGetNumericalMean() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  double expected = SAMPLE_SIZE * NUMBER_OF_SUCCESSES / POPULATION_SIZE;
  double actual = distribution.getNumericalMean();
  assertEquals(expected, actual, 0.0);
 }

 @Test
 public void testGetNumericalVariance() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  double expected = SAMPLE_SIZE * NUMBER_OF_SUCCESSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSES) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1.0));
  double actual = distribution.getNumericalVariance();
  assertEquals(expected, actual, 0.0);
 }

 @Test
 public void testGetSupportLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int expected = FastMath.max(0, SAMPLE_SIZE + NUMBER_OF_SUCCESSES - POPULATION_SIZE);
  int actual = distribution.getSupportLowerBound();
  assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int expected = FastMath.min(NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  int actual = distribution.getSupportUpperBound();
  assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
  assertTrue(distribution.isSupportConnected());
 }
}