import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructors() {
 try {
 new HypergeometricDistribution(-1, 1, 1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, -1, 1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, 1, -1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, 1, 0);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, 0);
 fail("Expected exception: NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, 1, 2);
 fail("Expected exception: NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, 1, 0);
 fail("Expected exception: NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
 fail("Expected exception: NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, 1, 1);
 // Expected no exception
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 assertEquals("Number of successes", 5, dist.getNumberOfSuccesses());
 assertEquals("Population size", 10, dist.getPopulationSize());
 assertEquals("Sample size", 15, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 int[] domain = dist.getDomain();
 assertEquals("Lower domain", 5, domain[0]);
 assertEquals("Upper domain", 14, domain[1]);
 for (int x = 5; x <= 14; x++) {
 double cp = dist.cumulativeProbability(x);
 double lp = dist.probability(x);
 assertEquals("Cumulative probability for x = " + x, cp, lp, 1e-8);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.probability(x);
 double sum = 0;
 for (int i = 0; i < 10000; i++) {
 sum += dist.sample();
 }
 double np = sum / 10000.0;
 assertEquals("Probability for x = " + x, p, np, 1e-8);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double ucp = dist.upperCumulativeProbability(x);
 double cp = dist.cumulativeProbability(x + 1);
 assertEquals("Upper cumulative probability for x = " + x, ucp, cp, 1e-8);
 }
 }

 @Test
 public void testGetSupportBounds() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 int lowerBound = dist.getSupportLowerBound();
 int upperBound = dist.getSupportUpperBound();
 assertEquals("Lower support bound", 0, lowerBound);
 assertEquals("Upper support bound", 14, upperBound);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 double mean = dist.getNumericalMean();
 assertEquals("Numerical mean", 5.0, mean, 1e-8);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 double variance = dist.getNumericalVariance();
 double expectedVariance = 5.0 * 14.0 / 10.0 / 10.0 / 9.0;
 assertEquals("Numerical variance", expectedVariance, variance, 1e-8);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
 assertTrue("Support is connected", dist.isSupportConnected());
 }

}