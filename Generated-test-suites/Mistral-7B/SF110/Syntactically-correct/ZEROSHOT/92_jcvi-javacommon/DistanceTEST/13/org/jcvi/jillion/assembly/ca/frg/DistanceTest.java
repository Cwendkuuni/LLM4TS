package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMeanStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 7.5f);
 assertEquals(5, distance.getMin());
 assertEquals(12, distance.getMax());
 assertEquals(7.0f, distance.getMean(), 0.01);
 assertEquals(1.25f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(2, 8);
 assertEquals(2, distance.getMin());
 assertEquals(8, distance.getMax());
 assertEquals(5.0f, distance.getMean(), 0.01);
 assertEquals(1.3333333f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(12, 23, 16.5f, 3.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(12, celeraDistance.getMin());
 assertEquals(23, celeraDistance.getMax());
 assertEquals(15.2962963f, celeraDistance.getMean(), 0.01);
 assertEquals(2.1428571f, celeraDistance.getStdDev(), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceSameMean() {
 Distance distance = Distance.buildDistance(12, 23, 16.5f, 3.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(distance, celeraDistance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
 Distance distance = Distance.buildDistance(12, 23, 12.5f, 3.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 }

 @Test
 public void testHashCode() {
 Distance distance1 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals(distance1, distance2);
 }

 @Test
 public void testNotEquals() {
 Distance distance1 = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(10, 20, 15.5f, 2.6f);
 assertNotSame(distance1, distance2);
 }

 @Test
 public void testToString() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals("15.500, std: 2.500", distance.toString());
 }
}