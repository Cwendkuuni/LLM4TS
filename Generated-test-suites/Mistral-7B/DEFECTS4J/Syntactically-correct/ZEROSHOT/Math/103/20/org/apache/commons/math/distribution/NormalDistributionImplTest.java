import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStdDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.5, 2.0);
 assertEquals(1.5, normalDistribution.getMean(), 0.0);
 assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStdDeviationEqualsToZeroAndOne() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(0.0, normalDistribution.getMean(), 0.0);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplConstructionWithNegativeStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, -1.0);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutsideDomain() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 double result = normalDistribution.cumulativeProbability(-10);
 assertEquals(0.0, result, 0.0);

 result = normalDistribution.cumulativeProbability(100);
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testCumulativeProbabilityInsideDomain() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double x = 0.5;
 double result = normalDistribution.cumulativeProbability(x);
 assertNotNull(result);
 assertEquals(0.5, result, 0.01);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.25;
 double result = normalDistribution.inverseCumulativeProbability(p);
 assertNotNull(result);
 assertEquals(-0.842700792385444, result, 0.01);

 p = 0.75;
 result = normalDistribution.inverseCumulativeProbability(p);
 assertNotNull(result);
 assertEquals(0.679439702452858, result, 0.01);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutsideDomain() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.0;
 try {
 normalDistribution.inverseCumulativeProbability(p);
 fail("MathException expected but not thrown.");
 } catch (MathException e) {
 // Expected
 }

 p = 1.0;
 try {
 normalDistribution.inverseCumulativeProbability(p);
 fail("MathException expected but not thrown.");
 } catch (MathException e) {
 // Expected
 }
 }
}