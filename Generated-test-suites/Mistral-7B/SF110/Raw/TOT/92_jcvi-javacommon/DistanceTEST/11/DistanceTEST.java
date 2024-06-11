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
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int) (mean - delta);
        int expectedMax = (int) (mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -5;
        int max = 20;
        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(5, 15, 10, 3.5f);
        Distance expectedDistance = new Distance(5, 15, 10, 1.05f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
        assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
        assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.01);
        assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithNoCorrection() {
        Distance distance = new Distance(5, 15, 10, 3.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.01);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.01);
    }
}