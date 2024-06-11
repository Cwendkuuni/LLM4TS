import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
 assertEquals(5, distance.getMin());
 assertEquals(10, distance.getMax());
 assertEquals(7.5f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMeanStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 7.5f);
 assertEquals(5, distance.getMin());
 assertEquals(11, distance.getMax());
 assertEquals(6.5f, distance.getMean(), 0.01);
 assertEquals(1.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(1, 11);
 assertEquals(1, distance.getMin());
 assertEquals(11, distance.getMax());
 assertEquals(6.0f, distance.getMean(), 0.01);
 assertEquals(1.0f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(5, 10, 7.5f, 2.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5, celeraDistance.getMin());
 assertEquals(10, celeraDistance.getMax());
 assertEquals(5.443f, celeraDistance.getMean(), 0.01);
 assertEquals(0.833f, celeraDistance.getStdDev(), 0.01);
 }

 @Test
 public void testHashCode() {
 Distance distance1 = new Distance(5, 10, 7.5f, 2.5f);
 Distance distance2 = new Distance(5, 10, 7.5f, 2.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = new Distance(5, 10, 7.5f, 2.5f);
 Distance distance2 = new Distance(5, 10, 7.5f, 2.5f);
 assertEquals(distance1, distance2);
 }

 @Test
 public void testToString() {
 Distance distance = new Distance(5, 10, 7.5f, 2.5f);
 assertEquals("6.000, std: 2.500", distance.toString());
 }

 @Test
 public void testCalculateDelta() {
 assertEquals(9.0f, Distance.calculateDelta(2.5f), 0.01);
 }

 @Test
 public void testComputeMean() {
 assertEquals(6.0f, Distance.computeMean(1, 11), 0.01);
 }

 @Test
 public void testComputeStandardDeviation() {
 assertEquals(1.0f, Distance.computeStandardDeviation(1, 11), 0.01);
 }

 @Test
 public void testBuildDistanceWithInvalidInput() {
 assertNotNull(Distance.buildDistance(0, 0));
 }
}