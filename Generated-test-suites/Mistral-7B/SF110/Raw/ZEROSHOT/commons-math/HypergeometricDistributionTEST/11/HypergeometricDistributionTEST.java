import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstructionWithValidParameters() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 Assert.assertEquals(10, hd.getPopulationSize());
 Assert.assertEquals(5, hd.getNumberOfSuccesses());
 Assert.assertEquals(5, hd.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNegativePopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithZeroPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 0, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, -2, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithZeroNumberOfSuccesses() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 0, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructionWithNegativeSampleSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructionWithZeroSampleSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructionWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = hd.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double cp = hd.cumulativeProbability(x);
 double up = hd.upperCumulativeProbability(x);
 Assert.assertEquals(up, 1 - cp, 1e-10);
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int x = 0; x <= hd.getSupportUpperBound(); x++) {
 double p = hd.probability(x);
 double cp = hd.cumulativeProbability(x);
 double up = hd.upperCumulativeProbability(x + 1);
 Assert.assertEquals(cp, hd.cumulativeProbability(x + 1) - p, 1e-10);
 Assert.assertEquals(up, hd.cumulativeProbability(x), 1e-10);
 }
 }

 @Test
 public void testHypergeometricDistributionGetNumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double mean = hd.getNumericalMean();
 Assert.assertEquals(5.0, mean, 1e-10);
 }

 @Test
 public void testHypergeometricDistributionGetNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double variance = hd.getNumericalVariance();
 double expectedVariance = 5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0));
 Assert.assertEquals(expectedVariance, variance, 1e-10);
 }
}