package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMean(), transformedDistance.getMean());
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev());
        assertNotEquals(distance.getMin(), transformedDistance.getMin());
        assertNotEquals(distance.getMax(), transformedDistance.getMax());
        float correctedStdDev = (distance.getMean() - 1.015f) / 3.0f;
        assertEquals(correctedStdDev, transformedDistance.getStdDev());
        assertTrue(transformedDistance.getMin() < distance.getMin());
        assertTrue(transformedDistance.getMax() > distance.getMax());
    }

    @Test
    public void testHashCodeAndEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertEquals(distance1, distance2);

        Distance distance3 = Distance.buildDistance(1, 10, 5.6f, 2.5f);
        assertNotEquals(distance1, distance3);

        Distance distance4 = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1, distance4);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals("5.500, std: 2.500", distance.toString());
    }
}