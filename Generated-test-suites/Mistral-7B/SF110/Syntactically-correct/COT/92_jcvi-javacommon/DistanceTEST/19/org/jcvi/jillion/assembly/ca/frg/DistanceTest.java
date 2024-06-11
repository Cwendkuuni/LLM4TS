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
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @org.junit.Test
    public void testBuildDistanceMean() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001);
        assertNotNull(distance.getMin());
        assertNotNull(distance.getMax());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMin() {
        int min = -5;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 3.5f;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMax() {
        int min = 5;
        int max = -10;
        float mean = 5.5f;
        float stdDev = 3.5f;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 15.5f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001);
        assertNotNull(distance.getMin());
        assertNotNull(distance.getMax());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceStdDevZero() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 0;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(3.0f * stdDev, delta, 0.001);
    }

    @org.junit.Test
    public void testComputeMean() {
        int min = 10;
        int max = 20;
        float expectedMean = 15.0f;
        float mean = Distance.computeMean(min, max);
        assertEquals(expectedMean, mean, 0.001);
    }

    @org.junit.Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 11;
        float expectedStdDev = 1.5f;
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(expectedStdDev, stdDev, 0.001);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = new Distance(15, 25, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(mean, celeraDistance.getMean(), 0.001);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.001);
        assertNotSame(distance, celeraDistance);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceMeanBelowDelta() {
        float mean = 5.5f;
        float stdDev = 3.5f;
        Distance distance = new Distance(15, 25, mean, stdDev);
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}