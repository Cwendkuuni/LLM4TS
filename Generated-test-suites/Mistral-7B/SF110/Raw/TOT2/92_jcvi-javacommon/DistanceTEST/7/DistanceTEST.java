
import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
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
    public void testBuildDistanceWithInvalidMeanAndStdDev() {
        float mean = Float.NaN;
        float stdDev = -5.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinAndMax() {
        int min = 5;
        int max = 3;
        Distance distance = Distance.buildDistance(min, max);
    }

    // Add test cases for other methods here

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15, 3.0f);
        Distance expected = new Distance(10, 20, 15, 1.035f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected.getMean(), transformed.getMean(), 0.01f);
        assertEquals(expected.getStdDev(), transformed.getStdDev(), 0.01f);
        assertEquals(expected.getMin(), transformed.getMin());
        assertEquals(expected.getMax(), transformed.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithInvalidDistance() {
        Distance distance = new Distance(10, 20, 15, 3.0f);
        Distance invalidDistance = new Distance();
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(invalidDistance);
        assertNull(transformed);
    }
}