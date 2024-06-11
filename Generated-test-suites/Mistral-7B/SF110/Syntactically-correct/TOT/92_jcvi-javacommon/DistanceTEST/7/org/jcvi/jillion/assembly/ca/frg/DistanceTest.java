import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 float delta = Distance.calculateDelta(stdDev);
 int plusDelta = (int)(mean + delta);
 int minusDelta = (int)(mean - delta);
 int expectedMin = Math.min(plusDelta, minusDelta);
 int expectedMax = Math.max(plusDelta, minusDelta);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMin() {
 Distance.buildDistance(-5, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMax() {
 Distance.buildDistance(10, -5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithInvalidInput() {
 Distance distance = new Distance(-1, 1);
 Assert.assertNull(Distance.transformIntoCeleraAssemblerDistance(distance));
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithValidInput() {
 Distance distance = new Distance(10, 20);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
 assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
 assertNotEquals(distance.getStdDev(), celeraDistance.getStdDev());
 }
}