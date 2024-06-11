package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 15;
        float mean = 10.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = 3.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 5;
        int max = 15;

        Distance distance = Distance.buildDistance(min, max);

        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);

        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMinOrMax() {
        int min = -5;
        int max = -15;

        Distance.buildDistance(min, max);
    }

    @Test
    public void testCalculateDelta() {
        float stdDev = 3.5f;

        float delta = Distance.calculateDelta(stdDev);

        assertEquals(3.0f * stdDev, delta, 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 15);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        if (mean < calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            assertEquals(distance.getMin(), celeraDistance.getMin());
            assertEquals(distance.getMax(), celeraDistance.getMax());
            assertEquals(mean, celeraDistance.getMean(), 0.001f);
            assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.001f);
        } else {
            assertEquals(distance, celeraDistance);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithNegativeMean() {
        Distance distance = new Distance(5, 15, 1.5f, 3.5f);

        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        fail("Expected IllegalArgumentException");
    }

    private float calculateDelta(final float stdDev) {
        return 3.0f * stdDev;
    }
}