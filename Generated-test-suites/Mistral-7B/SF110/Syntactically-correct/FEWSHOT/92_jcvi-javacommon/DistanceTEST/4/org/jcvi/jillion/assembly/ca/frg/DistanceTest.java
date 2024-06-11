package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 3.2f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(3.2f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 3.2f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(3.2f, distance.getStdDev());
        assertNull(distance.getMin());
        assertNull(distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(3.2f), 0.01f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(5.0f, Distance.computeMean(1, 10), 0.01f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.0f, Distance.computeStandardDeviation(1, 10), 0.01f);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(6.0f, distance.getMean(), 0.01f);
        assertEquals(1.0f, distance.getStdDev(), 0.01f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = new Distance(1, 10, 5.5f, 3.2f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(5.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(1.033f, celeraDistance.getStdDev(), 0.01f);

        Distance distanceWithMeanBelowDelta = new Distance(1, 10, 3.0f, 3.2f);
        Distance celeraDistanceWithMeanBelowDelta = Distance.transformIntoCeleraAssemblerDistance(distanceWithMeanBelowDelta);
        assertEquals(3.048f, celeraDistanceWithMeanBelowDelta.getMean(), 0.01f);
        assertEquals(1.033f, celeraDistanceWithMeanBelowDelta.getStdDev(), 0.01f);
    }

    @Test
    public void testHashCodeEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 3.2f);
        Distance distance2 = new Distance(1, 10, 5.5f, 3.2f);
        Distance distance3 = new Distance(1, 10, 5.5f, 3.3f);

        assertEquals(distance1.hashCode(), distance1.hashCode());
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertNotEquals(distance1.hashCode(), distance3.hashCode());
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 3.2f);
        assertEquals("5.500, std: 3.200", distance.toString());
    }
}