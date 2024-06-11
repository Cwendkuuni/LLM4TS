import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @org.junit.Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceInvalidMean() {
        int min = 10;
        int max = 20;
        float mean = 10.5f;
        float stdDev = 3.5f;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMinOrMax() {
        int min = -1;
        int max = 1;
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.0f;
        float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        float delta = Distance.calculateDelta(stdDev);
        int expectedMin = (int)(mean - delta);
        int expectedMax = (int)(mean + delta);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @org.junit.Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;
        float expectedDelta = 3.0f * stdDev;
        assertEquals(expectedDelta, Distance.calculateDelta(stdDev), 0.001f);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = -3.5f;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.0f, 3.5f);
        Distance expected = new Distance(9, 17, 13.132f, 1.132f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected.getMin(), transformed.getMin());
        assertEquals(expected.getMax(), transformed.getMax());
        assertEquals(expected.getMean(), transformed.getMean(), 0.001f);
        assertEquals(expected.getStdDev(), transformed.getStdDev(), 0.001f);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistanceWithNoCorrection() {
        Distance distance = new Distance(10, 20, 15.0f, 3.5f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformed);
    }

    @org.junit.Test
    public void testGetters() {
        Distance distance = new Distance(10, 20, 15.0f, 3.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(3.5f, distance.getStdDev(), 0.001f);
    }

    @org.junit.Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 3.5f);
        Distance distance2 = new Distance(10, 20, 15.0f, 3.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @org.junit.Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 3.5f);
        Distance distance2 = new Distance(10, 20, 15.0f, 3.5f);
        assertTrue(distance1.equals(distance2));
    }

    @org.junit.Test
    public void testNotEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 3.5f);
        Distance distance2 = new Distance(10, 20, 15.1f, 3.5f);
        assertFalse(distance1.equals(distance2));
    }

    @org.junit.Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 3.5f);
        assertEquals("15.000, std: 3.500", distance.toString());
    }
}