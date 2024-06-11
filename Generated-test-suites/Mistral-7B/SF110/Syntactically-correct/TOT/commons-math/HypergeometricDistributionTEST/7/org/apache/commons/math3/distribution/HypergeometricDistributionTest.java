import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructorWithValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(5, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 0, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testCumulativeProbabilityWithValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 3;
 double expected = dist.cumulativeProbability(x);
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithLowerDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithUpperDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 7;
 double expected = 0.728381635222571;
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbabilityWithValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 3;
 double expected = 0.285691826324324;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbabilityWithLowerDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testProbabilityWithUpperDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 7;
 double expected = 0.125;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 3;
 double expected = 0.714308563671843;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithUpperDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 7;
 double expected = 1.0;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithLowerDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double expected = (double) 5 * 5 / 10.0;
 double actual = dist.getNumericalMean();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double expected = 5.0 * 5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0));
 double actual = dist.getNumericalVariance();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int expected = 0;
 int actual = dist.getSupportLowerBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int expected = 5;
 int actual = dist.getSupportUpperBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 boolean expected = true;
 boolean actual = dist.isSupportConnected();
 assertEquals(expected, actual);
 }
}