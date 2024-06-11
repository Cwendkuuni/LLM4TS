import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.0f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f); // allow for floating point rounding error
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 3.0f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int)(mean - 3.0f);
        int max = (int)(mean + 3.0f);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceInvalidMinMax() {
        int min = 5;
        int max = 4;
        Distance.buildDistance(min, max); // should throw IllegalArgumentException
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        float mean = 5.0f;
        float stdDev = 3.0f;
        Distance distance = new Distance(10, 20, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
        Assert.assertNotEquals(distance.getMean(), celeraDistance.getMean());
        Assert.assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceNoChange() {
        float mean = 12.7f;
        float stdDev = 2.5f;
        Distance distance = new Distance(10, 20, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), celeraDistance.getMin());
        Assert.assertEquals(distance.getMax(), celeraDistance.getMax());
        Assert.assertEquals(distance.getMean(), celeraDistance.getMean(), 0.001f);
        Assert.assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testHashCode() {
        int min1 = 1;
        int max1 = 10;
        float mean1 = 5.5f;
        float stdDev1 = 2.5f;

        int min2 = 1;
        int max2 = 10;
        float mean2 = 5.5f;
        float stdDev2 = 2.5f;

        int min3 = 2;
        int max3 = 10;
        float mean3 = 5.5f;
        float stdDev3 = 2.5f;

        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);
        Distance distance3 = new Distance(min3, max3, mean3, stdDev3);

        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        int hashCode3 = distance3.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
        Assert.assertEquals(hashCode2, hashCode3); // since distance2 and distance3 have the same values
    }

    @Test
    public void testEquals() {
        int min1 = 1;
        int max1 = 10;
        float mean1 = 5.5f;
        float stdDev1 = 2.5f;

        int min2 = 1;
        int max2 = 10;
        float mean2 = 5.5f;
        float stdDev2 = 2.5f;

        int min3 = 2;
        int max3 = 10;
        float mean3 = 5.5f;
        float stdDev3 = 2.5f;

        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);
        Distance distance3 = new Distance(min3, max3, mean3, stdDev3);

        boolean equals12 = distance1.equals(distance2);
        boolean equals13 = distance1.equals(distance3);
        boolean equals23 = distance2.equals(distance3);

        Assert.assertTrue(equals12);
        Assert.assertFalse(equals13);
        Assert.assertTrue(equals23);
    }

    @Test
    public void testToString() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 3.0f;
        Distance distance = new Distance(min, max, mean, stdDev);
        String expected = String.format("%.3f, std: %.3f", mean, stdDev);
        String actual = distance.toString();
        Assert.assertEquals(expected, actual);
    }
}