package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet is = new IntervalsSet();
        assertEquals(0, is.size(), 0.0);
        assertEquals(Collections.emptyList(), is.asList());
        assertNull(is.getInf());
        assertNull(is.getSup());
    }

    @Test
    public void testIntervalsSetWithSingleInterval() {
        IntervalsSet is = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1, is.size(), 1.0);
        List<Interval> list = is.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
        assertEquals(1.5, is.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, is.getSup(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        IntervalsSet is = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        assertEquals(2, is.size(), 2.0);
        List<Interval> list = is.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(0.0, 1.0), list.get(0));
        assertEquals(new Interval(2.0, 3.0), list.get(1));
        assertEquals(0.5, is.getInf(), Precision.SAFE_EPSILON);
        assertEquals(3.0, is.getSup(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testIntervalsSetWithInfAndFiniteInterval() {
        IntervalsSet is = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(2.0, Double.POSITIVE_INFINITY));
        assertEquals(1, is.size(), Double.POSITIVE_INFINITY);
        List<Interval> list = is.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, 1.0), list.get(0));
        assertEquals(Double.POSITIVE_INFINITY, is.getInf(), Precision.SAFE_EPSILON);
        assertEquals(2.0, is.getSup(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testIntervalsSetWithEmptyAndFullInterval() {
        IntervalsSet is = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(1, is.size(), Double.POSITIVE_INFINITY);
        List<Interval> list = is.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), list.get(0));
        assertEquals(Double.NEGATIVE_INFINITY, is.getInf());
        assertEquals(Double.POSITIVE_INFINITY, is.getSup());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet is = new IntervalsSet();
        BSPTree<Euclidean1D> tree = is.buildTree(0.0, 3.0);
        IntervalsSet is2 = is.buildNew(tree);
        assertEquals(2, is2.size(), 2.0);
        List<Interval> list = is2.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(0.0, 1.0), list.get(0));
        assertEquals(new Interval(2.0, 3.0), list.get(1));
        assertEquals(0.5, is2.getInf(), Precision.SAFE_EPSILON);
        assertEquals(3.0, is2.getSup(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testMergeIntervals() {
        IntervalsSet is1 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        IntervalsSet is2 = new IntervalsSet(new IntervalsSet(new Interval(1.0, 2.0)), new Interval(3.0, 4.0));
        IntervalsSet merged = new IntervalsSet(is1, is2);
        assertEquals(3, merged.size(), 3.0);
        List<Interval> list = merged.asList();
        assertEquals(3, list.size());
        assertEquals(new Interval(0.0, 2.0), list.get(0));
        assertEquals(new Interval(3.0, 4.0), list.get(1));
        assertEquals(Double.NEGATIVE_INFINITY, merged.getInf());
        assertEquals(4.0, merged.getSup(), Precision.SAFE_EPSILON);
    }
}