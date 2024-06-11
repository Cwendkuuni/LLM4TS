package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.AbstractRegion;
import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.vector.Vector1D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervals = new IntervalsSet();
        assertEquals(Collections.emptyList(), intervals.asList());
        assertNull(intervals.getInf());
        assertNull(intervals.getSup());
    }

    @Test
    public void testSingleIntervalConstructor() {
        IntervalsSet intervals = new IntervalsSet(0.0, 5.0);
        List<Interval> expected = Arrays.asList(new Interval(0.0, 5.0));
        assertEquals(expected, intervals.asList());
        assertEquals(0.0, intervals.getInf(), 1e-12);
        assertEquals(5.0, intervals.getSup(), 1e-12);
    }

    @Test
    public void testBSPTreeConstructor() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervals = new IntervalsSet(tree);
        assertEquals(Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)), intervals.asList());
        assertNull(intervals.getInf());
        assertNull(intervals.getSup());
    }

    @Test
    public void testCollectionConstructor() {
        List<Interval> intervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
        IntervalsSet set = new IntervalsSet(intervals);
        assertEquals(intervals, set.asList());
        assertEquals(1.0, set.getInf(), 1e-12);
        assertEquals(5.0, set.getSup(), 1e-12);
    }

    @Test
    public void testBuildTreeEdgeCases() {
        IntervalsSet intervals = new IntervalsSet();
        List<Interval> expected = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
        IntervalsSet emptyIntervals = intervals.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertEquals(expected, emptyIntervals.asList());
        expected = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(1.0, Double.POSITIVE_INFINITY));
        IntervalsSet singleLowerEndpointIntervals = intervals.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
        assertEquals(expected, singleLowerEndpointIntervals.asList());
        expected = Arrays.asList(new Interval(0.0, Double.POSITIVE_INFINITY));
        IntervalsSet singleUpperEndpointIntervals = intervals.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane()));
        assertEquals(expected, singleUpperEndpointIntervals.asList());
        expected = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(1.0, Double.POSITIVE_INFINITY));
        IntervalsSet infiniteLowerEndIntervals = intervals.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
        assertEquals(expected, infiniteLowerEndIntervals.asList());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervals = new IntervalsSet();
        List<Interval> expected = Arrays.asList(new Interval(0.0, 3.0), new Interval(5.0, 12.0));
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 3.0, 5.0, 12.0);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        IntervalsSet builtIntervals = intervals.buildNew(tree);
        assertEquals(expected, builtIntervals.asList());
        assertEquals(0.0, builtIntervals.getInf(), 1e-12);
        assertEquals(12.0, builtIntervals.getSup(), 1e-12);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervals = new IntervalsSet();
        List<Interval> emptyList = Collections.emptyList();
        intervals.setTree(new BSPTree<Euclidean1D>(Boolean.TRUE));
        intervals.computeGeometricalProperties();
        assertEquals(emptyList, intervals.asList());
        assertNull(intervals.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervals.getSize());

        intervals = new IntervalsSet();
        List<Interval> singleInterval = Arrays.asList(new Interval(0.0, 5.0));
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        intervals.computeGeometricalProperties();
        List<Interval> expected = Arrays.asList(new Interval(0.0, 5.0));
        assertEquals(expected, intervals.asList());
        assertEquals(3.0, intervals.getBarycenter().getX(), 1e-12);
        assertEquals(15.0, intervals.getSize());

        intervals = new IntervalsSet();
        List<Interval> threeIntervals = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        intervals.computeGeometricalProperties();
        expected = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        assertEquals(expected, intervals.asList());
        assertEquals(7.0, intervals.getBarycenter().getX(), 1e-12);
        assertEquals(24.0, intervals.getSize());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertEquals(Double.NEGATIVE_INFINITY, intervals.getInf());

        intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        assertEquals(0.0, intervals.getInf(), 1e-12);

        intervals = new IntervalsSet();
        List<Interval> intervalsList = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        assertEquals(1.0, intervals.getInf(), 1e-12);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertEquals(Double.POSITIVE_INFINITY, intervals.getSup());

        intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        assertEquals(5.0, intervals.getSup(), 1e-12);

        intervals = new IntervalsSet();
        List<Interval> intervalsList = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        assertEquals(12.0, intervals.getSup(), 1e-12);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertEquals(Collections.emptyList(), intervals.asList());

        intervals = new IntervalsSet();
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        List<Interval> expected = Arrays.asList(new Interval(0.0, 3.0));
        assertEquals(expected, intervals.asList());

        intervals = new IntervalsSet();
        List<Interval> intervalsList = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        intervals.setTree(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane()));
        expected = Arrays.asList(new Interval(1.0, 3.0), new Interval(5.0, 8.0), new Interval(10.0, 12.0));
        assertEquals(expected, intervals.asList());
    }
}