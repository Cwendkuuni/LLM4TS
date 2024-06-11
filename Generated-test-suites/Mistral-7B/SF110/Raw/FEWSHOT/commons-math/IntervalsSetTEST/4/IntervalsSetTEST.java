package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;

public class IntervalsSetTest {
    @Test
    public void testEmptySet() {
        IntervalsSet set = new IntervalsSet();
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
        assertNull(set.getBarycenter());
    }

    @Test
    public void testSingleIntervalSet() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(2.0, set.getSup(), Precision.EPSILON);
        assertEquals(1.0, set.getSize(), Precision.EPSILON);
        assertEquals(new Vector1D(1.5), set.getBarycenter());
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(2.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        IntervalsSet set = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
        assertEquals(0.0, set.getInf(), Precision.EPSILON);
        assertEquals(4.0, set.getSup(), Precision.EPSILON);
        assertEquals(3.0, set.getSize(), Precision.EPSILON);
        List<Interval> intervals = set.asList();
        assertEquals(2, intervals.size());
        for (int i = 0; i < intervals.size(); i++) {
            Interval interval = intervals.get(i);
            if (i == 0) {
                assertEquals(0.0, interval.getInf(), Precision.EPSILON);
                assertEquals(1.0, interval.getSup(), Precision.EPSILON);
            } else {
                assertEquals(3.0, interval.getInf(), Precision.EPSILON);
                assertEquals(4.0, interval.getSup(), Precision.EPSILON);
            }
        }
    }

    @Test
    public void testIntervalsSetWithInfiniteLowerBound() {
        IntervalsSet set = new IntervalsSet(-1.0, 1.0);
        assertEquals(-1.0, set.getInf(), Precision.EPSILON);
        assertEquals(1.0, set.getSup(), Precision.EPSILON);
        assertEquals(2.0, set.getSize(), Precision.EPSILON);
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(-1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testIntervalsSetWithInfiniteUpperBound() {
        IntervalsSet set = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
        assertEquals(0.0, set.getInf(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, set.getSize(), Precision.EPSILON);
        List<Interval> intervals = set.asList();
        assertEquals(0, intervals.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithInvalidLowerBound() {
        new IntervalsSet(Double.NaN, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithInvalidUpperBound() {
        new IntervalsSet(0.0, Double.NaN);
    }

    @Test
    public void testIntervalsSetWithEmptyCollection() {
        IntervalsSet set = new IntervalsSet(Collections.emptyList());
        assertEquals(0.0, set.getInf(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), Precision.EPSILON);
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
        List<Interval> intervals = set.asList();
        assertEquals(0, intervals.size());
    }

    @Test
    public void testIntervalsSetWithCollectionContainingNull() {
        List<Interval> intervals = new ArrayList<>();
        intervals.add(null);
        try {
            new IntervalsSet(intervals);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIntervalsSetWithCollectionContainingInvalidIntervals() {
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(Double.NaN, Double.NaN));
        try {
            new IntervalsSet(intervals);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(-1.0, 1.0);
        assertEquals(Boolean.TRUE, tree.getAttribute());
        OrientedPoint op = (OrientedPoint) tree.getCut();
        assertEquals(-1.0, op.getLocation().getX(), Precision.EPSILON);
        tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
        assertEquals(Boolean.TRUE, tree.getAttribute());
        op = (OrientedPoint) tree.getCut();
        assertEquals(0.0, op.getLocation().getX(), Precision.EPSILON);
        tree = IntervalsSet.buildTree(1.0, 2.0);
        assertEquals(Boolean.FALSE, tree.getAttribute());
        op = (OrientedPoint) tree.getCut();
        assertEquals(1.0, op.getLocation().getX(), Precision.EPSILON);
        tree = IntervalsSet.buildTree(1.0, 3.0, new OrientedPoint(new Vector1D(2.0), true));
        assertEquals(Boolean.FALSE, tree.getAttribute());
        op = (OrientedPoint) tree.getCut();
        assertEquals(2.0, op.getLocation().getX(), Precision.EPSILON);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        IntervalsSet set = IntervalsSet.buildNew(tree);
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), Precision.EPSILON);
        assertEquals(1.0, set.getSize(), Precision.EPSILON);
        List<Interval> intervals = set.asList();
        assertEquals(0, intervals.size());

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        set = IntervalsSet.buildNew(tree);
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(1.0, set.getSup(), Precision.EPSILON);
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
        intervals = set.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        set = IntervalsSet.buildNew(tree);
        assertEquals(0.0, set.getInf(), Precision.EPSILON);
        assertEquals(1.0, set.getSup(), Precision.EPSILON);
        assertEquals(1.0, set.getSize(), Precision.EPSILON);
        intervals = set.asList();
        assertEquals(1, intervals.size());
        interval = intervals.get(0);
        assertEquals(0.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        set = IntervalsSet.buildNew(tree);
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), Precision.EPSILON);
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
        intervals = set.asList();
        assertEquals(0, intervals.size());

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        set = IntervalsSet.buildNew(tree);
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(1.0, set.getSup(), Precision.EPSILON);
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
        intervals = set.asList();
        assertEquals(1, intervals.size());
        interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        set.computeGeometricalProperties();
        assertEquals(3.0, set.getSize(), Precision.EPSILON);
        assertEquals(new Vector1D(2.5), set.getBarycenter());
    }
}