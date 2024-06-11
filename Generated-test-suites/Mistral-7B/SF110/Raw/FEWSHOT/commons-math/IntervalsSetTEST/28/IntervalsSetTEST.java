package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet emptySet = new IntervalsSet();
        assertTrue(emptySet.isEmpty());
        assertEquals(Collections.emptyList(), emptySet.asList());
        assertEquals(Double.NaN, emptySet.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSize());
    }

    @Test
    public void testConstructors() {
        IntervalsSet emptySet = new IntervalsSet();
        IntervalsSet intervalSet = new IntervalsSet(1.0, 3.0);
        IntervalsSet boundarySet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)));
        IntervalsSet treeSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));

        assertEquals(3.0, intervalSet.getSize());
        assertEquals(Arrays.asList(new Interval(1.0, 3.0)), intervalSet.asList());
        assertEquals(1.5, intervalSet.getBarycenter().getX(), 0.01);

        assertEquals(2, boundarySet.size());
        assertEquals(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)), boundarySet.asList());
        assertEquals(2.5, boundarySet.getBarycenter().getX(), 0.01);

        assertTrue(treeSet.isEmpty());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet set = new IntervalsSet();

        IntervalsSet lowerUpper = new IntervalsSet(1.0, 3.0);
        BSPTree<Euclidean1D> lowerTree = set.buildTree(1.0, 3.0);
        IntervalsSet expectedLowerTree = new IntervalsSet(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(), null);
        assertEquals(expectedLowerTree, new IntervalsSet(lowerTree));

        IntervalsSet lowerInfiniteUpper = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        BSPTree<Euclidean1D> lowerInfiniteTree = set.buildTree(Double.NEGATIVE_INFINITY, 3.0);
        IntervalsSet expectedLowerInfiniteTree = new IntervalsSet(new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane(), new IntervalsSet(), null);
        assertEquals(expectedLowerInfiniteTree, new IntervalsSet(lowerInfiniteTree));

        IntervalsSet upperUpper = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        BSPTree<Euclidean1D> upperTree = set.buildTree(1.0, Double.POSITIVE_INFINITY);
        IntervalsSet expectedUpperTree = new IntervalsSet(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new IntervalsSet(Boolean.TRUE), null);
        assertEquals(expectedUpperTree, new IntervalsSet(upperTree));

        IntervalsSet lowerInfiniteLowerUpper = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0, Double.POSITIVE_INFINITY);
        BSPTree<Euclidean1D> lowerInfiniteLowerUpperTree = set.buildTree(Double.NEGATIVE_INFINITY, 2.0, Double.POSITIVE_INFINITY);
        IntervalsSet expectedLowerInfiniteLowerUpperTree = new IntervalsSet(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new IntervalsSet(Boolean.FALSE), new IntervalsSet(Boolean.TRUE), null);
        assertEquals(expectedLowerInfiniteLowerUpperTree, new IntervalsSet(lowerInfiniteLowerUpperTree));
    }

    @Test
    public void testBuildNew() {
        IntervalsSet set = new IntervalsSet();
        BSPTree<Euclidean1D> originalTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
        BSPTree<Euclidean1D> newTree = set.buildTree(1.0, 3.0);
        IntervalsSet newSet = set.buildNew(newTree);

        assertEquals(newSet, new IntervalsSet(originalTree));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> intervals = new ArrayList<Interval>();
        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        set.computeGeometricalProperties();
        assertEquals(2.0, set.getSize());
        assertEquals(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)), set.asList());
        assertEquals(2.5, set.getBarycenter().getX(), 0.01);

        intervals.clear();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(2.0, 4.0));
        set = new IntervalsSet(intervals);

        set.computeGeometricalProperties();
        assertEquals(2.0, set.getSize());
        assertEquals(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)), set.asList());
        assertEquals(2.5, set.getBarycenter().getX(), 0.01);

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        set.computeGeometricalProperties();
        assertEquals(1.0, set.getSize());
        assertEquals(Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)), set.asList());
        assertEquals(Double.NaN, set.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(1.0, set.getInf(), 0.0);

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, 1.0));
        intervals.add(new Interval(2.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(1.0, set.getInf(), 0.0);

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(3.0, set.getSup(), 0.0);

        intervals.clear();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(2.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(4.0, set.getSup(), 0.0);

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)), set.asList());

        intervals.clear();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(2.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)), set.asList());

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        assertEquals(Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)), set.asList());
    }

    @Test
    public void testRecurseList() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> intervals = new ArrayList<Interval>();

        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        List<Interval> result = new ArrayList<Interval>();
        set.recurseList(set.getTree(false), result, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)), result);

        intervals.clear();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(-1.0, 0.0));
        intervals.add(new Interval(2.0, 4.0));
        set = new IntervalsSet(intervals);

        result.clear();
        set.recurseList(set.getTree(false), result, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)), result);

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        result.clear();
        set.recurseList(set.getTree(false), result, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckPoint() {
        IntervalsSet set = new IntervalsSet();

        List<Interval> intervals = new ArrayList<Interval>();
        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        set = new IntervalsSet(intervals);

        assertEquals(Region.Location.OUTSIDE, set.checkPoint(new Vector1D(0.5), null));
        assertEquals(Region.Location.INSIDE, set.checkPoint(new Vector1D(2.5), null));
        assertEquals(Region.Location.OUTSIDE, set.checkPoint(new Vector1D(5.0), null));

        intervals.clear();
        intervals.add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        intervals.add(new Interval(2.0, Double.POSITIVE_INFINITY));
        set = new IntervalsSet(intervals);

        assertEquals(Region.Location.OUTSIDE, set.checkPoint(new Vector1D(0.5), null));
        assertEquals(Region.Location.INSIDE, set.checkPoint(new Vector1D(2.5), null));
        assertEquals(Region.Location.INSIDE, set.checkPoint(new Vector1D(-1.0), null));
    }
}