import org.junit.Assert;import org.junit.Test;import org.junit.runner.RunWith;import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DistanceTest {

 private static final int MIN = 1;
 private static final int MAX = 10;
 private static final float MEAN = 5.5f;
 private static final float STD_DEV = 2.5f;

 @Test
 public void testBuildDistanceWithMinMaxMeanStdDev() {
 Distance distance = Distance.buildDistance(MIN, MAX, MEAN, STD_DEV);
 Assert.assertEquals(MEAN, distance.getMean(), 0.01f);
 Assert.assertEquals(STD_DEV, distance.getStdDev(), 0.01f);
 Assert.assertEquals(MIN, distance.getMin());
 Assert.assertEquals(MAX, distance.getMax());
 }

 @Test
 public void testBuildDistanceWithMeanStdDev() {
 Distance distance = Distance.buildDistance(MEAN, STD_DEV);
 int expectedMin = (int)(MEAN - 3.0f * STD_DEV);
 int expectedMax = (int)(MEAN + 3.0f * STD_DEV);
 Assert.assertEquals(expectedMin, distance.getMin());
 Assert.assertEquals(expectedMax, distance.getMax());
 Assert.assertEquals(MEAN, distance.getMean(), 0.01f);
 Assert.assertEquals(STD_DEV, distance.getStdDev(), 0.01f);
 }

 @Test
 public void testCalculateDelta() {
 Assert.assertEquals(9.0f, Distance.calculateDelta(STD_DEV), 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMin() {
 Distance.buildDistance(-1, MAX, MEAN, STD_DEV);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildDistanceWithNegativeMax() {
 Distance.buildDistance(MIN, -1, MEAN, STD_DEV);
 }

 @Test
 public void testComputeMean() {
 Assert.assertEquals(MEAN, Distance.computeMean(MIN, MAX), 0.01f);
 }

 @Test
 public void testComputeStandardDeviation() {
 Assert.assertEquals(STD_DEV, Distance.computeStandardDeviation(MIN, MAX), 0.01f);
 }

 @Test
 public void testTransformIntoCeleraAssemblerDistance() {
 Distance distance = Distance.buildDistance(MIN, MAX);
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 Assert.assertNotSame(distance, celeraDistance);
 Assert.assertEquals(MIN, celeraDistance.getMin());
 Assert.assertEquals(MAX, celeraDistance.getMax());
 Assert.assertEquals(MEAN / MAX_ROUNDING_ERROR, celeraDistance.getMean(), 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
 Distance distance = Distance.buildDistance(MIN, MAX);
 float mean = distance.getMean() - MAX_ROUNDING_ERROR;
 Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
 }
}