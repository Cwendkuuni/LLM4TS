import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance expected = new Distance(min, max, mean, stdDev);
        Distance actual = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDistanceWithInvalidInput() {
        // Test with null inputs
        assertThrows(NullPointerException.class, () -> Distance.buildDistance(null, 1, 2.5f, 3.0f));
        assertThrows(NullPointerException.class, () -> Distance.buildDistance(1, null, 2.5f, 3.0f));
        assertThrows(NullPointerException.class, () -> Distance.buildDistance(1, 10, null, 3.0f));
        assertThrows(NullPointerException.class, () -> Distance.buildDistance(1, 10, 2.5f, null));

        // Test with negative inputs
        assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(-1, 1, 2.5f, 3.0f));
        assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(1, -1, 2.5f, 3.0f));
    }

    @Test
    public void testBuildDistanceWithMeanEqualOrLessThanStandardDeviation() {
        assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(1, 2, 1.0f, 2.0f));
        assertThrows(IllegalArgumentException.class, () -> Distance.buildDistance(1, 2, 0.5f, 3.0f));
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanCalculatedDelta() {
        int min = 1;
        int max = 10;
        float mean = 1.0f;
        float stdDev = 3.1f;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance expected = new Distance(min, max, mean, stdDev / 3.0f);
        Distance actual = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeStandardDeviationEdgeCases() {
        int min = 0;
        int max = Integer.MAX_VALUE;
        float stdDev = 0.1f;
        assertThrows(IllegalArgumentException.class, () -> Distance.computeStandardDeviation(min, max));
        float result = Distance.computeStandardDeviation(min, max);
        assertEquals(result, 0.01666667f, 0.001f);
    }

    @Test
    public void testComputeMeanEdgeCases() {
        int min = 0;
        int max = Integer.MAX_VALUE;
        float result = Distance.computeMean(min, max);
        assertEquals(result, (min + max) / 2.0f, 0.001f);
    }
}