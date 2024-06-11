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
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean());
        assertEquals(2.5f, distance.getStdDev());
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(mean, distance.getMean());
        assertEquals(stdDev, distance.getStdDev());
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f * 2.5f, Distance.calculateDelta(2.5f), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5.0f, 10.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5.0f, celeraDistance.getMean());
        assertEquals(1.667f, celeraDistance.getStdDev(), 0.001f);

        distance = Distance.buildDistance(3.0f, 8.0f);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(4.015f, celeraDistance.getMean(), 0.001f);
        assertEquals(1.0f, celeraDistance.getStdDev(), 0.001f);
    }

    @Test
    public void testHashCodeAndEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance3 = new Distance(2, 11, 5.5f, 2.5f);
        Distance distance4 = new Distance(1, 10, 5.4f, 2.5f);

        assertEquals(distance1.hashCode(), distance1.hashCode());
        assertEquals(distance1, distance1);
        assertEquals(distance1, distance2);
        assertNotEquals(distance1, distance3);
        assertNotEquals(distance1, distance4);
    }
}