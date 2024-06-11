import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @org.junit.Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean(), 0.01f);
 assertEquals(2.5f, distance.getStdDev(), 0.01f);
 assertEquals(1, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @org.junit.Test
 public void testBuildDistanceMeanAndStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean(), 0.01f);
 assertEquals(2.5f, distance.getStdDev(), 0.01f);
 }

 @org.junit.Test
 public void testBuildDistanceMinAndMax() {
 Distance distance = Distance.buildDistance(1, 10);
 float mean = Distance.computeMean(1, 10);
 float stdDev = Distance.computeStandardDeviation(1, 10);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 assertEquals(1, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @org.junit.Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(5.0f, 12.0f);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5.385f, transformedDistance.getMean(), 0.01f);
 assertEquals(1.25f, transformedDistance.getStdDev(), 0.01f);
 }

 @org.junit.Test
 public void testTransformIntoCeleraAssemblerDistanceWithNegativeStdDev() {
 Distance distance = Distance.buildDistance(5.0f, 1.0f);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertNull(transformedDistance);
 }

 @org.junit.Test
 public void testHashCode() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @org.junit.Test
 public void testEquals() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertTrue(distance1.equals(distance2));
 }

 @org.junit.Test
 public void testNotEquals() {
 Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 3.5f);
 assertFalse(distance1.equals(distance2));
 }

 @org.junit.Test
 public void testToString() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals("5.500, std: 2.500", distance.toString());
 }
}