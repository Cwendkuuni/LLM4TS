package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.3f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.3f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanOnly() {
        Distance distance = Distance.buildDistance(5.5f, 2.3f);
        assertEquals(5.5f, distance.getMean());
        assertNull(distance.getMin());
        assertNull(distance.getMax());
        assertEquals(2.3f, distance.getStdDev());
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(6.5f, distance.getMean(), 0.01f);
        assertEquals(2, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(1.666667f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(2.5f), 0.01f);
        assertEquals(9.0f, Distance.calculateDelta(3.0f), 0.01f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.0f, Distance.computeStandardDeviation(2, 4), 0.01f);
        assertEquals(1.5f, Distance.computeStandardDeviation(3, 6), 0.01f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(3.5f, Distance.computeMean(1, 6), 0.01f);
        assertEquals(5.5f, Distance.computeMean(2, 9), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.5f, 2.3f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.5f, celeraDistance.getMean(), 0.01f);
        assertEquals(1.666667f, celeraDistance.getStdDev(), 0.001f);
        assertEquals(2, celeraDistance.getMin());
        assertEquals(8, celeraDistance.getMax());

        distance.setMean(4.5f);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotEquals(4.5f, celeraDistance.getMean(), 0.01f);
    }

    @Test
    public void testHashCodeAndEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.3f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.3f);
        Distance distance3 = new Distance(1, 10, 5.4f, 2.3f);

        assertEquals(distance1.hashCode(), distance1.hashCode());
        assertEquals(distance1, distance1);
        assertEquals(distance1, distance2);
        assertNotEquals(distance1, distance3);
    }
}