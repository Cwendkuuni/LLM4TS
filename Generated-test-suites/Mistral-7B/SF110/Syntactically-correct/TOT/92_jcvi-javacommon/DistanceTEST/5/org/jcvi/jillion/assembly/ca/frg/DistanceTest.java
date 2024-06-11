import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.01); // Allow for small rounding errors
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01); // Allow for small rounding errors
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int)(mean - 3.0f * stdDev);
        int expectedMax = (int)(mean + 3.0f * stdDev);
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.01); // Allow for small rounding errors
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01); // Allow for small rounding errors
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 5;
        int max = 15;
        Distance distance = Distance.buildDistance(min, max);
        float mean = computeMean(min, max);
        float stdDev = computeStandardDeviation(min, max);
        Distance expectedDistance = new Distance(min, max, mean, stdDev);
        Assert.assertEquals(expectedDistance.getMean(), distance.getMean(), 0.01); // Allow for small rounding errors
        Assert.assertEquals(expectedDistance.getStdDev(), distance.getStdDev(), 0.01); // Allow for small rounding errors
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinOrMax() {
        int min = 5;
        int max = 4; // max < min
        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.5f, 3.2f);
        Distance expectedDistance = new Distance(12, 19, 14.05f, 1.032f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.01); // Allow for small rounding errors
        Assert.assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.01); // Allow for small rounding errors
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        Distance distance = new Distance(10, 20, 10.0f, 3.2f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    private static float computeMean(int min, int max) {
        return (min + max) / 2.0f;
    }

    private static float computeStandardDeviation(int min, int max) {
        return (max - min) / 6.0f;
    }
}