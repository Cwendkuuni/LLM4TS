import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -10, 3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected NotStrictlyPositiveException, but NotPositiveException is thrown instead, so we catch both
 Assert.fail("Expected NotPositiveException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, -3, 5);
 } catch (NotStrictlyPositiveException e) {
 Assert.fail("Expected NotStrictlyPositiveException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 3, -5);
 } catch (NotStrictlyPositiveException e) {
 Assert.fail("Expected NotStrictlyPositiveException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 } catch (NotPositiveException e) {
 // expected NotPositiveException, but NumberIsTooLargeException is thrown instead, so we catch both
 Assert.fail("Expected NumberIsTooLargeException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 3, 11);
 } catch (NotPositiveException e) {
 // expected NotPositiveException, but NumberIsTooLargeException is thrown instead, so we catch both
 Assert.fail("Expected NumberIsTooLargeException, but got " + e.getClass().getName());
 }
 }

 @Test
 public void testHypergeometricDistributionGetNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 Assert.assertEquals(3, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testHypergeometricDistributionGetPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 Assert.assertEquals(10, distribution.getPopulationSize());
 }

 @Test
 public void testHypergeometricDistributionGetSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 Assert.assertEquals(5, distribution.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double p;
 int i;

 for (i = 0; i <= 5; i++) {
 p = distribution.cumulativeProbability(i);
 Assert.assertEquals(i * 6.0 / 45.0, p, 0.001);
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double p;
 int i;

 for (i = 0; i <= 5; i++) {
 p = distribution.probability(i);
 Assert.assertEquals(FastMath.comb(5, i) * FastMath.pow(6.0 / 10.0, i) * FastMath.pow(4.0 / 10.0, 5 - i) / FastMath.pow(10.0, 5.0), p, 0.001);
 }
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double p;
 int i;

 for (i = 0; i <= 5; i++) {
 p = distribution.upperCumulativeProbability(i);
 Assert.assertEquals(1.0 - (i * 6.0 / 45.0), p, 0.001);
 }
 }

 @Test
 public void testHypergeometricDistributionNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double mean = distribution.getNumericalMean();
 Assert.assertEquals(3.0, mean, 0.001);
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double variance = distribution.getNumericalVariance();
 Assert.assertEquals(1.5555555555555555, variance, 0.001);
 }

 @Test
 public void testHypergeometricDistributionSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int lowerBound = distribution.getSupportLowerBound();
 Assert.assertEquals(-2, lowerBound);
 }

 @Test
 public void testHypergeometricDistributionSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int upperBound = distribution.getSupportUpperBound();
 Assert.assertEquals(3, upperBound);
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 boolean result = distribution.isSupportConnected();
 Assert.assertEquals(true, result);
 }

}