import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PoissonDistributionImplTest {

 private PoissonDistributionImpl distribution;

 @Test
 public void testConstructorWithValidMean() {
 distribution = new PoissonDistributionImpl(2.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidMean() {
 distribution = new PoissonDistributionImpl(-1.0);
 }

 @Test
 public void testProbability() {
 // Test probability with valid input
 double probability = distribution.probability(0);
 assertEquals(0.0673469391453469, probability, 0.0001);

 // Test probability with invalid input (negative value)
 probability = distribution.probability(-1);
 assertEquals(0.0, probability);

 // Test probability with invalid input (maximum value)
 probability = distribution.probability(Integer.MAX_VALUE);
 assertEquals(0.0, probability);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 // Test cumulative probability with valid input
 double cumulativeProbability = distribution.cumulativeProbability(0);
 assertEquals(0.662102362194825, cumulativeProbability, 0.0001);

 // Test cumulative probability with invalid input (negative value)
 assertThrows(IllegalArgumentException.class, () -> distribution.cumulativeProbability(-1));

 // Test cumulative probability with invalid input (maximum value)
 cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
 assertEquals(1.0, cumulativeProbability);
 }

 @Test
 public void testNormalApproximateProbability() throws Exception {
 // Test normal approximate probability with valid input
 double normalApproximateProbability = distribution.normalApproximateProbability(0);
 assertEquals(0.662102362194825, normalApproximateProbability, 0.0001);

 // Test normal approximate probability with invalid input (negative value)
 assertThrows(IndexOutOfBoundsException.class, () -> distribution.normalApproximateProbability(-1));
 }

 @Test
 public void testSample() throws Exception {
 // Test sample method with valid input
 int sample = distribution.sample();
 assertEquals(2, sample);

 // Test sample method with invalid input (negative value)
 assertThrows(IllegalArgumentException.class, () -> distribution.sample(-1));
 }

 @Test
 public void testGetMean() {
 double mean = distribution.getMean();
 assertEquals(2.5, mean, 0.0);
 }
}