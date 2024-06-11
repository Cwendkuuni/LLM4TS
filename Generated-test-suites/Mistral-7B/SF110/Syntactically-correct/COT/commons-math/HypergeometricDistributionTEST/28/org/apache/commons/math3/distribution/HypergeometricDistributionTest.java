import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 private RandomGenerator rng = new MersenneTwisterFastRandom();

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_populationSizeNegative() {
 try {
 new HypergeometricDistribution(rng, -5, 3, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected: NotStrictlyPositiveException
 }
 new HypergeometricDistribution(rng, -5, 3, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_numberOfSuccessesNegative() {
 try {
 new HypergeometricDistribution(rng, 5, -3, 5);
 } catch (NotPositiveException e) {
 // expected: NotPositiveException
 }
 new HypergeometricDistribution(rng, 5, -3, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistribution_sampleSizeNegative() {
 try {
 new HypergeometricDistribution(rng, 5, 3, -5);
 } catch (NotPositiveException e) {
 // expected: NotPositiveException
 }
 new HypergeometricDistribution(rng, 5, 3, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_numberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(rng, 5, 6, 5);
 } catch (NotStrictlyPositiveException | NotPositiveException e) {
 // expected: NumberIsTooLargeException
 }
 new HypergeometricDistribution(rng, 5, 6, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistribution_sampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(rng, 5, 3, 6);
 } catch (NotStrictlyPositiveException | NotPositiveException e) {
 // expected: NumberIsTooLargeException
 }
 new HypergeometricDistribution(rng, 5, 3, 6);
 }

 @Test
 public void testHypergeometricDistribution_cumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.cumulativeProbability(x);
 System.out.println("cumulativeProbability(" + x + ") = " + p);
 assertEquals(p, dist.probability(x), 0.00001);
 }
 }

 @Test
 public void testHypergeometricDistribution_probability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.probability(x);
 System.out.println("probability(" + x + ") = " + p);
 double cumulative = 0.0;
 for (int i = domain[0]; i < x; i++) {
 cumulative += p;
 p = dist.probability(i + 1);
 }
 assertEquals(p, dist.cumulativeProbability(x) - cumulative, 0.00001);
 }
 }

 @Test
 public void testHypergeometricDistribution_upperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.upperCumulativeProbability(x);
 System.out.println("upperCumulativeProbability(" + x + ") = " + p);
 double cumulative = 0.0;
 for (int i = domain[1]; i > x; i--) {
 cumulative += dist.probability(i);
 }
 assertEquals(1.0 - cumulative, p, 0.00001);
 }
 }

 @Test
 public void testHypergeometricDistribution_supportBounds() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 int lower = dist.getSupportLowerBound();
 int upper = dist.getSupportUpperBound();
 assertTrue(lower >= 0);
 assertTrue(upper <= 10);
 }

 @Test
 public void testHypergeometricDistribution_isSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testHypergeometricDistribution_numericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 10);
 double numericalVariance = dist.getNumericalVariance();
 double populationSize = dist.getPopulationSize();
 double numberOfSuccesses = dist.getNumberOfSuccesses();
 double sampleSize = dist.getSampleSize();
 double theoreticalVariance = dist.calculateNumericalVariance();
 System.out.println("Numerical variance: " + numericalVariance);
 System.out.println("Theoretical variance: " + theoreticalVariance);
 assertEquals(numericalVariance, theoreticalVariance, 0.001);
 }
}