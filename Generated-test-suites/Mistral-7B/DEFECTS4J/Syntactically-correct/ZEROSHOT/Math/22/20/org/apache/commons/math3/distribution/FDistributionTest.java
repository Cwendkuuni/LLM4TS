import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

 @Test
 public void testFDistributionCreation() {
 FDistribution fd;
 try {
 fd = new FDistribution(2.0, 4.0);
 } catch (NotStrictlyPositiveException e) {
 // this exception should not be thrown
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testFDistributionCreationWithRandomGenerator() {
 RandomGenerator rng = new Well19937c();
 FDistribution fd = new FDistribution(rng, 2.0, 4.0);
 assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
 FDistribution fd = new FDistribution(new Well19937c(), -2.0, 4.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionNegativeDenominatorDegreesOfFreedom() {
 FDistribution fd = new FDistribution(new Well19937c(), 2.0, -4.0);
 }

 @Test
 public void testFDistributionDensity() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double x = 1.5;
 double density = fd.density(x);
 assertEquals(0.2303634825539931, density, 1e-9);
 }

 @Test
 public void testFDistributionCumulativeProbability() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double x = 1.5;
 double cdf = fd.cumulativeProbability(x);
 assertEquals(0.9134254440948326, cdf, 1e-9);
 }

 @Test
 public void testFDistributionCumulativeProbabilityZero() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double cdf = fd.cumulativeProbability(0.0);
 assertEquals(0.0, cdf, 1e-9);
 }

 @Test
 public void testFDistributionCumulativeProbabilityInfinity() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double cdf = fd.cumulativeProbability(Double.POSITIVE_INFINITY);
 assertEquals(1.0, cdf, 1e-9);
 }

 @Test
 public void testFDistributionNumericalMean() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double mean = fd.getNumericalMean();
 assertEquals(3.0, mean, 1e-9);
 }

 @Test
 public void testFDistributionNumericalVariance() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 double variance = fd.getNumericalVariance();
 assertEquals(15.0 / 12.0, variance, 1e-9);
 }

 @Test
 public void testFDistributionGetSupportLowerBound() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 assertEquals(0.0, fd.getSupportLowerBound(), 1e-9);
 }

 @Test
 public void testFDistributionGetSupportUpperBound() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 1e-9);
 }

 @Test
 public void testFDistributionSupportIsConnected() {
 FDistribution fd = new FDistribution(2.0, 4.0);
 assertTrue(fd.isSupportConnected());
 }
}