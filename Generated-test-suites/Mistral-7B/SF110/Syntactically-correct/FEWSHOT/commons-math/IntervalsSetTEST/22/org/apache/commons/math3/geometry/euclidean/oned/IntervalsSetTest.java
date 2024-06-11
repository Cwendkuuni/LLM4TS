package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmptyConstruction() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.isEmpty());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetConstructionWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertFalse(intervalsSet.isEmpty());
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), 0.0);
        assertEquals(3.0, intervals.get(1).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetConstructionWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertTrue(intervalsSet.isEmpty());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertFalse(newIntervalsSet.isEmpty());
        assertEquals(2.0, newIntervalsSet.getSize(), 0.0);
        assertNull(newIntervalsSet.getBarycenter());

        List<Interval> intervals = newIntervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(Double.NEGATIVE_INFINITY, intervals.get(0).getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervals.get(1).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetConstructionWithBoundary() {
        Point2D p1 = new Point2D(1.0);
        Point2D p2 = new Point2D(2.0);
        SubHyperplane<Euclidean2D> boundary = new Line(p1, p2).wholeHyperplane();

        IntervalsSet intervalsSet = new IntervalsSet(Collections.singletonList(boundary));
        assertFalse(intervalsSet.isEmpty());
        assertEquals(1.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), 0.0);
        assertEquals(2.0, intervals.get(0).getSup(), 0.0);
    }

    @Test
    public void testBuildTree() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;

        BSPTree<Euclidean1D> tree1 = IntervalsSet.buildTree(lower, upper);
        assertTrue(tree1.isLeaf());
        assertTrue(tree1.getAttribute());

        double lower1 = 1.0;
        double upper1 = 3.0;
        BSPTree<Euclidean1D> tree2 = IntervalsSet.buildTree(lower1, upper1);
        assertFalse(tree2.isLeaf());
        assertFalse(tree2.getAttribute());

        OrientedPoint op1 = (OrientedPoint)tree2.getCut().getHyperplane();
        assertEquals(new Vector1D(lower1), op1.getNormal());
        assertEquals(Vector1D.ZERO, op1.getOffset());

        OrientedPoint op2 = (OrientedPoint)tree2.getMinus().getCut().getHyperplane();
        assertEquals(new Vector1D(upper1), op2.getNormal());
        assertEquals(Vector1D.ZERO, op2.getOffset());

        OrientedPoint op3 = (OrientedPoint)tree2.getPlus().getCut().getHyperplane();
        assertEquals(new Vector1D(Double.POSITIVE_INFINITY), op3.getNormal());
        assertEquals(Vector1D.ZERO, op3.getOffset());
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 0.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        intervalsSet.computeGeometricalProperties();
        assertEquals(3.0, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), 0.0);
        assertEquals(1.5, intervals.get(0).getSize(), 0.0);
        assertEquals(1.5, intervals.get(0).getBarycenter().getX(), 0.0);

        assertEquals(2.5, intervals.get(1).getInf(), 0.0);
        assertEquals(1.0, intervals.get(1).getSize(), 0.0);
        assertEquals(2.5, intervals.get(1).getBarycenter().getX(), 0.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());

        assertEquals(0.0, intervals.get(0).getInf(), 0.0);
        assertEquals(1.5, intervals.get(0).getSize(), 0.0);
        assertEquals(1.5, intervals.get(0).getBarycenter().getX(), 0.0);

        assertEquals(2.5, intervals.get(1).getInf(), 0.0);
        assertEquals(1.0, intervals.get(1).getSize(), 0.0);
        assertEquals(2.5, intervals.get(1).getBarycenter().getX(), 0.0);
    }

    @Test
    public void testRecurseList() {
        double lower = 0.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> list = new ArrayList<Interval>();
        intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(2, list.size());

        assertEquals(0.0, list.get(0).getInf(), 0.0);
        assertEquals(1.5, list.get(0).getSize(), 0.0);
        assertEquals(1.5, list.get(0).getBarycenter().getX(), 0.0);

        assertEquals(2.5, list.get(1).getInf(), 0.0);
        assertEquals(1.0, list.get(1).getSize(), 0.0);
        assertEquals(2.5, list.get(1).getBarycenter().getX(), 0.0);
    }
}