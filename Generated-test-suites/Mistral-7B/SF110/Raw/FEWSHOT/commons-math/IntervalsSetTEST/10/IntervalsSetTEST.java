package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.util.Vector1D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree(false));
        assertNull(intervalsSet.getInf());
        assertNull(intervalsSet.getSup());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetWithLowerUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(new OrientedPoint(new Vector1D(0.0), false), tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(5.0), true), tree.getRightChild().getCut());
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSize(), 0.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(0.0, 5.0), intervals.get(0));
        assertEquals(new Interval(5.0, Double.POSITIVE_INFINITY), intervals.get(1));
    }

    @Test
    public void testIntervalsSetWithTree() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(lower), false), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(upper), true), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree(false));
        assertEquals(new OrientedPoint(new Vector1D(0.0), false), tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(5.0), true), tree.getRightChild().getCut());
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSize(), 0.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(0.0, 5.0), intervals.get(0));
        assertEquals(new Interval(5.0, Double.POSITIVE_INFINITY), intervals.get(1));
    }

    @Test
    public void testIntervalsSetWithCollection() {
        double lower = 0.0;
        double upper = 5.0;
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundary.add(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet.getBoundary());
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(new OrientedPoint(new Vector1D(0.0), false), tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(5.0), true), tree.getRightChild().getCut());
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSize(), 0.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(0.0, 5.0), intervals.get(0));
        assertEquals(new Interval(5.0, Double.POSITIVE_INFINITY), intervals.get(1));
    }

    @Test
    public void testBuildTreeWithLowerUpper() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(new OrientedPoint(new Vector1D(lower), false), tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(upper), true), tree.getRightChild().getCut());
        assertEquals(5.0, tree.getRightChild().getRightChild().getInf(), 0.0);
        assertEquals(0.0, tree.getCut().getLocation().getX(), 0.0);
    }

    @Test
    public void testBuildTreeWithInfiniteLower() {
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, upper);
        assertEquals(new BSPTree<Euclidean1D>(Boolean.TRUE), tree);
        assertEquals(new OrientedPoint(new Vector1D(upper), true), tree.getRightChild().getCut());
        assertEquals(5.0, tree.getRightChild().getInf(), 0.0);
    }

    @Test
    public void testBuildTreeWithInfiniteUpper() {
        double lower = 0.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, Double.POSITIVE_INFINITY);
        assertEquals(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), tree);
        assertEquals(lower, tree.getCut().getLocation().getX(), 0.0);
    }

    @Test
    public void testBuildTreeWithLowerAndUpper() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(new OrientedPoint(new Vector1D(lower), false), tree.getCut());
        assertEquals(new OrientedPoint(new Vector1D(upper), true), tree.getRightChild().getCut());
        assertEquals(5.0, tree.getRightChild().getRightChild().getInf(), 0.0);
        assertEquals(0.0, tree.getCut().getLocation().getX(), 0.0);
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(lower), false), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(upper), true), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), null));
        intervalsSet.computeGeometricalProperties();
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSize(), 0.0);
        if (Double.isInfinite(intervalsSet.getSize())) {
            assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        } else {
            assertEquals(5.0, intervalsSet.getBarycenter().getX(), Precision.SAFE_EPSILON);
        }
    }
}