package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import java.util.*;
import java.util.Collections;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(lower, upper);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Collections.emptyList(), intervalsSet.asList());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertEquals(-1.0, intervals.get(0).getInf(), 1e-12);
 assertEquals(1.0, intervals.get(1).getSup(), 1e-12);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(Collections.singletonList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)), intervals);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 assertEquals(intervalsList.get(0), intervalsList.get(0));
 assertEquals(intervalsList.get(1), intervalsList.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLowerBound() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, Double.NEGATIVE_INFINITY);
 fail();
 }

 @Test
 public void testBuildNewWithEmptyTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testBuildNewWithNonEmptyTree() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.FALSE, new BSPTree<Euclidean1D>(Boolean.TRUE), new BSPTree<Euclidean1D>(Boolean.TRUE, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), null));
 List<Interval> intervalsList = newIntervalsSet.asList();
 assertEquals(intervalsSet.asList(), intervalsList);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 intervalsSet.computeGeometricalProperties();
 double size = intervalsSet.getSize();
 double barycenter = intervalsSet.getBarycenter().getX();
 List<Interval> intervalsList = intervalsSet.asList();
 double sum = 0.0;
 for (Interval interval : intervalsList) {
 sum += interval.getSize() * interval.getBarycenter();
 }
 assertEquals(size, sum, 1e-12);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 1e-12);
 }

 @Test
 public void testGetInfWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 double inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 1e-12);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double sup = intervalsSet.getSup();
 assertEquals(1.0, sup, 1e-12);
 }

 @Test
 public void testGetSupWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 double sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup, 1e-12);
 }

 @Test
 public void testAsListWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(Collections.emptyList(), intervalsList);
 }

 @Test
 public void testAsListWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testListSizeWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 int size = intervalsSet.size();
 assertEquals(0, size);
 }

 @Test
 public void testListSizeWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 int size = intervalsSet.size();
 assertEquals(2, size);
 }

 @Test
 public void testIsEmptyWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 boolean isEmpty = intervalsSet.isEmpty();
 assertTrue(isEmpty);
 }

 @Test
 public void testIsEmptyWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 boolean isEmpty = intervalsSet.isEmpty();
 assertFalse(isEmpty);
 }

 @Test
 public void testContainsPointWithPointInside() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double point = 0.0;
 boolean containsPoint = intervalsSet.contains(new Point2D(point));
 assertTrue(containsPoint);
 }

 @Test
 public void testContainsPointWithPointOutsideLowerBound() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double point = -2.0;
 boolean containsPoint = intervalsSet.contains(new Point2D(point));
 assertFalse(containsPoint);
 }

 @Test
 public void testContainsPointWithPointOutsideUpperBound() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double point = 2.0;
 boolean containsPoint = intervalsSet.contains(new Point2D(point));
 assertFalse(containsPoint);
 }

 @Test
 public void testIntersectsWithEmptySet() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = new IntervalsSet();
 IntervalsSet intersect = intervalsSet1.intersection(intervalsSet2);
 assertEquals(new IntervalsSet(), intersect);
 }

 @Test
 public void testIntersectsWithOverlappingSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(1.0, 2.0);
 IntervalsSet intersect = intervalsSet1.intersection(intervalsSet2);
 List<Interval> intersectIntervals = intersect.asList();
 assertEquals(1, intersectIntervals.size());
 assertEquals(intervalsSet1.get(0).intersect(intervalsSet2.get(0)), intersectIntervals.get(0));
 }

 @Test
 public void testIntersectsWithDisjointSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 3.0);
 IntervalsSet intersect = intervalsSet1.intersection(intervalsSet2);
 assertEquals(new IntervalsSet(), intersect);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntersectionWithNull() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 IntervalsSet intersect = intervalsSet.intersection(null);
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testIntersectionWithNullBoundary() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 intervalsSet.intersection(null);
 fail();
 }

 @Test
 public void testUnionWithEmptySet() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = new IntervalsSet();
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 assertEquals(new IntervalsSet(), union);
 }

 @Test
 public void testUnionWithOverlappingSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(1.0, 2.0);
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 List<Interval> unionIntervals = union.asList();
 assertEquals(2, unionIntervals.size());
 assertEquals(intervalsSet1.get(0), unionIntervals.get(0));
 assertEquals(intervalsSet2.get(0), unionIntervals.get(1));
 }

 @Test
 public void testUnionWithDisjointSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 3.0);
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 List<Interval> unionIntervals = union.asList();
 assertEquals(2, unionIntervals.size());
 assertEquals(intervalsSet1.get(0), unionIntervals.get(0));
 assertEquals(intervalsSet2.get(0), unionIntervals.get(1));
 }

 @Test
 public void testUnionWithSelf() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 IntervalsSet union = intervalsSet.union(intervalsSet);
 List<Interval> unionIntervals = union.asList();
 assertEquals(2, unionIntervals.size());
 assertEquals(intervalsSet.get(0), unionIntervals.get(0));
 assertEquals(intervalsSet.get(0), unionIntervals.get(1));
 }

 @Test
 public void testSubtractWithEmptySet() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = new IntervalsSet();
 IntervalsSet difference = intervalsSet1.difference(intervalsSet2);
 assertEquals(new IntervalsSet(), difference);
 }

 @Test
 public void testSubtractWithOverlappingSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(0.5, 1.5);
 IntervalsSet difference = intervalsSet1.difference(intervalsSet2);
 List<Interval> differenceIntervals = difference.asList();
 assertEquals(1, differenceIntervals.size());
 assertEquals(intervalsSet1.get(0).subtract(intervalsSet2.get(0)), differenceIntervals.get(0));
 }

 @Test
 public void testSubtractWithDisjointSets() {
 IntervalsSet intervalsSet1 = createIntervalsSet(0.0, 1.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 3.0);
 IntervalsSet difference = intervalsSet1.difference(intervalsSet2);
 assertEquals(new IntervalsSet(), difference);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceWithNull() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 IntervalsSet difference = intervalsSet.difference(null);
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceWithNullBoundary() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 intervalsSet.difference(null);
 fail();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComplementWithNull() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 IntervalsSet complement = intervalsSet.complement(null);
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testComplementWithNullBoundary() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 intervalsSet.complement(null);
 fail();
 }
}