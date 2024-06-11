package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 // Test for buildDistance method with valid arguments
 @Test
 public void testBuildDistanceWithValidArguments() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(mean, distance.getMean(), 0.001);
 assertEquals(stdDev, distance.getStdDev(), 0.001);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 // Test for buildDistance method with mean and standard deviation only
 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 10.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int)(mean - 3.0f * stdDev);
 int expectedMax = (int)(mean + 3.0f * stdDev);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.001);
 assertEquals(stdDev, distance.getStdDev(), 0.001);
 }

 // Test for buildDistance method with minimum and maximum only
 @Test
 public void testBuildDistanceWithMinAndMax() {
 int min = 5;
 int max = 15;
 Distance distance = Distance.buildDistance(min, max);
 float mean = computeMean(min, max);
 float stdDev = computeStandardDeviation(min, max);
 assertEquals(mean, distance.getMean(), 0.001);
 assertEquals(stdDev, distance.getStdDev(), 0.001);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 // Test for calculateDelta method
 @Test
 public void testCalculateDelta() {
 float stdDev = 5.0f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals(15.0f, delta, 0.001);
 }

 // Test for computeMean method
 @Test
 public void testComputeMean() {
 int min = 1;
 int max = 10;
 float mean = Distance.computeMean(min, max);
 assertEquals(5.5f, mean, 0.001);
 }

 // Test for computeStandardDeviation method
 @Test
 public void testComputeStandardDeviation() {
 int min = 1;
 int max = 10;
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(1.6666667f, stdDev, 0.001);
 }

 // Test for transformIntoCeleraAssemblerDistance method with valid distance
 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
 float mean = 12.5f;
 float stdDev = 3.5f;
 Distance distance = new Distance(10, 15, mean, stdDev);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(mean, celeraDistance.getMean(), 0.001);
 assertEquals(stdDev, celeraDistance.getStdDev(), 0.001);
 assertEquals(10, celeraDistance.getMin());
 assertEquals(15, celeraDistance.getMax());
 }

 // Test for transformIntoCeleraAssemblerDistance method with incorrect standard deviation
 @Test (expected = AssertionError.class)
 public void testTransformIntoCeleraAssemblerDistanceWithIncorrectStandardDeviation() {
 float mean = 12.5f;
 float stdDev = 3.5f;
 Distance distance = new Distance(10, 15, mean, stdDev);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 float incorrectStdDev = 2.5f;
 Distance incorrectDistance = new Distance(distance.getMin(), distance.getMax(), distance.getMean(), incorrectStdDev);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(incorrectDistance);
 assertEquals(mean, transformedDistance.getMean(), 0.001);
 assertEquals(incorrectStdDev, transformedDistance.getStdDev(), 0.001);
 }

 // Test for hashCode and equals methods
 @Test
 public void testHashCodeAndEquals() {
 int min = 1;
 int max = 10;
 float mean = 5.5f;
 float stdDev = 1.6666667f;
 Distance distance1 = new Distance(min, max, mean, stdDev);
 Distance distance2 = new Distance(min, max, mean, stdDev);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 assertTrue(distance1.equals(distance2));
 }
}