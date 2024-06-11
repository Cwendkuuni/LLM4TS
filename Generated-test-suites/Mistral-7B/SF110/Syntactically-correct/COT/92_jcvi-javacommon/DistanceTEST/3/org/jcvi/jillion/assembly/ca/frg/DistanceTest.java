import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int)(mean - 3 * stdDev);
        int expectedMax = (int)(mean + 3 * stdDev);
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float expectedDelta = 3.0f * stdDev;
        float calculatedDelta = Distance.calculateDelta(stdDev);
        Assert.assertEquals(expectedDelta, calculatedDelta, 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -5;
        int max = -3;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNonPositiveStdDev() {
        int min = 10;
        int max = 20;
        float stdDev = -2.5f;
        Distance.buildDistance(min, max, 15.5f, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01f);
        Assert.assertNotEquals(distance.getStdDev(), celeraDistance.getStdDev());
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());

        Distance incorrectDistance = new Distance(10, 20, 11.015f, 2.5f);
        Distance celeraIncorrectDistance = Distance.transformIntoCeleraAssemblerDistance(incorrectDistance);
        Assert.assertEquals(incorrectDistance.getMean(), celeraIncorrectDistance.getMean(), 0.01f);
        Assert.assertEquals(incorrectDistance.getStdDev(), celeraIncorrectDistance.getStdDev());
        Assert.assertEquals(incorrectDistance.getMin(), celeraIncorrectDistance.getMin());
        Assert.assertEquals(incorrectDistance.getMax(), celeraIncorrectDistance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
        Distance invalidDistance = new Distance(-1, 2, 15.5f, 2.5f);
        Distance.transformIntoCeleraAssemblerDistance(invalidDistance);
    }
}