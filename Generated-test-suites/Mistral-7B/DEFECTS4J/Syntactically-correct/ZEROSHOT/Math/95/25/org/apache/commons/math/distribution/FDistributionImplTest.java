import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImpl() {
 // Test constructor
 distribution = new FDistributionImpl(2.0, 4.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 // Test valid values
 distribution = new FDistributionImpl(2.0, 4.0);
 double x = 1.5;
 double cdf = distribution.cumulativeProbability(x);
 double expectedCdf = Beta.regularizedBeta(3.0, 2.5, 0.5 * 2.0, 0.5 * 4.0);
 assertEquals(expectedCdf, cdf, 0.001);

 // Test invalid value (negative x)
 x = -1.0;
 try {
 distribution.cumulativeProbability(x);
 fail("Expected MathException");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 // Test valid values
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = 0.95;
 double x = distribution.inverseCumulativeProbability(p);
 double expectedX = Beta.incompleteBetaInvErfc(1.0 - p, 2.5, 2.0) / 2.0;
 assertEquals(expectedX, x, 0.001);

 // Test edge cases
 p = 0.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.0);

 p = 1.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
 }
}