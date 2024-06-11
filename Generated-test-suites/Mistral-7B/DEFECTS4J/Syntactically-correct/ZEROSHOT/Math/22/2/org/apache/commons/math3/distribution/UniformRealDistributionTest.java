import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

 private UniformRealDistribution distribution;
 private Well19937c random;

 @Before
 public void setUp() {
 random = new Well19937c();
 try {
 distribution = new UniformRealDistribution(random, 0, 1);
 } catch (NumberIsTooLargeException e) {
 fail("Unexpected NumberIsTooLargeException: " + e.getMessage());
 }
 }

 @Test
 public void testDensity() {
 for (double x = 0; x <= 1; x += 0.01) {
 double density = distribution.density(x);
 double expected = 1 / (1 - 0);
 assertEquals("Unexpected density at x = " + x, expected, density, 0.001);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = 0; x <= 1; x += 0.01) {
 double cumProb = distribution.cumulativeProbability(x);
 double expected = x;
 assertEquals("Unexpected cumulative probability at x = " + x, expected, cumProb, 0.001);
 }
 }

 @Test
 public void testInverseCumulativeProbability() {
 for (double x = 0; x <= 1; x += 0.01) {
 double inverseCumProb = distribution.inverseCumulativeProbability(x);
 double expected = x;
 assertEquals("Unexpected inverse cumulative probability at x = " + x, expected, inverseCumProb, 0.001);
 }
 }

 @Test
 public void testSample() {
 for (int i = 0; i < 1000; i++) {
 double sample = distribution.sample();
 assertTrue("Unexpected sample value: " + sample, sample >= 0 && sample < 1);
 }
 }

 @Test
 public void testGetNumericalMean() {
 double mean = distribution.getNumericalMean();
 double expected = 0.5;
 assertEquals("Unexpected numerical mean", expected, mean, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 double variance = distribution.getNumericalVariance();
 double expected = 0.25 / 12;
 assertEquals("Unexpected numerical variance", expected, variance, 0.001);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidBounds() {
 distribution = new UniformRealDistribution(random, 1, 0);
 fail("Expected NumberIsTooLargeException");
 }
}