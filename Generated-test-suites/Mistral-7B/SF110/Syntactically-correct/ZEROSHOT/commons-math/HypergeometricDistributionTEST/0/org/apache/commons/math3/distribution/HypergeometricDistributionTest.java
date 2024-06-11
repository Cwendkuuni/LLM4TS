import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstructor() {
 try {
 new HypergeometricDistribution(-1, 1, 1);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, -1, 1);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, 1, -1);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, 1, 0);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, 1, 2);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, 2, 2);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(1, 2, 3);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 1, 2, 3);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 1, 2, 0);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 1, 2, 4);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 2, 3);
 } catch (NotPositiveException e) {
 fail("Expected no exception");
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 2, 5);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 5);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 5);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 6);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 7);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 8);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 9);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] expected = new int[] { 0, 5, 6, 7, 8, 9 };
 for (int x : expected) {
 double ret = dist.cumulativeProbability(x);
 double retExpected = 0.0;
 for (int i = 0; i < x; i++) {
 retExpected += dist.probability(i);
 }
 assertEquals("Expected cumulativeProbability to be " + retExpected + " but was " + ret, retExpected, ret, 0.001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] expected = new int[] { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0 };
 for (int x = 0; x < 11; x++) {
 double ret = dist.probability(x);
 assertEquals("Expected probability of " + x + " to be " + Math.comb(10, x) / Math.pow(15, 2) + " but was " + ret, Math.comb(10, x) / Math.pow(15, 2), ret, 0.001);
 }
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals("Expected numericalMean to be 5.0 but was " + dist.getNumericalMean(), 5.0, dist.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals("Expected numericalVariance to be 0.2222222222222222 but was " + dist.getNumericalVariance(), 0.2222222222222222, dist.getNumericalVariance(), 0.0001);
 }
}