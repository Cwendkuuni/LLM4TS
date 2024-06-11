import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.001f);
 assertEquals(2.5f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceMeanAndStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 12.5f);
 assertEquals(5, distance.getMin());
 assertEquals(12, distance.getMax());
 assertEquals(8.0f, distance.getMean(), 0.001f);
 assertEquals(2.33333f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceMinAndMax() {
 Distance distance = Distance.buildDistance(1, 21);
 assertEquals(1, distance.getMin());
 assertEquals(21, distance.getMax());
 assertEquals(10.5f, distance.getMean(), 0.001f);
 assertEquals(3.5f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testCalculateDelta() {
 assertEquals(9.0f, Distance.calculateDelta(5.0f), 0.001f);
 }

 @Test
 public void testComputeStandardDeviation() {
 Distance distance = Distance.buildDistance(1, 21);
 assertEquals(3.5f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testComputeMean() {
 Distance distance = Distance.buildDistance(1, 21);
 assertEquals(10.5f, distance.getMean(), 0.001f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(5, 15);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5, transformedDistance.getMin());
 assertEquals(15, transformedDistance.getMax());
 assertEquals(4.7619f, transformedDistance.getMean(), 0.001f);
 assertEquals(1.33333f, transformedDistance.getStdDev(), 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidMeanAndStdDev() {
 Distance distance = null;
 try {
 distance = Distance.buildDistance(-1.2f, 2.5f);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNotNull(distance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceInvalidDistance() {
 Distance distance = Distance.buildDistance(5, 15);
 Distance transformedDistance = null;
 try {
 transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNotNull(transformedDistance);
 }
}