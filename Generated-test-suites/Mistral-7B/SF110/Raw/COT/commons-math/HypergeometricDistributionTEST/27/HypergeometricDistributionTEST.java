package org.apache.commons.math3.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;

 @org.junit.Before
 public void setUp() throws Exception {
 this.distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
 }

 @org.junit.Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativePopulationSize() {
 new HypergeometricDistribution(new Well19937c(), -1, 5, 10);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveNumberOfSuccesses() {
 new HypergeometricDistribution(new Well19937c(), 10, 0, 10);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNonPositiveSampleSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 0);
 }

 @org.junit.Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 11, 10);
 }

 @org.junit.Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeLargerThanPopulationSize() {
 new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @org.junit.Test
 public void testGetNumberOfSuccesses() {
 assertEquals(5, this.distribution.getNumberOfSuccesses());
 }

 @org.junit.Test
 public void testGetPopulationSize() {
 assertEquals(10, this.distribution.getPopulationSize());
 }

 @org.junit.Test
 public void testGetSampleSize() {
 assertEquals(10, this.distribution.getSampleSize());
 }

 @org.junit.Test
 public void testCumulativeProbability() {
 int[] domain = this.distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double cumulativeProbability = this.distribution.cumulativeProbability(x);
 double probability = this.distribution.probability(x);
 assertEquals(cumulativeProbability, cumulativeProbability + probability, 1e-10);
 }
 }

 @org.junit.Test(expected = IndexOutOfBoundsException.class)
 public void testCumulativeProbabilityWithInvalidIndex() {
 this.distribution.cumulativeProbability(-1);
 }

 @org.junit.Test
 public void testProbability() {
 int[] domain = this.distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double probability = this.distribution.probability(x);
 double cumulativeProbability = this.distribution.cumulativeProbability(x);
 assertEquals(probability, cumulativeProbability - this.distribution.cumulativeProbability(x - 1), 1e-10);
 }
 }

 @org.junit.Test(expected = IndexOutOfBoundsException.class)
 public void testProbabilityWithInvalidIndex() {
 this.distribution.probability(-1);
 }

 @org.junit.Test
 public void testUpperCumulativeProbability() {
 int[] domain = this.distribution.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double upperCumulativeProbability = this.distribution.upperCumulativeProbability(x);
 double probability = this.distribution.probability(x);
 assertEquals(1.0 - upperCumulativeProbability, probability, 1e-10);
 }
 }

 @org.junit.Test(expected = IndexOutOfBoundsException.class)
 public void testUpperCumulativeProbabilityWithInvalidIndex() {
 this.distribution.upperCumulativeProbability(-1);
 }

 @org.junit.Test
 public void testGetNumericalMean() {
 double numericalMean = this.distribution.getNumericalMean();
 assertEquals(5.0, numericalMean, 1e-10);
 }

 @org.junit.Test
 public void testGetNumericalVariance() {
 double numericalVariance = this.distribution.getNumericalVariance();
 double expectedVariance = (10.0 * 5.0 * (10.0 - 10.0) * (10.0 - 5.0)) / (10.0 * 10.0 * (10.0 - 1.0));
 assertEquals(expectedVariance, numericalVariance, 1e-10);
 }

 @org.junit.Test
 public void testGetSupportLowerBound() {
 int supportLowerBound = this.distribution.getSupportLowerBound();
 assertEquals(0, supportLowerBound);
 }

 @org.junit.Test
 public void testGetSupportUpperBound() {
 int supportUpperBound = this.distribution.getSupportUpperBound();
 assertEquals(5, supportUpperBound);
 }

 @org.junit.Test
 public void testIsSupportConnected() {
 assertTrue(this.distribution.isSupportConnected());
 }

}