package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet set = new IntervalsSet();
        assertEquals(0.0, set.getSize(), 0.0);
        assertFalse(set.contains(new Vector1D(1.0)));
        assertTrue(set.isEmpty());
    }

    @Test
    public void testSetWithSingleInterval() {
        IntervalsSet set = new IntervalsSet(new Interval(3.0, 5.0));
        assertEquals(2.0, set.getSize(), 0.0);
        assertTrue(set.contains(new Vector1D(4.0)));
        assertFalse(set.contains(new Vector1D(1.0)));
        assertFalse(set.isEmpty());
    }

    @Test
    public void testSetWithMultipleIntervals() {
        IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0), new Interval(7.0, 8.0)));
        assertEquals(5.0, set.getSize(), 0.0);
        assertTrue(set.contains(new Vector1D(2.5)));
        assertFalse(set.contains(new Vector1D(0.5)));
        assertFalse(set.isEmpty());
    }

    @Test
    public void testInfAndNegInfSets() {
        IntervalsSet set1 = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, set1.getSize(), 0.0);
        assertTrue(set1.contains(new Vector1D(10.0)));
        assertTrue(set1.contains(new Vector1D(-10.0)));
        assertFalse(set1.isEmpty());

        IntervalsSet set2 = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertEquals(Double.POSITIVE_INFINITY, set2.getSize(), 0.0);
        assertTrue(set2.contains(new Vector1D(10.0)));
        assertTrue(set2.contains(new Vector1D(-10.0)));
        assertFalse(set2.isEmpty());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet set = new IntervalsSet(3.0, 5.0);
        BSPTree<Euclidean1D> tree = set.getTree(false);
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(3.0, op.getLocation().getX(), 0.0);
        op = (OrientedPoint) tree.getMinus().getCut().getHyperplane();
        assertEquals(5.0, op.getLocation().getX(), 0.0);
    }

    @Test
    public void testGetInfAndSup() {
        IntervalsSet set = new IntervalsSet(new Interval(3.0, 5.0));
        assertEquals(5.0, set.getSup(), 0.0);
        assertEquals(3.0, set.getInf(), 0.0);
    }

    @Test
    public void testGetList() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(4.0, 5.0), new Interval(7.0, 8.0));
        List<Interval> list = set.asList();
        assertEquals(3, list.size());
        assertTrue(list.get(0).getInf() == 1.0);
        assertTrue(list.get(0).getSup() == 2.0);
        assertTrue(list.get(1).getInf() == 4.0);
        assertTrue(list.get(1).getSup() == 5.0);
        assertTrue(list.get(2).getInf() == 7.0);
        assertTrue(list.get(2).getSup() == 8.0);
    }
}