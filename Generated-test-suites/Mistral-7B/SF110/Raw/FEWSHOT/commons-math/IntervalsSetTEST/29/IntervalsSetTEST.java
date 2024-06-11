package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet is = new IntervalsSet();
        assertTrue(is.isEmpty());
        assertEquals(0, is.size());
        assertNull(is.getInf());
        assertNull(is.getSup());
    }

    @Test
    public void testConstructorWithDouble() {
        IntervalsSet is = new IntervalsSet(0.0, 1.0);
        assertFalse(is.isEmpty());
        assertEquals(1, is.size());
        assertEquals(0.0, is.getInf(), 0.001);
        assertEquals(1.0, is.getSup(), 0.001);
    }

    @Test
    public void testConstructorWithBSPTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
        IntervalsSet is = new IntervalsSet(tree);
        assertFalse(is.isEmpty());
        assertEquals(1, is.size());
        assertEquals(0.0, is.getInf(), 0.001);
        assertEquals(0.0, is.getSup(), 0.001);
    }

    @Test
    public void testConstructorWithCollection() {
        List<Interval> intervals = new ArrayList<Interval>();
        intervals.add(new Interval(0.0, 1.0));
        IntervalsSet is = new IntervalsSet(intervals);
        assertFalse(is.isEmpty());
        assertEquals(1, is.size());
        assertEquals(0.0, is.getInf(), 0.001);
        assertEquals(1.0, is.getSup(), 0.001);
    }

    @Test
    public void testBuildTreeWithNull() {
        assertNull(IntervalsSet.buildTree(null, 1.0));
    }

    @Test
    public void testBuildTreeWithLowerLessThanUpper() {
        IntervalsSet is = new IntervalsSet();
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 1.0);
        IntervalsSet newIs = is.buildNew(tree);
        assertFalse(is.isEmpty());
        assertFalse(newIs.isEmpty());
        assertEquals(1, is.size());
        assertEquals(1, newIs.size());
        assertEquals(0.0, is.getInf(), 0.001);
        assertEquals(1.0, is.getSup(), 0.001);
        assertEquals(0.0, newIs.getInf(), 0.001);
        assertEquals(1.0, newIs.getSup(), 0.001);
        assertTrue(is.equals(newIs));
        assertTrue(newIs.equals(is));
    }

    @Test
    public void testBuildTreeWithLowerGreaterThanUpper() {
        IntervalsSet is = new IntervalsSet();
        assertNull(IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0));
    }

    @Test
    public void testBuildTreeWithBothInfinite() {
        IntervalsSet is = new IntervalsSet();
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        IntervalsSet newIs = is.buildNew(tree);
        assertTrue(is.isEmpty());
        assertTrue(newIs.isEmpty());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet is = new IntervalsSet(new Interval(0.0, 1.0));
        is.computeGeometricalProperties();
        assertEquals(1.0, is.size(), 0.001);
        assertEquals(0.5, is.getBarycenter().getX(), 0.001);
    }

    @Test
    public void testGetInf() {
        IntervalsSet is = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, is.getInf());
        is = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(0.0, is.getInf(), 0.001);
    }

    @Test
    public void testGetSup() {
        IntervalsSet is = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, is.getSup());
        is = new IntervalsSet(new Interval(0.0, 1.0));
        assertEquals(1.0, is.getSup(), 0.001);
    }

    @Test
    public void testAsList() {
        IntervalsSet is = new IntervalsSet(new Interval(0.0, 1.0));
        List<Interval> list = is.asList();
        assertEquals(1, list.size());
        assertEquals(0.0, list.get(0).getInf(), 0.001);
        assertEquals(1.0, list.get(0).getSup(), 0.001);
    }
}