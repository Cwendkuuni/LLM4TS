import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Before
 public void setUp() {
 distribution = new NormalDistributionImpl(0.0, 1.0);
 }

 @Test
 public void testInstance() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 assertEquals(NormalDistribution.class, normalDistribution.getClass());
 }

 @Test
 public void testMeanGetSet() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 assertEquals(1.0, normalDistribution.getMean(), 0.0);
 normalDistribution.setMean(2.0);
 assertEquals(2.0, normalDistribution.getMean(), 0.0);
 }

 @Test
 public void testStandardDeviationGetSet() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
 normalDistribution.setStandardDeviation(3.0);
 assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegative() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 normalDistribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 // Test within 1 standard deviation
 assertEquals(0.841344746, distribution.cumulativeProbability(0.0), 0.0001);
 // Test outside of domain
 assertEquals(0.0, distribution.cumulativeProbability(10.0), 0.0);
 // Test with mean
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 assertEquals(0.841344746, normalDistribution.cumulativeProbability(1.0), 0.0001);
 // Test with mean + 3 standard deviations
 double x = normalDistribution.getMean() + 3 * normalDistribution.getStandardDeviation();
 assertEquals(0.0, normalDistribution.cumulativeProbability(x), 0.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 // Test with p = 0
 assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
 // Test with p = 0.25
 double p = 0.25;
 double expected = -1.644853627;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.0001);
 // Test with p = 0.5
 p = 0.5;
 expected = 0.0;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.0001);
 // Test with p = 0.75
 p = 0.75;
 expected = 1.644853627;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.0001);
 }
}