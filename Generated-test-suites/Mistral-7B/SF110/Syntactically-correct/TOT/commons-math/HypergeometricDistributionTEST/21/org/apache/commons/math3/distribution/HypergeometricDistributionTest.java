package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testInvalidPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), -5, 1, 1);
 }

 @Test(expected = NotPositiveException.class)
 public void testInvalidNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, -3, 1);
 }

 @Test(expected = NotPositiveException.class)
 public void testInvalidSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, -1);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 6, 2);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
 assertEquals(0.375, dist.cumulativeProbability(3), 0.001);
 assertEquals(0.625, dist.cumulativeProbability(5), 0.001);
 assertEquals(1.0, dist.cumulativeProbability(7), 0.001);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.192, dist.probability(0), 0.001);
 assertEquals(0.235, dist.probability(1), 0.001);
 assertEquals(0.235, dist.probability(3), 0.001);
 assertEquals(0.192, dist.probability(4), 0.001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.625, dist.upperCumulativeProbability(5), 0.001);
 assertEquals(0.808, dist.upperCumulativeProbability(7), 0.001);
 assertEquals(1.0, dist.upperCumulativeProbability(10), 0.001);
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
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(-2, dist.getSupportLowerBound());
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
 assertEquals(3.5, dist.getNumericalMean(), 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(2.25, dist.getNumericalVariance(), 0.001);
 }
}