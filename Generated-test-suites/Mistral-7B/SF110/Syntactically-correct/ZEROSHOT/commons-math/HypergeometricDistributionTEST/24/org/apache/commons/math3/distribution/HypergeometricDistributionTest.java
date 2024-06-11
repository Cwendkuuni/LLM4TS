import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 10);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 10);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
 // expected
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -1);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 10);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 fail("Expected NumberIsTooLargeException");
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 fail("Expected NumberIsTooLargeException");
 }
 }

 @Test
 public void testCumulativeProbabilityZeroSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 0, 10);
 assertEquals(0.0, dist.cumulativeProbability(0), 1e-12);
 }

 @Test
 public void testCumulativeProbabilityOneSuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 1, 10);
 assertEquals(1.0 / 11.0, dist.cumulativeProbability(1), 1e-12);
 }

 @Test
 public void testCumulativeProbabilityMaxSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 10);
 assertEquals(1.0, dist.cumulativeProbability(10), 1e-12);
 }

 @Test
 public void testCumulativeProbabilityOutOfRange() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(0.4808958021926222, dist.cumulativeProbability(5), 1e-12);
 }

 @Test
 public void testProbabilityZeroSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 0, 10);
 assertEquals(0.0, dist.probability(0), 1e-12);
 }

 @Test
 public void testProbabilityOneSuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 1, 10);
 assertEquals(1.0 / 11.0, dist.probability(1), 1e-12);
 }

 @Test
 public void testProbabilityMaxSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 10);
 assertEquals(1.0, dist.probability(10), 1e-12);
 }

 @Test
 public void testProbabilityOutOfRange() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(dist.probability(2), 0.013895556331826715, 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityZeroSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 0, 10);
 assertEquals(1.0, dist.upperCumulativeProbability(0), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityOneSuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 1, 10);
 assertEquals(10.0 / 11.0, dist.upperCumulativeProbability(1), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityMaxSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 10, 10);
 assertEquals(0.0, dist.upperCumulativeProbability(10), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityOutOfRange() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(0.5191044436689656, dist.upperCumulativeProbability(5), 1e-12);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(10, dist.getSampleSize());
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(3.0, dist.getNumericalMean(), 1e-12);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertEquals(1.4112, dist.getNumericalVariance(), 1e-12);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 10);
 assertTrue(dist.isSupportConnected());
 }

}