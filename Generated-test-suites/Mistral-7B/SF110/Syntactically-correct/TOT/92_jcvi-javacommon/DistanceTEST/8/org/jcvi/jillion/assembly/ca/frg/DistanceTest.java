import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithValidValues() {
        int min = 5;
        int max = 15;
        float mean = 10.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 12.0f;
        float stdDev = 4.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);

        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 3;
        int max = 13;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -1;
        int max = 3;

        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMax() {
        int min = 3;
        int max = -1;

        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 15, 10.5f, 3.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertNotEquals(distance.getStdDev(), transformedDistance.getStdDev());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithOutOfRangeMean() {
        Distance distance = Distance.buildDistance(5, 15, 3.0f, 3.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
    }
}