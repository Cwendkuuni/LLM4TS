package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertFalse(intervalsSet.isEmpty());
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(3.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
    }

    @Test
    public void testConstructorWithBoundary() {
        SubHyperplane<Euclidean1D> lowerBound = new Line(new Vector1D(1.0), new Vector1D(2.0));
        SubHyperplane<Euclidean1D> upperBound = new Line(new Vector1D(4.0), new Vector1D(5.0));
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundary.add(lowerBound);
        boundary.add(upperBound);
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertFalse(intervalsSet.isEmpty());
    }

    @Test
    public void testBuildTreeWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 5.0);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(5.0, newIntervalsSet.getSup(), 0.0);
        assertEquals(0.0, newIntervalsSet.getInf(), 0.0);
        assertFalse(newIntervalsSet.isEmpty());
    }

    @Test
    public void testBuildTreeWithLowerAndUpperInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> treeNegativeInf = new BSPTree<Euclidean1D>(Boolean.TRUE);
        BSPTree<Euclidean1D> treeUpperInf = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), null);
        IntervalsSet newIntervalsSetNegativeInf = intervalsSet.buildNew(treeNegativeInf);
        IntervalsSet newIntervalsSetUpperInf = intervalsSet.buildNew(treeUpperInf);
        assertEquals(Double.NEGATIVE_INFINITY, newIntervalsSetNegativeInf.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, newIntervalsSetUpperInf.getSup(), 0.0);
        assertTrue(newIntervalsSetNegativeInf.isEmpty());
        assertFalse(newIntervalsSetUpperInf.isEmpty());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 2.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
        assertEquals(1.0, intervalsSet.getBarycenter().getX(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(0.0, 5.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(5.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        intervalsSet = new IntervalsSet(new Interval(-3.0, 3.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(6.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 5.0));
        assertEquals(0.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(2.0, 5.0));
        assertEquals(2.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(-3.0, 3.0));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 5.0));
        assertEquals(5.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(2.0, 5.0));
        assertEquals(5.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(new Interval(-3.0, 3.0));
        assertEquals(3.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 5.0));
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(0.0, 5.0), list.get(0));

        intervalsSet = new IntervalsSet(new Interval(0.0, 2.0), new Interval(4.0, 5.0));
        list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(new Interval(0.0, 2.0), list.get(0));
        assertEquals(new Interval(4.0, 5.0), list.get(1));
    }
}