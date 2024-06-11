package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithValidArguments() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;

        Distance actual = Distance.buildDistance(min, max, mean, stdDev);

        Distance expected = new Distance(min, max, mean, stdDev);

        assertEquals(expected, actual);
    }

    // Add test cases for other methods and constructors, getters, and edge cases here.

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidArguments() {
        float mean = 5.5f;
        float stdDev = 2.5f;

        Distance.buildDistance(mean, stdDev); // No min and max arguments provided
    }

    @Test
    public void testDistanceConstructionWithValidArguments() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.5f;

        Distance distance = new Distance(min, max, mean, stdDev);

        Distance expected = new Distance(min, max, mean, stdDev);

        assertEquals(expected, distance);
    }

    @Test
    public void testGetters() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);

        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        int min = distance.getMin();
        int max = distance.getMax();

        assertEquals(5.5f, mean, 0.001f);
        assertEquals(2.5f, stdDev, 0.001f);
        assertEquals(1, min);
        assertEquals(10, max);
    }

    // Add test cases for other methods, constructors, and edge cases here.
}

//