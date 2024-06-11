package org.apache.commons.math3.distribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(-5, 2, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, -2, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, -3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 11, 3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 11);
 }

 @Test
 public void testCumulativeProbability_Success() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 5;
 double expected = 0.5;
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testCumulativeProbability_Failure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 6;
 double expected = 0.546824121764916; // calculated using mathematical formula
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testCumulativeProbability_EdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testCumulativeProbability_Error() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 11;
 double expected = Double.NaN;
 double actual = dist.cumulativeProbability(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testProbability_Success() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 5;
 double expected = 0.096875;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testProbability_Failure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 6;
 double expected = 0.0687827123017275; // calculated using mathematical formula
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testProbability_EdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 0;
 double expected = 0.0;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testProbability_Error() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 11;
 double expected = 0.0;
 double actual = dist.probability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability_Success() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 5;
 double expected = 0.5;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability_Failure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 6;
 double expected = 0.453125878235294; // calculated using mathematical formula
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability_EdgeCase() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 10;
 double expected = 1.0;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testUpperCumulativeProbability_Error() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int x = 11;
 double expected = 0.0;
 double actual = dist.upperCumulativeProbability(x);
 assertEquals(expected, actual);
 }
}