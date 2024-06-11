package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 2.5f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(7.5f, delta, 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotSame(distance, celeraDistance);
        assertEquals(mean, celeraDistance.getMean(), 0.001f);
        assertNotEquals(stdDev, celeraDistance.getStdDev(), 0.001f);
        // The min and max values remain the same
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceNegativeMean() {
        int min = 10;
        int max = 20;
        float mean = 14.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    @Test
    public void testHashCodeAndEquals() {
        int min1 = 10;
        int max1 = 20;
        float mean1 = 15.5f;
        float stdDev1 = 2.5f;

        int min2 = 10;
        int max2 = 20;
        float mean2 = 15.5f;
        float stdDev2 = 2.5f;

        int min3 = 11;
        int max3 = 21;
        float mean3 = 15.5f;
        float stdDev3 = 2.5f;

        int min4 = 10;
        int max4 = 20;
        float mean4 = 15.5f;
        float stdDev4 = 3.5f;

        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);
        Distance distance3 = Distance.buildDistance(min3, max3, mean3, stdDev3);
        Distance distance4 = Distance.buildDistance(min4, max4, mean4, stdDev4);

        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        int hashCode3 = distance3.hashCode();
        int hashCode4 = distance4.hashCode();

        assertTrue(hashCode1 != hashCode2);
        assertTrue(hashCode1 != hashCode3);
        assertTrue(hashCode1 != hashCode4);
        assertTrue(hashCode2 != hashCode3);
        assertTrue(hashCode2 != hashCode4);
        assertTrue(hashCode3 != hashCode4);

        assertTrue(distance1.equals(distance1));
        assertTrue(distance1.equals(distance2)); // should be false
        assertTrue(distance1.equals(distance3)); // should be false
        assertTrue(distance1.equals(distance4)); // should be false

        assertTrue(distance2.equals(distance2));
        assertTrue(distance2.equals(distance1)); // should be false
        assertTrue(distance2.equals(distance3)); // should be false
        assertTrue(distance2.equals(distance4)); // should be false

        assertTrue(distance3.equals(distance3));
        assertTrue(distance3.equals(distance1)); // should be false
        assertTrue(distance3.equals(distance2)); // should be false
        assertTrue(distance3.equals(distance4)); // should be false

        assertTrue(distance4.equals(distance4));
        assertTrue(distance4.equals(distance1)); // should be false
        assertTrue(distance4.equals(distance2)); // should be false
        assertTrue(distance4.equals(distance3)); // should be false
    }
}