import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImpl_constructor() {
 FDistributionImpl dist = new FDistributionImpl(5.0, 10.0);
 assertEquals(5.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(10.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Basic test case
 distribution = new FDistributionImpl(5.0, 10.0);
 double x = 2.0;
 double expected = Beta.regularizedBeta((5.0 * x) / (10.0 + 5.0 * x), 0.5 * 5.0, 0.5 * 10.0);
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);

 // Edge case: x = 0
 x = 0.0;
 expected = 0.0;
 actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual);

 // Edge case: x = Double.POSITIVE_INFINITY
 x = Double.POSITIVE_INFINITY;
 expected = 1.0;
 actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithInvalidX() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double x = -1.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Basic test case
 distribution = new FDistributionImpl(5.0, 10.0);
 double p = 0.25;
 double expected = (10.0 * p) / (5.0 + 10.0 * p);
 double actual = distribution.inverseCumulativeProbability(p);
 assertEquals(expected, actual, 0.001);

 // Edge case: p = 0
 p = 0.0;
 expected = 0.0;
 actual = distribution.inverseCumulativeProbability(p);
 assertEquals(expected, actual);

 // Edge case: p = 1
 p = 1.0;
 expected = Double.POSITIVE_INFINITY;
 actual = distribution.inverseCumulativeProbability(p);
 assertEquals(expected, actual);

 // Exception scenario: p < 0 or p > 1
 p = 1.1;
 try {
 distribution.inverseCumulativeProbability(p);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}