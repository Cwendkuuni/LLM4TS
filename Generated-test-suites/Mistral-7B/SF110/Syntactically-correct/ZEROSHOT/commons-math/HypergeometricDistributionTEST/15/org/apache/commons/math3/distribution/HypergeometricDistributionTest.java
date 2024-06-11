import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution hd;
 // Test with valid parameters
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 // Test with invalid population size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 fail("Should have thrown NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 // Test with invalid number of successes
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 // Test with invalid sample size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 fail("Should have thrown NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 // Test with number of successes larger than population size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 12, 5);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 // Test with sample size larger than population size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 12);
 fail("Should have thrown NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, hd.getPopulationSize());
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getNumberOfSuccesses());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getSampleSize());
 }

 @Test
 public void testGetLowerDomain() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getLowerDomain(10, 5, 5));
 }

 @Test
 public void testGetUpperDomain() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getUpperDomain(5, 5));
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 doublecp = hd.cumulativeProbability(i);
 assertEquals(i * 1.0 / 10.0, cp, 0.0001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 doublep = hd.probability(i);
 assertEquals(FastMath.binomialProbability(5, 5, i * 1.0 / 10.0), p, 0.0001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int i = 0; i <= 5; i++) {
 doubleupcumulativeProbability = hd.upperCumulativeProbability(i);
 assertEquals(1.0 - hd.cumulativeProbability(i), upcumulativeProbability, 0.0001);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, hd.getNumericalMean(), 0.0001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(2.9166666666666665, hd.getNumericalVariance(), 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0, hd.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertTrue(hd.isSupportConnected());
 }

}