package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet emptySet = new IntervalsSet();
        assertEquals(0.0, emptySet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSup(), 0.0);
        assertEquals(0.0, emptySet.size(), 0.0);
        assertTrue(emptySet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithSingleInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertEquals(1.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(2.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 4.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(4.0, intervalsSet.getSup(), 0.0);
        assertEquals(3.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(3.0, list.get(0).getSup(), 0.0);
        assertEquals(2.0, list.get(1).getInf(), 0.0);
        assertEquals(4.0, list.get(1).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoNonOverlappingIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(4.0, intervalsSet.getSup(), 0.0);
        assertEquals(3.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(2.0, list.get(0).getSup(), 0.0);
        assertEquals(3.0, list.get(1).getInf(), 0.0);
        assertEquals(4.0, list.get(1).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithInfAndFiniteInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 2.0));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertEquals(1.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf());
        assertEquals(2.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithInfAndEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet(new IntervalsSet());
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.size(), 0.0);
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithEmptySetAndInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new IntervalsSet(), new Interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.size(), 0.0);
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithFiniteAndInfInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Infinity.POSITIVE, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithTwoOverlappingInfIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Infinity.POSITIVE, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithMergeableIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0), new Interval(3.0, 4.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(4.0, intervalsSet.getSup(), 0.0);
        assertEquals(3.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(4.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithMergeableInfIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 2.0), new Interval(3.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Infinity.POSITIVE, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf());
        assertEquals(Double.POSITIVE_INFINITY, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithNonMergeableIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0), new Interval(1.0, 3.0));
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.0);
        assertEquals(3.0, intervalsSet.size(), 0.0);
        assertFalse(intervalsSet.isEmpty());

        List<Interval> list = intervalsSet.asList();
        assertEquals(3, list.size());
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(2.0, list.get(0).getSup(), 0.0);
        assertEquals(1.0, list.get(1).getInf(), 0.0);
        assertEquals(3.0, list.get(1).getSup(), 0.0);
        assertEquals(1.0, list.get(2).getInf(), 0.0);
        assertEquals(2.0, list.get(2).getSup(), 0.0);
    }
}