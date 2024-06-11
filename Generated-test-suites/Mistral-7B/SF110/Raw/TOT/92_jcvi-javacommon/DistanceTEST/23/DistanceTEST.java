import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 // Test for buildDistance method with valid input
 @Test
 public void testBuildDistanceWithValidInput() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 // Test for buildDistance method with mean and stdDev only
 @Test
 public void testBuildDistanceWithMeanAndStdDevOnly() {
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 float delta = Distance.calculateDelta(stdDev);
 int expectedMin = (int)(mean - delta);
 int expectedMax = (int)(mean + delta);
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 }

 // Test for buildDistance method with min and max only
 @Test
 public void testBuildDistanceWithMinAndMaxOnly() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals(mean, distance.getMean(), 0.01f);
 assertEquals(stdDev, distance.getStdDev(), 0.01f);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 // Test for transformIntoCeleraAssemblerDistance method with valid input
 @Test
 public void testTransformIntoCeleraAssemblerDistanceWithValidInput() {
 Distance distance = new Distance(5, 15, 10.5f, 3.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 float expectedMean = 9.765f; // Since mean - 1.015 * 3 = 9.765
 assertEquals(expectedMean, celeraDistance.getMean(), 0.01f);
 assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
 assertEquals(distance.getMin(), celeraDistance.getMin());
 assertEquals(distance.getMax(), celeraDistance.getMax());
 }

 // Test for buildDistance method with invalid input (mean < stdDev)
 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithMeanLessThanStdDev() {
 float mean = 5.0f;
 float stdDev = 6.0f;
 Distance.buildDistance(mean, stdDev); // This should throw an IllegalArgumentException
 }

 // Test for calculateDelta method with negative stdDev
 @Test(expected = IllegalArgumentException.class)
 public void testCalculateDeltaWithNegativeStdDev() {
 float stdDev = -3.5f;
 float delta = Distance.calculateDelta(stdDev);
 fail(" calculateDeltaWithNegativeStdDev should throw an IllegalArgumentException");
 }
}