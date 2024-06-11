import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Before
 public void setUp() {
 distribution = new NormalDistributionImpl(0, 1);
 }

 @Test
 public void testDefaultConstructor() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 NormalDistribution expectedNormalDistribution = new NormalDistributionImpl(0, 1);

 Assert.assertEquals(expectedNormalDistribution.getMean(), normalDistribution.getMean());
 Assert.assertEquals(expectedNormalDistribution.getStandardDeviation(), normalDistribution.getStandardDeviation());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeStandardDeviationConstructor() {
 NormalDistribution distribution = new NormalDistributionImpl(0, -1);
 }

 @Test
 public void testMeanSetterAndGetter() {
 distribution.setMean(2.5);
 Assert.assertEquals(2.5, distribution.getMean(), 0.001);
 }

 @Test
 public void testStandardDeviationSetterAndGetter() {
 distribution.setStandardDeviation(2.5);
 Assert.assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 for (double x = -5; x <= 5; x += 0.1) {
 double expectedCumulativeProbability = 0.5 * (1.0 + Erf.erf((x - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0))));
 double actualCumulativeProbability = distribution.cumulativeProbability(x);
 Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws MathException {
 distribution.cumulativeProbability(100);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 for (double p = 0; p <= 1; p += 0.01) {
 double expectedInverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
 double actualInverseCumulativeProbability = distribution.cumulativeProbability(expectedInverseCumulativeProbability);
 Assert.assertEquals(p, actualInverseCumulativeProbability, 0.001);
 }
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
 distribution.inverseCumulativeProbability(-1);
 }
}