import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

 @Test
 public void testBuildDistanceWithMinMaxMeanAndStdDev() {
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
 public void testBuildDistanceWithMeanAndStdDev() {
 float mean = 15.5f;
 float stdDev = 3.2f;
 Distance distance = Distance.buildDistance(mean, stdDev);
 int expectedMin = (int)(mean - calculateDelta(stdDev));
 int expectedMax = (int)(mean + calculateDelta(stdDev));
 assertEquals(expectedMin, distance.getMin());
 assertEquals(expectedMax, distance.getMax());
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceWithMinMax() {
 int min = 10;
 int max = 20;
 Distance distance = Distance.buildDistance(min, max);
 float mean = (min + max) / 2.0f;
 float stdDev = (max - min) / 6.0f;
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 assertEquals(min, distance.getMin());
 assertEquals(max, distance.getMax());
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 3.2f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals(9.6f, delta, 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(5, 15, 10.5f, 3.2f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 float expectedMean = 10.185f;
 float expectedStdDev = 1.055f;
 assertEquals(expectedMean, celeraDistance.getMean(), 0.01);
 assertEquals(expectedStdDev, celeraDistance.getStdDev(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMin() {
 int min = -5;
 int max = 20;
 float mean = 15.5f;
 float stdDev = 3.2f;
 try {
 Distance.buildDistance(min, max, mean, stdDev);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMax() {
 int min = 10;
 int max = -5;
 float mean = 15.5f;
 float stdDev = 3.2f;
 try {
 Distance.buildDistance(min, max, mean, stdDev);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testHashCodeAndEquals() {
 Distance distance1 = new Distance(5, 15, 10.5f, 3.2f);
 Distance distance2 = new Distance(5, 15, 10.5f, 3.2f);
 Distance distance3 = new Distance(6, 15, 10.5f, 3.2f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 assertTrue(distance1.equals(distance2));
 assertFalse(distance1.equals(distance3));
 }
}