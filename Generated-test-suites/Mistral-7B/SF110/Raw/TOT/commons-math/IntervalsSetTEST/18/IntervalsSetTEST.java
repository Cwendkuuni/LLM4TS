package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.AbstractRegion;
import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.util.Vector1D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet emptySet = new IntervalsSet();
        assertEquals(Collections.emptyList(), emptySet.asList());
        assertEquals(Double.NaN, emptySet.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSize());
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getInf());
        assertEquals(Double.NEGATIVE_INFINITY, emptySet.getSup());
    }

    @Test
    public void testSetWithTwoIntervals() {
        IntervalsSet set = new IntervalsSet(new IntervalsSet(0.0, 2.0));
        List<Interval> expectedList = Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 2.0));
        assertEquals(expectedList, set.asList());
        assertEquals(new Vector1D(1.5), set.getBarycenter(), 0.0);
        assertEquals(3.0, set.getSize());
        assertEquals(0.0, set.getInf());
        assertEquals(2.0, set.getSup());
    }

    @Test
    public void testSetWithThreeIntervals() {
        IntervalsSet set = new IntervalsSet(new IntervalsSet(0.0, 1.0), new IntervalsSet(2.0, 3.0));
        List<Interval> expectedList = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        assertEquals(expectedList, set.asList());
        assertEquals(new Vector1D(1.5), set.getBarycenter(), 0.0);
        assertEquals(4.0, set.getSize());
        assertEquals(0.0, set.getInf());
        assertEquals(3.0, set.getSup());
    }

    @Test
    public void testSetWithOneIntervalAndInfiniteLower() {
        IntervalsSet set = new IntervalsSet(new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0));
        List<Interval> expectedList = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 1.0));
        assertEquals(expectedList, set.asList());
        assertEquals(Double.NaN, set.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, set.getSize());
        assertEquals(Double.NEGATIVE_INFINITY, set.getInf());
        assertEquals(1.0, set.getSup());
    }

    @Test
    public void testSetWithOneIntervalAndInfiniteUpper() {
        IntervalsSet set = new IntervalsSet(new IntervalsSet(0.0, Double.POSITIVE_INFINITY));
        List<Interval> expectedList = Arrays.asList(new Interval(0.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, set.asList());
        assertEquals(Double.NaN, set.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, set.getSize());
        assertEquals(0.0, set.getInf());
        assertEquals(Double.POSITIVE_INFINITY, set.getSup());
    }

    @Test
    public void testSetWithOneIntervalAndEmptyBoundary() {
        IntervalsSet set = new IntervalsSet(Collections.emptyList());
        assertEquals(Collections.emptyList(), set.asList());
        assertEquals(Double.NaN, set.getBarycenter(), 0.0);
        assertEquals(0.0, set.getSize());
        assertEquals(Double.POSITIVE_INFINITY, set.getInf());
        assertEquals(Double.NEGATIVE_INFINITY, set.getSup());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithLowerThanUpper() {
        new IntervalsSet(1.0, 0.0);
    }
}