package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertTrue(intervalsSet.isEmpty());
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0));
        assertEquals(1, intervalsSet.size());
        assertEquals(new Vector1D(2.0), intervalsSet.getBarycenter());
        assertFalse(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isEmpty());
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 3.0), list.get(0));
    }

    @Test
    public void testIntervalsSetWithInfAndFinite() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, Double.POSITIVE_INFINITY));
        assertEquals(2, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertFalse(intervalsSet.isEmpty());
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(1.0, 3.0), list.get(0));
        assertEquals(new Interval(5.0, Double.POSITIVE_INFINITY), list.get(1));
    }

    @Test
    public void testIntervalsSetWithInfAndInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        assertEquals(1, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertTrue(intervalsSet.isEmpty());
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testIntervalsSetWithEmptyCollection() {
        IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isInfinite());
        assertTrue(intervalsSet.isEmpty());
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testBuildTree() {
        double lower = 1.0;
        double upper = 3.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(1, intervalsSet.size());
        assertEquals(new Interval(lower, upper), intervalsSet.asList().get(0));
    }

    @Test
    public void testBuildTreeWithInf() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(1, intervalsSet.size());
        assertTrue(intervalsSet.isInfinite());
    }

    @Test
    public void testBuildTreeWithInfAndFinite() {
        double lower = 1.0;
        double upper = 3.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(lower, upper), tree);
        assertEquals(1, intervalsSet.size());
        assertEquals(new Interval(lower, upper), intervalsSet.asList().get(0));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
        intervalsSet.computeGeometricalProperties();
        double size = intervalsSet.size();
        double barycenterX = intervalsSet.getBarycenter().getX();
        double inf = intervalsSet.getInf();
        double sup = intervalsSet.getSup();
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, size);
        assertEquals(4.5, barycenterX, 0.01);
        assertEquals(1.0, inf);
        assertEquals(7.0, sup);
        assertEquals(2, list.size());
        assertEquals(new Interval(1.0, 3.0), list.get(0));
        assertEquals(new Interval(5.0, 7.0), list.get(1));
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
        assertEquals(1.0, intervalsSet.getInf());
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
        assertEquals(7.0, intervalsSet.getSup());
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(1.0, 3.0), list.get(0));
        assertEquals(new Interval(5.0, 7.0), list.get(1));
    }
}