import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
        Assert.assertEquals(15.5f, distance.getMean(), 0.01f);
        Assert.assertEquals(3.5f, distance.getStdDev(), 0.01f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.5f, 3.5f);
        int min = (int)(15.5f - 3.0f * 3.5f);
        int max = (int)(15.5f + 3.0f * 3.5f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(15.5f, distance.getMean(), 0.01f);
        Assert.assertEquals(3.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        float mean = (float)(10.0f + 20.0f) / 2.0f;
        float stdDev = (20.0f - 10.0f) / 6.0f;
        Assert.assertEquals(mean, distance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMeanAndStdDev() {
        Distance.buildDistance(-1.0f, 3.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinMax() {
        Distance.buildDistance(3, 2);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(5, 15, 10.0f, 3.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = 10.0f - (3.0f / 3.0f);
        float stdDev = mean / 3.0f;
        Assert.assertEquals(mean, celeraDistance.getMean(), 0.01f);
        Assert.assertEquals(stdDev, celeraDistance.getStdDev(), 0.01f);
        Assert.assertEquals(5, celeraDistance.getMin());
        Assert.assertEquals(15, celeraDistance.getMax());

        distance = new Distance(5, 15, 10.0f, 5.0f);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance, celeraDistance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
        Distance distance = new Distance();
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}