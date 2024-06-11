import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.01f);
 assertEquals(3.5f, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceMeanStdDev() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f);
 assertEquals(12.5f, distance.getMean(), 0.01f);
 float delta = Distance.calculateDelta(3.5f);
 assertEquals(15.5f + delta, distance.getMean(), 0.01f);
 assertEquals(3.5f, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(10, 20);
 float mean = Distance.computeMean(10, 20);
 assertEquals(12.5f, distance.getMean(), 0.01f);
 assertEquals(15, distance.getMax());
 assertEquals(5, distance.getMin());
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(5, 15, 10, 3);
 float correctedMean = (10 - 1.015f) / 3.0f;
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(correctedMean, celeraDistance.getMean(), 0.01f);
 assertEquals(5, celeraDistance.getMin());
 assertEquals(15, celeraDistance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceNegativeValues() {
 Distance distance = Distance.buildDistance(-5, -10);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test
 public void testHashCode() {
 Distance distance1 = new Distance(5, 15, 10, 3);
 Distance distance2 = new Distance(5, 15, 10, 3);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = new Distance(5, 15, 10, 3);
 Distance distance2 = new Distance(5, 15, 10, 3);
 assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testToString() {
 Distance distance = new Distance(5, 15, 10, 3);
 assertEquals("10.000, std: 3.000", distance.toString());
 }
}