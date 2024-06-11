package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.AbstractRegion;
import org.apache.commons.math3.geometry.Euclidean1D;
import org.apache.commons.math3.geometry.Interval;
import org.apache.commons.math3.geometry.Point2D;
import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Vector1D;

import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstuctor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet.getTree());
        assertEquals(0, intervalsSet.size());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        assertNotNull(tree);
        assertEquals(1, intervalsSet.size());
        Interval expectedInterval = new Interval(2.0, 5.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane(),
                new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), null, null, null), null, null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(1, intervalsSet.size());
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(2.0, 5.0);
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(
                new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane(),
                new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(1, intervalsSet.size());
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(2.0, 5.0);
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testBuildTree() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(Boolean.TRUE, tree.getAttribute());
    }

    @Test
    public void testBuildTreeWithFiniteLowerAndUpper() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertFalse(tree.getAttribute());
        List<Interval> intervals = new IntervalsSet(tree).asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(lower, upper);
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane(),
                new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), null, null, null), null, null);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(1, newIntervalsSet.size());
        List<Interval> intervals = newIntervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(2.0, 5.0);
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        intervalsSet.computeGeometricalProperties();
        double barycenter = intervalsSet.getBarycenter().getX();
        double size = intervalsSet.getSize();
        assertEquals(5.0, barycenter, Precision.SAFE_EPSILON);
        assertEquals(3.0, size, Precision.SAFE_EPSILON);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        double inf = intervalsSet.getInf();
        assertEquals(2.0, inf, Precision.SAFE_EPSILON);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        double sup = intervalsSet.getSup();
        assertEquals(5.0, sup, Precision.SAFE_EPSILON);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(2.0, 5.0);
        assertEquals(expectedInterval, intervals.get(0));
    }

    @Test
    public void testListWithMultipleIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(1.0, 3.0), intervals.get(0));
        assertEquals(new Interval(4.0, 6.0), intervals.get(1));
    }

    @Test
    public void testListWithEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(0, intervals.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        List<Interval> intervalsList = intervalsSet.asList();
        intervalsList.remove(0);
    }

    @Test
    public void testCheckPointWithPointInsideInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        Point2D point = new Point2D(3.5, 0.0);
        Region.Location location = intervalsSet.checkPoint(point);
        assertEquals(Region.Location.INSIDE, location);
    }

    @Test
    public void testCheckPointWithPointOutsideIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        Point2D point = new Point2D(0.5, 0.0);
        Region.Location location = intervalsSet.checkPoint(point);
        assertEquals(Region.Location.OUTSIDE, location);
    }

    @Test
    public void testCheckPointWithPointOnBoundary() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        Point2D point = new Point2D(3.0, 0.0);
        Region.Location location = intervalsSet.checkPoint(point);
        assertEquals(Region.Location.BOUNDARY, location);
    }
}