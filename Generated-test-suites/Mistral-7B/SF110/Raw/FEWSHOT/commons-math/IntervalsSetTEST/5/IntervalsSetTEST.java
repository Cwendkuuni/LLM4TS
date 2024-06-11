package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptySet() {
        IntervalsSet emptySet = new IntervalsSet();
        assertTrue(emptySet.isEmpty());
        assertNull(emptySet.getInf());
        assertNull(emptySet.getSup());
        assertTrue(emptySet.contains(Vector1D.ZERO));
        List<Interval> emptyList = emptySet.asList();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testSingletonSet() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
        assertFalse(set.isEmpty());
        assertEquals(1.0, set.getInf(), 1e-14);
        assertEquals(2.0, set.getSup(), 1e-14);
        assertTrue(set.contains(Vector1D.ONE));
        assertTrue(set.contains(Vector1D.TWO));
        assertFalse(set.contains(Vector1D.ZERO));
        List<Interval> list = set.asList();
        assertEquals(1, list.size());
        assertEquals(new Interval(1.0, 2.0), list.get(0));
    }

    @Test
    public void testIntervalSetBuilding() {
        IntervalsSet set1 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        IntervalsSet set2 = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        IntervalsSet set3 = new IntervalsSet(Arrays.asList(new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
        IntervalsSet set4 = new IntervalsSet(new Interval(1.0, 2.0), set3);

        assertEquals(set2, set1);
        assertEquals(set4, set3.union(set1));

        IntervalsSet set5 = new IntervalsSet();
        set5.add(new Interval(1.0, 2.0));
        set5.add(new Interval(3.0, 4.0));
        assertEquals(set5, set4);

        IntervalsSet set6 = new IntervalsSet();
        set6.add(new Interval(2.0, 3.0));
        set6.add(new Interval(4.0, 5.0));
        assertEquals(set5, set6.subtract(set1));
    }

    @Test
    public void testBoundaryCalculation() {
        IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        List<SubHyperplane<Euclidean1D>> boundary = set.getBoundary();
        assertEquals(3, boundary.size());
        SubHyperplane<Euclidean1D> upperCut = boundary.get(0);
        assertEquals(new OrientedPoint(new Vector1D(2.0), true), upperCut.getHyperplane());
        SubHyperplane<Euclidean1D> lowerCut1 = boundary.get(1);
        assertEquals(new OrientedPoint(new Vector1D(1.0), false), lowerCut1.getHyperplane());
        SubHyperplane<Euclidean1D> lowerCut2 = boundary.get(2);
        assertEquals(new OrientedPoint(new Vector1D(3.0), false), lowerCut2.getHyperplane());
    }

    @Test
    public void testSizeAndBarycenter() {
        IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        assertEquals(2.0, set.getSize(), 1e-14);
        assertEquals(new Vector1D((1.0 + 3.0) / 2.0), set.getBarycenter(), 1e-14);
    }

    @Test
    public void testIntervalsWithInfiniteBounds() {
        IntervalsSet set = new IntervalsSet(new Interval(-Double.MAX_VALUE, Double.MAX_VALUE));
        assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 1e-14);
        assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 1e-14);
        assertTrue(set.contains(Vector1D.NEGATIVE_INFINITY));
        assertTrue(set.contains(Vector1D.POSITIVE_INFINITY));
        assertTrue(set.isEmpty());
    }

    @Test
    public void testIntervalsWithEmptyIntervals() {
        IntervalsSet set = new IntervalsSet(new Interval(1.0, 1.0));
        assertEquals(0.0, set.getSize(), 1e-14);
        assertNull(set.getBarycenter());
        List<Interval> emptyList = set.asList();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testIntervalsWithComplexStructure() {
        IntervalsSet set = new IntervalsSet();
        set.add(new Interval(0.0, 1.0));
        set.add(new Interval(2.0, 3.0));
        set.add(new Interval(4.0, 5.0));
        set.add(new Interval(6.0, 7.0));
        set.add(new Interval(8.0, Double.POSITIVE_INFINITY));

        assertEquals(5, set.size());
        List<Interval> list = set.asList();
        assertEquals(new Interval(0.0, 1.0), list.get(0));
        assertEquals(new Interval(2.0, 3.0), list.get(1));
        assertEquals(new Interval(4.0, 5.0), list.get(2));
        assertEquals(new Interval(6.0, 7.0), list.get(3));
        assertEquals(new Interval(0.0, Double.POSITIVE_INFINITY), list.get(4));
    }
}