import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 Assert.assertNotNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 0, 1, 1);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 Assert.assertNotNull(dist);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
 // expected
 }
 Assert.assertNotNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 1, 0, 1);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
 // expected
 }
 Assert.assertNotNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 }
 Assert.assertNotNull(dist);
 Assert.assertEquals(2, dist.getNumberOfSuccesses());
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 1, 1, 2);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 }
 Assert.assertNotNull(dist);
 Assert.assertEquals(2, dist.getSampleSize());
 }

 @Test
 public void testCalculateCumulativeProbabilityWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 double cdf = dist.cumulativeProbability(i);
 double expected = (double) i / 10.0;
 Assert.assertEquals(expected, cdf, 0.0001);
 }
 }

 @Test
 public void testCalculateCumulativeProbabilityWithInvalidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 6; i <= 10; i++) {
 double cdf = dist.cumulativeProbability(i);
 double expected = 1.0;
 Assert.assertEquals(expected, cdf, 0.0001);
 }
 for (int i = 11; i <= 15; i++) {
 double cdf = dist.cumulativeProbability(i);
 double expected = 1.0;
 Assert.assertEquals(expected, cdf, 0.0001);
 }
 }

 @Test
 public void testCalculateProbabilityWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 double probability = dist.probability(i);
 double expected = FastMath.binomialProbability(5, 5.0 / 10.0, 0.5);
 Assert.assertEquals(expected, probability, 0.0001);
 }
 }

 @Test
 public void testCalculateProbabilityWithInvalidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 6; i <= 10; i++) {
 double probability = dist.probability(i);
 double expected = 0.0;
 Assert.assertEquals(expected, probability, 0.0001);
 }
 for (int i = 11; i <= 15; i++) {
 double probability = dist.probability(i);
 double expected = 0.0;
 Assert.assertEquals(expected, probability, 0.0001);
 }
 }

 @Test
 public void testCalculateUpperCumulativeProbabilityWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 double ucdf = dist.upperCumulativeProbability(i);
 double expected = 1.0 - FastMath.binomialProbability(5, 5.0 / 10.0, 0.5);
 Assert.assertEquals(expected, ucdf, 0.0001);
 }
 }

 @Test
 public void testCalculateUpperCumulativeProbabilityWithInvalidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 6; i <= 10; i++) {
 double ucdf = dist.upperCumulativeProbability(i);
 double expected = 1.0;
 Assert.assertEquals(expected, ucdf, 0.0001);
 }
 for (int i = 11; i <= 15; i++) {
 double ucdf = dist.upperCumulativeProbability(i);
 double expected = 1.0;
 Assert.assertEquals(expected, ucdf, 0.0001);
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(5, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double numericalVariance = dist.getNumericalVariance();
 double expected = dist.calculateNumericalVariance();
 Assert.assertEquals(expected, numericalVariance, 0.0001);
 }

 @Test
 public void testCalculateMeanWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double mean = dist.getNumericalMean();
 double expected = dist.getNumberOfSuccesses() * dist.getSampleSize() / dist.getPopulationSize();
 Assert.assertEquals(expected, mean, 0.0001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSupportLowerBoundWithNegativeSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 }

 @Test
 public void testGetSupportLowerBoundWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int supportLowerBound = dist.getSupportLowerBound();
 int expected = FastMath.max(0, dist.getSampleSize() + dist.getNumberOfSuccesses() - dist.getPopulationSize());
 Assert.assertEquals(expected, supportLowerBound);
 }

 @Test
 public void testGetSupportUpperBoundWithValidInput() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int supportUpperBound = dist.getSupportUpperBound();
 int expected = FastMath.min(dist.getNumberOfSuccesses(), dist.getSampleSize());
 Assert.assertEquals(expected, supportUpperBound);
 }
}