import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @org.junit.Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @org.junit.Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int) (mean - 3 * stdDev);
        int max = (int) (mean + 3 * stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @org.junit.Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -5;
        int max = 5;
        float mean = 0;
        float stdDev = 3;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        int min = 5;
        int max = 10;
        float mean = 5;
        float stdDev = -3;
        Distance.buildDistance(min, max, mean, stdDev);
    }

    @org.junit.Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(3, 8, 5.5f, 2.1f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, celeraDistance.getMin());
        assertEquals(8, celeraDistance.getMax());
        assertNotEquals(5.5f, celeraDistance.getMean(), 0.001f);
        assertEquals(1.0216667f, celeraDistance.getStdDev(), 0.001f);

        Distance distance2 = new Distance(3, 8, 3.5f, 1.5f);
        Distance celeraDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(3, celeraDistance2.getMin());
        assertEquals(8, celeraDistance2.getMax());
        assertEquals(3.5f, celeraDistance2.getMean(), 0.001f);
        assertEquals(1.5f, celeraDistance2.getStdDev(), 0.001f);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowCalculatedDelta() {
        Distance distance = new Distance(2, 6, 1.5f, 2.1f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }
}