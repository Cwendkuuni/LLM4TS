import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01); // allow for rounding error
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 10.0f;
        float stdDev = 3.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - 3.0f * stdDev);
        int expectedMax = (int) (mean + 3.0f * stdDev);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 5;
        int max = 15;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = min / 2.0f + max / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.01);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -5;
        int max = 15;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMax() {
        int min = 5;
        int max = -15;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        int min = 5;
        int max = 15;
        float stdDev = -3.0f;
        Distance.buildDistance(min, max, 10.0f, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float expectedMean = distance.getMean() - 1.015f;
        float expectedCorrectedStdDev = expectedMean / 3.0f;
        assertEquals(distance.getMean(), celeraDistance.getMean(), 0.01);
        assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.01);
        assertEquals(expectedMean, celeraDistance.getMean(), 0.01);
        assertEquals(expectedCorrectedStdDev, celeraDistance.getStdDev(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
        Distance invalidDistance = new Distance(-1, 1, 1.0f, 2.0f);
        Distance.transformIntoCeleraAssemblerDistance(invalidDistance);
    }
}