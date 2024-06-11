package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = 6.0f;
        float expectedStdDev = (max - min) / 6.0f;
        Assert.assertEquals(expectedMean, distance.getMean(), 0.01f);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.01f);
    }
}