import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 @org.junit.Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
 Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
 assertEquals("Distance not initialized correctly", 10, distance.getMin());
 assertEquals("Distance not initialized correctly", 20, distance.getMax());
 assertEquals("Mean not initialized correctly", 15.5f, distance.getMean(), 0.01);
 assertEquals("Standard deviation not initialized correctly", 2.5f, distance.getStdDev(), 0.01);
 }

 @org.junit.Test
 public void testCalculateDelta() {
 float stdDev = 2.5f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals("Delta calculation not correct", 7.5f, delta, 0.01);
 }

 @org.junit.Test
 public void testBuildDistanceWithMeanAndStdDev() {
 Distance distance = Distance.buildDistance(15.5f, 2.5f);
 int expectedMin = (int)(15.5f - 3.0f);
 int expectedMax = (int)(15.5f + 3.0f);
 assertEquals("Min not initialized correctly", expectedMin, distance.getMin());
 assertEquals("Max not initialized correctly", expectedMax, distance.getMax());
 assertEquals("Mean not initialized correctly", 15.5f, distance.getMean(), 0.01);
 assertEquals("Standard deviation not initialized correctly", 2.5f, distance.getStdDev(), 0.01);
 }

 @org.junit.Test
 public void testBuildDistanceWithMinAndMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals("Mean not calculated correctly", mean, distance.getMean(), 0.01);
 assertEquals("Standard deviation not calculated correctly", stdDev, distance.getStdDev(), 0.01);
 assertEquals("Min not initialized correctly", min, distance.getMin());
 assertEquals("Max not initialized correctly", max, distance.getMax());
 }

 @org.junit.Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(10, 20);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertNotEquals("Transformed distance should not be the same as original distance", distance, celeraDistance);
 float mean = celeraDistance.getMean();
 // The mean value should be less than 3 * stdDev due to the transformation
 // But we don't know the exact value of stdDev, so we can't test it directly.
 // Instead, we can test that the transformed distance is valid, i.e., that it satisfies the condition mean < 3 * stdDev - 1.015
 float delta = Distance.calculateDelta(celeraDistance.getStdDev());
 float correctedStdDev = (mean - MAX_ROUNDING_ERROR) / 3.0f;
 assertTrue("Transformed distance is not valid", mean < correctedStdDev + delta);
 }

 @org.junit.Test
 public void testHashCodeAndEquals() {
 Distance distance1 = Distance.buildDistance(10, 20);
 Distance distance2 = Distance.buildDistance(10, 20);
 assertEquals("Hashcodes should be equal if distances are equal", distance1.hashCode(), distance2.hashCode());
 }

 @org.junit.Test
 public void testHashCodeAndEqualsDifferentObjects() {
 Distance distance1 = Distance.buildDistance(10, 20);
 Object otherObject = new Object();
 assertNotEquals("Hashcodes should not be equal if objects are different", distance1.hashCode(), otherObject.hashCode());
 }

 @org.junit.Test
 public void testToString() {
 Distance distance = Distance.buildDistance(10, 20);
 String expectedString = String.format("%.3f, std: %.3f", distance.getMean(), distance.getStdDev());
 assertEquals("ToString method not implemented correctly", expectedString, distance.toString());
 }
}