import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSSES = 5;
 private static final int SAMPLE_SIZE = 3;

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE - 1, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveNumberOfSuccesses() {
  new HypergeometricDistribution(POPULATION_SIZE, 0, SAMPLE_SIZE);
  new HypergeometricDistribution(POPULATION_SIZE, -1, SAMPLE_SIZE);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveSampleSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, 0);
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, -1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, POPULATION_SIZE, SAMPLE_SIZE);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
  new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSSES, POPULATION_SIZE + 1);
 }

 @Test
 public void testCumulativeProbabilityWithinBounds() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();
  double expected = distribution.cumulativeProbability(domain[1] - 1);
  double actual = distribution.cumulativeProbability(domain[1] - 1);
  Assert.assertEquals("Incorrect cumulative probability for x = " + (domain[1] - 1), expected, actual, 1e-12);
 }

 @Test
 public void testCumulativeProbabilityBelowLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 0.0;
  double actual = distribution.cumulativeProbability(-1);
  Assert.assertEquals("Incorrect cumulative probability for x = -1", expected, actual, 1e-12);
 }

 @Test
 public void testCumulativeProbabilityAboveUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 1.0;
  double actual = distribution.cumulativeProbability(Integer.MAX_VALUE);
  Assert.assertEquals("Incorrect cumulative probability for x = Integer.MAX_VALUE", expected, actual, 1e-12);
 }

 @Test
 public void testProbabilityWithinBounds() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = distribution.probability(5);
  double actual = distribution.probability(5);
  Assert.assertEquals("Incorrect probability for x = 5", expected, actual, 1e-12);
 }

 @Test
 public void testProbabilityBelowLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 0.0;
  double actual = distribution.probability(-1);
  Assert.assertEquals("Incorrect probability for x = -1", expected, actual, 1e-12);
 }

 @Test
 public void testProbabilityAboveUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 0.0;
  double actual = distribution.probability(Integer.MAX_VALUE);
  Assert.assertEquals("Incorrect probability for x = Integer.MAX_VALUE", expected, actual, 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityWithinBounds() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int[] domain = distribution.getDomain();
  double expected = 1.0 - distribution.cumulativeProbability(domain[0] - 1);
  double actual = distribution.upperCumulativeProbability(domain[0] - 1);
  Assert.assertEquals("Incorrect upper cumulative probability for x = " + (domain[0] - 1), expected, actual, 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityBelowLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 1.0;
  double actual = distribution.upperCumulativeProbability(-1);
  Assert.assertEquals("Incorrect upper cumulative probability for x = -1", expected, actual, 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityAboveUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = 0.0;
  double actual = distribution.upperCumulativeProbability(Integer.MAX_VALUE);
  Assert.assertEquals("Incorrect upper cumulative probability for x = Integer.MAX_VALUE", expected, actual, 1e-12);
 }

 @Test
 public void testGetNumberOfSuccesses() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = NUMBER_OF_SUCCESSSES;
  int actual = distribution.getNumberOfSuccesses();
  Assert.assertEquals("Incorrect number of successes", expected, actual);
 }

 @Test
 public void testGetPopulationSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = POPULATION_SIZE;
  int actual = distribution.getPopulationSize();
  Assert.assertEquals("Incorrect population size", expected, actual);
 }

 @Test
 public void testGetSampleSize() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = SAMPLE_SIZE;
  int actual = distribution.getSampleSize();
  Assert.assertEquals("Incorrect sample size", expected, actual);
 }

 @Test
 public void testGetNumericalVariance() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = distribution.calculateNumericalVariance();
  double actual = distribution.getNumericalVariance();
  Assert.assertEquals("Incorrect numerical variance", expected, actual, 1e-12);
 }

 @Test
 public void testGetNumericalMean() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  double expected = (POPULATION_SIZE * NUMBER_OF_SUCCESSSES + SAMPLE_SIZE * NUMBER_OF_SUCCESSSES) / (POPULATION_SIZE + SAMPLE_SIZE);
  double actual = distribution.getNumericalMean();
  Assert.assertEquals("Incorrect numerical mean", expected, actual, 1e-12);
 }

 @Test
 public void testGetSupportLowerBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = FastMath.max(0, POPULATION_SIZE + NUMBER_OF_SUCCESSSES - SAMPLE_SIZE);
  int actual = distribution.getSupportLowerBound();
  Assert.assertEquals("Incorrect support lower bound", expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int expected = FastMath.min(NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  int actual = distribution.getSupportUpperBound();
  Assert.assertEquals("Incorrect support upper bound", expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
  HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
  boolean expected = true;
  boolean actual = distribution.isSupportConnected();
  Assert.assertEquals("Incorrect support connected", expected, actual);
 }
}