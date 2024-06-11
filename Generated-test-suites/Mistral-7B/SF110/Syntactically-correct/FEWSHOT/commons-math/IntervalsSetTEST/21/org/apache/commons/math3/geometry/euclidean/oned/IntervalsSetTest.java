package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.util.Vector1D;
import org.apache.commons.math3.geometry.Region;

public class IntervalsSetTest {

    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet emptySet = new IntervalsSet();
        assertEquals(Collections.emptyList(), emptySet.asList());
        assertEquals(Double.NaN, emptySet.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSize());
        assertNull(emptySet.getInf());
        assertNull(emptySet.getSup());
    }

    @Test
    public void testIntervalsSetWithSingleInterval() {
        IntervalsSet singleIntervalSet = new IntervalsSet(new Interval(1.0, 2.0));
        List<Interval> expectedList = Collections.singletonList(new Interval(1.0, 2.0));
        assertEquals(expectedList, singleIntervalSet.asList());
        assertEquals(new Vector1D(1.5), singleIntervalSet.getBarycenter(), 0.0);
        assertEquals(1.0, singleIntervalSet.getSize());
        assertEquals(1.0, singleIntervalSet.getInf(), 0.0);
        assertEquals(2.0, singleIntervalSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithTwoIntervals() {
        IntervalsSet twoIntervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        List<Interval> expectedList = new ArrayList<>();
        expectedList.add(new Interval(0.0, 1.0));
        expectedList.add(new Interval(2.0, 3.0));
        assertEquals(expectedList, twoIntervalsSet.asList());
        assertEquals(new Vector1D(1.5), twoIntervalsSet.getBarycenter(), 0.0);
        assertEquals(2.0, twoIntervalsSet.getSize());
        assertEquals(0.0, twoIntervalsSet.getInf(), 0.0);
        assertEquals(3.0, twoIntervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithEmptyAndFullIntervals() {
        IntervalsSet emptyAndFullIntervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(5.0, Double.POSITIVE_INFINITY));
        List<Interval> expectedList = new ArrayList<>();
        expectedList.add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        expectedList.add(new Interval(5.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, emptyAndFullIntervalsSet.asList());
        assertEquals(Double.NaN, emptyAndFullIntervalsSet.getBarycenter(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, emptyAndFullIntervalsSet.getSize());
        assertNull(emptyAndFullIntervalsSet.getInf());
        assertEquals(5.0, emptyAndFullIntervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetWithNestedIntervals() {
        IntervalsSet nestedIntervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0), new Interval(3.5, 4.5));
        List<Interval> expectedList = new ArrayList<>();
        expectedList.add(new Interval(1.0, 2.0));
        expectedList.add(new Interval(3.5, 4.5));
        expectedList.add(new Interval(5.0, 6.0));
        expectedList.add(new Interval(3.0, 4.0));
        assertEquals(expectedList, nestedIntervalsSet.asList());
        assertEquals(new Vector1D(3.25), nestedIntervalsSet.getBarycenter(), 0.0);
        assertEquals(8.5, nestedIntervalsSet.getSize());
        assertEquals(1.0, nestedIntervalsSet.getInf(), 0.0);
        assertEquals(6.0, nestedIntervalsSet.getSup(), 0.0);
    }
}