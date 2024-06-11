import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @org.junit.Test
 public void testBuildDistance() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance expectedDistance = Distance.buildDistance(min, max, mean, stdDev);
 Distance actualDistance = Distance.buildDistance(mean, stdDev);
 assertEquals(expectedDistance.getMin(), actualDistance.getMin());
 assertEquals(expectedDistance.getMax(), actualDistance.getMax());
 assertEquals(expectedDistance.getMean(), actualDistance.getMean(), 0.001f);
 assertEquals(expectedDistance.getStdDev(), actualDistance.getStdDev(), 0.001f);
 }

 @org.junit.Test
 public void testBuildDistanceWithMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 Distance expectedDistance = Distance.buildDistance(mean, stdDev);
 assertEquals(distance.getMin(), expectedDistance.getMin());
 assertEquals(distance.getMax(), expectedDistance.getMax());
 assertEquals(distance.getMean(), expectedDistance.getMean(), 0.001f);
 assertEquals(distance.getStdDev(), expectedDistance.getStdDev(), 0.001f);
 }

 @org.junit.Test
 public void testCalculateDelta() {
 float stdDev = 3.2f;
 float expectedDelta = 9.6f;
 float actualDelta = Distance.calculateDelta(stdDev);
 assertEquals(expectedDelta, actualDelta, 0.001f);
 }

 @org.junit.Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(10, 20);
 float expectedMean = 11.5f;
 float expectedStdDev = 2.5f;
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(expectedMean, celeraDistance.getMean(), 0.001f);
 assertEquals(expectedStdDev, celeraDistance.getStdDev(), 0.001f);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithInvalidMean() {
 Distance distance = Distance.buildDistance(10, 20);
 float mean = distance.getMean() - Distance.MAX_ROUNDING_ERROR;
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 fail("IllegalArgumentException expected");
 }
}