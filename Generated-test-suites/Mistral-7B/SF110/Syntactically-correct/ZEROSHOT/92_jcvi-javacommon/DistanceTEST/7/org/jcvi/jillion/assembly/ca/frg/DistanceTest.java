import org.junit.Test;
import org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
 int min = 10;
 int max = 20;
 float mean = 15.0f;
 float stdDev = 3.5f;

 Distance distance = Distance.buildDistance(min, max, mean, stdDev);

 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01); // allow for rounding error
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMeanOnly() {
 float mean = 15.0f;
 float stdDev = 3.5f;

 Distance distance = Distance.buildDistance(mean, stdDev);
 int minExpected = (int)(mean - calculateDelta(stdDev));
 int maxExpected = (int)(mean + calculateDelta(stdDev));

 assertEquals(minExpected, distance.getMin());
 assertEquals(maxExpected, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 int min = 10;
 int max = 20;

 Distance distance = Distance.buildDistance(min, max);
 float mean = computeMean(min, max);
 float stdDev = computeStandardDeviation(min, max);

 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidMeanAndStdDev() {
 float mean = 15.0f;
 float stdDev = 3.5f;
 int min = mean - calculateDelta(stdDev) - 1;
 int max = mean + calculateDelta(stdDev) + 1;

 try {
 Distance.buildDistance(min, max);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(10, 20);
 float mean = distance.getMean();
 float stdDev = distance.getStdDev();

 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

 assertEquals(distance.getMin(), celeraDistance.getMin());
 assertEquals(distance.getMax(), celeraDistance.getMax());
 assertEquals(mean - MAX_ROUNDING_ERROR, celeraDistance.getMean(), 0.01); // mean is adjusted in the method
 assertEquals(stdDev / MAX_ROUNDING_ERROR, celeraDistance.getStdDev(), 0.01); // stdDev is adjusted in the method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceInvalidMean() {
 Distance distance = Distance.buildDistance(10, 20);
 float mean = distance.getMean() - calculateDelta(distance.getStdDev()) * 2;

 try {
 Distance.transformIntoCeleraAssemblerDistance(distance);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 private static float calculateDelta(float stdDev) {
 return 3.0f * stdDev;
 }

 private static float computeMean(int min, int max) {
 return min / 2.0f + max / 2.0f;
 }
}