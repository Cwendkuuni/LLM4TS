import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanStdDev() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
 assertEquals(10, distance.getMin());
 assertEquals(20, distance.getMax());
 assertEquals(15.5f, distance.getMean(), 0.001f);
 assertEquals(3.5f, distance.getStdDev(), 0.001f);
 }

 @Test
 public void testBuildDistanceWithMeanStdDev() {
 Distance distance = Distance.buildDistance(15.5f, 3.5f);
 int min = 12;
 int max = 19;
 Distance expected = Distance.buildDistance(min, max);
 Distance result = Distance.buildDistance(min, max);
 result = Distance.transformIntoCeleraAssemblerDistance(result);
 assertEquals(expected, result);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 Distance distance = Distance.buildDistance(10, 20);
 float mean = 15.5f;
 float stdDev = Distance.computeStandardDeviation(10, 20) / 2.0f;
 Distance expected = Distance.buildDistance(mean, stdDev);
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMin() {
 Distance.buildDistance(-1, 2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithInvalidMax() {
 Distance.buildDistance(1, -2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeStdDev() {
 Distance.buildDistance(10, 20, 15.5f, -3.5f);
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 3.5f;
 float expected = 3.0f * stdDev;
 float result = Distance.calculateDelta(stdDev);
 assertEquals(expected, result, 0.001f);
 }

 @Test
 public void testComputeMean() {
 int min = 10;
 int max = 20;
 float expected = (min + max) / 2.0f;
 float result = Distance.computeMean(min, max);
 assertEquals(expected, result, 0.001f);
 }

 @Test
 public void testComputeStandardDeviation() {
 int min = 10;
 int max = 20;
 float expected = (max - min) / 6.0f;
 float result = Distance.computeStandardDeviation(min, max);
 assertEquals(expected, result, 0.001f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(15.5f, 3.5f);
 float mean = distance.getMean();
 float stdDev = distance.getStdDev();
 if (mean < Distance.calculateDelta(stdDev)) {
 Distance expected = new Distance((int)(mean - (mean - 1.015f) / 3.0f), (int)(mean + (mean - 1.015f) / 3.0f), mean, stdDev / 3.0f);
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expected, result);
 } else {
 Distance expected = distance;
 Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expected, result);
 }
 }

}