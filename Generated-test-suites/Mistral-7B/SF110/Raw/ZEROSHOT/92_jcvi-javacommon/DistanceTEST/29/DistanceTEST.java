import org.junit.Test;
import org.junit.Assert;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);

        int min = (int) (mean - 3 * stdDev);
        int max = (int) (mean + 3 * stdDev);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        Distance expectedDistance = Distance.buildDistance(mean, stdDev);

        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());

        Assert.assertEquals(expectedDistance.getMean(), distance.getMean(), 0.01);
        Assert.assertEquals(expectedDistance.getStdDev(), distance.getStdDev(), 0.01);
        Assert.assertEquals(expectedDistance.getMin(), distance.getMin());
        Assert.assertEquals(expectedDistance.getMax(), distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(mean, celeraDistance.getMean(), 0.01);
        Assert.assertEquals(stdDev / 3.0f, celeraDistance.getStdDev(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistance_meanBelowDelta() {
        int min = 10;
        int max = 20;
        float mean = 12.5f;
        float stdDev = 3.2f;

        Distance distance = Distance.buildDistance(min, max);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}