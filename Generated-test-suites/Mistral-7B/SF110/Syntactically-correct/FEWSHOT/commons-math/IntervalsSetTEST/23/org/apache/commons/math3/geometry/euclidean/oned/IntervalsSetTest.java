package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithOneInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1, intervalsSet.size());
        assertEquals(new Interval(1.0, 2.0), intervalsSet.getIntervals().get(0));
        assertEquals(1.5, intervalsSet.getBarycenter().getX());
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        assertEquals(2, intervalsSet.size());
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(1.0, 3.0));
        expectedIntervals.add(new Interval(4.0, 5.0));
        assertEquals(expectedIntervals, intervalsSet.getIntervals());
        assertEquals(3.0, intervalsSet.getBarycenter().getX());
    }

    @Test
    public void testInfiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithSubHyperplanesBoundary() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new OrientedPoint(new Vector1D(-1.0), false).wholeHyperplane(),
                new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetBuildTree() {
        IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 5.0));
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(0.0, 5.0));
        assertEquals(expectedIntervals, intervalsSet.getIntervals());
        assertEquals(2.5, intervalsSet.getBarycenter().getX());
    }

    @Test
    public void testIntervalsSetGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
        assertEquals(4.0, intervalsSet.getInf(), 0.001);
    }

    @Test
    public void testIntervalsSetGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
        assertEquals(5.0, intervalsSet.getSup(), 0.001);
    }

    @Test
    public void testIntervalsSetAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(1.0, 2.0));
        expectedIntervals.add(new Interval(4.0, 5.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(expectedIntervals, intervals);
    }

    private static BSPTree<Euclidean1D> buildTree(double lower, double upper) {
        return IntervalsSet.buildTree(lower, upper);
    }
}