import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionWithMeanAndSD = new NormalDistributionImpl(1.0, 2.0);
 }

 @Test
 public void testMeanGettersAndSetters() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(5.0);
 assertEquals(5.0, normalDistribution.getMean(), 0.0);
 }

 @Test
 public void testStandardDeviationGettersAndSetters() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(3.0);
 assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeStandardDeviationSetter() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double cumulativeProbability = normalDistribution.cumulativeProbability(0.5);
 assertEquals(0.5, cumulativeProbability, 0.0001);
 }

 @Test
 public void testCumulativeProbabilityNegativeValue() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double cumulativeProbability = normalDistribution.cumulativeProbability(-1.0);
 assertEquals(0.0, cumulativeProbability, 0.0);
 }

 @Test
 public void testCumulativeProbabilityLargeValue() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double cumulativeProbability = normalDistribution.cumulativeProbability(50.0);
 assertEquals(1.0, cumulativeProbability, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.cumulativeProbability(20 * NormalDistributionImpl.stdDevMultiplier);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.1);
 assertEquals(-0.842645, inverseCumulativeProbability, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.inverseCumulativeProbability(2.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityConvergenceFailure() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(0.0);
 normalDistribution.setStandardDeviation(1.0);
 normalDistribution.inverseCumulativeProbability(1.1);
 }
}