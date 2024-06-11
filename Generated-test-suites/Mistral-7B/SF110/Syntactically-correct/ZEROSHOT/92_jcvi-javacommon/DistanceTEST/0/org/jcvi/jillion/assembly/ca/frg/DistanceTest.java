import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @ org.junit.BeforeClass
 static void setupBeforeClass() {
 // You can initialize any static variables or perform any setup required by your tests here
 }

 @ org.junit.AfterClass
 static void tearDownAfterClass() {
 // You can perform any cleanup required by your tests here
 }

 @ org.junit.Before
 static void setup() {
 // You can perform any setup required by individual tests here
 }

 @ org.junit.After
 static void tearDown() {
 // You can perform any cleanup required by individual tests here
 }

 @Test
 public void testBuildDistance() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int min = (int)(mean - calculateDelta(stdDev)) > 0 ? (int)(mean - calculateDelta(stdDev)) : (int)(mean + calculateDelta(stdDev));
 int max = (int)(mean + calculateDelta(stdDev));
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceMinAndMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = computeMean(min, max);
 float stdDev = computeStandardDeviation(min, max);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.001f);
 assertEquals(stdDev, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(5, 15);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 float mean = distance.getMean();
 float stdDev = distance.getStdDev();
 if (mean < calculateDelta(stdDev)) {
 float correctedStdDev = (mean - 1.015f) / 3.0f;
 assertEquals(5, transformedDistance.getMin());
 assertEquals(15, transformedDistance.getMax());
 assertEquals(mean, transformedDistance.getMean(), 0.001f);
 assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001f);
 } else {
 assertEquals(distance, transformedDistance);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceNegativeMinOrMax() {
 int min = -5;
 int max = 20;
 assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(min, max));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceNegativeMeanOrStdDev() {
 float mean = -5.5f;
 float stdDev = 3.2f;
 assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(mean, stdDev));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceInvalidDistance() {
 Distance invalidDistance = new Distance(-5, 15, 10, -3.2f);
 assertThrows(IllegalArgumentException.class, () -> Distance.transformIntoCeleraAssemblerDistance(invalidDistance));
 }

 private static float calculateDelta(float stdDev) {
 return 3.0f * stdDev;
 }

 private static float computeMean(int min, int max) {
 return min / 2.0f + max / 2.0f;
 }

 private static float computeStandardDeviation(int min, int max) {
 return (max - min) / 6.0f;
 }
}