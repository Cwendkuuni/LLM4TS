import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*; // for assertX methods
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01); // allow for some rounding error
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;
        float delta = Distance.calculateDelta(stdDev);
        Assert.assertEquals(10.5f, delta, 0.01);
    }

    @Test
    public void testComputeMean() {
        int min = 1;
        int max = 10;
        float expectedMean = 5.5f;
        float actualMean = Distance.computeMean(min, max);
        Assert.assertEquals(expectedMean, actualMean, 0.01);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 10;
        float stdDev = Distance.computeStandardDeviation(min, max);
        Assert.assertEquals(1.16667f, stdDev, 0.01);
    }

    @Test
    public void testBuildDistanceFromMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int minExpected = 12;
        int maxExpected = 19;
        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
        Assert.assertEquals(minExpected, distance.getMin());
        Assert.assertEquals(maxExpected, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 10);
        Distance expectedDistance = Distance.buildDistance(5, 8);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.01);
        Assert.assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.01);
        Assert.assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
        Distance distance = Distance.buildDistance(1, 2);
        Distance.transformIntoCeleraAssemblerDistance(distance);
        fail("Expected an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceNegativeStdDev() {
        Distance distance = Distance.buildDistance(1, 2);
        Distance.transformIntoCeleraAssemblerDistance(distance);
        distance.setStdDev(-1.5f);
        Distance.transformIntoCeleraAssemblerDistance(distance);
        fail("Expected an IllegalArgumentException");
    }
}