package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new Euclidean1D(lower), new Euclidean1D(upper));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 assertEquals(1, intervalsSet.size());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(1.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(1, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 assertEquals(2, intervalsSet.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 0.0)));
 }

 @Test
 public void testBuildTreeWithFiniteLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), null, null, null), null);
 assertEquals(expectedTree, tree);
 }

 @Test
 public void testBuildTreeWithLowerInfiniteAndUpperFinite() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 assertEquals(expectedTree, tree);
 }

 @Test
 public void testBuildTreeWithLowerFiniteAndUpperInfinite() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 assertEquals(expectedTree, tree);
 }

 @Test
 public void testBuildTreeWithLowerAndUpperInfinite() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 assertEquals(expectedTree, tree);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 intervalsSet.computeGeometricalProperties();
 double size = intervalsSet.size();
 double barycenterX = intervalsSet.getBarycenter().getX();
 assertEquals(1.0, size, 0.0);
 assertEquals(0.5, barycenterX, 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 double inf = intervalsSet.getInf();
 assertEquals(-Double.MAX_VALUE, inf, 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 double sup = intervalsSet.getSup();
 assertEquals(1.0, sup, 0.0);
 }

 @Test
 public void testAsListEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 assertEquals(0, list.size());
 }

 @Test
 public void testAsListSingleInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(1.0, interval.getSup(), 0.0);
 }

 @Test
 public void testAsListMultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 Interval interval1 = list.get(0);
 assertEquals(0.0, interval1.getInf(), 0.0);
 assertEquals(1.0, interval1.getSup(), 0.0);
 Interval interval2 = list.get(1);
 assertEquals(2.0, interval2.getInf(), 0.0);
 assertEquals(3.0, interval2.getSup(), 0.0);
 }
}