import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class DistanceTest {

 private int min;
 private int max;
 private float mean;
 private float stdDev;

 @Test
 public void testBuildDistance() {
 Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean());
 assertEquals(2.5f, distance.getStdDev());
 assertEquals(1, distance.getMin());
 assertEquals(10, distance.getMax());
 }

 @Test
 public void testBuildDistanceMeanStdDev() {
 Distance distance = Distance.buildDistance(5.5f, 2.5f);
 assertEquals(5.5f, distance.getMean(), 0.01);
 assertEquals(2.5f, distance.getStdDev(), 0.01);
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 2.5f;
 float delta = Distance.calculateDelta(stdDev);
 assertEquals(7.5f, delta, 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 Distance distance = Distance.buildDistance(1, 10);
 float mean = Distance.computeMean(1, 10);
 float stdDev = Distance.computeStandardDeviation(1, 10);
 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = new Distance(4, 8, 5.5f, 2.5f);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 assertEquals(5.395f, celeraDistance.getMean(), 0.01);
 assertEquals(1.667f, celeraDistance.getStdDev(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidMean() {
 Distance.buildDistance(-1, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceInvalidStdDev() {
 Distance.buildDistance(1, 10, -1, 10);
 }

 @Test
 public void testHashCode() {
 Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
 Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
 assertEquals(distance1.hashCode(), distance2.hashCode());
 }

 @Test
 public void testEquals() {
 Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
 Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
 assertTrue(distance1.equals(distance2));
 }

 @Test
 public void testNotEquals() {
 Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
 Distance distance2 = new Distance(1, 10, 5.5f, 2.6f);
 assertFalse(distance1.equals(distance2));
 }
}