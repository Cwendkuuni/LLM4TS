import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

 // Test case for buildDistance(int min, int max, float mean, float stdDev) method
 @Test
 public void testBuildDistance() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals("Incorrect mean", mean, distance.getMean(), 0.01);
 assertEquals("Incorrect standard deviation", stdDev, distance.getStdDev(), 0.01);
 assertEquals("Incorrect minimum", min, distance.getMin());
 assertEquals("Incorrect maximum", max, distance.getMax());
 }

 // Test case for buildDistance(float mean, float stdDev) method
 @Test
 public void testBuildDistanceMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 float delta = Distance.calculateDelta(stdDev);
 int expectedMin = (int)(mean - delta);
 int expectedMax = (int)(mean + delta);
 assertEquals("Incorrect minimum", expectedMin, distance.getMin());
 assertEquals("Incorrect maximum", expectedMax, distance.getMax());
 assertEquals("Incorrect mean", mean, distance.getMean(), 0.01);
 assertEquals("Incorrect standard deviation", stdDev, distance.getStdDev(), 0.01);
 }

 // Test case for buildDistance(int min, int max) method
 @Test
 public void testBuildDistanceMinAndMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = Distance.computeMean(min, max);
 float stdDev = Distance.computeStandardDeviation(min, max);
 assertEquals("Incorrect mean", mean, distance.getMean(), 0.01);
 assertEquals("Incorrect standard deviation", stdDev, distance.getStdDev(), 0.01);
 assertEquals("Incorrect minimum", min, distance.getMin());
 assertEquals("Incorrect maximum", max, distance.getMax());
 }

 // Test case for transformIntoCeleraAssemblerDistance(Distance distance) method with valid input
 @Test
 public void testTransformIntoCeleraAssemblerDistanceValidInput() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance = Distance.buildDistance(min, max, mean, stdDev);
 assertNotNull("transformIntoCeleraAssemblerDistance returned null", Distance.transformIntoCeleraAssemblerDistance(distance));
 }

 // Test case for transformIntoCeleraAssemblerDistance(Distance distance) method with invalid input (mean < calculateDelta(stdDev))
 @Test (expected = AssertionError.class)
 public void testTransformIntoCeleraAssemblerDistanceInvalidInput() {
 int min = 10;
 int max = 20;
 float mean = 12.0f;
 float stdDev = 4.5f;
 Distance distance = Distance.buildDistance(min, max);
 Distance invalidDistance = new Distance(min, max, mean, stdDev);
 Distance.transformIntoCeleraAssemblerDistance(invalidDistance);
 }

 // Test case for hashCode() and equals() methods
 @Test
 public void testHashCodeAndEquals() {
 int min = 10;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.5f;
 Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
 Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
 assertEquals("Hash codes don't match", distance1.hashCode(), distance2.hashCode());
 assertTrue("Objects are not equal", distance1.equals(distance2));
 }

 // Test case for hashCode() method with different values
 @Test
 public void testHashCodeDifferentValues() {
 int min1 = 10;
 int max1 = 20;
 float mean1 = 15.5f;
 float stdDev1 = 3.5f;
 int min2 = 20;
 int max2 = 30;
 float mean2 = 25.5f;
 float stdDev2 = 5.5f;
 Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
 Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);
 assertNotEquals("Hash codes should not match", distance1.hashCode(), distance2.hashCode());
 }
}