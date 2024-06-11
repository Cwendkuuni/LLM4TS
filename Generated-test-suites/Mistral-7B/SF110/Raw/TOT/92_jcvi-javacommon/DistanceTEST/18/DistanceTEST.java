import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals("Mean should be equal", mean, distance.getMean(), 0.001f);
 assertEquals("Standard deviation should be equal", stdDev, distance.getStdDev(), 0.001f);
 assertEquals("Min should be equal", min, distance.getMin());
 assertEquals("Max should be equal", max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 12.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int) (mean - 3.0f * stdDev);
 int expectedMax = (int) (mean + 3.0f * stdDev);
 assertEquals("Min should be equal", expectedMin, distance.getMin());
 assertEquals("Max should be equal", expectedMax, distance.getMax());
 assertEquals("Mean should be equal", mean, distance.getMean(), 0.001f);
 assertEquals("Standard deviation should be equal", stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 int min = 5;
 int max = 15;
 Distance distance = Distance.buildDistance(min, max);
 float expectedMean = (min + max) / 2.0f;
 float expectedStdDev = (max - min) / 6.0f;
 assertEquals("Mean should be equal", expectedMean, distance.getMean(), 0.001f);
 assertEquals("Standard deviation should be equal", expectedStdDev, distance.getStdDev(), 0.001f);
 assertEquals("Min should be equal", min, distance.getMin());
 assertEquals("Max should be equal", max, distance.getMax());
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(10, 15, 12.5f, 2.5f);
 Distance expectedDistance = new Distance(10, 15, 11.265f, 1.24f);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals("Transformed mean should be equal", expectedDistance.getMean(), transformedDistance.getMean(), 0.001f);
 assertEquals("Transformed standard deviation should be equal", expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithNegativeStdDev() {
 Distance distance = new Distance(10, 15, 12.5f, -2.5f);
 try {
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 fail("Transformed distance should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}