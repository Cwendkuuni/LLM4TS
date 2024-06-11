import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, -1, 5);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 3, 11);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);

 assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
 assertEquals(0.0455, dist.cumulativeProbability(1), 0.001);
 assertEquals(0.1389, dist.cumulativeProbability(2), 0.001);
 assertEquals(0.2361, dist.cumulativeProbability(3), 0.001);
 assertEquals(0.3333, dist.cumulativeProbability(4), 0.001);
 assertEquals(0.4307, dist.cumulativeProbability(5), 0.001);
 assertEquals(0.5278, dist.cumulativeProbability(6), 0.001);
 assertEquals(0.6249, dist.cumulativeProbability(7), 0.001);
 assertEquals(0.7222, dist.cumulativeProbability(8), 0.001);
 assertEquals(0.8195, dist.cumulativeProbability(9), 0.001);
 assertEquals(0.9167, dist.cumulativeProbability(10), 0.001);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);

 assertEquals(0.0039, dist.probability(0), 0.001);
 assertEquals(0.0455, dist.probability(1), 0.001);
 assertEquals(0.0769, dist.probability(2), 0.001);
 assertEquals(0.1101, dist.probability(3), 0.001);
 assertEquals(0.1430, dist.probability(4), 0.001);
 assertEquals(0.1758, dist.probability(5), 0.001);
 assertEquals(0.2086, dist.probability(6), 0.001);
 assertEquals(0.2414, dist.probability(7), 0.001);
 assertEquals(0.2742, dist.probability(8), 0.001);
 assertEquals(0.3069, dist.probability(9), 0.001);
 assertEquals(0.3397, dist.probability(10), 0.001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);

 assertEquals(0.9961, dist.upperCumulativeProbability(0), 0.001);
 assertEquals(0.9545, dist.upperCumulativeProbability(1), 0.001);
 assertEquals(0.9011, dist.upperCumulativeProbability(2), 0.001);
 assertEquals(0.8479, dist.upperCumulativeProbability(3), 0.001);
 assertEquals(0.7947, dist.upperCumulativeProbability(4), 0.001);
 assertEquals(0.7415, dist.upperCumulativeProbability(5), 0.001);
 assertEquals(0.6883, dist.upperCumulativeProbability(6), 0.001);
 assertEquals(0.6351, dist.upperCumulativeProbability(7), 0.001);
 assertEquals(0.5818, dist.upperCumulativeProbability(8), 0.001);
 assertEquals(0.5283, dist.upperCumulativeProbability(9), 0.001);
 assertEquals(0.4747, dist.upperCumulativeProbability(10), 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(-3, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3.0, dist.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(1.1111111111111112, dist.getNumericalVariance(), 0.001);
 }
}