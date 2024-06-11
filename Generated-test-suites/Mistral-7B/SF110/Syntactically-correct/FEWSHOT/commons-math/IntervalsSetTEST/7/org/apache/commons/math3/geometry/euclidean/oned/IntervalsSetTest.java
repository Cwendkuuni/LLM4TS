package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetConstructorWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1, intervalsSet.size());
        Interval expectedInterval = new Interval(0.0, 1.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(expectedInterval, intervals.get(0));
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(1, intervalsSet.size());
        Interval expectedInterval = new Interval(Double.NEGATIVE_INFINITY, 0.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(expectedInterval, intervals.get(0));
        assertNull(intervalsSet.getBarycenter());

        tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(2, intervalsSet.size());
        Interval firstInterval = new Interval(Double.NEGATIVE_INFINITY, 0.0);
        Interval secondInterval = new Interval(0.0, 1.0);
        List<Interval> intervalsList = intervalsSet.asList();
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(firstInterval);
        expectedIntervals.add(secondInterval);
        Collections.sort(expectedIntervals);
        Collections.sort(intervalsList);
        assertEquals(expectedIntervals, intervalsList);
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetConstructorWithCollection() {
        List<Interval> intervalsList = new ArrayList<>();
        intervalsList.add(new Interval(0.0, 1.0));
        IntervalsSet intervalsSet = new IntervalsSet(intervalsList);
        assertEquals(1, intervalsSet.size());
        Interval expectedInterval = new Interval(0.0, 1.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(expectedInterval, intervals.get(0));
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertNotNull(tree);
        assertTrue(tree.getCut() == null);

        tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
        assertNotNull(tree);
        OrientedPoint expectedOrientedPoint1 = new OrientedPoint(new Vector1D(0.0), false);
        assertEquals(expectedOrientedPoint1.wholeHyperplane(), tree.getCut());

        tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
        assertNotNull(tree);
        OrientedPoint expectedOrientedPoint2 = new OrientedPoint(new Vector1D(0.0), true);
        assertEquals(expectedOrientedPoint2.wholeHyperplane(), tree.getCut());

        tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0, Double.POSITIVE_INFINITY);
        assertNotNull(tree);
        OrientedPoint lowerOrientedPoint = new OrientedPoint(new Vector1D(0.0), false);
        OrientedPoint upperOrientedPoint = new OrientedPoint(new Vector1D(1.0), true);
        BSPTree<Euclidean1D> expectedTree = new BSPTree<>(lowerOrientedPoint.wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(upperOrientedPoint.wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
        assertEquals(expectedTree, tree);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        IntervalsSet expectedIntervalsSet = new IntervalsSet(tree);
        IntervalsSet intervalsSet = IntervalsSet.buildNew(tree);
        assertEquals(expectedIntervalsSet, intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getSize());
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());

        intervalsSet = new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY));
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
        assertNull(intervalsSet.getBarycenter());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
        assertNull(intervalsSet.getBarycenter());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(1.0, intervalsSet.getInf());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(1.0, intervalsSet.getSup());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval expectedInterval = new Interval(0.0, 1.0);
        assertEquals(expectedInterval, intervals.get(0));

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
        intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        Interval firstInterval = new Interval(Double.NEGATIVE_INFINITY, 0.0);
        Interval secondInterval = new Interval(0.0, Double.POSITIVE_INFINITY);
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(firstInterval);
        expectedIntervals.add(secondInterval);
        Collections.sort(expectedIntervals);
        Collections.sort(intervals);
        assertEquals(expectedIntervals, intervals);
    }
}