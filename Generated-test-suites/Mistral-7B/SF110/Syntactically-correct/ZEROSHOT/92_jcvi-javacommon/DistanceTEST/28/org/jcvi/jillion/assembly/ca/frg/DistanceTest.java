import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @org.junit.Test
 public void testBuildDistance() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f); // allow for rounding error
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @org.junit.Test
 public void testBuildDistanceMeanStdDev() {
 float mean = 15.5f;
 float stdDev = 2.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int) (mean - 3.0f * stdDev);
 int expectedMax = (int) (mean + 3.0f * stdDev);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @org.junit.Test
 public void testBuildDistanceMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 @org.junit.Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(5, 15, 10.5f, 2.5f);
 float correctedStdDev = 1.667f; // calculated according to the logic in the method
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(distance.getMin(), celeraDistance.getMin());
 assertEquals(distance.getMax(), celeraDistance.getMax());
 assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
 assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceInvalidMean() {
 Distance distance = Distance.buildDistance(5, 15, 5.5f, 2.5f);
 Distance.transformIntoCeleraAssemblerDistance(distance); // should throw an exception
 }
}