import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl dist;

 @Before
 public void setUp() {
 dist = new NormalDistributionImpl(0, 1);
 }

 @Test
 public void testNormalDistributionImpl() {
 NormalDistribution normDist = dist;
 assertEquals("Mean should be set to 0", 0.0, normDist.getMean(), 0.001);
 assertEquals("Standard Deviation should be set to 1", 1.0, normDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testSetMean() {
 dist.setMean(2.5);
 NormalDistribution normDist = dist;
 assertEquals("Mean should be set to 2.5", 2.5, normDist.getMean(), 0.001);
 }

 @Test
 public void testSetStandardDeviation() {
 dist.setStandardDeviation(2);
 NormalDistribution normDist = dist;
 assertEquals("Standard Deviation should be set to 2", 2.0, normDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 NormalDistribution normDist = dist;
 assertEquals("CDF for x = 0 should be 0.5", 0.5, normDist.cumulativeProbability(0), 0.001);
 assertEquals("CDF for x = 1 should be 0.841344746", 0.841344746, normDist.cumulativeProbability(1), 0.001);
 assertEquals("CDF for x = 2 should be 0.977249833", 0.977249833, normDist.cumulativeProbability(2), 0.001);
 assertEquals("CDF for x = 3 should be 0.997377236", 0.997377236, normDist.cumulativeProbability(3), 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws Exception {
 NormalDistribution normDist = dist;
 normDist.cumulativeProbability(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 NormalDistribution normDist = dist;
 assertEquals("Inverse CDF for p = 0 should be negative infinity", Double.NEGATIVE_INFINITY, normDist.inverseCumulativeProbability(0), 0.0);
 assertEquals("Inverse CDF for p = 0.5 should be mean", 0.0, normDist.inverseCumulativeProbability(0.5), 0.001);
 assertEquals("Inverse CDF for p = 1 should be positive infinity", Double.POSITIVE_INFINITY, normDist.inverseCumulativeProbability(1), 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws Exception {
 NormalDistribution normDist = dist;
 normDist.inverseCumulativeProbability(-1);
 }
}