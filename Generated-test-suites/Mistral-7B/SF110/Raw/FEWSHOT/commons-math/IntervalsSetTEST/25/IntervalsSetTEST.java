package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithOneInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(1.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 4.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(4.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(2.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoNonOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 6.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(6.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(2.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoIntervalsThatTouch() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(3.0, 5.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(2.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithInfiniteLowerBound() {
        IntervalsSet intervalsSet = new IntervalsSet(Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, 2.0)));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(1.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithInfiniteUpperBound() {
        IntervalsSet intervalsSet = new IntervalsSet(Collections.singletonList(new Interval(1.0, Double.POSITIVE_INFINITY)));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(1.0, intervalsSet.size(), 0.0);
    }

    @Test
    public void testIntervalsSetWithRootAndTree() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0));
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(1.0, tree.getCut().getHyperplane().getLocation().getX(), 0.0);
        assertEquals(2.0, intervalsSet.getInf(), 0.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithList() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2.0, intervals.size(), 0.0);
        assertEquals(1.0, intervals.get(0).getInf(), 0.0);
        assertEquals(2.0, intervals.get(0).getSup(), 0.0);
        assertEquals(3.0, intervals.get(1).getInf(), 0.0);
        assertEquals(4.0, intervals.get(1).getSup(), 0.0);
    }
}