package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = (min + max) / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMin() {
        int min = -1;
        int max = 10;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMax() {
        int min = 1;
        int max = -1;
        Distance.buildDistance(min, max);
    }

    @Test
    public void testBuildDistanceWithSameMinAndMax() {
        int min = 1;
        int max = 1;
        Distance distance = Distance.buildDistance(min, max);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(min, distance.getMean());
        assertEquals(0.0f, distance.getStdDev());
    }

    // Add other test cases for other methods here
}