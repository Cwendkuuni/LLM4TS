import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

 private UniformRealDistribution distribution;
 private Well19937c random;

 @Before
 public void setUp() {
 random = new Well19937c();
 distribution = new UniformRealDistribution(random, 0, 1);
 }

 @Test
 public void testDefaultConstructor() {
 UniformRealDistribution urd = new UniformRealDistribution();
 assertEquals(0.0, urd.getLowerBound(), 0.0);
 assertEquals(1.0, urd.getUpperBound(), 0.0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testInvalidConstructor() {
 UniformRealDistribution urd = null;
 try {
 urd = new UniformRealDistribution(random, 5, 3);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 assertEquals(0.0, urd.getLowerBound(), 0.0);
 assertEquals(3.0, urd.getUpperBound(), 0.0);
 }

 @Test
 public void testSample() {
 for (int i = 0; i < 10000; i++) {
 double sample = distribution.sample();
 assertTrue(sample >= 0.0 && sample < 1.0);
 }
 }

 @Test
 public void testDensity() {
 for (double x = 0.0; x <= 1.0; x += 0.01) {
 double density = distribution.density(x);
 assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = 0.0; x <= 1.0; x += 0.01) {
 double cdf = distribution.cumulativeProbability(x);
 assertEquals(x, cdf, 0.001);
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testInvalidCumulativeProbability() {
 double x = 2.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() {
 for (double p = 0.0; p <= 1.0; p += 0.01) {
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(p, distribution.cumulativeProbability(x), 0.001);
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testInvalidInverseCumulativeProbability() {
 double p = 1.1;
 distribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testGetNumericalMean() {
 assertEquals(0.5, distribution.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 assertEquals(0.0625, distribution.getNumericalVariance(), 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 assertTrue(distribution.isSupportLowerBoundInclusive());
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 assertFalse(distribution.isSupportUpperBoundInclusive());
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(distribution.isSupportConnected());
 }
}