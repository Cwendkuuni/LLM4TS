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
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int) (mean - 3.0f * stdDev);
 int expectedMax = (int) (mean + 3.0f * stdDev);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float expectedMean = (min + max) / 2.0f;
 float expectedStdDev = (max - min) / 6.0f;
 assertEquals(expectedMean, distance.getMean(), 0.01);
 assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMin() {
 int min = -5;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 try {
 Distance.buildDistance(min, max, mean, stdDev);
 } catch (IllegalArgumentException e) {
 // expected exception
 assertEquals("The minimum value should be greater than or equal to the maximum value.", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = -3.2f;
 try {
 Distance.buildDistance(min, max, mean, stdDev);
 } catch (IllegalArgumentException e) {
 // expected exception
 assertEquals("The standard deviation should be a positive value.", e.getMessage());
 }
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.2f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01);
 assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01);
 assertEquals(distance.getMin(), celeraDistance.getMin());
 assertEquals(distance.getMax(), celeraDistance.getMax());
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
 Distance distance = new Distance(10, 20, 10.5f, 3.2f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertNotEquals(distance, celeraDistance);
 assertEquals(10.5f, celeraDistance.getMean(), 0.01);
 assertEquals(1.05f, celeraDistance.getStdDev(), 0.01);
 assertEquals(10, celeraDistance.getMin());
 assertEquals(20, celeraDistance.getMax());
 }
}