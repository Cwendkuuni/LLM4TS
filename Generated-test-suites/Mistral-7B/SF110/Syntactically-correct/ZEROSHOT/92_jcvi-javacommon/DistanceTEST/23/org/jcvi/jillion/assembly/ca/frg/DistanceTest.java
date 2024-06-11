import org.junit.Test;
import static org.junit.Assert.*;

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
 Distance distance = Distance.buildDistance(5.5f, 11.5f);
 assertEquals(5, distance.getMin());
 assertEquals(12, distance.getMax());
 assertEquals(8.0f, distance.getMean(), 0.01);
 assertEquals(1.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(5, 15);
 assertEquals(5, distance.getMin());
 assertEquals(15, distance.getMax());
 assertEquals(10.0f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testCalculateDelta() {
 assertEquals(9.0f, Distance.calculateDelta(3.0f), 0.01);
 }

 @Test
 public void testComputeMean() {
 assertEquals(12.5f, Distance.computeMean(3, 15), 0.01);
 }

 @Test
 public void testComputeStandardDeviation() {
 assertEquals(1.0f, Distance.computeStandardDeviation(3, 15), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(5, 15, 8.0f, 1.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5, celeraDistance.getMin());
 assertEquals(15, celeraDistance.getMax());
 assertEquals(7.365f, celeraDistance.getMean(), 0.01); // calculated as (8.0 - 1.015) / 3.0
 assertEquals(0.805f, celeraDistance.getStdDev(), 0.01); // calculated as (8.0 - 1.015) / 3.0
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceNegativeMin() {
 Distance.buildDistance(-5, 10);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceNegativeMax() {
 Distance.buildDistance(5, -10);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testHashCode() {
 Distance distance1 = new Distance(5, 15, 8.0f, 1.5f);
 Distance distance2 = new Distance(5, 15, 8.0f, 1.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = new Distance(5, 15, 8.0f, 1.5f);
 Distance distance2 = new Distance(5, 15, 8.0f, 1.5f);
 assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testToString() {
 Distance distance = new Distance(5, 15, 8.0f, 1.5f);
 assertEquals("8.000, std: 1.500", distance.toString());
 }
}