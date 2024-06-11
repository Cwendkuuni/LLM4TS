import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private RandomGenerator rng = new MersenneTwisterFastRandom();

 @Test
 public void testHypergeometricDistributionConstruction_ValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstruction_InvalidPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 0, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstruction_InvalidNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, -1, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstruction_InvalidSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, -1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstruction_NumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstruction_SampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 11);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 int[] domain = distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double prob = distribution.cumulativeProbability(x);
 double expected = x * 1.0 / (double) (distribution.getPopulationSize() - distribution.getSampleSize()) + (x > domain[0] ? (distribution.getSampleSize() - x + 1) * 1.0 / (double) distribution.getPopulationSize() : 0);
 assertEquals(expected, prob, 0.0001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 for (int x = 0; x <= distribution.getSupportUpperBound(); x++) {
 double prob = distribution.probability(x);
 double expected = FastMath.comb(distribution.getSampleSize(), x) * Math.pow(distribution.getNumberOfSuccesses() / (double) distribution.getPopulationSize(), x) * Math.pow((1.0 - distribution.getNumberOfSuccesses() / (double) distribution.getPopulationSize()), distribution.getSampleSize() - x);
 assertEquals(expected, prob, 0.0001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 int[] domain = distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double prob = distribution.upperCumulativeProbability(x);
 double expected = 1.0 - distribution.cumulativeProbability(domain[1] - x);
 assertEquals(expected, prob, 0.0001);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 double expected = 5.0 * 5.0 / 10.0;
 assertEquals(expected, distribution.getNumericalMean(), 0.0001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 double expected = 5.0 * 5.0 / (10.0 * 10.0 * (10.0 - 1.0));
 assertEquals(expected, distribution.getNumericalVariance(), 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 int expected = FastMath.max(0, distribution.getSampleSize() + distribution.getNumberOfSuccesses() - distribution.getPopulationSize());
 assertEquals(expected, distribution.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 int expected = FastMath.min(distribution.getNumberOfSuccesses(), distribution.getSampleSize());
 assertEquals(expected, distribution.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 assertTrue(distribution.isSupportConnected());
 }

}