import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;
 private RandomGenerator rng;

 @Before
 public void setup() {
 rng = new Well19937c();
 try {
 distribution = new HypergeometricDistribution(rng, 10, 5, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testConstructorWithPopulationSizeNegative() {
 try {
 new HypergeometricDistribution(rng, -5, 5, 5);
 } catch (NotStrictlyPositiveException e) {
 assertEquals("Population size must be strictly positive", e.getLocalizedMessage(), e.getMessage());
 }
 }

 @Test
 public void testConstructorWithNumberOfSuccessesNegative() {
 try {
 new HypergeometricDistribution(rng, 5, -5, 5);
 } catch (NotPositiveException e) {
 assertEquals("Number of successes must be non-negative", e.getLocalizedMessage(), e.getMessage());
 }
 }

 @Test
 public void testConstructorWithSampleSizeNegative() {
 try {
 new HypergeometricDistribution(rng, 5, 5, -5);
 } catch (NotPositiveException e) {
 assertEquals("Sample size must be non-negative", e.getLocalizedMessage(), e.getMessage());
 }
 }

 @Test
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(rng, 5, 6, 5);
 } catch (NumberIsTooLargeException e) {
 assertEquals("Number of successes cannot be larger than population size", e.getLocalizedMessage(), e.getMessage());
 }
 }

 @Test
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(rng, 5, 5, 6);
 } catch (NumberIsTooLargeException e) {
 assertEquals("Sample size cannot be larger than population size", e.getLocalizedMessage(), e.getMessage());
 }
 }

 @Test
 public void testCumulativeProbabilityWithInvalidArgument() {
 final int[] domain = distribution.getDomain();
 for (int x = domain[0] - 1; x < domain[0]; x++) {
 try {
 distribution.cumulativeProbability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 for (int x = domain[1] + 1; x < Integer.MAX_VALUE; x++) {
 try {
 distribution.cumulativeProbability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testCumulativeProbability() {
 final int[] domain = distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = distribution.probability(x);
 double actual = distribution.cumulativeProbability(x);
 assertEquals("Cumulative probability does not match expected probability", expected, actual, 1e-10);
 }
 }

 @Test
 public void testProbabilityWithInvalidArgument() {
 for (int x = Integer.MIN_VALUE; x < domain[0]; x++) {
 try {
 distribution.probability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 for (int x = domain[1] + 1; x < Integer.MAX_VALUE; x++) {
 try {
 distribution.probability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testProbability() {
 final int[] domain = distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = distribution.probability(x);
 double actual = distribution.probability(x);
 assertEquals("Probability does not match expected value", expected, actual, 1e-10);
 }
 }

 @Test
 public void testUpperCumulativeProbabilityWithInvalidArgument() {
 final int[] domain = distribution.getDomain();
 for (int x = domain[0] - 1; x < domain[0]; x++) {
 try {
 distribution.upperCumulativeProbability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 for (int x = domain[1] + 1; x < Integer.MAX_VALUE; x++) {
 try {
 distribution.upperCumulativeProbability(x);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 final int[] domain = distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double expected = 1.0 - distribution.cumulativeProbability(x);
 double actual = distribution.upperCumulativeProbability(x);
 assertEquals("Upper cumulative probability does not match expected value", expected, actual, 1e-10);
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 assertEquals("Number of successes is incorrect", 5, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 assertEquals("Population size is incorrect", 10, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 assertEquals("Sample size is incorrect", 5, distribution.getSampleSize());
 }

 @Test
 public void testGetNumericalVariance() {
 double expected = 0.01111111111111111;
 double actual = distribution.getNumericalVariance();
 assertEquals("Numerical variance does not match expected value", expected, actual, 1e-14);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCalculateNumericalVariance() {
 distribution.calculateNumericalVariance();
 }

 @Test
 public void testGetSupportLowerBound() {
 int expected = 0;
 int actual = distribution.getSupportLowerBound();
 assertEquals("Lower bound of support is incorrect", expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 int expected = 5;
 int actual = distribution.getSupportUpperBound();
 assertEquals("Upper bound of support is incorrect", expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue("Support is not connected");
 }

}