package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree(false));
        assertEquals(Collections.emptyList(), intervalsSet.asList());
        assertEquals(Double.NaN, intervalsSet.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
        assertNull(intervalsSet.getInf());
        assertNull(intervalsSet.getSup());
    }

    @Test
    public void testIntervalSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0));
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(1, tree.getNumberOfNodes());
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(1.0, intervalsSet.getBarycenter().getX(), Precision.EPSILON);
        assertEquals(2.0, intervalsSet.getSize());
        assertEquals(1.0, intervalsSet.getInf());
        assertEquals(3.0, intervalsSet.getSup());
    }

    @Test
    public void testIntervalSetWithInfAndNegInf() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(1, tree.getNumberOfNodes());
        assertEquals(0, intervalsSet.asList().size());
        assertNull(intervalsSet.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
    }

    @Test
    public void testIntervalSetWithThreeIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(4, tree.getNumberOfNodes());
        assertEquals(3, intervalsSet.asList().size());
        List<Interval> expectedList = new ArrayList<>();
        expectedList.add(new Interval(1.0, 2.0));
        expectedList.add(new Interval(3.0, 4.0));
        expectedList.add(new Interval(5.0, 6.0));
        assertEquals(expectedList, intervalsSet.asList());
        assertEquals(6.0, intervalsSet.getSize());
        assertEquals(1.0, intervalsSet.getInf());
        assertEquals(6.0, intervalsSet.getSup());
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 3.0);
        assertEquals(3, tree.getNumberOfNodes());
        BSPTree<Euclidean1D> node1 = tree.getMinus();
        assertEquals(1, node1.getNumberOfNodes());
        BSPTree<Euclidean1D> node2 = node1.getPlus();
        assertEquals(1, node2.getNumberOfNodes());
        assertEquals(Boolean.FALSE, node1.getAttribute());
        assertEquals(Boolean.TRUE, node2.getAttribute());
        assertEquals(1.0, node2.getCut().getHyperplane().getLocation().getX(), Precision.EPSILON);
    }

    @Test
    public void testBuildTreeWithInfAndNegInf() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, tree.getNumberOfNodes());
        assertEquals(Boolean.TRUE, tree.getAttribute());
    }

    @Test
    public void testBuildTreeWithThreeIntervals() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0, 3.0, 4.0);
        assertEquals(5, tree.getNumberOfNodes());
        BSPTree<Euclidean1D> node1 = tree.getMinus();
        assertEquals(1, node1.getNumberOfNodes());
        BSPTree<Euclidean1D> node2 = node1.getPlus();
        assertEquals(1, node2.getNumberOfNodes());
        assertEquals(Boolean.FALSE, node1.getAttribute());
        assertEquals(Boolean.FALSE, node2.getAttribute());
        OrientedPoint cut1 = (OrientedPoint)node2.getCut().getHyperplane();
        assertEquals(2.0, cut1.getLocation().getX(), Precision.EPSILON);
        BSPTree<Euclidean1D> node3 = node2.getMinus();
        assertEquals(1, node3.getNumberOfNodes());
        BSPTree<Euclidean1D> node4 = node3.getPlus();
        assertEquals(1, node4.getNumberOfNodes());
        assertEquals(Boolean.FALSE, node3.getAttribute());
        assertEquals(Boolean.TRUE, node4.getAttribute());
        OrientedPoint cut2 = (OrientedPoint)node4.getCut().getHyperplane();
        assertEquals(3.0, cut2.getLocation().getX(), Precision.EPSILON);
        BSPTree<Euclidean1D> node5 = node4.getMinus();
        assertEquals(1, node5.getNumberOfNodes());
        BSPTree<Euclidean1D> node6 = node5.getPlus();
        assertEquals(1, node6.getNumberOfNodes());
        assertEquals(Boolean.FALSE, node5.getAttribute());
        assertEquals(Boolean.TRUE, node6.getAttribute());
        OrientedPoint cut3 = (OrientedPoint)node6.getCut().getHyperplane();
        assertEquals(4.0, cut3.getLocation().getX(), Precision.EPSILON);
        assertEquals(Boolean.TRUE, tree.getPlus().getAttribute());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 3.0);
        IntervalsSet intervalsSet = IntervalsSet.buildNew(tree);
        assertEquals(3, intervalsSet.getTree(false).getNumberOfNodes());
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(1.0, intervalsSet.getInf());
        assertEquals(3.0, intervalsSet.getSup());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
        intervalsSet.computeGeometricalProperties();
        assertEquals(6.0, intervalsSet.getSize());
        assertEquals(2.5, intervalsSet.getBarycenter().getX(), Precision.EPSILON);
    }
}