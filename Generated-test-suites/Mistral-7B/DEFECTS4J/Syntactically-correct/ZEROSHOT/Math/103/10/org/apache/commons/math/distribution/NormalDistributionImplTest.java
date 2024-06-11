import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testConstructorWithMeanAndStandardDeviation() {
 distribution = new NormalDistributionImpl(1.5, 2.5);
 assertEquals(1.5, distribution.getMean(), 0.001);
 assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testConstructorWithDefaultValues() {
 distribution = new NormalDistributionImpl();
 assertEquals(0.0, distribution.getMean(), 0.001);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testMeanAccessors() {
 NormaldistributionImpl dist = new NormalDistributionImpl();
 dist.setMean(3.14);
 assertEquals(3.14, dist.getMean(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMeanSetterWithNegativeValue() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.setMean(-1.23);
 }

 @Test
 public void testStandardDeviationAccessors() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.setStandardDeviation(2.718);
 assertEquals(2.718, dist.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardDeviationSetterWithZeroOrNegativeValue() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.setStandardDeviation(0.0);
 dist.setStandardDeviation(-1.23);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 double p = 0.3456;
 double result = dist.cumulativeProbability(1.23);
 assertEquals(p, result, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithValueFarAwayFromMean() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 double value = 20 * dist.getStandardDeviation() + dist.getMean();
 dist.cumulativeProbability(value);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 double p = 0.789;
 double result = dist.inverseCumulativeProbability(p);
 assertEquals(dist.getMean() + Erf.erfInverse(2 * p - 1) * dist.getStandardDeviation(), result, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithNonValidProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.inverseCumulativeProbability(1.1);
 }

 @Test
 public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 double result = dist.inverseCumulativeProbability(0.0);
 assertEquals(Double.NEGATIVE_INFINITY, result);
 }

 @Test
 public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 double result = dist.inverseCumulativeProbability(1.0);
 assertEquals(Double.POSITIVE_INFINITY, result);
 }
}