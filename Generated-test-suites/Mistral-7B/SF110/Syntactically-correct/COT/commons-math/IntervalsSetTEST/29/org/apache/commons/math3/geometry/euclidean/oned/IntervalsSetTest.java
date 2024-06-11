package org.apache.commons.math3.geometry.euclidean.oned.test;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double... values) {
 IntervalsSet intervalsSet = new IntervalsSet();
 for (double value : values) {
 intervalsSet.add(value);
 }
 return intervalsSet;
 }

 @Test
 public void testIntervalsSetConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 // Test default constructor
 assertTrue(intervalsSet.isEmpty());

 IntervalsSet intervalsSetWithValues = new IntervalsSet(1.0, 2.0);
 // Test constructor with two values
 assertEquals(2, intervalsSetWithValues.size());
 assertEquals(1.0, intervalsSetWithValues.getInf(), 0.0);
 assertEquals(2.0, intervalsSetWithValues.getSup(), 0.0);

 IntervalsSet intervalsSetWithTree = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 // Test constructor with empty tree
 assertTrue(intervalsSetWithTree.isEmpty());

 IntervalsSet intervalsSetWithBoundary = new IntervalsSet(Arrays.asList(new Line(1.0, 1.0, 0.0), new Line(2.0, 0.0, 1.0)));
 // Test constructor with boundary
 assertEquals(2, intervalsSetWithBoundary.size());
 }

 @Test
 public void testIntervalsSetBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 // Test buildNew with empty tree
 assertTrue(newIntervalsSet.isEmpty());

 IntervalsSet intervalsSetWithValues = new IntervalsSet(1.0, 2.0);
 BSPTree<Euclidean1D> tree = intervalsSetWithValues.getTree(false);
 IntervalsSet newIntervalsSet2 = intervalsSet.buildNew(tree);
 // Test buildNew with tree created from constructor with two values
 assertEquals(2, newIntervalsSet2.size());
 assertEquals(1.0, newIntervalsSet2.getInf(), 0.0);
 assertEquals(2.0, newIntervalsSet2.getSup(), 0.0);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 // Test computeGeometricalProperties with four values
 assertEquals(3, intervalsSet.size());
 double size = 0.0;
 double sum = 0.0;
 List<Interval> list = intervalsSet.asList();
 for (Interval interval : list) {
 size += interval.getSize();
 sum += interval.getSize() * interval.getBarycenter();
 }
 assertEquals(6.0, size, 0.0);
 assertEquals(1.0, sum / size, 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 assertEquals(-1.0, intervalsSet.getInf(), 0.0);

 IntervalsSet intervalsSetWithInfiniteInf = new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSetWithInfiniteInf.getInf(), 0.0);

 IntervalsSet intervalsSetWithInfiniteSup = new IntervalsSet();
 intervalsSetWithInfiniteSup.add(Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithInfiniteSup.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 assertEquals(3.0, intervalsSet.getSup(), 0.0);

 IntervalsSet intervalsSetWithInfiniteInf = new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0);
 assertEquals(1.0, intervalsSetWithInfiniteInf.getSup(), 0.0);

 IntervalsSet intervalsSetWithInfiniteSup = new IntervalsSet();
 intervalsSetWithInfiniteSup.add(Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithInfiniteSup.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 List<Interval> list = intervalsSet.asList();
 // Test asList with four values
 assertEquals(4, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(-1.0, list.get(0).getSup(), 0.0);
 assertEquals(1.0, list.get(1).getInf(), 0.0);
 assertEquals(1.0, list.get(1).getSup(), 0.0);
 assertEquals(2.0, list.get(2).getInf(), 0.0);
 assertEquals(2.0, list.get(2).getSup(), 0.0);
 assertEquals(3.0, list.get(3).getInf(), 0.0);
 assertEquals(3.0, list.get(3).getSup(), 0.0);
 }

 @Test
 public void testListEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 // Test asList with empty IntervalsSet
 assertEquals(0, list.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyList() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 List<Interval> list = intervalsSet.asList();
 // Test modification of list returned by asList
 list.remove(0);
 }

 @Test
 public void testAdd() {
 IntervalsSet intervalsSet = new IntervalsSet();
 // Test add with null value
 assertFalse(intervalsSet.add(null));

 IntervalsSet intervalsSetWithValues = new IntervalsSet(1.0, 2.0);
 intervalsSet.add(3.0);
 // Test add with value not in set
 assertEquals(2, intervalsSet.size());

 IntervalsSet intervalsSetWithValuesAndDuplicate = new IntervalsSet(1.0, 2.0);
 intervalsSet.add(2.0);
 // Test add with duplicate value
 assertEquals(2, intervalsSet.size());
 }

 @Test
 public void testRemove() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 // Test remove with value not in set
 assertFalse(intervalsSet.remove(4.0));

 List<Interval> list = intervalsSet.asList();
 intervalsSet.remove(list.get(1));
 // Test remove with value in set
 assertEquals(2, intervalsSet.size());
 }

 @Test
 public void testContains() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 // Test contains with value not in set
 assertFalse(intervalsSet.contains(4.0));

 List<Interval> list = intervalsSet.asList();
 assertTrue(intervalsSet.contains(list.get(1).getInf()));
 assertTrue(intervalsSet.contains(list.get(1).getSup()));
 }

 @Test
 public void testCheckPoint() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0, 2.0, 3.0);
 // Test checkPoint with point inside set
 assertTrue(intervalsSet.checkPoint(new Point2D(1.5), null) == Region.Location.INSIDE);

 // Test checkPoint with point on boundary
 assertTrue(intervalsSet.checkPoint(new Point2D(1.0), null) == Region.Location.BOUNDARY);

 // Test checkPoint with point outside set
 assertTrue(intervalsSet.checkPoint(new Point2D(0.5), null) == Region.Location.OUTSIDE);
 }

 @Test
 public void testRecurseList() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(1.0);
 intervalsSet.add(3.0);
 intervalsSet.add(2.0);
 List<Interval> list = new ArrayList<Interval>();
 intervalsSet.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 // Test recurseList with three values
 assertEquals(3, list.size());
 assertEquals(1.0, list.get(0).getInf(), 0.0);
 assertEquals(1.0, list.get(0).getSup(), 0.0);
 assertEquals(2.0, list.get(1).getInf(), 0.0);
 assertEquals(2.0, list.get(1).getSup(), 0.0);
 assertEquals(3.0, list.get(2).getInf(), 0.0);
 assertEquals(3.0, list.get(2).getSup(), 0.0);
 }
}