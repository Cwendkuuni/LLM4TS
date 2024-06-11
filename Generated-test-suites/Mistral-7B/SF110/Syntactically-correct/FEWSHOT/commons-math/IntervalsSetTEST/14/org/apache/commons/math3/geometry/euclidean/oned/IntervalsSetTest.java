package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Interval;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getInf());
        assertNull(intervalsSet.getSup());
        assertTrue(intervalsSet.isEmpty());
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(0, intervals.size());
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
        assertEquals(2, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(3.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(2.0, 3.0));
        assertEquals(expectedIntervals, intervalsList);
    }

    @Test
    public void testIntervalsSetWithInfiniteLowerBound() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, new Vector1D(2.0));
        assertEquals(1, intervalsSet.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.001);
        assertEquals(2.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        List<Interval> expectedInterval = Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, 2.0));
        assertEquals(expectedInterval, intervalsList);
    }

    @Test
    public void testIntervalsSetWithInfiniteUpperBound() {
        IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        List<Interval> expectedInterval = Collections.singletonList(new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedInterval, intervalsList);
    }

    @Test
    public void testIntervalsSetWithMultipleIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
        assertEquals(3, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(6.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(3, intervalsList.size());
        assertEquals(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)), intervalsList);
    }

    @Test
    public void testIntervalsSetWithOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(2.0, 4.0)));
        assertEquals(2, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(4.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0), new Interval(2.0, 4.0));
        assertEquals(expectedIntervals, intervalsList);
    }

    @Test
    public void testIntervalsSetWithEmptyIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 1.0), new Interval(3.0, 3.0)));
        assertEquals(2, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(1.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 1.0), new Interval(3.0, 3.0));
        assertEquals(expectedIntervals, intervalsList);
    }

    @Test
    public void testIntervalsSetWithTinyOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0 - 1e-6, 1.0 + 1e-6), new Interval(1.0, 2.0)));
        assertEquals(2, intervalsSet.size());
        assertEquals(1.0 - 1e-6, intervalsSet.getInf(), 2 * 1e-6);
        assertEquals(2.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0 - 1e-6, 1.0 + 1e-6), new Interval(1.0, 2.0));
        assertEquals(expectedIntervals, intervalsList);
    }

    @Test
    public void testIntervalsSetWithIntervalsThatDoNotOverlap() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0)));
        assertEquals(2, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
        assertEquals(5.0, intervalsSet.getSup(), 0.001);
        assertFalse(intervalsSet.isEmpty());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
        assertEquals(expectedIntervals, intervalsList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithNullLowerBound() {
        new IntervalsSet(null, new Vector1D(2.0));
    }

    @Test
    public void testIntervalsSetWithNullCollection() {
        try {
            new IntervalsSet(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithNullInterval() {
        IntervalsSet intervalsSet = new IntervalsSet();
        try {
            intervalsSet.add(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}