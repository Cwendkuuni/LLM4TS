package org.apache.commons.math3.distribution;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(0, 5, 10);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(10, -5, 10);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(10, 11, 10);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 fail("Expected NumberIsTooLargeException");
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new RandomGenerator(), 10, 5, 11);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // not expected
 fail("Expected NumberIsTooLargeException");
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int x = 2;
 double expectedProbability = dist.probability(x);
 double actualProbability = dist.probability(x);
 assertEquals(expectedProbability, actualProbability, 0.001);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int x = 2;
 double expectedCumulativeProbability = dist.cumulativeProbability(x);
 double actualCumulativeProbability = dist.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int x = 8;
 double expectedUpperCumulativeProbability = dist.upperCumulativeProbability(x);
 double actualUpperCumulativeProbability = dist.upperCumulativeProbability(x);
 assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.001);
 }

 @Test
 public void testHypergeometricDistributionGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 double expectedMean = 5.0;
 double actualMean = dist.getNumericalMean();
 assertEquals(expectedMean, actualMean, 0.001);
 }

 @Test
 public void testHypergeometricDistributionGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 double expectedVariance = 5.5555555555555555; // approximate value
 double actualVariance = dist.getNumericalVariance();
 assertEquals(expectedVariance, actualVariance, 0.0001);
 }

 @Test
 public void testHypergeometricDistributionGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int expectedLowerBound = 0;
 int actualLowerBound = dist.getSupportLowerBound();
 assertEquals(expectedLowerBound, actualLowerBound);
 }

 @Test
 public void testHypergeometricDistributionGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int expectedUpperBound = 5;
 int actualUpperBound = dist.getSupportUpperBound();
 assertEquals(expectedUpperBound, actualUpperBound);
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 boolean expectedIsSupportConnected = true;
 boolean actualIsSupportConnected = dist.isSupportConnected();
 assertEquals(expectedIsSupportConnected, actualIsSupportConnected);
 }

 @Test
 public void testHypergeometricDistributionSample() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
 int samplesSize = 1000;
 int[] samples = new int[samplesSize];
 for (int i = 0; i < samplesSize; i++) {
 samples[i] = dist.nextInt();
 }
 int expectedNumberOfSuccesses = (int) (samplesSize * 0.5);
 int actualNumberOfSuccesses = 0;
 for (int sample : samples) {
 if (sample >= 0 && sample <= 5) {
 actualNumberOfSuccesses++;
 }
 }
 int absDifference = Math.abs(expectedNumberOfSuccesses - actualNumberOfSuccesses);
 double relativeDifference = (double) absDifference / (double) expectedNumberOfSuccesses;
 assertTrue("Expected " + expectedNumberOfSuccesses + " successes, but got " + actualNumberOfSuccesses
 + ". Difference is " + absDifference + " (" + relativeDifference + ").",
 relativeDifference <= 0.05);
 }
}