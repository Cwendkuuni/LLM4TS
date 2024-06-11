import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeParams() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 4, 3);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 0, 4, 3);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -4, 3);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 4, -3);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 3);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 4, 6);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test
 public void testGetLowerDomain() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 final int[] domain = dist.getDomain();
 assertEquals(0, domain[0]);
 assertEquals(5, domain[1]);
 }

 @Test
 public void testGetUpperDomain() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 final int[] domain = dist.getDomain();
 assertEquals(5, domain[1]);
 }

 @Test
 public void testCumulativeProbability() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
 assertEquals(0.5, dist.cumulativeProbability(2), 0.001);
 assertEquals(0.64, dist.cumulativeProbability(3), 0.001);
 assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testProbability() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.360761535628829, dist.probability(2), 0.0001);
 assertEquals(0.096918374107255, dist.probability(4), 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.5, dist.upperCumulativeProbability(2), 0.001);
 assertEquals(0.64, dist.upperCumulativeProbability(3), 0.001);
 assertEquals(1.0, dist.upperCumulativeProbability(5), 0.001);
 }

 @Test
 public void testNumericalMean() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, dist.getNumericalMean(), 0.01);
 }

 @Test
 public void testNumericalVariance() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(1.95257314285714, dist.getNumericalVariance(), 0.01);
 }

 @Test
 public void testSupportLowerBound() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(-1, dist.getSupportLowerBound());
 }

 @Test
 public void testSupportUpperBound() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertTrue(dist.isSupportConnected());
 }
}