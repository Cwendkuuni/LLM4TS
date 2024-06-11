import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class DistanceTest {

 private static final float EPSILON = 0.001f;

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
 assertEquals(7.5f, distance.getMean(), EPSILON);
 assertEquals(2.5f, distance.getStdDev(), EPSILON);
 assertEquals(5, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @Test
 public void testBuildDistanceMeanStandardDeviation() {
 Distance distance = Distance.buildDistance(2.5f, 3.5f);
 assertEquals(3.0f, distance.getMean(), EPSILON);
 assertEquals(1.0f, distance.getStdDev(), EPSILON);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(1, 11);
 assertEquals(6.0f, distance.getMean(), EPSILON);
 assertEquals(5.5f, distance.getStdDev(), EPSILON);
 assertEquals(1, distance.getMin());
 assertEquals(11, distance.getMax());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidMeanAndStandardDeviation() {
 try {
 Distance.buildDistance(-1.0f, 1.0f);
 } catch (IllegalArgumentException e) {
 assertEquals("Both mean and standard deviation must be positive.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidStandardDeviation() {
 try {
 Distance.buildDistance(1.0f, 0.0f);
 } catch (IllegalArgumentException e) {
 assertEquals("Standard deviation must be positive.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testCalculateDelta() {
 assertEquals(9.0f, Distance.calculateDelta(3.0f), EPSILON);
 }

 @Test
 public void testComputeMean() {
 int min = 1;
 int max = 11;
 float expectedMean = (min + max) / 2.0f;
 Distance distance = new Distance(min, max);
 assertEquals(expectedMean, distance.getMean(), EPSILON);
 }

 @Test
 public void testComputeStandardDeviation() {
 int min = 1;
 int max = 11;
 float expectedStdDev = (max - min) / 6.0f;
 Distance distance = new Distance(min, max);
 assertEquals(expectedStdDev, distance.getStdDev(), EPSILON);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(3.5f, 7.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(4.015f, celeraDistance.getMean(), EPSILON);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistanceNoChange() {
 Distance distance = new Distance(5.5f, 8.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5.5f, celeraDistance.getMean(), EPSILON);
 }
}