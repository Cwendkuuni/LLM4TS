import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.0f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 Assert.assertEquals(mean, distance.getMean(), 0.01);
 Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
 Assert.assertEquals(min, distance.getMin());
 Assert.assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 15.0f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int) (mean - calculateDelta(stdDev));
 int expectedMax = (int) (mean + calculateDelta(stdDev));
 Assert.assertEquals(expectedMin, distance.getMin());
 Assert.assertEquals(expectedMax, distance.getMax());
 Assert.assertEquals(mean, distance.getMean(), 0.01);
 Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float expectedMean = computeMean(min, max);
 float expectedStdDev = computeStandardDeviation(min, max);
 Assert.assertEquals(expectedMean, distance.getMean(), 0.01);
 Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
 Assert.assertEquals(min, distance.getMin());
 Assert.assertEquals(max, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMin() {
 int min = -5;
 int max = 10;
 try {
 Distance.buildDistance(min, max);
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
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMean() {
 int min = 10;
 int max = 20;
 float mean = -3.5f;
 try {
 Distance.buildDistance(min, max, mean, 3.5f);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.0f;
 float stdDev = -3.5f;
 try {
 Distance.buildDistance(min, max, mean, stdDev);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(20, 30, 25.0f, 5.0f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 float expectedMean = (distance.getMean() - 1.015f) / 3.0f;
 Assert.assertEquals(expectedMean, celeraDistance.getMean(), 0.01);
 Assert.assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01);
 Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
 Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
 Distance distance = Distance.buildDistance(20, 30, 18.0f, 5.0f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 Assert.assertNotEquals(distance, celeraDistance);
 Assert.assertEquals(18.0f / 3.0f, celeraDistance.getMean(), 0.01);
 Assert.assertEquals(5.0f, celeraDistance.getStdDev(), 0.01);
 Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
 Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
 }

 private static float calculateDelta(final float stdDev) {
 return 3.0f * stdDev;
 }

 private static float computeMean(final int min, final int max) {
 return min / 2.0f + max / 2.0f;
 }
}