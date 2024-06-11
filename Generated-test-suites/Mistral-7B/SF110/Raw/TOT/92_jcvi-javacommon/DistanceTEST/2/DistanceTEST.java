import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int)(mean - 3.0f * stdDev);
        int max = (int)(mean + 3.0f * stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -5;
        int max = 5; //assuming max cannot be negative
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithMeanLessThanDelta() {
        float mean = 5.5f;
        float stdDev = 3.2f;
        Distance.buildDistance(mean, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance input = new Distance(10, 20, 15.5f, 3.2f);
        Distance output = Distance.transformIntoCeleraAssemblerDistance(input);
        Assert.assertEquals(input.getMean(), output.getMean(), 0.001f);
        Assert.assertNotEquals(input.getStdDev(), output.getStdDev());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance input = new Distance(10, 20, 5.5f, 3.2f);
        Distance.transformIntoCeleraAssemblerDistance(input);
    }
}