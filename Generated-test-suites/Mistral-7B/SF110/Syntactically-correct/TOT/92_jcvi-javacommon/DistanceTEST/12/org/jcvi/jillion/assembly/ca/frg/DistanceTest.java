package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        final int min = 5;
        final int max = 15;
        final float mean = 10.5f;
        final float stdDev = 3.5f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        final float mean = 5.5f;
        final float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int)(mean - 3.0f * stdDev);
        int expectedMax = (int)(mean + 3.0f * stdDev);
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        final int min = 1;
        final int max = 20;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = (min + max) / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001f);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMinOrMax() {
        final int min = -5;
        final int max = 15;
        Distance.buildDistance(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        final int min = 5;
        final int max = 15;
        final float stdDev = -3.5f;
        Distance.buildDistance(min, max, 10.5f, stdDev);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        final float mean = 11.5f;
        final float stdDev = 5.0f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        Distance expectedDistance = new Distance(1, 10, mean, stdDev / 3.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.001f);
        Assert.assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        final float mean = 10.0f;
        final float stdDev = 5.0f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        Distance.transformIntoCeleraAssemblerDistance(distance);
    }

    @Test
    public void testHashCodeAndEquals() {
        final int min = 1;
        final int max = 10;
        final float mean = 5.5f;
        final float stdDev = 2.5f;
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testHashCodeAndEqualsWithDifferentValues() {
        final int min1 = 1;
        final int max1 = 10;
        final float mean1 = 5.5f;
        final float stdDev1 = 2.5f;
        final int min2 = 20;
        final int max2 = 25;
        final float mean2 = 15.5f;
        final float stdDev2 = 5.0f;
        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);
        Assert.assertNotEquals(distance1.hashCode(), distance2.hashCode());
        Assert.assertNotEquals(distance1, distance2);
    }
}