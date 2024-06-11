package org.jcvi.jillion.assembly.ca.frg.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.jcvi.jillion.assembly.ca.frg.Distance;

import org.junit.Test;

public class DistanceTest {

	@Test
	public void testBuildDistanceWithMinMaxMeanStdDev() {
		final int min = 1;
		final int max = 10;
		final float mean = 5.5f;
		final float stdDev = 2.5f;
		final Distance distance = Distance.buildDistance(min, max, mean, stdDev);
		assertEquals(mean, distance.getMean(), 0.01);
		assertEquals(stdDev, distance.getStdDev(), 0.01);
		assertEquals(min, distance.getMin());
		assertEquals(max, distance.getMax());
	}

	@Test
	public void testBuildDistanceWithMeanStdDev() {
		final float mean = 5.5f;
		final float stdDev = 2.5f;
		final Distance distance = Distance.buildDistance(mean, stdDev);
		final float delta = Distance.calculateDelta(stdDev);
		final int expectedMin = (int)(mean - delta);
		final int expectedMax = (int)(mean + delta);
		assertEquals(expectedMin, distance.getMin());
		assertEquals(expectedMax, distance.getMax());
		assertEquals(mean, distance.getMean(), 0.01);
		assertEquals(stdDev, distance.getStdDev(), 0.01);
	}

	@Test
	public void testBuildDistanceWithMinMax() {
		final int min = 1;
		final int max = 10;
		final Distance distance = Distance.buildDistance(min, max);
		final float mean = Distance.computeMean(min, max);
		final float stdDev = Distance.computeStandardDeviation(min, max);
		assertEquals(mean, distance.getMean(), 0.01);
		assertEquals(stdDev, distance.getStdDev(), 0.01);
		assertEquals(min, distance.getMin());
		assertEquals(max, distance.getMax());
	}

	@Test
	public void testCalculateDelta() {
		final float stdDev = 2.5f;
		final float expectedDelta = 7.5f;
		assertEquals(expectedDelta, Distance.calculateDelta(stdDev), 0.01);
	}

	@Test
	public void testTransformIntoCeleraAssemblerDistance() {
		final float mean = 5.5f;
		final float stdDev = 2.5f;
		final Distance distance = new Distance(1, 10, mean, stdDev);
		final Distance expectedDistance = new Distance(2, 9, mean, 0.833333f);
		assertEquals(expectedDistance, Distance.transformIntoCeleraAssemblerDistance(distance));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransformIntoCeleraAssemblerDistanceWithNegativeStdDev() {
		final float mean = 5.5f;
		final float stdDev = -2.5f;
		final Distance distance = new Distance(1, 10, mean, stdDev);
		Distance.transformIntoCeleraAssemblerDistance(distance);
	}

	@Test
	public void testHashCodeAndEquals() {
		final int min = 1;
		final int max = 10;
		final float mean = 5.5f;
		final float stdDev = 2.5f;
		final Distance distance1 = new Distance(min, max, mean, stdDev);
		final Distance distance2 = new Distance(min, max, mean, stdDev);
		assertEquals(distance1.hashCode(), distance2.hashCode());
		assertEquals(distance1, distance2);

		final int min2 = 11;
		final int max2 = 15;
		final float mean2 = 6.5f;
		final float stdDev2 = 3.5f;
		final Distance distance3 = new Distance(min2, max2, mean2, stdDev2);
		assertNotEquals(distance1, distance3);
	}
}