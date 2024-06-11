package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMeanAndStdDev() {
        float mean = 15.5f;
        float stdDev = 2.2f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int min = (int)(mean - 3 * stdDev);
        int max = (int)(mean + 3 * stdDev);
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testBuildDistanceMinAndMax() {
        int min = 10;
        int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.01f);
        assertEquals(stdDev, distance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 10;
        int max = 20;
        float mean = 15.5f;
        float stdDev = 2.2f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        float correctedMean = mean - 1.015f;
        float correctedStdDev = correctedMean / 3.0f;
        assertEquals(correctedMean, celeraDistance.getMean(), 0.01f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithCorrectedValues() {
        int min = 10;
        int max = 20;
        float mean = 12.015f;
        float stdDev = 2.2f;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        float correctedMean = mean - 1.015f;
        float correctedStdDev = correctedMean / 3.0f;
        assertEquals(correctedMean, celeraDistance.getMean(), 0.01f);
        assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithOutOfLimitValues() {
        int min = 10;
        int max = 20;
        float mean = 12.0f;
        float stdDev = 2.2f;
        Distance distance = new Distance(min, max, mean, stdDev);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(min, celeraDistance.getMin());
        assertEquals(max, celeraDistance.getMax());
        assertEquals(mean, celeraDistance.getMean(), 0.01f);
        assertEquals(stdDev, celeraDistance.getStdDev(), 0.01f);
    }
}