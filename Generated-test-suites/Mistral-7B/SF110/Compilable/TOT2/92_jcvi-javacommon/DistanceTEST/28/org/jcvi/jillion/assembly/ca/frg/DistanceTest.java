package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        float expectedMean = mean;
        float expectedStdDev = stdDev;
        float expectedMin = (int) (mean - 3 * stdDev);
        float expectedMax = (int) (mean + 3 * stdDev);
        assertEquals(expectedMean, distance.getMean(), 0.01f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01f);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
    }
}