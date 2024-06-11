package org.example.junit;

import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree(false).getCut());
    }

    @Test
    public void testIntervalsSetWithLimits() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), 0.001);
        assertEquals(5.0, interval.getSup(), 0.001);
    }

    @Test
    public void testIntervalsSetWithBSPTree() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(lower, interval.getInf(), 0.001);
        assertEquals(upper, interval.getSup(), 0.001);
    }

    @Test
    public void testBuildTreeWithFiniteLimits() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(3, tree.getDepth());
        // Add tests for edge cases and exceptions here
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
        intervalsSet.computeGeometricalProperties();
        double barycenter = intervalsSet.getBarycenter().getX();
        double size = intervalsSet.getSize();
        assertEquals(3.0, barycenter, 0.001);
        if (size >= Precision.SAFE_MIN) {
            assertEquals(10.0, size, 0.001);
        }
        // Add tests for edge cases and exceptions here
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.001);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.001);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), 0.001);
        assertEquals(5.0, interval.getSup(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeWithLowerThanZero() {
        double lower = -1.0;
        double upper = 5.0;
        assertThrows(IllegalArgumentException.class, () -> IntervalsSet.buildTree(lower, upper));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeWithUpperThanZeroAndLowerThanLower() {
        double lower = 2.0;
        double upper = 1.0;
        assertThrows(IllegalArgumentException.class, () -> IntervalsSet.buildTree(lower, upper));
    }

    @Test
    public void testGetInfWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.001);
    }

    @Test
    public void testGetInfWithSetContainingOnePoint() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 1.0));
        double inf = intervalsSet.getInf();
        assertEquals(1.0, inf, 0.001);
    }

    @Test
    public void testGetSupWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.001);
    }

    @Test
    public void testGetSupWithSetContainingOnePoint() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 1.0));
        double sup = intervalsSet.getSup();
        assertEquals(1.0, sup, 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAsListWithModification() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
        List<Interval> intervals = intervalsSet.asList();
        intervals.add(new Interval(6.0, 7.0));
        assertThrows(UnsupportedOperationException.class, () -> intervalsSet.computeGeometricalProperties());
    }

    @Test
    public void testBuildTreeWithFiniteLimitsAndEmptySet() {
        double lower = 1.0;
        double upper = 1.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(0, intervals.size());
    }

    @Test
    public void testGetInfWithSetContainingOnePointAtZero() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        double inf = intervalsSet.getInf();
        assertEquals(0.0, inf, 0.001);
    }

    @Test
    public void testGetInfWithSetContainingOnePointAtInfinity() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(4.0, Double.POSITIVE_INFINITY));
        double inf = intervalsSet.getInf();
        assertEquals(4.0, inf, 0.001);
    }

    @Test
    public void testGetSupWithSetContainingOnePointAtZero() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0));
        double sup = intervalsSet.getSup();
        assertEquals(1.0, sup, 0.001);
    }

    @Test
    public void testGetSupWithSetContainingOnePointAtNegativeInfinity() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, -1.0));
        double sup = intervalsSet.getSup();
        assertEquals(-1.0, sup, 0.001);
    }
}