import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -3, 2, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected NotPositiveException
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonStrictlyPositiveNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 3, -2, 5);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected NotStrictlyPositiveException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 3, 4, 2);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected NumberIsTooLargeException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 3, 2, 4);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected NumberIsTooLargeException
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testGetLowerDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0, dist.getLowerDomain(10, 5, 5));
 }

 @Test
 public void testGetUpperDomain() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getUpperDomain(10, 5));
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.2734375, dist.cumulativeProbability(2), 0.0001);
 }

 @Test
 public void testCumulativeProbabilityWithLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, dist.cumulativeProbability(0));
 }

 @Test
 public void testCumulativeProbabilityWithUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(1.0, dist.cumulativeProbability(5));
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.probability(2);
 assertEquals(0.1265625, p, 0.0001);
 }

 @Test
 public void testProbabilityWithLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.probability(0);
 assertEquals(0.0, p, 0.0001);
 }

 @Test
 public void testProbabilityWithUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double p = dist.probability(5);
 assertEquals(0.1265625, p, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.4721875, dist.upperCumulativeProbability(3), 0.0001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(1.0, dist.upperCumulativeProbability(5));
 }

 @Test
 public void testUpperCumulativeProbabilityWithUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, dist.upperCumulativeProbability(6));
 }

 @Test
 public void testInnerCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double pSum = 0.0;
 for (int i = 0; i < 10; i++) {
 pSum += dist.probability(i);
 }
 assertEquals(1.0, pSum, 0.0001);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, dist.getNumericalMean(), 0.0001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(3.5125, dist.getNumericalVariance(), 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(-3, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertTrue(dist.isSupportConnected());
 }

}