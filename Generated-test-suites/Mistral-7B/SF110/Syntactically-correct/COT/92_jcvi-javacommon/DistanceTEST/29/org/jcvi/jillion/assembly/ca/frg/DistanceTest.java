import org.junit.Test;
import org.junit.Assert.*;
import static org.jcvi.jillion.assembly.ca.frg.Distance.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        Distance distance = buildDistance(10, 20, 15.5f, 2.5f);
        float expectedMean = 15.5f;
        float expectedStdDev = 2.5f;
        int expectedMin = 10;
        int expectedMax = 20;

        Assert.assertEquals(expectedMean, distance.getMean());
        Assert.assertEquals(expectedStdDev, distance.getStdDev());
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = buildDistance(15.5f, 2.5f);
        int expectedMin = 13;
        int expectedMax = 18;

        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float expectedDelta = 7.5f;

        Assert.assertEquals(expectedDelta, calculateDelta(stdDev));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeStandardDeviationWithInvalidMinMax() {
        int min = 0;
        int max = 0;
        computeStandardDeviation(min, max);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 12;
        float expectedStandardDeviation = 2.0f;

        Assert.assertEquals(expectedStandardDeviation, computeStandardDeviation(min, max), 0.001f);
    }

    @Test
    public void testComputeMean() {
        int min = 1;
        int max = 12;
        float expectedMean = 6.5f;

        Assert.assertEquals(expectedMean, computeMean(min, max), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidDistance() {
        Distance distance = buildDistance(15, 16);
        Distance expectedDistance = buildDistance(15.142857142857143f, 15.857142857142856f);

        Assert.assertEquals(expectedDistance, transformIntoCeleraAssemblerDistance(distance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
        Distance distance = buildDistance(5, 10);
        Distance invalidDistance = new Distance(5, 10, 1.0f, 0.0f);

        Assert.assertNotEquals(invalidDistance, transformIntoCeleraAssemblerDistance(distance));
    }
}