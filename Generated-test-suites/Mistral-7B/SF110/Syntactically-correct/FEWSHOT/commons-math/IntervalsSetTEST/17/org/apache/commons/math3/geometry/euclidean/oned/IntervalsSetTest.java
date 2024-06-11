package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree(false));
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(1, tree.getLeafCount());
        assertEquals(1, tree.getInternalNodeCount());
        assertNotNull(tree.getCut());
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(1.0, op.getLocation().getX(), 1e-9);

        intervalsSet = intervalsSet.buildNew(tree);
        assertEquals(1.0, intervalsSet.getInf(), 1e-9);
        assertEquals(3.0, intervalsSet.getSup(), 1e-9);
        intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), 1e-9);
        assertEquals(3.0, interval.getSup(), 1e-9);
    }

    @Test
    public void testInfiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(0, tree.getLeafCount());
        assertEquals(1, tree.getInternalNodeCount());
        assertNull(tree.getCut());

        intervalsSet = intervalsSet.buildNew(tree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 1e-9);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 1e-9);
        intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testIntervalsSetWithThreeIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new double[]{1.0, 2.5, 4.0});
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(3, tree.getLeafCount());
        assertEquals(2, tree.getInternalNodeCount());
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedList = new ArrayList<>(ListUtils.nCopies(3, (i) -> new Interval(i + 0.5, i + 1.5)));
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithTwoOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new double[]{1.0, 3.0, 2.5});
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(3, tree.getLeafCount());
        assertEquals(2, tree.getInternalNodeCount());
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedList = new ArrayList<>(ListUtils.nCopies(2, (i) -> new Interval(i + 0.5, i + 2.5)));
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithOneContainedInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new double[]{1.0, 2.5, 1.5});
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(3, tree.getLeafCount());
        assertEquals(2, tree.getInternalNodeCount());
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedList = new ArrayList<>(ListUtils.nCopies(2, (i) -> new Interval(i + 0.5, i + 1.5)));
        expectedList.remove(1);
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithOneProperlyNestedInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new double[]{1.0, 2.0, 3.5});
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(3, tree.getLeafCount());
        assertEquals(2, tree.getInternalNodeCount());
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedList = new ArrayList<>(ListUtils.nCopies(2, (i) -> new Interval(i + 0.5, i + 1.5)));
        expectedList.set(1, new Interval(2.0, 3.0));
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithOneCoincidingInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new double[]{1.0, 1.0, 2.5});
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(3, tree.getLeafCount());
        assertEquals(2, tree.getInternalNodeCount());
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedList = new ArrayList<>(ListUtils.nCopies(2, (i) -> new Interval(1.0, 1.0)));
        assertEquals(expectedList, intervalsList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithInvalidInput() {
        new IntervalsSet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithTwoEqualLowerBounds() {
        new IntervalsSet(new double[]{1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithTwoEqualUpperBounds() {
        new IntervalsSet(new double[]{2.0, 2.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithLowerBoundGreaterThanUpperBound() {
        new IntervalsSet(new double[]{3.0, 2.0});
    }
}