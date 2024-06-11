package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervals = new IntervalsSet();
        assertNotNull(intervals.getTree(false));
        assertTrue(intervals.isEmpty());
        assertEquals(0, intervals.size());
        assertNull(intervals.getInf());
        assertNull(intervals.getSup());
        List<Interval> intervalsList = intervals.asList();
        assertTrue(intervalsList.isEmpty());
    }

    @Test
    public void testIntervalsSetWithOneInterval() {
        IntervalsSet intervals = new IntervalsSet(new Interval(1.0, 2.0));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(1.5, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
    }

    @Test
    public void testIntervalsSetWithMultipleIntervals() {
        IntervalsSet intervals = new IntervalsSet(Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0),
                new Interval(5.0, 6.0)
        ));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(3, intervals.size());
        assertEquals(1.5, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(6.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(3, intervalsList.size());
        Collections.sort(intervalsList);
        List<Interval> expectedList = Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0),
                new Interval(5.0, 6.0)
        );
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithNegativeAndPositiveInfinities() {
        IntervalsSet intervals = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(0, intervals.size());
        assertNull(intervals.getInf());
        assertEquals(Double.POSITIVE_INFINITY, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertTrue(intervalsList.isEmpty());
    }

    @Test
    public void testIntervalsSetWithOneIntervalAndNegativeInfinity() {
        IntervalsSet intervals = new IntervalsSet(new Interval(1.0, 2.0), Double.NEGATIVE_INFINITY);
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(1.5, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
    }

    @Test
    public void testIntervalsSetWithOneIntervalAndPositiveInfinity() {
        IntervalsSet intervals = new IntervalsSet(new Interval(1.0, 2.0), Double.POSITIVE_INFINITY);
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(1.5, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
    }

    @Test
    public void testIntervalsSetWithMultipleIntervalsAndNegativeInfinity() {
        IntervalsSet intervals = new IntervalsSet(
                Arrays.asList(
                        new Interval(1.0, 2.0),
                        new Interval(3.0, 4.0),
                        new Interval(5.0, 6.0)
                ),
                Double.NEGATIVE_INFINITY
        );
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(3, intervals.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(6.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(3, intervalsList.size());
        Collections.sort(intervalsList);
        List<Interval> expectedList = Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0),
                new Interval(5.0, 6.0)
        );
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithMultipleIntervalsAndPositiveInfinity() {
        IntervalsSet intervals = new IntervalsSet(
                Arrays.asList(
                        new Interval(1.0, 2.0),
                        new Interval(3.0, 4.0),
                        new Interval(5.0, 6.0)
                ),
                Double.POSITIVE_INFINITY
        );
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(3, intervals.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(6.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(3, intervalsList.size());
        Collections.sort(intervalsList);
        List<Interval> expectedList = Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0),
                new Interval(5.0, 6.0)
        );
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testIntervalsSetWithEmptyCollection() {
        IntervalsSet intervals = new IntervalsSet(Collections.emptyList());
        assertNotNull(intervals.getTree(false));
        assertTrue(intervals.isEmpty());
        assertEquals(0, intervals.size());
        assertNull(intervals.getInf());
        assertNull(intervals.getSup());
        List<Interval> intervalsList = intervals.asList();
        assertTrue(intervalsList.isEmpty());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervals = new IntervalsSet(buildTree(1.0, 2.0));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
    }

    @Test
    public void testBuildTreeWithLowerThanMinusInfinity() {
        IntervalsSet intervals = new IntervalsSet(buildTree(Double.MIN_VALUE - 1.0, 2.0));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(Double.MIN_VALUE, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(Double.MIN_VALUE, 2.0), intervalsList.get(0));
    }

    @Test
    public void testBuildTreeWithUpperThanPositiveInfinity() {
        IntervalsSet intervals = new IntervalsSet(buildTree(1.0, Double.MAX_VALUE));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(Double.MAX_VALUE, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, Double.MAX_VALUE), intervalsList.get(0));
    }

    @Test
    public void testBuildTreeWithLowerThanMinusInfinityAndUpperThanPositiveInfinity() {
        IntervalsSet intervals = new IntervalsSet(buildTree(Double.MIN_VALUE - 1.0, Double.MAX_VALUE));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(0, intervals.size());
        assertNull(intervals.getInf());
        assertNull(intervals.getSup());
        List<Interval> intervalsList = intervals.asList();
        assertTrue(intervalsList.isEmpty());
    }

    @Test
    public void testBuildTreeWithMultipleIntervals() {
        IntervalsSet intervals = new IntervalsSet(buildTree(1.0, 2.0, 3.0, 4.0));
        assertNotNull(intervals.getTree(false));
        assertFalse(intervals.isEmpty());
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(4.0, intervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> intervalsList = intervals.asList();
        assertEquals(2, intervalsList.size());
        Collections.sort(intervalsList);
        List<Interval> expectedList = Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0)
        );
        assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervals = new IntervalsSet();
        IntervalsSet newIntervals = intervals.buildNew(buildTree(1.0, 2.0));
        assertNotNull(newIntervals.getTree(false));
        assertFalse(newIntervals.isEmpty());
        assertEquals(1, newIntervals.size());
        assertEquals(1.0, newIntervals.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, newIntervals.getSup(), Precision.SAFE_EPSILON);
        List<Interval> newIntervalsList = newIntervals.asList();
        assertEquals(1, newIntervalsList.size());
        assertEquals(new Interval(1.0, 2.0), newIntervalsList.get(0));
    }
}