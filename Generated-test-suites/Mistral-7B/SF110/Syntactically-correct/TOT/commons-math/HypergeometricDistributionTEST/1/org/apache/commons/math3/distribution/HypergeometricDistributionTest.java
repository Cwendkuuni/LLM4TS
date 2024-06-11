import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testInstanciationWithValidParameters() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInstanciationWithNegativePopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 // The test should fail with NotStrictlyPositiveException
 }

 @Test(expected = NotPositiveException.class)
 public void testInstanciationWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 // The test should fail with NotPositiveException
 }

 @Test(expected = NotPositiveException.class)
 public void testInstanciationWithNegativeSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 // The test should fail with NotPositiveException
 }

 @Test
 public void testInstanciationWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 // The constructor should throw an exception
 try {
 distribution.cumulativeProbability(5);
 fail("Expected NotNumberIsTooLargeException");
 } catch (NotNumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test
 public void testInstanciationWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 // The constructor should throw an exception
 try {
 distribution.cumulativeProbability(5);
 fail("Expected NotNumberIsTooLargeException");
 } catch (NotNumberIsTooLargeException e) {
 // Expected exception
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, distribution.cumulativeProbability(0), 0.001);
 assertEquals(0.5, distribution.cumulativeProbability(5), 0.001);
 assertEquals(1.0, distribution.cumulativeProbability(10), 0.001);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.333, distribution.probability(3), 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, distribution.getNumericalMean(), 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.5, distribution.getNumericalVariance(), 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(-5, distribution.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, distribution.getSupportUpperBound());
 }
}