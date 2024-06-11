import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Test
 public void testNormalDistributionCreation() {
     // Test default constructor
     distribution = new NormalDistributionImpl();
     assertEquals(0, distribution.getMean(), 0.001);
     assertEquals(1, distribution.getStandardDeviation(), 0.001);

     // Test constructor with mean and standard deviation
     distribution = new NormalDistributionImpl(1, 2);
     assertEquals(1, distribution.getMean(), 0.001);
     assertEquals(2, distribution.getStandardDeviation(), 0.001);

     // Test constructor with mean, standard deviation and inverse cumulative distribution accuracy
     distribution = new NormalDistributionImpl(1, 2, 1e-7);
     assertEquals(1, distribution.getMean(), 0.001);
     assertEquals(2, distribution.getStandardDeviation(), 0.001);
     assertEquals(1e-7, distribution.getSolverAbsoluteAccuracy(), 0.001);
 }

 @Test
 public void testDensity() {
     distribution = new NormalDistributionImpl(0, 1);
     double x = 0.5;
     double expectedDensity = 0.398942280401;
     double density = distribution.density(x);
     assertEquals(expectedDensity, density, 0.001);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0, 1);
     double x = 0.5;
     double expectedCdf = 0.682689492137;
     double cdf = distribution.cumulativeProbability(x);
     assertEquals(expectedCdf, cdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws Exception {
     distribution = new NormalDistributionImpl(0, 1);
     double x = 100;
     distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0, 1);
     double p = 0.5;
     double expectedX = 0;
     double x = distribution.inverseCumulativeProbability(p);
     assertEquals(expectedX, x, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityNotStrictlyPositiveStandardDeviation() {
     try {
         distribution = new NormalDistributionImpl(0, -1);
     } catch (NotStrictlyPositiveException e) {
         return; // expected
     }
     fail("NotStrictlyPositiveException not thrown");
 }

 @Test
 public void testSample() throws Exception {
     distribution = new NormalDistributionImpl(0, 1);
     double sample = distribution.sample();
     assertEquals(0, sample, 0.01);
 }

 @Test
 public void testSampleMultipleTimes() throws Exception {
     distribution = new NormalDistributionImpl(0, 1);
     double sample1 = distribution.sample();
     double sample2 = distribution.sample();
     double sample3 = distribution.sample();
     double mean = 0;
     double standardDeviation = 1;
     double expectedVariance = standardDeviation * standardDeviation;
     assertEquals(mean, sample1, 0.01);
     assertEquals(mean, sample2, 0.01);
     assertEquals(mean, sample3, 0.01);
     double variance = (sample1 - mean) * (sample1 - mean) + (sample2 - mean) * (sample2 - mean) + (sample3 - mean) * (sample3 - mean);
     assertEquals(expectedVariance, variance, 0.01);
 }

 @Test
 public void testSampleNegativeStandardDeviation() throws Exception {
     distribution = new NormalDistributionImpl(0, -1);
     assertThrows(IllegalArgumentException.class, () -> distribution.sample());
 }
}