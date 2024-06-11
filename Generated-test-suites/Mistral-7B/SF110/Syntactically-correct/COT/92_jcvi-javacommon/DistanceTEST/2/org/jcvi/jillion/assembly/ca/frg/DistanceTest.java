import org.junit.Assert;
import org.junit.Test;

public class DistanceTest
{
    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 12.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float calculatedMean = Distance.computeMean(distance.getMin(), distance.getMax());
        float calculatedStdDev = Distance.computeStandardDeviation(distance.getMin(), distance.getMax());

        Assert.assertEquals(mean, calculatedMean, 0.01f);
        Assert.assertEquals(stdDev, calculatedStdDev, 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 5;
        int max = 15;

        Distance distance = Distance.buildDistance(min, max);

        Assert.assertEquals(computeMean(min, max), distance.getMean(), 0.01f);
        Assert.assertEquals(computeStandardDeviation(min, max), distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 5.0f;

        float delta = Distance.calculateDelta(stdDev);

        Assert.assertEquals(15.0f, delta, 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithinLimit() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
        Assert.assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceOutsideLimit() {
        int min = 10;
        int max = 20;
        float mean = 10.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertNotEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
        Assert.assertNotEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01f);
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
    }

    private float computeMean(int min, int max) {
        return min / 2.0f + max / 2.0f;
    }

    private float computeStandardDeviation(int min, int max) {
        return (max - min) / 6.0f;
    }
}