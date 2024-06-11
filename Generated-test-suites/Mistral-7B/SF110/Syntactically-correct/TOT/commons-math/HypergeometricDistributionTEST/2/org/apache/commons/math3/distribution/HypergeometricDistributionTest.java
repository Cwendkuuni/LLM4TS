import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, -5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithInvalidNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithInvalidSampleSize() {
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
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.cumulativeProbability(x);
 double expected = this.expectedCumulativeProbability(x, 10, 5, 5);
 assertEquals(expected, p, 0.001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int x = 0; x <= 10; x++) {
 double p = dist.probability(x);
 double expected = this.expectedProbability(x, 10, 5, 5);
 assertEquals(expected, p, 0.001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = dist.getDomain();
 for (int x = domain[1] + 1; x <= 10; x++) {
 double p = dist.upperCumulativeProbability(x);
 assertEquals(0.0, p, 0.001);
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double expected = 5.0 * 5.0 / 10.0;
 double actual = dist.getNumericalMean();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double expected = 5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0));
 double actual = dist.getNumericalVariance();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int expected = 5 + 5 - 10;
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
 assertTrue(dist.isSupportConnected());
 }

 private double expectedCumulativeProbability(int x, int N, int m, int k) {
 double p = (double) k / N;
 double q = (double) (N - k) / N;
 double p2 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
 double p3 = SaddlePointExpansion.logBinomialProbability(N - x, N - m, p, q);
 double p4 = SaddlePointExpansion.logBinomialProbability(N, N, p, q);
 return Math.exp(p2 + p3 - p4);
 }

 private double expectedProbability(int x, int N, int m, int k) {
 double p = (double) k / N;
 double q = (double) (N - k) / N;
 double p2 = SaddlePointExpansion.logBinomialProbability(x, m, p, q);
 double p3 = SaddlePointExpansion.logBinomialProbability(N - x, N - m, p, q);
 double p4 = SaddlePointExpansion.logBinomialProbability(N, N, p, q);
 return Math.exp(p2 + p3 - p4) - Math.exp(p2 + p3 - p4 - 1.0);
 }
}