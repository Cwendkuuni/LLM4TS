import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 5.5f;
        float stdDev = 2.5f;
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
    public void testBuildDistanceWithMinMax() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -1;
        int max = -10;
        Distance.buildDistance(min, max);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMean(), celeraDistance.getMean(), 0.001f);
        assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.001f);
        assertNotSame(distance, celeraDistance);
        float mean = celeraDistance.getMean();
        float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / Distance.SIX;
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        Distance distance = Distance.buildDistance(1, 10, 2.0f, 3.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        float mean = celeraDistance.getMean();
        float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / Distance.SIX;
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testNotEquals() {
        Distance distance1 = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(5, 15, 10.0f, 4.0f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 15, 10.0f, 3.0f);
        String expected = "10.000, std: 1.667";
        assertEquals(expected, distance.toString());
    }
}