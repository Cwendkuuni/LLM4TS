import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanStdDev() {
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int)(mean - 3 * stdDev);
 int expectedMax = (int)(mean + 3 * stdDev);
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
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMin() {
 int min = -5;
 int max = 10;
 try {
 Distance.buildDistance(min, max);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMax() {
 int min = 10;
 int max = -5;
 try {
 Distance.buildDistance(min, max);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(5, 10, 7.5f, 2.5f);
 Distance expectedResult = new Distance(5, 10, 6.315f, 0.83333f);
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expectedResult.getMean(), result.getMean(), 0.01);
 assertEquals(expectedResult.getStdDev(), result.getStdDev(), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance_meanBelowDelta() {
 Distance distance = new Distance(5, 10, 4.5f, 2.5f);
 assertNull(Distance.transformIntoCeleraAssemblerDistance(distance));
 }
}