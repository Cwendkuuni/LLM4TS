package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.GeometryConfiguration;
import org.apache.commons.math3.geometry.Euclidean1D;
import org.apache.commons.math3.geometry.Partitioning;
import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.util.Precision;
import org.apache.commons.math3.geometry.vector.Vector1D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0.0, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        double lower = 1.0;
        double upper = 4.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(1.0, intervals.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(2.0, intervals.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(3.0, intervals.get(1).getInf(), Precision.SAFE_MIN);
        assertEquals(4.0, intervals.get(1).getSup(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testIntervalsSetWithOneInterval() {
        double lower = 1.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(lower, upper));

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(lower, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(upper, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(1.5, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(3.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testIntervalsSetWithEmptyCollection() {
        IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
        assertEquals(0.0, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertTrue(intervalsSet.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithNullCollection() {
        new IntervalsSet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithNullLower() {
        new IntervalsSet(null, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithNaNLower() {
        new IntervalsSet(Double.NaN, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithInfLower() {
        new IntervalsSet(Double.POSITIVE_INFINITY, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntervalsSetWithInfUpper() {
        new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testIntervalsSetWithNegativeInfUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, Double.NEGATIVE_INFINITY);
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(1, intervalsList.size());
        assertEquals(1.0, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(1.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testIntervalsSetWithTwoOverlappingIntervals() {
        double lower = 1.0;
        double upper1 = 3.0;
        double upper2 = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(lower, upper1), new Interval(upper1, upper2));

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(lower, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(upper1, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(upper1, intervalsList.get(1).getInf(), Precision.SAFE_MIN);
        assertEquals(upper2, intervalsList.get(1).getSup(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testIntervalsSetWithTwoNonOverlappingIntervals() {
        double lower1 = 1.0;
        double upper1 = 3.0;
        double lower2 = 5.0;
        double upper2 = 7.0;

        IntervalsSet intervalsSet = new IntervalsSet(new Interval(lower1, upper1), new Interval(lower2, upper2));

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(lower1, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(upper1, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(lower2, intervalsList.get(1).getInf(), Precision.SAFE_MIN);
        assertEquals(upper2, intervalsList.get(1).getSup(), Precision.SAFE_MIN);
        assertEquals(3.0, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(7.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testBuildNew() {
        double lower = 1.0;
        double upper = 4.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet = intervalsSet.buildNew(tree);

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(1.0, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(2.0, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(3.0, intervalsList.get(1).getInf(), Precision.SAFE_MIN);
        assertEquals(4.0, intervalsList.get(1).getSup(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getInf(), Precision.SAFE_MIN);
        assertEquals(5.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 1.0;
        double upper = 4.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        intervalsSet.computeGeometricalProperties();

        assertEquals(3.0, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertNotNull(intervalsSet.getBarycenter());
        assertEquals(2.5, intervalsSet.getBarycenter().getX(), Precision.SAFE_MIN);
    }

    @Test
    public void testGetInf() {
        double lower = 1.0;
        double upper = 4.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), Precision.SAFE_MIN);
    }

    @Test
    public void testGetSup() {
        double lower = 1.0;
        double upper = 4.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        assertEquals(5.0, intervalsSet.getSup(), Precision.SAFE_MIN);
    }

    @Test
    public void testAsList() {
        double lower = 1.0;
        double upper = 4.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(1.0, intervalsList.get(0).getInf(), Precision.SAFE_MIN);
        assertEquals(2.0, intervalsList.get(0).getSup(), Precision.SAFE_MIN);
        assertEquals(3.0, intervalsList.get(1).getInf(), Precision.SAFE_MIN);
        assertEquals(4.0, intervalsList.get(1).getSup(), Precision.SAFE_MIN);
    }
}