import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @org.junit.Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMeanAndStdDev() {
        float mean = -1.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = -3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinAndMax() {
        int min = -1;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinAndMax2() {
        int min = 20;
        int max = -1;
        Distance distance = Distance.buildDistance(min, max);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinMaxAndMean() {
        int min = -1;
        int max = 20;
        float mean = 15.5f;
        Distance distance = Distance.buildDistance(min, max, mean);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinMaxAndMean2() {
        int min = 20;
        int max = -1;
        float mean = 15.5f;
        Distance distance = Distance.buildDistance(min, max, mean);
    }

    @org.junit.Test
    public void testComputeMean() {
        int min = 1;
        int max = 11;
        float expected = 6.0f;
        float actual = Distance.computeMean(min, max);
        assertEquals(expected, actual, 0.01);
    }

    @org.junit.Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 11;
        float expected = 2.5f;
        float actual = Distance.computeStandardDeviation(min, max);
        assertEquals(expected, actual, 0.01);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            Distance expectedDistance = Distance.buildDistance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
            assertEquals(expectedDistance.getMin(), celeraDistance.getMin());
            assertEquals(expectedDistance.getMax(), celeraDistance.getMax());
            assertEquals(expectedDistance.getMean(), celeraDistance.getMean(), 0.01);
            assertEquals(expectedDistance.getStdDev(), celeraDistance.getStdDev(), 0.01);
        } else {
            assertEquals(distance, celeraDistance);
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidMean() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.5f);
        float mean = distance.getMean() - 1.015f;
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}