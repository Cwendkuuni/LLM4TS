package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmpty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetConstruction() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), 0.0);
        assertEquals(2.0, intervals.get(0).getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetBoundary() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0),
                new Interval(4.0, Double.POSITIVE_INFINITY)
        ));

        assertEquals(3, intervalsSet.size());
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(3, intervalsList.size());
        assertEquals(0.0, intervalsList.get(0).getInf(), 0.0);
        assertEquals(1.0, intervalsList.get(0).getSup(), 0.0);
        assertEquals(2.0, intervalsList.get(1).getInf(), 0.0);
        assertEquals(3.0, intervalsList.get(1).getSup(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsList.get(2).getInf(), 0.0);
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervalsSet = IntervalsSet.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
        assertEquals(1, intervalsSet.size());
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0),
                new Interval(4.0, Double.POSITIVE_INFINITY)
        ));

        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertNull(intervalsSet.getBarycenter());

        List<Interval> intervalsList = intervalsSet.asList();
        double sum = 0.0;
        for (Interval interval : intervalsList) {
            sum += interval.getSize();
        }

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(new Vector1D(sum / intervalsSet.size()), intervalsSet.getBarycenter());

        intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0)));
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getSize());
        assertEquals(new Vector1D(0.5), intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0),
                new Interval(4.0, Double.POSITIVE_INFINITY)
        ));
        assertEquals(0.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0)));
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0),
                new Interval(4.0, Double.POSITIVE_INFINITY)
        ));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, Double.POSITIVE_INFINITY)));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
                new Interval(0.0, 1.0),
                new Interval(2.0, 3.0),
                new Interval(4.0, Double.POSITIVE_INFINITY)
        ));

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(3, intervalsList.size());

        intervalsSet = new IntervalsSet(Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0)));
        intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
    }

    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
        List<Interval> intervalsList = new ArrayList<>();

        intervalsSet.recurseList(intervalsSet.getTree(false), intervalsList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsList.size());
        assertEquals(1.0, intervalsList.get(0).getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsList.get(0).getSup(), 0.0);
    }
}