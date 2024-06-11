import org.junit.Test;
import org.junit.Assert.*;
import static org.jcvi.jillion.assembly.ca.frg.Distance.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 2.5f;
        Distance distance = buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 10.0f;
        float stdDev = 2.0f;
        Distance distance = buildDistance(mean, stdDev);
        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;
        float delta = calculateDelta(stdDev);
        Assert.assertEquals(10.5f, delta, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        int min = -5;
        int max = -10;
        buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNonPositiveStdDev() {
        int min = 5;
        int max = 10;
        float stdDev = -2.5f;
        buildDistance(min, max, mean, stdDev);
    }

    @Test
    public void testComputeStandardDeviation() {
        int min = 5;
        int max = 10;
        float stdDev = computeStandardDeviation(min, max);
        Assert.assertEquals(1.0f, stdDev, 0.01);
    }

    @Test
    public void testComputeMean() {
        int min = 5;
        int max = 10;
        float mean = computeMean(min, max);
        Assert.assertEquals(7.5f, mean, 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = buildDistance(5, 10);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        Distance celeraDistance = transformIntoCeleraAssemblerDistance(distance);
        if (mean < calculateDelta(stdDev)) {
            Assert.assertEquals(5, celeraDistance.getMin());
            Assert.assertEquals(10, celeraDistance.getMax());
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            Assert.assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
        } else {
            Assert.assertEquals(distance, celeraDistance);
        }
    }

    @Test
    public void testHashCode() {
        Distance distance1 = buildDistance(5, 10);
        Distance distance2 = buildDistance(5, 10);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());

        Distance distance3 = buildDistance(6, 11);
        Assert.assertNotEquals(distance1.hashCode(), distance3.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = buildDistance(5, 10);
        Distance distance2 = buildDistance(5, 10);
        Assert.assertTrue(distance1.equals(distance2));

        Distance distance3 = buildDistance(6, 11);
        Assert.assertFalse(distance1.equals(distance3));
    }

    @Test
    public void testToString() {
        Distance distance = buildDistance(5, 10);
        String expected = String.format("%.3f, std: %.3f", 7.5, 1.0);
        Assert.assertEquals(expected, distance.toString());
    }
}