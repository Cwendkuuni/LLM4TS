package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution hd;

 @Before
 public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 }

 @Test
 public void testHypergeometricDistributionConstructor_ValidParameters() {
 // Test the constructor with valid parameters
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructor_InvalidPopulationSize() {
 // Test the constructor with negative population size
 hd = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructor_InvalidNumberOfSuccesses() {
 // Test the constructor with negative number of successes
 hd = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructor_InvalidSampleSize() {
 // Test the constructor with negative sample size
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructor_NumberOfSuccessesLargerThanPopulationSize() {
 // Test the constructor with numberOfSuccesses larger than population size
 hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructor_SampleSizeLargerThanPopulationSize() {
 // Test the constructor with sample size larger than population size
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testGetDomain() {
 int[] domain = hd.getDomain();
 assertEquals(5, domain[0]);
 assertEquals(10, domain[1]);
 }

 @Test
 public void testCumulativeProbability() {
 // Test cumulativeProbability with valid inputs
 double p = hd.cumulativeProbability(5);
 assertEquals(0.45, p, 0.01);

 // Test cumulativeProbability with invalid input (x < domain[0])
 p = hd.cumulativeProbability(0);
 assertEquals(0.0, p, 0.01);

 // Test cumulativeProbability with invalid input (x > domain[1])
 p = hd.cumulativeProbability(11);
 assertEquals(1.0, p, 0.01);
 }

 @Test
 public void testProbability() {
 // Test probability with valid input
 double p = hd.probability(5);
 assertEquals(0.27153525691335303, p, 0.0001);

 // Test probability with invalid input (x < domain[0])
 p = hd.probability(0);
 assertEquals(0.0, p, 0.01);

 // Test probability with invalid input (x > domain[1])
 p = hd.probability(11);
 assertEquals(0.0, p, 0.01);
 }

 @Test
 public void testUpperCumulativeProbability() {
 // Test upperCumulativeProbability with valid inputs
 double p = hd.upperCumulativeProbability(5);
 assertEquals(0.548464743086647, p, 0.01);

 // Test upperCumulativeProbability with invalid input (x <= domain[0])
 p = hd.upperCumulativeProbability(0);
 assertEquals(1.0, p, 0.01);

 // Test upperCumulativeProbability with invalid input (x > domain[1])
 p = hd.upperCumulativeProbability(11);
 assertEquals(0.0, p, 0.01);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 assertEquals(5, hd.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 assertEquals(10, hd.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 assertEquals(5, hd.getSampleSize());
 }

 @Test
 public void testGetNumericalMean() {
 assertEquals(5.0, hd.getNumericalMean(), 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 double variance = hd.getNumericalVariance();
 assertEquals(0.22196407406858633, variance, 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0, hd.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(5, hd.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(hd.isSupportConnected());
 }
}