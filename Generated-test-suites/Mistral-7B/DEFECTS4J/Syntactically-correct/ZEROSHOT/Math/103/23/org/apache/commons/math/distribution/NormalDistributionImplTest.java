import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Test
 public void testNormalDistributionImplConstruction() {
     distribution = new NormalDistributionImpl(1.5, 2.5);
     assertEquals(1.5, distribution.getMean(), 0.001);
     assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplZeroMeanOneStandardDeviation() {
     distribution = new NormalDistributionImpl();
     assertEquals(0.0, distribution.getMean(), 0.001);
     assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplNegativeStandardDeviation() {
     distribution = new NormalDistributionImpl(0.0, -1.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double cdf;
     for (double x = -5.0; x <= 5.0; x += 0.1) {
         cdf = distribution.cumulativeProbability(x);
         System.out.printf("%.2f : %.6f%n", x, cdf);
         assertTrue(cdf >= 0.0 && cdf <= 1.0);
     }
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double p;
     for (p = 0.0; p <= 1.0; p += 0.1) {
         double x = distribution.inverseCumulativeProbability(p);
         System.out.printf("%.2f : %.6f%n", p, x);
         assertTrue(x >= -10.0 && x <= 10.0);
     }
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     distribution.inverseCumulativeProbability(2.0);
 }

 @Test
 public void testInverseCumulativeProbabilityLimits() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
     assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
 }

 @Test
 public void testHighAccuracyCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double cdf = distribution.cumulativeProbability(0.5);
     assertEquals(0.682689462137, cdf, 0.00000000001);
 }

 @Test
 public void testHighAccuracyInverseCumulativeProbability() throws Exception {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double p = 0.99999;
     double x = distribution.inverseCumulativeProbability(p);
     assertEquals(-0.00135354995122, x, 0.00000000001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegative() {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     distribution.setStandardDeviation(-1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanNegative() {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     distribution.setMean(-1.0);
 }

 @Test
 public void testToString() {
     distribution = new NormalDistributionImpl(1.5, 2.5);
     String s = distribution.toString();
     assertTrue(s.contains("class NormalDistributionImpl"));
     assertTrue(s.contains("mean = 1.5"));
     assertTrue(s.contains("standardDeviation = 2.5"));
 }
}