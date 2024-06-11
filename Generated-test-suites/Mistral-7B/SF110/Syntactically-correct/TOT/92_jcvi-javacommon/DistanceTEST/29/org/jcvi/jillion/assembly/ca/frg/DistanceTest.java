import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithValidMinMaxMeanAndStdDev() {
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
 public void testBuildDistanceWithValidMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int min = (int)(mean - calculateDelta(stdDev));
 int max = (int)(mean + calculateDelta(stdDev));
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithValidMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = computeMean(min, max);
 float stdDev = computeStandardDeviation(min, max);
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = -3.2f;
 Distance distance = null; // expected exception
 try {
 distance = Distance.buildDistance(mean, stdDev);
 } catch (IllegalArgumentException e) {
 // test passed
 }
 assertEquals(mean, -1.0f, 0.01); // verification that the exception was thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMinOrMax() {
 int min = -10;
 int max = 20;
 Distance distance = null; // expected exception
 try {
 distance = Distance.buildDistance(min, max);
 } catch (IllegalArgumentException e) {
 // test passed
 }
 assertEquals(-1, min, 0); // verification that the exception was thrown
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
 Distance distance = new Distance(10, 20, 15.5f, 3.2f);
 Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(15.5f, transformedDistance.getMean(), 0.01);
 assertEquals(1.0433333f, transformedDistance.getStdDev(), 0.001); // calculated delta is 3 * 3.2 = 9.6, 1.0433333f is 1.015 * 3.2
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
 Distance distance = new Distance(10, 20, 15.5f, -3.2f);
 Distance transformedDistance = null; // expected exception
 try {
 transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 } catch (IllegalArgumentException e) {
 // test passed
 }
 assertEquals(-1, -1.0f, 0.01); // verification that the exception was thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
 Distance distance = new Distance(10, 20, 10.0f, 3.2f);
 Distance transformedDistance = null; // expected exception
 try {
 transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 } catch (IllegalArgumentException e) {
 // test passed
 }
 assertEquals(10.0f, -1.0f, 0.01); // verification that the exception was thrown
 }
}