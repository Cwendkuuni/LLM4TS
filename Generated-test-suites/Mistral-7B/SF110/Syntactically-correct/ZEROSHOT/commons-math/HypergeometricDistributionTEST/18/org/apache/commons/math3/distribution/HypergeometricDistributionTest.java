import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(-5, 3, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, -3, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 11);
 }

 @Test
 public void testGetLowerDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 int lowerDomain = dist.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
 int expectedLowerDomain = FastMath.max(0, populationSize - (sampleSize - numberOfSuccesses));
 assertEquals(expectedLowerDomain, lowerDomain);
 }

 @Test
 public void testGetUpperDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 int upperDomain = dist.getUpperDomain(populationSize, numberOfSuccesses);
 int expectedUpperDomain = Math.min(numberOfSuccesses, sampleSize);
 assertEquals(expectedUpperDomain, upperDomain);
 }

 @Test
 public void testGetDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain(populationSize, numberOfSuccesses, sampleSize);
 int expectedLowerDomain = FastMath.max(0, populationSize - (sampleSize - numberOfSuccesses));
 int expectedUpperDomain = Math.min(numberOfSuccesses, sampleSize);
 AssertArrayEquals(new int[]{expectedLowerDomain, expectedUpperDomain}, domain);
 }

 @Test
 public void testCumulativeProbability() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 double expectedCumulativeProbability = 0.15625;
 double cumulativeProbability = dist.cumulativeProbability(2);
 assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
 }

 @Test
 public void testProbability() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 double expectedProbability = 0.27273;
 double probability = dist.probability(3);
 assertEquals(expectedProbability, probability, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 double expectedUpperCumulativeProbability = 0.84375;
 double upperCumulativeProbability = dist.upperCumulativeProbability(2);
 assertEquals(expectedUpperCumulativeProbability, upperCumulativeProbability, 0.0001);
 }

 @Test
 public void testNumericalMean() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 double expectedNumericalMean = 3.0;
 double numericalMean = dist.getNumericalMean();
 assertEquals(expectedNumericalMean, numericalMean, 0.0001);
 }

 @Test
 public void testNumericalVariance() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), populationSize, numberOfSuccesses, sampleSize);
 double expectedNumericalVariance = 0.1111111111111111;
 double numericalVariance = dist.getNumericalVariance();
 assertEquals(expectedNumericalVariance, numericalVariance, 0.000001);
 }
}