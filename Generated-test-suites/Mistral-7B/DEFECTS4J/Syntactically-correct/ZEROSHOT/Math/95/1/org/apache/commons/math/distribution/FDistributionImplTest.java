import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImpl() {
 // Test constructor with valid parameters
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals("Numerator degrees of freedom not set correctly", 5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals("Denominator degrees of freedom not set correctly", 10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test constructor with invalid parameters
 try {
 distribution = new FDistributionImpl(-5.0, 10.0);
 fail("Exception expected for negative numerator degrees of freedom");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 distribution = new FDistributionImpl(5.0, -10.0);
 fail("Exception expected for negative denominator degrees of freedom");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double pVal;
 double expectedCDF;

 // Test small values
 distribution = new FDistributionImpl(1.0, 1.0);
 for (double x = 0.0; x <= 3.0; x += 0.1) {
 pVal = distribution.cumulativeProbability(x);
 expectedCDF = Beta.regularizedBeta((1.0 * x) / (1.0 + 1.0 * x), 0.5, 0.5);
 assertEquals("CDF not calculated correctly for x = " + x, expectedCDF, pVal, 0.001);
 }

 // Test large values
 distribution = new FDistributionImpl(10.0, 20.0);
 for (double x = 3.0; x <= 30.0; x += 1.0) {
 pVal = distribution.cumulativeProbability(x);
 expectedCDF = Beta.regularizedBeta((10.0 * x) / (20.0 + 10.0 * x), 5.0, 5.0);
 assertEquals("CDF not calculated correctly for x = " + x, expectedCDF, pVal, 0.001);
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double p;
 double expectedX;
 double tolerance = 1e-6;

 // Test p = 0
 p = 0.0;
 assertEquals("Inverse CDF not calculated correctly for p = 0", 0.0, distribution.inverseCumulativeProbability(p), tolerance);

 // Test p = 1
 p = 1.0;
 assertEquals("Inverse CDF not calculated correctly for p = 1", Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(p), tolerance);

 // Test intermediate values
 p = 0.25;
 expectedX = Beta.regularizedBetaInverse(0.25, 0.5, 0.5);
 assertEquals("Inverse CDF not calculated correctly for p = 0.25", expectedX, distribution.inverseCumulativeProbability(p), tolerance);

 // Test large p
 p = 0.99999;
 expectedX = Beta.regularizedBetaInverse(0.99999, 5.0, 5.0);
 assertEquals("Inverse CDF not calculated correctly for p = 0.99999", expectedX, distribution.inverseCumulativeProbability(p), tolerance);
 }
}