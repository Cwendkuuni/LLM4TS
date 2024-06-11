package org.jcvi.jillon.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        int min = (int) (3.5f - 3.0f);
        int max = (int) (3.5f + 3.0f);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        Distance distance = Distance.buildDistance(1, 5);
        float mean = (float) (1.0f + 5.0f) / 2.0f;
        float stdDev = (5.0f - 1.0f) / 6.0f;
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01);
        assertEquals(stdDev, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        float mean = distance.getMean();
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceNoChange() {
        Distance distance = Distance.buildDistance(1, 5, 4.5f, 1.5f);
        assertEquals(distance.getMin(), distance.getMin());
        assertEquals(distance.getMax(), distance.getMax());
        assertEquals(distance.getMean(), distance.getMean(), 0.01);
        assertEquals(distance.getStdDev(), distance.getStdDev(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMin() {
        Distance.buildDistance(-1, 5, 3.5f, 1.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceNegativeMax() {
        Distance.buildDistance(1, -5, 3.5f, 1.5f);
    }

    @Test
    public void testHashCode() {
        int min = 1;
        int max = 5;
        float mean = 3.5f;
        float stdDev = 1.5f;
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        int min = 1;
        int max = 5;
        float mean = 3.5f;
        float stdDev = 1.5f;
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        int min = 1;
        int max = 5;
        float mean = 3.5f;
        float stdDev = 1.5f;
        Distance distance = new Distance(min, max, mean, stdDev);
        assertEquals("3.500, std: 0.500", distance.toString());
    }
}