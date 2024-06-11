import org.junit.Test;
import org.junit.Assert.*;
import static org.jcvi.jillion.assembly.ca.frg.Distance.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = buildDistance(mean, stdDev);
        float delta = calculateDelta(stdDev);
        Assert.assertEquals(mean - delta, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        int min = 10;
        int max = 20;
        Distance distance = buildDistance(min, max);
        float mean = computeMean(min, max);
        float stdDev = computeStandardDeviation(min, max);
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;
        float delta = calculateDelta(stdDev);
        Assert.assertEquals(3.0f * stdDev, delta, 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeStdDev() {
        int min = 10;
        int max = 20;
        float stdDev = -3.5f;
        buildDistance(min, max, mean, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = buildDistance(10, 20);
        Distance celeraDistance = transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (distance.getMean() - 1.015f) / 3.0f;
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
        Assert.assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
        Assert.assertNotEquals(distance.getStdDev(), celeraDistance.getStdDev());
        Assert.assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceInvalidMean() {
        Distance distance = buildDistance(10, 20);
        distance.setMean(Float.NEGATIVE_INFINITY);
        transformIntoCeleraAssemblerDistance(distance);
    }
}