package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class IntervalsSetTest {
    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        assertEquals(Collections.emptyList(), intervalsSet.asList());
    }

    @Test
    public void testFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1, intervalsSet.size());
        assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());
        assertFalse(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
    }

    @Test
    public void testInfiniteIntervalsSetBelow() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testInfiniteIntervalsSetAbove() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testFiniteAndInfiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(1, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0);
        assertTrue(tree.isLeaf());
        assertTrue(tree.getCut() == null);
        assertEquals(new Interval(1.0, 2.0), tree.getAttribute());

        tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
        assertTrue(tree.isLeaf());
        assertTrue(tree.getCut() == null);
        assertNull(tree.getAttribute());

        tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
        assertTrue(tree.isLeaf());
        assertTrue(tree.getCut() == null);
        assertNull(tree.getAttribute());

        tree = IntervalsSet.buildTree(1.0, 2.0, 3.0);
        assertFalse(tree.isLeaf());
        assertNull(tree.getCut());
        assertNull(tree.getAttribute());

        tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0, Double.POSITIVE_INFINITY);
        assertFalse(tree.isLeaf());
        assertNull(tree.getCut());
        assertNull(tree.getAttribute());

        tree = IntervalsSet.buildTree(-1.0, 1.0, 2.0);
        assertFalse(tree.isLeaf());
        assertNotNull(tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(1.0), true), tree.getCut().getHyperplane());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(intervalsSet, newIntervalsSet);

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(intervalsSet, newIntervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(6.0, intervalsSet.getSize());
        assertEquals(new Vector1D(2.5), intervalsSet.getBarycenter());
        assertFalse(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isBounded());
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
        assertEquals(new Interval(3.0, 4.0), list.get(1));
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(1.0, intervalsSet.getInf());

        intervalsSet = new IntervalsSet(new Interval(-1.0, 1.0));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());

        intervalsSet = new IntervalsSet(new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf());

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(4.0, intervalsSet.getSup());

        intervalsSet = new IntervalsSet(new Interval(-1.0, 1.0));
        assertEquals(1.0, intervalsSet.getSup());

        intervalsSet = new IntervalsSet(new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
        assertEquals(new Interval(3.0, 4.0), list.get(1));

        intervalsSet = new IntervalsSet(new Interval(-1.0, 1.0));
        list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(-1.0, 1.0), list.get(0));

        intervalsSet = new IntervalsSet(new Interval(1.0, Double.POSITIVE_INFINITY));
        list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, Double.POSITIVE_INFINITY), list.get(0));

        intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), list.get(0));
    }
}