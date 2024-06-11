package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMeanOnly() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        assertEquals(5, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001f);
        assertEquals(1.1666667f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(1.5f), 0.001f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(3.5f, Distance.computeMean(1, 5), 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.1666667f, Distance.computeStandardDeviation(1, 5), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(6, transformedDistance.getMax());
        assertEquals(2.485f, transformedDistance.getMean(), 0.001f);
        assertEquals(0.7272727f, transformedDistance.getStdDev(), 0.001f);

        Distance distanceWithMeanBelowThreshold = Distance.buildDistance(1, 5, 1.5f, 1.5f);
        Distance transformedDistanceWithMeanBelowThreshold = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowThreshold);
        assertEquals(1, transformedDistanceWithMeanBelowThreshold.getMin());
        assertEquals(5, transformedDistanceWithMeanBelowThreshold.getMax());
        assertEquals(1.6666667f, transformedDistanceWithMeanBelowThreshold.getMean(), 0.001f);
        assertEquals(0.5f, transformedDistanceWithMeanBelowThreshold.getStdDev(), 0.001f);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());

        Distance distance3 = Distance.buildDistance(1, 5, 3.1f, 1.5f);
        assertNotEquals(distance1.hashCode(), distance3.hashCode());

        Distance distance4 = Distance.buildDistance(2, 5, 3.0f, 1.5f);
        assertNotEquals(distance1.hashCode(), distance4.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertTrue(distance1.equals(distance2));

        Distance distance3 = Distance.buildDistance(1, 5, 3.1f, 1.5f);
        assertFalse(distance1.equals(distance3));

        Distance distance4 = new Distance(1, 5, 3.0f, 1.5f);
        assertTrue(distance1.equals(distance4));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals("3.000, std: 1.500", distance.toString());
    }

    @Test
    public void testNull() {
        assertNull(Distance.buildDistance(null, null));
    }
}