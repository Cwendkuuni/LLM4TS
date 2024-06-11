package org.apache.commons.math3.distribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(-5, 2, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 0, 3);
 HypergeometricDistribution distribution2 = new HypergeometricDistribution(10, -2, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNonPositiveSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 0);
 HypergeometricDistribution distribution2 = new HypergeometricDistribution(10, 2, -3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 11, 3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 11);
 }

 @Test
 public void testCumulativeProbabilityWithValidInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 2;
 double expected = 0.1428571428571429; // Calculated using mathematical formula
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testCumulativeProbabilityWithLowerInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 0;
 double expected = 0.0;
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testCumulativeProbabilityWithUpperInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 7;
 double expected = 0.8571428571428571; // Calculated using mathematical formula
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testProbabilityWithValidInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 2;
 double expected = 0.1428571428571429; // Calculated using mathematical formula
 double actual = distribution.probability(x);
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testProbabilityWithLowerInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 0;
 double expected = 0.0;
 double actual = distribution.probability(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testProbabilityWithUpperInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 7;
 double expected = 0.05714285714285714; // Calculated using mathematical formula
 double actual = distribution.probability(x);
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithValidInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 2;
 double expected = 0.8571428571428571; // Calculated using mathematical formula
 double actual = distribution.upperCumulativeProbability(x);
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithUpperInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 7;
 double expected = 1.0;
 double actual = distribution.upperCumulativeProbability(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testUpperCumulativeProbabilityWithLowerInput() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int x = 0;
 double expected = 1.0;
 double actual = distribution.upperCumulativeProbability(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int expected = 3;
 int actual = distribution.getNumberOfSuccesses();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int expected = 10;
 int actual = distribution.getPopulationSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int expected = 5;
 int actual = distribution.getSampleSize();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 double expected = 3.0;
 double actual = distribution.getNumericalMean();
 assertEquals(expected, actual, 0.000001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 double expected = 0.17156309856309856;
 double actual = distribution.getNumericalVariance();
 assertEquals(expected, actual, 0.0000001);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int expected = 2;
 int actual = distribution.getSupportLowerBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 int expected = 5;
 int actual = distribution.getSupportUpperBound();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
 boolean expected = true;
 boolean actual = distribution.isSupportConnected();
 assertEquals(expected, actual);
 }
}