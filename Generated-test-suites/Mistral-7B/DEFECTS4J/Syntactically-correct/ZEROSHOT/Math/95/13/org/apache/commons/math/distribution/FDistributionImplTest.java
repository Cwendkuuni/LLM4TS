import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Test
 public void testFDistributionImpl() {
 // Test constructor with valid parameters
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test constructor with invalid parameter
 try {
 distribution = new FDistributionImpl(-1.0, 4.0);
 } catch (IllegalArgumentException e) {
 assertEquals("degrees of freedom must be positive.", e.getMessage());
 }
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double pValue;
 double expectedCdf;

 // Test cdf for different values of x
 pValue = 0.0;
 expectedCdf = 0.0;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(pValue), 0.0);

 pValue = 1.0;
 expectedCdf = 1.0;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(pValue), 0.0);

 pValue = 0.5;
 expectedCdf = 0.6526;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(pValue), 0.001);

 pValue = 0.95;
 expectedCdf = 0.9973;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedCdf, distribution.cumulativeProbability(pValue), 0.001);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double probability;
 double expectedValue;

 // Test inverse cdf for different probabilities
 probability = 0.0;
 expectedValue = 0.0;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(probability), 0.0);

 probability = 1.0;
 expectedValue = Double.POSITIVE_INFINITY;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(probability), 0.0);

 probability = 0.5;
 expectedValue = 1.2086;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(probability), 0.01);

 probability = 0.95;
 expectedValue = 3.5925;
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(expectedValue, distribution.inverseCumulativeProbability(probability), 0.01);
 }
}