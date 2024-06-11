package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
 assertEquals(5.5f, distance.getMean(), 0.001f);
 assertEquals(1.5f, distance.getStdDev(), 0.001f);
 assertEquals(1, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @Test
 public void testBuildDistanceMeanAndStdDev() {
 Distance distance = Distance.buildDistance(4.5f, 2.5f);
 assertEquals(3.5f, distance.getMean(), 0.001f);
 assertEquals(2.5f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceMinAndMax() {
 Distance distance = Distance.buildDistance(5, 15);
 float mean = Distance.computeMean(5, 15);
 float stdDev = Distance.computeStandardDeviation(5, 15);
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 assertEquals(5, distance.getMin());
 assertEquals(15, distance.getMax());
 }

 @Test
 public void testCalculateDelta() {
 assertEquals(3.0f, Distance.calculateDelta(1.5f), 0.001f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5.5f, celeraDistance.getMean(), 0.001f);
 assertEquals(1.125f, celeraDistance.getStdDev(), 0.001f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
 Distance distance = Distance.buildDistance(1, 10, -5.5f, 1.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertNotEquals(distance.getMean(), celeraDistance.getMean());
 assertEquals(-5.545f, celeraDistance.getMean(), 0.001f);
 assertEquals(1.125f, celeraDistance.getStdDev(), 0.001f);
 }

 @Test
 public void testHashCodeAndEquals() {
 Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
 Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 assertTrue(distance1.equals(distance2));

 Distance distance3 = new Distance(1, 10, 5.6f, 1.5f);
 assertNotEquals(distance1.hashCode(), distance3.hashCode());
 assertFalse(distance1.equals(distance3));

 Distance distance4 = new Distance(1, 11, 5.5f, 1.5f);
 assertNotEquals(distance1.hashCode(), distance4.hashCode());
 assertFalse(distance1.equals(distance4));
 }

 @Test
 public void testToString() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
 assertEquals("5.500, std: 1.500", distance.toString());
 }
}