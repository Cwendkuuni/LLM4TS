package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.util.Precision;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet set = new IntervalsSet();
        assertEquals(Collections.emptyList(), set.asList());
        assertNull(set.getInf());
        assertNull(set.getSup());
    }

    @Test
    public void testFiniteSet() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 3.0));
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(1.0, set.getInf());
        assertEquals(3.0, set.getSup());
    }

    @Test
    public void testLowerInfiniteSet() {
        IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervals.get(0).getInf());
        assertEquals(3.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(0.0, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(Double.NEGATIVE_INFINITY, set.getInf());
        assertEquals(3.0, set.getSup());
    }

    @Test
    public void testUpperInfiniteSet() {
        IntervalsSet set = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(1.0, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(1.0, set.getInf());
        assertEquals(Double.POSITIVE_INFINITY, set.getSup());
    }

    @Test
    public void testInfAndInfSet() {
        IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        List<Interval> intervals = set.asList();
        assertEquals(0, intervals.size());
        assertNull(set.getInf());
        assertNull(set.getSup());
    }

    @Test
    public void testSplitSet() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
        List<Interval> intervals = set.asList();
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(4.0, intervals.get(1).getInf(), Precision.EPSILON);
        assertEquals(6.0, intervals.get(1).getSup(), Precision.EPSILON);
        assertEquals(4.5, intervals.get(1).getBarycenter(), Precision.EPSILON);
        assertEquals(1.0, set.getInf());
        assertEquals(6.0, set.getSup());
    }

    @Test
    public void testSplitSetWithOverlap() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
        List<Interval> intervals = set.asList();
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(1).getInf(), Precision.EPSILON);
        assertEquals(5.0, intervals.get(1).getSup(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(1).getBarycenter(), Precision.EPSILON);
        assertEquals(1.0, set.getInf());
        assertEquals(5.0, set.getSup());
    }

    @Test
    public void testSplitSetWithTouchingIntervals() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0));
        List<Interval> intervals = set.asList();
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(0).getSup(), Precision.EPSILON);
        assertEquals(1.5, intervals.get(0).getBarycenter(), Precision.EPSILON);
        assertEquals(2.0, intervals.get(1).getInf(), Precision.EPSILON);
        assertEquals(3.0, intervals.get(1).getSup(), Precision.EPSILON);
        assertEquals(2.5, intervals.get(1).getBarycenter(), Precision.EPSILON);
        assertEquals(1.0, set.getInf());
        assertEquals(3.0, set.getSup());
    }
}