import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 private int min;
 private int max;
 private float mean;
 private float stdDev;
 private Distance distance;

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
     min = 10;
     max = 20;
     mean = 15.5f;
     stdDev = 2.5f;
     distance = Distance.buildDistance(min, max, mean, stdDev);
     Distance expectedDistance = new Distance(min, max, mean, stdDev);
     assertEquals(expectedDistance.getMean(), distance.getMean(), 0.001f);
     assertEquals(expectedDistance.getStdDev(), distance.getStdDev(), 0.001f);
     assertEquals(expectedDistance.getMin(), distance.getMin());
     assertEquals(expectedDistance.getMax(), distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
     mean = 15.5f;
     stdDev = 2.5f;
     distance = Distance.buildDistance(mean, stdDev);
     int expectedMin = (int)(mean - calculateDelta(stdDev));
     int expectedMax = (int)(mean + calculateDelta(stdDev));
     distance = new Distance(expectedMin, expectedMax, mean, stdDev);
     assertEquals(expectedMin, distance.getMin());
     assertEquals(expectedMax, distance.getMax());
     assertEquals(mean, distance.getMean(), 0.001f);
     assertEquals(stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testCalculateDelta() {
     stdDev = 2.5f;
     float delta = Distance.calculateDelta(stdDev);
     assertEquals(3.0f * stdDev, delta, 0.001f);
 }

 @Test
 public void testComputeStandardDeviation() {
     min = 10;
     max = 20;
     stdDev = computeStandardDeviation(min, max);
     float expectedStdDev = (max - min) / 6.0f;
     assertEquals(expectedStdDev, stdDev, 0.001f);
 }

 @Test
 public void testComputeMean() {
     min = 10;
     max = 20;
     mean = Distance.computeMean(min, max);
     float expectedMean = min / 2.0f + max / 2.0f;
     assertEquals(expectedMean, mean, 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMin() {
     min = 5;
     max = 10;
     stdDev = 2.5f;
     try {
         Distance.buildDistance(min, max);
         fail("An IllegalArgumentException was expected.");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeStdDev() {
     min = 10;
     max = 20;
     stdDev = -2.5f;
     try {
         Distance.buildDistance(mean, stdDev);
         fail("An IllegalArgumentException was expected.");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
     min = 10;
     max = 20;
     mean = 15.5f;
     stdDev = 2.5f;
     distance = new Distance(min, max, mean, stdDev);
     Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
     float expectedMean = mean / MAX_ROUNDING_ERROR;
     Distance expectedCeleraDistance = new Distance(min, max, expectedMean, mean / (3 * MAX_ROUNDING_ERROR));
     assertEquals(expectedCeleraDistance.getMean(), celeraDistance.getMean(), 0.001f);
     assertEquals(expectedCeleraDistance.getStdDev(), celeraDistance.getStdDev(), 0.001f);
     assertEquals(expectedCeleraDistance.getMin(), celeraDistance.getMin());
     assertEquals(expectedCeleraDistance.getMax(), celeraDistance.getMax());
 }

 @Test
 public void testHashCode() {
     Distance distance1 = new Distance(10, 20, 15.5f, 2.5f);
     Distance distance2 = new Distance(10, 20, 15.5f, 2.5f);
     assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
     Distance distance1 = new Distance(10, 20, 15.5f, 2.5f);
     Distance distance2 = new Distance(10, 20, 15.5f, 2.5f);
     assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testToString() {
     min = 10;
     max = 20;
     mean = 15.5f;
     stdDev = 2.5f;
     distance = new Distance(min, max, mean, stdDev);
     String expectedToString = String.format("%.3f, std: %.3f", mean, stdDev);
     assertEquals(expectedToString, distance.toString());
 }
}