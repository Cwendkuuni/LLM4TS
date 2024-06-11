package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

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
    public void testBuildDistanceMeanOnly() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertNull(distance.getMin());
        assertNull(distance.getMax());
        assertEquals(2.5f, distance.getStdDev());
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(3.5f, distance.getMean(), 0.1f); // computed mean based on formula in the code
        assertEquals(distance.getMean(), distance.getMean(), 0.1f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(1.0f / 6.0f, distance.getStdDev(), 0.01f); // computed standard deviation based on formula in the code
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.0f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.0f, celeraDistance.getMean());
        assertEquals(0.8333333f, celeraDistance.getStdDev(), 0.001f); // calculated using the formula in the code
        assertNotSame(distance, celeraDistance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceMeanBelowThreshold() {
        Distance distance = new Distance(1, 10, 1.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        fail("Expected an IllegalArgumentException, but no exception was thrown.");
    }
}