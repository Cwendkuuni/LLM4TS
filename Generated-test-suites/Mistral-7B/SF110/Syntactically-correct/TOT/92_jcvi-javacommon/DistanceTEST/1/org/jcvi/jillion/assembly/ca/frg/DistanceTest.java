package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DistanceTest {
@Test
public void testBuildDistanceWithMinMax() {
int min = 1;
int max = 10;
Distance distance = Distance.buildDistance(min, max);
float expectedMean = (min + max) / 2.0f;
float expectedStdDev = (max - min) / 6.0f;
assertEquals(expectedMean, distance.getMean(), 0.01);
assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
assertEquals(min, distance.getMin());
assertEquals(max, distance.getMax());
}

@Test
public void testBuildDistanceWithMeanAndStdDev() {
float mean = 5.5f;
float stdDev = 2.5f;
Distance distance = Distance.buildDistance(mean, stdDev);
float expectedPlusDelta = mean + (3.0f * stdDev);
float expectedMinusDelta = mean - (3.0f * stdDev);
int expectedMin = Math.min((int) expectedPlusDelta, (int) expectedMinusDelta);
int expectedMax = Math.max((int) expectedPlusDelta, (int) expectedMinusDelta);
assertEquals(mean, distance.getMean(), 0.01);
assertEquals(stdDev, distance.getStdDev(), 0.01);
assertEquals(expectedMin, distance.getMin());
assertEquals(expectedMax, distance.getMax());
}

@Test
public void testTransformIntoCeleraAssemblerDistance() {
int min = 1;
int max = 5;
float mean = 3.5f;
float stdDev = 1.5f;
Distance distance = Distance.buildDistance(min, max, mean, stdDev);
Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
float correctedStdDev = (mean - 1.015f) / 3.0f;
assertEquals(mean, celeraDistance.getMean(), 0.01);
assertEquals(correctedStdDev, celeraDistance.getStdDev(), 0.01);
assertEquals(min, celeraDistance.getMin());
assertEquals(max, celeraDistance.getMax());
}
}