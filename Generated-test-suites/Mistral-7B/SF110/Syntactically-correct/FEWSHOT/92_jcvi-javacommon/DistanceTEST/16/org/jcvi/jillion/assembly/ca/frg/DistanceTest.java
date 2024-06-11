package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals(3.5f, distance.getMean());
        assertEquals(1.5f, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.2f, 2.3f);
        assertEquals(5.2f, distance.getMean());
        assertEquals(2.3f, distance.getStdDev());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(2.5f), 0.01);
    }

    @Test
    public void testComputeStandardDeviation() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1.0f / 6.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testComputeMean() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(3.0f, distance.getMean(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 2.5f, 1.5f);
        Distance expected = Distance.buildDistance(3, 5);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected.getMean(), transformed.getMean(), 0.01);
        assertEquals(expected.getStdDev(), transformed.getStdDev(), 0.01);
        assertEquals(expected.getMin(), transformed.getMin());
        assertEquals(expected.getMax(), transformed.getMax());

        distance = Distance.buildDistance(1, 5, 1.5f, 1.5f);
        assertNotEquals(expected, Distance.transformIntoCeleraAssemblerDistance(distance));
    }

    @Test
    public void testHashCodeAndEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance distance3 = new Distance(1, 5, 3.6f, 1.5f);
        Distance distance4 = new Distance(2, 5, 3.5f, 1.5f);

        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertTrue(distance1.equals(distance2));
        assertNotEquals(distance1, distance3);
        assertNotEquals(distance1, distance4);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals("3.500, std: 1.500", distance.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidArgs() {
        Distance.buildDistance(1, 1);
    }
}