import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DistanceTest {

 @Test
 public void testBuildDistance() {
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
 public void testBuildDistanceMeanStdDev() {
 float mean = 15.5f;
 float stdDev = 3.2f;

 Distance distance = Distance.buildDistance(mean, stdDev);

 assertEquals(mean, distance.getMean(), 0.01);
 assertEquals(stdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testBuildDistanceMinMax() {
 int min = 10;
 int max = 20;

 Distance distance = Distance.buildDistance(min, max);

 float computedMean = Distance.computeMean(min, max);
 float computedStdDev = Distance.computeStandardDeviation(min, max);

 assertEquals(computedMean, distance.getMean(), 0.01);
 assertEquals(computedStdDev, distance.getStdDev(), 0.01);
 }

 @Test
 public void testCalculateDelta() {
 float stdDev = 3.2f;

 float delta = Distance.calculateDelta(stdDev);

 assertEquals(9.6f, delta, 0.01);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 float mean = 15.5f;
 float stdDev = 3.2f;
 int min = 10;
 int max = 20;

 Distance distance = Distance.buildDistance(min, max, mean, stdDev);

 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

 float correctedStdDev = (mean - 1.015f) / 3.0f;

 assertEquals(mean, celeraDistance.getMean(), 0.01);
 assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
 assertNotNull(celeraDistance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithInvalidMean() {
 float mean = 10.5f;
 float stdDev = 3.2f;
 int min = 10;
 int max = 20;

 Distance distance = Distance.buildDistance(min, max, mean, stdDev);

 assertNull(Distance.transformIntoCeleraAssemblerDistance(distance));
 }
}