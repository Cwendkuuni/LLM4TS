package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        assertEquals(3.0f, Distance.calculateDelta(2.5f), 0.001f);
        assertEquals(9.0f, Distance.calculateDelta(3.0f), 0.001f);
    }

    @Test
    public void testComputeMean() {
        assertEquals(5.5f, Distance.computeMean(1, 10), 0.001f);
        assertEquals(7.5f, Distance.computeMean(5, 10), 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.0f, Distance.computeStandardDeviation(1, 2), 0.001f);
        assertEquals(1.5f, Distance.computeStandardDeviation(1, 3), 0.001f);
    }

    @Test
    public void testBuildDistanceFromMeanAndStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 2.5f);
        assertEquals(8, distance.getMin(), 0.001f);
        assertEquals(9, distance.getMax(), 0.001f);
        assertEquals(7.5f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceFromMinAndMax() {
        Distance distance = Distance.buildDistance(5, 10);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, celeraDistance.getMin());
        assertEquals(10, celeraDistance.getMax());
        assertEquals(6.929f, celeraDistance.getMean(), 0.001f);
        assertEquals(1.358f, celeraDistance.getStdDev(), 0.001f);

        Distance distanceWithSmallMean = Distance.buildDistance(5, 10, 1.5f, 2.5f);
        Distance celeraDistanceWithSmallMean = Distance.transformIntoCeleraAssemblerDistance(distanceWithSmallMean);
        assertEquals(5, celeraDistanceWithSmallMean.getMin());
        assertEquals(10, celeraDistanceWithSmallMean.getMax());
        assertEquals(1.857f, celeraDistanceWithSmallMean.getMean(), 0.001f);
        assertEquals(1.231f, celeraDistanceWithSmallMean.getStdDev(), 0.001f);
    }

    @Test
    public void testHashCodeEquals() {
        Distance distance1 = new Distance(5, 10, 7.5f, 2.5f);
        Distance distance2 = new Distance(5, 10, 7.5f, 2.5f);
        Distance distance3 = new Distance(6, 10, 7.5f, 2.5f);

        assertEquals(distance1.hashCode(), distance1.hashCode());
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertNotEquals(distance1.hashCode(), distance3.hashCode());
        assertEquals(distance1, distance2);
        assertNotEquals(distance1, distance3);
    }
}