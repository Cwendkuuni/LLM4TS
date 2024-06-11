import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int) (mean - 3 * stdDev);
 int expectedMax = (int) (mean + 3 * stdDev);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceWithMinAndMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float expectedMean = (min + max) / 2.0f;
 float expectedStdDev = (max - min) / 6.0f;
 assertEquals(expectedMean, distance.getMean(), 0.001f);
 assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(10, 20, 15.5f, 2.5f);
 Distance expectedResult = new Distance(10, 20, 14.325f, 0.84375f); // Calculate expected result using the formula in the method
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expectedResult.getMean(), result.getMean(), 0.001f);
 assertEquals(expectedResult.getStdDev(), result.getStdDev(), 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithNegativeMean() {
 Distance distance = new Distance(10, 20, -5.5f, 2.5f);
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 fail("Expected an IllegalArgumentException");
 }
}