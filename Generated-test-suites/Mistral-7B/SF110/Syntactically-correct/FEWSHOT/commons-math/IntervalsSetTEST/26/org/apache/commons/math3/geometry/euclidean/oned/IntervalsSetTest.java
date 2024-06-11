package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1, intervalsSet.size());
        assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(new Interval(1.0, 2.0), intervals.get(0));
    }

    @Test
    public void testInfiniteLowerBoundIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 2.0);
        assertEquals(1, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(-1.0, 2.0), intervalsList.get(0));
    }

    @Test
    public void testInfiniteUpperBoundIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(1.0, Double.POSITIVE_INFINITY), intervalsList.get(0));
    }

    @Test
    public void testTwoIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(2, intervalsSet.size());

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(3.0, 4.0), intervalsList.get(1));
    }

    @Test
    public void testMultiplyIntervalsSet() {
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet intervalsSet2 = intervalsSet1.union(new IntervalsSet(new Interval(2.0, 3.0), new Interval(4.0, 5.0)));

        List<Interval> intervalsList = intervalsSet2.asList();
        assertEquals(4, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(2.0, 3.0), intervalsList.get(1));
        assertEquals(new Interval(3.0, 4.0), intervalsList.get(2));
        assertEquals(new Interval(4.0, 5.0), intervalsList.get(3));
    }

    @Test
    public void testInfAndFiniteIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(2, intervalsSet.size());

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), intervalsList.get(1));
    }

    @Test
    public void testInfIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), intervalsList.get(0));
    }

    @Test
    public void testEmptyIntersectionIntervalsSet() {
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(5.0, 6.0), new Interval(7.0, 8.0));

        IntervalsSet intersectionSet = intervalsSet1.intersection(intervalsSet2);
        assertEquals(0, intersectionSet.size());
    }

    @Test
    public void testNonEmptyIntersectionIntervalsSet() {
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(2.0, 3.0), new Interval(3.0, 4.0));

        IntervalsSet intersectionSet = intervalsSet1.intersection(intervalsSet2);
        assertEquals(1, intersectionSet.size());
        assertEquals(new Interval(3.0, 4.0), intersectionSet.asList().get(0));
    }

    @Test
    public void testUnionIntervalsSet() {
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(2.0, 3.0), new Interval(4.0, 5.0));

        IntervalsSet unionSet = intervalsSet1.union(intervalsSet2);
        assertEquals(3, unionSet.size());
        List<Interval> intervalsList = unionSet.asList();
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(2.0, 3.0), intervalsList.get(1));
        assertEquals(new Interval(3.0, 5.0), intervalsList.get(2));
    }

    @Test
    public void testDifferenceIntervalsSet() {
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(2.0, 3.0));

        IntervalsSet differenceSet = intervalsSet1.subtract(intervalsSet2);
        assertEquals(2, differenceSet.size());
        List<Interval> intervalsList = differenceSet.asList();
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(3.0, 4.0), intervalsList.get(1));
    }

    @Test
    public void testSubtractEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet emptyIntervalsSet = new IntervalsSet();

        IntervalsSet differenceSet = intervalsSet.subtract(emptyIntervalsSet);
        assertEquals(2, differenceSet.size());
        List<Interval> intervalsList = differenceSet.asList();
        assertEquals(new Interval(1.0, 2.0), intervalsList.get(0));
        assertEquals(new Interval(3.0, 4.0), intervalsList.get(1));
    }

    @Test
    public void testGetInfAndSup() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(2.0, intervalsSet.getSup(), 1e-14);
        assertEquals(-1.0, intervalsSet.getInf(), 1e-14);
    }

    @Test
    public void testGetInfAndSupForSingleInterval() {
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
        assertEquals(1.0, intervalsSet.getInf(), 1e-14);
        assertEquals(1.0, intervalsSet.getSup(), 1e-14);
    }

    @Test
    public void testGetInfAndSupForEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 1e-14);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 1e-14);
    }
}