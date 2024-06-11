import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;
import org.junit.Test;

public class DistanceTest {

    @Test
    public static void testBuildDistance() {
        // Test case 1
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertNotNull(distance);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());

        // Test case 2
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        assertNotNull(distance);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());

        // Test case 3
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        assertNotNull(distance);
        assertEquals(min / 2.0f + max / 2.0f, distance.getMean());
        assertEquals(Math.abs((max - min) / 6.0f), distance.getStdDev());
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public static void testBuildDistanceMeanAndStdDev() {
        // Test case 1
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        assertNotNull(distance);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
    }

    @Test
    public static void testBuildDistanceMinAndMax() {
        // Test case 1
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        assertNotNull(distance);
        assertEquals(min / 2.0f + max / 2.0f, distance.getMean());
        assertEquals(Math.abs((max - min) / 6.0f), distance.getStdDev());
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public static void testTransformIntoCeleraAssemblerDistance() {
        // Test case 1
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance3 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        assertEquals(distance1.getMean(), distance3.getMean());
        assertEquals(distance1.getStdDev(), distance3.getStdDev());
        assertEquals(distance1.getMin(), distance3.getMin());
        assertEquals(distance1.getMax(), distance3.getMax());

        // Test case 2
        Distance distance2 = new Distance(1, 10, 2.0f, 2.5f);
        Distance distance4 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(distance2.getMean(), distance4.getMean());
        assertEquals(distance2.getStdDev(), distance4.getStdDev());
        assertEquals(distance2.getMin(), distance4.getMin());
        assertEquals(distance2.getMax(), distance4.getMax());
    }

    @Test
    public static void testBuildDistanceWithNegativeMin() {
        int min = -1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;

        // Throws IllegalStateException
        try {
            Distance distance = Distance.buildDistance(min, max, mean, stdDev);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test
    public static void testBuildDistanceWithNegativeMax() {
        int min = 1;
        int max = -1;
        float mean = 5.5f;
        float stdDev = 2.5f;

        // Throws IllegalStateException
        try {
            Distance distance = Distance.buildDistance(min, max, mean, stdDev);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test
    public static void testBuildDistanceWithMeanLessThanMin() {
        int min = 1;
        int max = 10;
        float mean = 0.5f;
        float stdDev = 2.5f;

        // Throws IllegalArgumentException
        try {
            Distance distance = Distance.buildDistance(min, max, mean, stdDev);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public static void testBuildDistanceWithNegativeStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = -2.5f;

        // Throws IllegalArgumentException
        try {
            Distance distance = Distance.buildDistance(min, max, mean, stdDev);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public static void testBuildDistanceWithMinEqualsMax() {
        int min = 1;
        int max = 1;
        float mean = 2.5f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max);
        assertNotNull(distance);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public static void testBuildDistanceWithVeryLargeMeanAndStdDev() {
        int min = 1;
        int max = 10;
        float mean = 1000000f;
        float stdDev = 500000f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertNotNull(distance);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public static void testTransformIntoCeleraAssemblerDistanceWithInvalidInput() {
        Distance distance = new Distance(1, 10, 2.0f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        fail("IllegalArgumentException expected");
    }
}