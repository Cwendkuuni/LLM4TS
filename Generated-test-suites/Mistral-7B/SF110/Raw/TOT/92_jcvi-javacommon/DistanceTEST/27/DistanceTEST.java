import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals("Mean should be equal", mean, distance.getMean(), 0.001f);
        assertEquals("Standard Deviation should be equal", stdDev, distance.getStdDev(), 0.001f);
        assertEquals("Min should be equal", min, distance.getMin());
        assertEquals("Max should be equal", max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - 3.0f * stdDev);
        int expectedMax = (int) (mean + 3.0f * stdDev);
        assertEquals("Min should be equal", expectedMin, distance.getMin());
        assertEquals("Max should be equal", expectedMax, distance.getMax());
        assertEquals("Mean should be equal", mean, distance.getMean(), 0.001f);
        assertEquals("Standard Deviation should be equal", stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 5;
        int max = 15;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals("Mean should be equal", mean, distance.getMean(), 0.001f);
        assertEquals("Standard Deviation should be equal", stdDev, distance.getStdDev(), 0.001f);
        assertEquals("Min should be equal", min, distance.getMin());
        assertEquals("Max should be equal", max, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 12.5f;
        float stdDev = 5.0f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertTrue("Mean should be less than 3 standard deviations", mean < 3.0f * stdDev);
        assertEquals("Celera mean should be equal to corrected mean", mean, celeraDistance.getMean(), 0.001f);
        assertEquals("Celera std dev should be equal to corrected std dev", stdDev / 3.0f, celeraDistance.getStdDev(), 0.001f);
        assertEquals("Celera min should be equal to original min", distance.getMin(), celeraDistance.getMin());
        assertEquals("Celera max should be equal to original max", distance.getMax(), celeraDistance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanStdDev() {
        float mean = 15.0f;
        float stdDev = 5.0f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        fail("Exception should be thrown");
    }
}