import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.01);
 assertEquals(3.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMeanStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 7.5f);
 assertEquals(5, distance.getMin());
 assertEquals(8, distance.getMax());
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
 Distance distance = Distance.buildDistance(5, 15);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5, transformedDistance.getMin());
 assertEquals(15, transformedDistance.getMax());
 assertEquals(5.015f, transformedDistance.getMean(), 0.01);
 assertEquals(0.5f, transformedDistance.getStdDev(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
 Distance distance = Distance.buildDistance(5, 15);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(new Distance(5, 15, 4.5f, 3.5f));
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testHashCode() {
 Distance distance1 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
 Distance distance2 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
 Distance distance2 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
 assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 Distance distance1 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
 String string = "Hello World";
 assertFalse(distance1.equals(string));
 }
}