import static org.junit.Assert.*;

import org.jcvi.jillion.assembly.ca.frg.Distance;

import org.junit.Test;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.01f);
 assertEquals(2.5f, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceMeanOnly() {
 Distance distance = Distance.buildDistance(15.5f, 2.5f);
 int min = (int)(15.5f - 3.0f * 2.5f);
 int max = (int)(15.5f + 3.0f * 2.5f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.01f);
 assertEquals(2.5f, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(5, 15);
 float mean = (float)(5.0f + 15.0f) / 2.0f;
 float stdDev = (15.0f - 5.0f) / 6.0f;
 assertEquals(5, distance.getMin());
 assertEquals(15, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 2.5f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals(7.5f, delta, 0.01f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(10, 20, 15.5f, 2.5f);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(10, transformedDistance.getMin());
 assertEquals(20, transformedDistance.getMax());
 assertEquals(14.455f, transformedDistance.getMean(), 0.01f);
 assertEquals(0.83333f, transformedDistance.getStdDev(), 0.01f);

 Distance distanceWithMeanBelowDelta = new Distance(10, 20, 12.0f, 2.5f);
 Distance transformedDistanceWithMeanBelowDelta = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowDelta);
 assertEquals(11, transformedDistanceWithMeanBelowDelta.getMin());
 assertEquals(19, transformedDistanceWithMeanBelowDelta.getMax());
 assertEquals(13.045f, transformedDistanceWithMeanBelowDelta.getMean(), 0.01f);
 assertEquals(0.83333f, transformedDistanceWithMeanBelowDelta.getStdDev(), 0.01f);
 }

 @Test
 public void testHashCodeEquals() {
 Distance distance1 = new Distance(10, 20, 15.5f, 2.5f);
 Distance distance2 = new Distance(10, 20, 15.5f, 2.5f);
 Distance distance3 = new Distance(11, 20, 15.5f, 2.5f);
 Distance distance4 = new Distance(10, 20, 15.51f, 2.5f);
 Distance distance5 = new Distance(10, 20, 15.5f, 2.499f);

 assertEquals(distance1.hashCode(), distance1.hashCode());
 assertEquals(distance1.hashCode(), distance2.hashCode());
 assertNotEquals(distance1.hashCode(), distance3.hashCode());
 assertNotEquals(distance1.hashCode(), distance4.hashCode());
 assertNotEquals(distance1.hashCode(), distance5.hashCode());

 assertTrue(distance1.equals(distance1));
 assertTrue(distance1.equals(distance2));
 assertFalse(distance1.equals(distance3));
 assertFalse(distance1.equals(distance4));
 assertFalse(distance1.equals(distance5));
 }

 @Test
 public void testToString() {
 Distance distance = new Distance(10, 20, 15.5f, 2.5f);
 assertEquals("15.500, std: 2.500", distance.toString());
 }
}