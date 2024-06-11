package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        int min = 0;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = min / 2.0f + max / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    // Add test cases for edge cases and exceptions
}