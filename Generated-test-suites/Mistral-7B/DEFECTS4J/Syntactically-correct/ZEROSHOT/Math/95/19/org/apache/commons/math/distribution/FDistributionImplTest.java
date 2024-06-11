import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Test
 public void testFDistributionImplConstruction() {
 // Test construction with valid parameters
 distribution = new FDistributionImpl(3.0, 4.0);
 assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test construction with invalid parameters
 try {
 distribution = new FDistributionImpl(-3.0, 4.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 distribution = new FDistributionImpl(3.0, -4.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 // Test zero value
 distribution = new FDistributionImpl(3.0, 4.0);
 double result = distribution.cumulativeProbability(0.0);
 assertEquals(0.0, result, 1e-12);

 // Test values close to zero
 result = distribution.cumulativeProbability(1e-12);
 assertTrue(result > 0.0);

 // Test values close to the mean
 double mean = distribution.getNumeratorDegreesOfFreedom() / (distribution.getDenominatorDegreesOfFreedom() + distribution.getNumeratorDegreesOfFreedom() * 1.0);
 result = distribution.cumulativeProbability(mean);
 assertEquals(0.5, result, 1e-12);

 // Test values close to infinity
 result = distribution.cumulativeProbability(1e6);
 assertTrue(result > 0.9999999999999999);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 // Test zero probability
 distribution = new FDistributionImpl(3.0, 4.0);
 double result = distribution.inverseCumulativeProbability(0.0);
 assertEquals(0.0, result, 1e-12);

 // Test one probability
 result = distribution.inverseCumulativeProbability(1.0);
 assertTrue(Double.isInfinite(result));

 // Test probabilities close to zero and one
 double p = 1e-6;
 result = distribution.inverseCumulativeProbability(p);
 assertTrue(result > 0.0);
 assertTrue(result < distribution.getNumeratorDegreesOfFreedom() / (distribution.getDenominatorDegreesOfFreedom() + distribution.getNumeratorDegreesOfFreedom() * p));

 p = 1.0 - 1e-6;
 result = distribution.inverseCumulativeProbability(p);
 assertTrue(result > distribution.getNumeratorDegreesOfFreedom() / (distribution.getDenominatorDegreesOfFreedom() + distribution.getNumeratorDegreesOfFreedom() * p));
 }
}