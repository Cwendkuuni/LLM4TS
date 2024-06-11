package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size(), 0.0);
        assertNull(intervalsSet.getBarycenter());
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1, intervalsSet.size(), 1.0);
        assertNotNull(intervalsSet.getBarycenter());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        Interval interval = intervalsList.get(0);
        assertEquals(1.0, interval.getInf(), 0.0);
        assertEquals(2.0, interval.getSup(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        assertEquals(2, intervalsSet.size(), 2.0);
        intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        interval = intervalsList.get(0);
        assertEquals(0.5, interval.getInf(), 0.0);
        assertEquals(1.5, interval.getSup(), 0.0);
        interval = intervalsList.get(1);
        assertEquals(2.5, interval.getInf(), 0.0);
        assertEquals(3.5, interval.getSup(), 0.0);
    }

    @Test
    public void testInfiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsSet.size(), Double.POSITIVE_INFINITY);
        assertNull(intervalsSet.getBarycenter());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        Interval interval = intervalsList.get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(3.0, Double.POSITIVE_INFINITY));
        assertEquals(2, intervalsSet.size(), Double.POSITIVE_INFINITY);
        intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        interval = intervalsList.get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
        assertEquals(0.0, interval.getSup(), 0.0);
        interval = intervalsList.get(1);
        assertEquals(3.0, interval.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0);
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        OrientedPoint op = (OrientedPoint)tree.getCut().getHyperplane();
        assertEquals(1.0, op.getLocation().getX(), 0.0);
        tree = ((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false).getPlus();
        op = (OrientedPoint)tree.getCut().getHyperplane();
        assertEquals(2.0, op.getLocation().getX(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        tree = intervalsSet.getTree(false);
        op = (OrientedPoint)tree.getCut().getHyperplane();
        assertEquals(1.0, op.getLocation().getX(), 0.0);
        tree = ((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false).getPlus();
        op = (OrientedPoint)tree.getCut().getHyperplane();
        assertEquals(2.0, op.getLocation().getX(), 0.0);
        tree = tree.getPlus();
        op = (OrientedPoint)tree.getCut().getHyperplane();
        assertEquals(3.0, op.getLocation().getX(), 0.0);
    }

    @Test
    public void testBarycenterAndSize() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.5, intervalsSet.getBarycenter().getX(), Precision.SAFE_MIN);
        assertEquals(1.0, intervalsSet.size(), 1.0);

        intervalsSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        intervalsSet.computeGeometricalProperties();
        double barycenterX = intervalsSet.getBarycenter().getX();
        assertEquals(2.0, barycenterX, Precision.SAFE_MIN);
        assertEquals(3.0, intervalsSet.size(), 3.0);

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(3.0, Double.POSITIVE_INFINITY));
        intervalsSet.computeGeometricalProperties();
        assertEquals(2.5, intervalsSet.getBarycenter().getX(), Precision.SAFE_MIN);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.size(), Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInfAndSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(4.0, intervalsSet.getSup(), 0.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(3.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }
}