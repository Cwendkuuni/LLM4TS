import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImpl() {
 // Test constructor with valid arguments
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test constructor with invalid arguments
 try {
 distribution = new FDistributionImpl(-3.0, 5.0);
 } catch (IllegalArgumentException e) {
 // Expected exception
 assertEquals("degrees of freedom must be positive.", e.getMessage());
 }
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double p;
 double expectedCdf;

 // Test with p = 0.0
 p = 0.0;
 expectedCdf = 0.0;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(p), 0.0);

 // Test with p = 0.5
 p = 0.5;
 expectedCdf = 0.5;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(p), 0.001);

 // Test with p = 1.0
 p = 1.0;
 expectedCdf = 1.0;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(p), 0.001);

 // Test with negative p
 p = -0.5;
 try {
 distribution.cumulativeProbability(p);
 } catch (MathException e) {
 // Expected exception
 assertEquals("Invalid probability: " + p, e.getMessage());
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double p;
 double expectedValue;

 // Test with p = 0.0
 p = 0.0;
 expectedValue = 0.0;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(p), 0.0);

 // Test with p = 0.5
 p = 0.5;
 expectedValue = 1.47037582561546;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(p), 0.001);

 // Test with p = 1.0
 p = 1.0;
 expectedValue = Double.POSITIVE_INFINITY;
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(p), 0.0);

 // Test with negative p
 p = -0.5;
 try {
 distribution.inverseCumulativeProbability(p);
 } catch (MathException e) {
 // Expected exception
 assertEquals("Invalid probability: " + p, e.getMessage());
 }
 }
}