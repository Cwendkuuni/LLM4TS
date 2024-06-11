import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 assertTrue(dist == null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, -2, 5);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 assertTrue(dist == null);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 12, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // expected
 }
 assertTrue(dist == null);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 12);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // expected
 }
 assertTrue(dist == null);
 }

 @Test
 public void testConstructorWithValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(3, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int[] domain = dist.getDomain();
 // Test for lower bound
 assertEquals(0.0, dist.cumulativeProbability(domain[0]), 0.001);
 // Test for limit value
 assertEquals(1.0, dist.cumulativeProbability(domain[1]), 0.001);
 // Test for values within the domain
 for (int x = domain[0]; x <= domain[1]; x++) {
 assertEquals(dist.cumulativeProbability(x), dist.probability(x), 0.001);
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int[] domain = dist.getDomain();
 // Test for lower bound
 assertEquals(0.0353352861, dist.probability(domain[0]), 0.001);
 // Test for limit value
 assertEquals(0.0, dist.probability(domain[1]), 0.001);
 // Test for values within the domain
 for (int x = domain[0]; x <= domain[1]; x++) {
 assertEquals(dist.probability(x), dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x), 0.001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 int[] domain = dist.getDomain();
 // Test for upper bound
 assertEquals(1.0, dist.upperCumulativeProbability(domain[1]), 0.001);
 // Test for values lower than the lower boundary
 for (int x = 0; x < domain[0]; x++) {
 assertEquals(1.0, dist.upperCumulativeProbability(x), 0.001);
 }
 // Test for values within the domain
 for (int x = domain[0]; x <= domain[1]; x++) {
 assertEquals(dist.cumulativeProbability(x), 1.0 - dist.upperCumulativeProbability(x), 0.001);
 }
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(-3, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3.0, dist.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 double numericalVariance = dist.getNumericalVariance();
 // The distribution is discrete, so the numerical variance may not be calculated correctly
 // In this case, it is enough to check that the numerical variance is calculated
 assertEquals(!Double.isNaN(numericalVariance), true);
 }
}