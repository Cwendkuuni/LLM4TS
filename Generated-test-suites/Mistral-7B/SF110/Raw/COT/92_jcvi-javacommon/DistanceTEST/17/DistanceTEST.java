import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 // Test case for buildDistance(int min, int max, float mean, float stdDev)
 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
 assertEquals(7.5f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 assertEquals(5, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 // Test case for buildDistance(float mean, float stdDev)
 @Test
 public void testBuildDistanceWithMeanAndStdDev() {
 Distance distance = Distance.buildDistance(7.5f, 2.5f);
 assertEquals(7.5f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 }

 // Test case for buildDistance(int min, int max)
 @Test
 public void testBuildDistanceWithMinAndMax() {
 Distance distance = Distance.buildDistance(5, 10);
 float mean = Distance.computeMean(5, 10);
 float stdDev = Distance.computeStandardDeviation(5, 10);
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(5, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 // Test case for calculateDelta(float stdDev)
 @Test
 public void testCalculateDelta() {
 float stdDev = 2.5f;
 float calculatedDelta = Distance.calculateDelta(stdDev);
 assertEquals(7.5f, calculatedDelta, 0.01);
 }

 // Test case for computeStandardDeviation(int min, int max)
 @Test
 public void testComputeStandardDeviation() {
 int min = 1;
 int max = 10;
 float expectedStdDev = (max - min) / 6.0f;
 float computedStdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(expectedStdDev, computedStdDev, 0.01);
 }

 // Test case for computeMean(int min, int max)
 @Test
 public void testComputeMean() {
 int min = 1;
 int max = 10;
 float expectedMean = (min + max) / 2.0f;
 float computedMean = Distance.computeMean(min, max);
 assertEquals(expectedMean, computedMean, 0.01);
 }

 // Test case for transformIntoCeleraAssemblerDistance(Distance distance)
 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance originalDistance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
 assertNotSame(originalDistance, celeraDistance);
 float mean = celeraDistance.getMean();
 float stdDev = celeraDistance.getStdDev();
 assertEquals(7.5f, mean, 0.01);
 float correctedStdDev = (mean - 1.015f) / 3.0f;
 assertEquals(correctedStdDev, stdDev, 0.01);
 }
}