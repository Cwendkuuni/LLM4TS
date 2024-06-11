import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 try {
 new HypergeometricDistribution(-1, 5, 10);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, -5, 10);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, 5, -10);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(1, 5, 10.0);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(null, 5, 10);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), -1, 5, 10);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, -5, 10);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, -10);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 10.0);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 11);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 0, 1);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 1, 0);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 5);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 5);
 } catch (Throwable e) {
 fail("Unexpected exception: " + e);
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 5, 1.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 1, 5, 5, 1.0);
 } catch (Throwable e) {
 fail("Unexpected exception: " + e);
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 } catch (Throwable e) {
 // Expected, since this constructor does not throw any exceptions
 }
 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 5, 1.0);
 // Expected, since this constructor does not throw any exceptions
 } catch (Throwable e) {
 fail("Unexpected exception: " + e);
 }

 new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 assertEquals(10, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 assertEquals(20, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 assertEquals(15, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);

 // Test with invalid arguments
 assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
 assertEquals(1.0, dist.cumulativeProbability(21), 0.0);

 // Test with valid arguments
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double prob = dist.probability(i);
 double cumProb = dist.cumulativeProbability(i);
 assertEquals(cumProb, prob, 0.0);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);

 // Test with invalid arguments
 assertEquals(0.0, dist.probability(-1), 0.0);
 assertEquals(0.0, dist.probability(21), 0.0);

 // Test with valid arguments
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double prob = dist.probability(i);
 double cumProb = dist.cumulativeProbability(i);
 double cumProbPrev = dist.cumulativeProbability(i - 1);
 assertEquals(cumProb, cumProbPrev + prob, 0.0);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);

 // Test with invalid arguments
 assertEquals(0.0, dist.upperCumulativeProbability(-1), 0.0);
 assertEquals(0.0, dist.upperCumulativeProbability(21), 0.0);

 // Test with valid arguments
 int[] domain = dist.getDomain();
 for (int i = domain[0]; i <= domain[1]; i++) {
 double prob = dist.probability(i);
 double cumProb = 1.0 - dist.cumulativeProbability(i);
 double upperCumProb = dist.upperCumulativeProbability(i);
 assertEquals(cumProb, upperCumProb, 0.0);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 assertEquals(12.5, dist.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 double numericalVariance = dist.getNumericalVariance();
 assertEquals(1.0 / 14.0, numericalVariance, 0.01);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 int lowerBound = dist.getSupportLowerBound();
 assertEquals(-5, lowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 int upperBound = dist.getSupportUpperBound();
 assertEquals(10, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 20, 10, 15);
 assertTrue(dist.isSupportConnected());
 }

}