import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructorWithValidArguments() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test
 public void testConstructorWithInvalidPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 fail("Should throw NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test
 public void testConstructorWithInvalidNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 fail("Should throw NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test
 public void testConstructorWithInvalidSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 fail("Should throw NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }
 }

 @Test
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 fail("Should throw NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 fail("Should throw NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test
 public void testCumulativeProbabilityWithValidArguments() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, distribution.cumulativeProbability(0), 0.001);
 assertEquals(1.0, distribution.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testProbabilityWithValidArguments() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.25, distribution.probability(2), 0.001);
 }

 @Test
 public void testProbabilityWithInvalidArguments() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 try {
 distribution.probability(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, distribution.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0278508275862069, distribution.getNumericalVariance(), 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0, distribution.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, distribution.getSupportUpperBound());
 }
}