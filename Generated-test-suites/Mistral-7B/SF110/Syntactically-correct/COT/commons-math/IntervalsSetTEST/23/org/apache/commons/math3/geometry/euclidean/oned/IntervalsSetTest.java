package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isEmpty());
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testIntervalsSetWithFiniteLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(2.0, intervalsSet.getSize(), 0.01);
        Vector1D barycenter = intervalsSet.getBarycenter();
        assertNotNull(barycenter);
        assertEquals(2.0, barycenter.getX(), 0.01);
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        Interval firstInterval = intervalsList.get(0);
        assertEquals(1.0, firstInterval.getInf(), 0.01);
        assertEquals(2.0, firstInterval.getSup(), 0.01);
        Interval secondInterval = intervalsList.get(1);
        assertEquals(2.0, secondInterval.getInf(), 0.01);
        assertEquals(3.0, secondInterval.getSup(), 0.01);
    }

    @Test
    public void testIntervalsSetWithInfiniteLowerAndFiniteUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        assertEquals(1.0, intervalsSet.getSize(), 0.01);
        Vector1D barycenter = intervalsSet.getBarycenter();
        assertNotNull(barycenter);
        assertEquals(2.0, barycenter.getX(), 0.01);
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        Interval interval = intervalsList.get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.01);
        assertEquals(3.0, interval.getSup(), 0.01);
    }

    @Test
    public void testIntervalsSetWithFiniteLowerAndInfiniteUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.01);
        Vector1D barycenter = intervalsSet.getBarycenter();
        assertNotNull(barycenter);
        assertTrue(barycenter.isNaN());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        Interval interval = intervalsList.get(0);
        assertEquals(1.0, interval.getInf(), 0.01);
        assertTrue(interval.getSup().isNaN());
    }

    @Test
    public void testIntervalsSetWithInfiniteLowerAndInfiniteUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.01);
        Vector1D barycenter = intervalsSet.getBarycenter();
        assertNotNull(barycenter);
        assertTrue(barycenter.isNaN());
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(0, intervalsList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeWithInvalidArguments() {
        IntervalsSet.buildTree(Double.NaN, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.01);
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.01);
        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.01);
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.01);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.01);
        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.01);
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.01);
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.01);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsList = intervalsSet.asList();
        assertEquals(0, intervalsList.size());
    }
}