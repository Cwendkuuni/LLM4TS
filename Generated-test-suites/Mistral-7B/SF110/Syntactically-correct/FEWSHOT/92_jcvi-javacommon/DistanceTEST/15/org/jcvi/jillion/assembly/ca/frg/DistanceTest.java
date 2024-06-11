package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        float delta = Distance.calculateDelta(2.5f);
        int plusDelta = (int)(5.5f + delta);
        int minusDelta = (int)(5.5f - delta);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMean(), transformedDistance.getMean());
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev());
        if (distance.getMean() < Distance.calculateDelta(distance.getStdDev())) {
            float correctedStdDev = (distance.getMean() - Distance.MAX_ROUNDING_ERROR) / Distance.SIX;
            Distance expectedDistance = Distance.buildDistance((int)(distance.getMean() - 3.0f * distance.getStdDev()), (int)(distance.getMean() + 3.0f * distance.getStdDev()));
            assertEquals(expectedDistance.getMin(), transformedDistance.getMin());
            assertEquals(expectedDistance.getMax(), transformedDistance.getMax());
            assertEquals(correctedStdDev, transformedDistance.getStdDev());
        } else {
            assertEquals(distance, transformedDistance);
        }
    }
}