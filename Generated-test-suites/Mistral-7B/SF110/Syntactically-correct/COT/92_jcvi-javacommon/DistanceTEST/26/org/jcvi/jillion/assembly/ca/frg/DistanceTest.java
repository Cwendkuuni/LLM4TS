import org.junit.Test;
import org.junit.Assert;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntMinIntMaxFloatMeanFloatStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithFloatMeanFloatStdDev() {
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - 3 * stdDev);
        int expectedMax = (int) (mean + 3 * stdDev);
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithIntMinIntMax() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -1;
        int max = 10;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMax() {
        int min = 1;
        int max = -1;
        Distance.buildDistance(min, max);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float expectedDelta = 3.0f * stdDev;
        float calculatedDelta = Distance.calculateDelta(stdDev);
        Assert.assertEquals(expectedDelta, calculatedDelta, 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(mean, celeraDistance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, celeraDistance.getStdDev(), 0.01f);
        Assert.assertEquals(min, celeraDistance.getMin());
        Assert.assertEquals(max, celeraDistance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        int min = 1;
        int max = 10;
        float mean = 2.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        Assert.assertEquals(mean, celeraDistance.getMean(), 0.01f);
        Assert.assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);
        Assert.assertEquals(min, celeraDistance.getMin());
        Assert.assertEquals(max, celeraDistance.getMax());
    }
}