package org.junit.test;

import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmpty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(5, intervals.size());
        for (int i = 0; i < intervals.size(); i++) {
            double expectedStart = i * 1 + 0.0;
            double expectedEnd = (i + 1) * 1 + 0.0;
            Interval interval = intervals.get(i);
            assertEquals(expectedStart, interval.getInf(), 0.0);
            assertEquals(expectedEnd, interval.getSup(), 0.0);
        }
    }

    @Test
    public void testIntervalsSetWithBSPTree() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(bspTree);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(5, intervals.size());
        for (int i = 0; i < intervals.size(); i++) {
            double expectedStart = i * 1 + lower;
            double expectedEnd = (i + 1) * 1 + lower;
            Interval interval = intervals.get(i);
            assertEquals(expectedStart, interval.getInf(), 0.0);
            assertEquals(expectedEnd, interval.getSup(), 0.0);
        }
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        double barycenter = intervalsSet.getBarycenter().getX();
        double size = intervalsSet.getSize();
        double expectedBarycenter = (lower + upper) / 2.0;
        double expectedSize = (upper - lower) * intervalsSet.size();
        assertEquals(expectedBarycenter, barycenter, 0.0);
        assertEquals(expectedSize, size, 0.0);
    }

    @Test
    public void testGetInf() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double inf = intervalsSet.getInf();
        assertEquals(lower, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double sup = intervalsSet.getSup();
        assertEquals(upper, sup, 0.0);
    }

    @Test
    public void testAsList() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(5, intervals.size());
        for (int i = 0; i < intervals.size(); i++) {
            double expectedStart = i * 1 + lower;
            double expectedEnd = (i + 1) * 1 + lower;
            Interval interval = intervals.get(i);
            assertEquals(expectedStart, interval.getInf(), 0.0);
            assertEquals(expectedEnd, interval.getSup(), 0.0);
        }
    }

    // Add test methods for edge cases and exceptions
}