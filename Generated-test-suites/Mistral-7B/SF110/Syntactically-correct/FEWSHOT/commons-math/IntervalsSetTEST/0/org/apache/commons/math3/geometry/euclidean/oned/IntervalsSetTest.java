package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet set = new IntervalsSet();
        assertEquals(Collections.emptyList(), set.asList());
        assertEquals(Double.NaN, set.getBarycenter(), 0.0);
        assertEquals(0.0, set.getSize(), 0.0);
        assertNotNull(set.getTree(false));
        assertFalse(set.contains(new Vector1D(0.5)));
    }

    @Test
    public void testSetWithSingleInterval() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
        List<Interval> list = set.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
        assertEquals(new Vector1D(1.5), set.getBarycenter(), 0.0);
        assertEquals(1.0, set.getSize(), 0.0);
        assertTrue(set.contains(new Vector1D(1.5)));
        assertFalse(set.contains(new Vector1D(0.5)));
    }

    @Test
    public void testSetWithMultipleIntervals() {
        IntervalsSet set = new IntervalsSet(Arrays.asList(
                new Interval(1.0, 2.0),
                new Interval(3.0, 4.0),
                new Interval(5.0, 6.0)
        ));
        List<Interval> list = set.asList();
        assertEquals(3, list.size());
        Interval interval1 = list.get(0);
        Interval interval2 = list.get(1);
        Interval interval3 = list.get(2);
        assertEquals(new Interval(1.0, 2.0), interval1);
        assertEquals(new Interval(3.0, 4.0), interval2);
        assertEquals(new Interval(5.0, 6.0), interval3);
        assertEquals(new Vector1D(3.5), set.getBarycenter(), 0.0);
        assertEquals(5.0, set.getSize(), 0.0);
        assertTrue(set.contains(new Vector1D(3.5)));
        assertFalse(set.contains(new Vector1D(0.5)));
    }

    @Test
    public void testSetWithInfiniteIntervals() {
        IntervalsSet set1 = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        List<Interval> list1 = set1.asList();
        assertEquals(1, list1.size());
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, 0.0), list1.get(0));
        assertEquals(Double.NaN, set1.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, set1.getSize(), 0.0);
        assertTrue(set1.contains(new Vector1D(-1.0)));
        assertFalse(set1.contains(new Vector1D(0.5)));

        IntervalsSet set2 = new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY));
        List<Interval> list2 = set2.asList();
        assertEquals(1, list2.size());
        assertEquals(new Interval(0.0, Double.POSITIVE_INFINITY), list2.get(0));
        assertEquals(Double.NaN, set2.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, set2.getSize(), 0.0);
        assertTrue(set2.contains(new Vector1D(1.0)));
        assertFalse(set2.contains(new Vector1D(0.5)));
    }

    @Test
    public void testSetWithEmptyTree() {
        IntervalsSet set = new IntervalsSet(new BSPTree<>());
        assertEquals(Collections.emptyList(), set.asList());
        assertEquals(Double.NaN, set.getBarycenter(), 0.0);
        assertEquals(0.0, set.getSize(), 0.0);
        assertNotNull(set.getTree(false));
        assertFalse(set.contains(new Vector1D(0.5)));
    }

    @Test
    public void testSetWithComplexTree() {
        IntervalsSet set = new IntervalsSet(new BSPTree<>(
                new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(),
                new BSPTree<>(
                        new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane(),
                        new BSPTree<>(
                                new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane(),
                                new BSPTree<>(Boolean.TRUE),
                                new BSPTree<>(Boolean.TRUE),
                                null
                        ),
                        new BSPTree<>(Boolean.FALSE),
                        new BSPTree<>(Boolean.TRUE),
                        null
                ),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null
        ));
        List<Interval> list = set.asList();
        assertEquals(3, list.size());
        Interval interval1 = list.get(0);
        Interval interval2 = list.get(1);
        Interval interval3 = list.get(2);
        assertEquals(new Interval(1.0, 3.0), interval1);
        assertEquals(new Interval(5.0, Double.POSITIVE_INFINITY), interval2);
        assertEquals(new Interval(3.0, 5.0), interval3);
        assertEquals(new Vector1D(3.5), set.getBarycenter(), 0.0);
        assertEquals(5.0, set.getSize(), 0.0);
        assertTrue(set.contains(new Vector1D(3.5)));
        assertFalse(set.contains(new Vector1D(0.5)));
    }

    @Test
    public void testGetInf() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1.0, set.getInf(), 0.0);

        set = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 0.0);

        set = new IntervalsSet(new BSPTree<>());
        assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(2.0, set.getSup(), 0.0);

        set = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 0.0);

        set = new IntervalsSet(new BSPTree<>());
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 0.0);
    }
}