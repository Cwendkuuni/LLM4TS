import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.2);
 assertEquals(2.5, normalDistribution.getMean(), 0.001);
 assertEquals(1.2, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviationZero() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(0.0, normalDistribution.getMean(), 0.001);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testSetMean() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(-2.5);
 assertEquals(-2.5, normalDistribution.getMean(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanWithNegativeValue() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(-1.2);
 }

 @Test
 public void testSetStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(2.5);
 assertEquals(2.5, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationWithZeroOrNegativeValue() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(0);
 }

 @Test
 public void testCumulativeProbabilityWithinTwoStandardDeviationsFromMean() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double x = normalDistribution.getMean() + 2 * normalDistribution.getStandardDeviation();
 double p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.9772, p, 0.001);
 }

 @Test
 public void testCumulativeProbabilityFarFromMean() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double x = normalDistribution.getMean() + 20 * normalDistribution.getStandardDeviation();
 double p = normalDistribution.cumulativeProbability(x);
 assertEquals(0.0, p, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutsideDomain() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double x = normalDistribution.getMean() - 100 * normalDistribution.getStandardDeviation();
 normalDistribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.0;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 1.0;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityInRange() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.5;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.1;
 normalDistribution.inverseCumulativeProbability(p);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 1.2;
 assertThrows(IllegalArgumentException.class, () -> normalDistribution.inverseCumulativeProbability(p));
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityWithMaxIterationsExceeded() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.9999999;
 double x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(7.154438586886286, x, 0.001);
 }
}