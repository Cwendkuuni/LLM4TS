import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.5f, distance.getMean(), 0.01f);
        assertEquals(2.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(15.5f, 2.5f);
        float delta = Distance.calculateDelta(2.5f);
        int expectedMin = (int)(15.5f - delta);
        int expectedMax = (int)(15.5f + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(15.5f, distance.getMean(), 0.01f);
        assertEquals(2.5f, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        Distance distance = new Distance(1, 2, 0.5f, 2.5f);
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithValidMean() {
        Distance distance = new Distance(1, 2, 5.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(2.5f, celeraDistance.getStdDev(), 0.01f);
        assertEquals(1, celeraDistance.getMin());
        assertEquals(2, celeraDistance.getMax());
    }
}