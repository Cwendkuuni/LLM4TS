package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testCreateEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Collections.emptyList(), intervalsSet.asList());
    }

    @Test
    public void testCreateIntervalsSetWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(0.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testCreateIntervalsSetWithTree() {
        double lower = 0.0;
        double upper = 1.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(0.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testCreateIntervalsSetWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        double lower = 0.0;
        double upper = 1.0;
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
        boundary.add(lowerCut);
        boundary.add(upperCut);
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(0.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getInf(), Precision.EPSILON);

        intervalsSet = new IntervalsSet(-1.0, 0.0);
        assertEquals(-1.0, intervalsSet.getInf(), Precision.EPSILON);

        intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getInf());
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getSup(), Precision.EPSILON);

        intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), Precision.EPSILON);

        intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getSup());
    }

    @Test
    public void testAsList_EmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testAsList_SingleInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(0.0, interval.getInf(), Precision.EPSILON);
        assertEquals(1.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testAsList_MultipleIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(1.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(1).getInf(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(1).getSup(), Precision.EPSILON);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        intervalsSet.computeGeometricalProperties();

        // Check barycenter
        assertEquals(1.5, intervalsSet.getBarycenter().getX(), Precision.EPSILON);

        // Check size
        assertEquals(2.0, intervalsSet.getSize(), Precision.EPSILON);
    }
}