package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStdDev() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 assertNotNull(normalDist);
 assertEquals(5.0, normalDist.mean, 0.001);
 assertEquals(2.0, normalDist.standardDeviation, 0.001);
 }

 @Test
 public void testNormalDistributionImplConstructorDefault() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl();
 assertNotNull(normalDist);
 assertEquals(0.0, normalDist.mean, 0.001);
 assertEquals(1.0, normalDist.standardDeviation, 0.001);
 }

 @Test
 public void testGetMean() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 assertEquals(5.0, normalDist.getMean(), 0.001);
 }

 @Test
 public void testSetMean() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 normalDist.setMean(10.0);
 assertEquals(10.0, normalDist.getMean(), 0.001);
 }

 @Test
 public void testGetStandardDeviation() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 assertEquals(2.0, normalDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testSetStandardDeviation() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 normalDist.setStandardDeviation(3.0);
 assertEquals(3.0, normalDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testCumulativeProbabilityValidInput() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = normalDist.cumulativeProbability(3.75);
 assertEquals(0.6724, probability, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityEdgeCaseNegative() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = normalDist.cumulativeProbability(-10.0);
 fail("Expected a MathException to be thrown");
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityEdgeCasePositive() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = normalDist.cumulativeProbability(100.0);
 fail("Expected a MathException to be thrown");
 }

 @Test
 public void testInverseCumulativeProbabilityValidInput() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = 0.85;
 double expectedValue = normalDist.inverseCumulativeProbability(probability);
 assertEquals(2.2136, expectedValue, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityEdgeCaseZero() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = 0.0;
 double expectedValue = normalDist.inverseCumulativeProbability(probability);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityEdgeCaseOne() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = 1.0;
 double expectedValue = normalDist.inverseCumulativeProbability(probability);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityMaxIterationsExceeded() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
 double probability = 0.501; // Slightly above 0.5
 double expectedValue = normalDist.inverseCumulativeProbability(probability);
 fail("Expected a MaxIterationsExceededException to be thrown");
 }
}