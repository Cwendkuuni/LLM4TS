import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(-1, 5, 10);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, -5, 10);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(0, 5, 10);
 } catch (NotPositiveException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithZeroNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, 0, 10);
 } catch (NotPositiveException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, 11, 10);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 Assert.assertNull(dist);
 }

 @Test
 public void testConstructor() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(10, dist.getPopulationSize());
 Assert.assertEquals(5, dist.getNumberOfSuccesses());
 Assert.assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double p = dist.cumulativeProbability(i);
 double q = dist.probability(i);
 Assert.assertEquals(p, q, 1e-10);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double p = dist.probability(i);
 double q = dist.cumulativeProbability(i + 1) - dist.cumulativeProbability(i);
 Assert.assertEquals(p, q, 1e-10);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double p = dist.upperCumulativeProbability(i);
 double q = 1.0 - dist.cumulativeProbability(i);
 Assert.assertEquals(p, q, 1e-10);
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
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(-5, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double mean = dist.getNumericalMean();
 Assert.assertEquals(5.0, mean, 1e-10);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double variance = dist.getNumericalVariance();
 Assert.assertEquals(2.978723603155768E-3, variance, 1e-10);
 }
}