import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean(), 0.01f);
 assertEquals(2.5f, distance.getStdDev(), 0.01f);
 assertEquals(1, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 2.5f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals(7.5f, delta, 0.01f);
 }

 @Test
 public void testBuildDistanceFromMeanAndStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean(), 0.01f);
 assertEquals(7.5f, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceFromMinMax() {
 Distance distance = Distance.buildDistance(1, 10);
 float mean = Distance.computeMean(1, 10);
 float stdDev = Distance.computeStandardDeviation(1, 10);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5.5f, celeraDistance.getMean(), 0.01f);
 assertNotEquals(2.5f, celeraDistance.getStdDev(), 0.01f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithCorrectedMean() {
 Distance distance = Distance.buildDistance(1, 10, 1.5f, 3.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(1.5f, celeraDistance.getMean(), 0.01f);
 assertEquals(1.042f, celeraDistance.getStdDev(), 0.01f);
 }

 @Test
 public void testHashCode() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testNotEquals() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Object obj = new Object();
 assertFalse(distance1.equals(obj));
 }

 @Test
 public void testToString() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals("5.500, std: 2.500", distance.toString());
 }
}