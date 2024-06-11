package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private BSPTree<Euclidean1D> tree;

 @Before
 public void setUp() {
 IntervalsSet intervalsSet = new IntervalsSet();
 tree = IntervalsSet.buildTree(0.0, 5.0);
 this.intervalsSet = intervalsSet.buildNew(tree);
 }

 @Test
 public void testEmptySet() {
 IntervalsSet emptySet = new IntervalsSet();
 assertEquals(0.0, emptySet.getInf(), 0.01);
 assertEquals(Double.POSITIVE_INFINITY, emptySet.getSup(), 0.01);
 assertTrue(emptySet.asList().isEmpty());
 }

 @Test
 public void testSingletonSet() {
 IntervalsSet singletonSet = new IntervalsSet(new Interval(2.0, 3.0));
 assertEquals(2.0, singletonSet.getInf(), 0.01);
 assertEquals(3.0, singletonSet.getSup(), 0.01);
 List<Interval> list = singletonSet.asList();
 assertEquals(1, list.size());
 assertEquals(2.0, list.get(0).getInf(), 0.01);
 assertEquals(3.0, list.get(0).getSup(), 0.01);
 }

 @Test
 public void testFiniteSet() {
 IntervalsSet finiteSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
 assertEquals(1.0, finiteSet.getInf(), 0.01);
 assertEquals(7.0, finiteSet.getSup(), 0.01);
 List<Interval> list = finiteSet.asList();
 assertEquals(2, list.size());
 assertEquals(1.0, list.get(0).getInf(), 0.01);
 assertEquals(3.0, list.get(0).getSup(), 0.01);
 assertEquals(5.0, list.get(1).getInf(), 0.01);
 assertEquals(7.0, list.get(1).getSup(), 0.01);
 }

 @Test
 public void testInfiniteSetBelow() {
 IntervalsSet infiniteSetBelow = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(Double.NEGATIVE_INFINITY, infiniteSetBelow.getInf(), 0.01);
 assertEquals(0.0, infiniteSetBelow.getSup(), 0.01);
 List<Interval> list = infiniteSetBelow.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testInfiniteSetAbove() {
 IntervalsSet infiniteSetAbove = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(0.0, infiniteSetAbove.getInf(), 0.01);
 assertEquals(Double.POSITIVE_INFINITY, infiniteSetAbove.getSup(), 0.01);
 List<Interval> list = infiniteSetAbove.asList();
 assertTrue(list.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSet() {
 IntervalsSet invalidSet = new IntervalsSet(new Interval(5.0, 3.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCollection() {
 IntervalsSet emptyCollectionSet = new IntervalsSet(new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCollection() {
 IntervalsSet nullCollectionSet = new IntervalsSet(null);
 }

 @Test
 public void testBuildTreeWithLowerThanUpper() {
 BSPTree<Euclidean1D> treeWithLowerThanUpper = IntervalsSet.buildTree(1.0, 5.0);
 IntervalsSet intervalsSetWithTree = new IntervalsSet(treeWithLowerThanUpper);
 assertEquals(1.0, intervalsSetWithTree.getInf(), 0.01);
 assertEquals(5.0, intervalsSetWithTree.getSup(), 0.01);
 }

 @Test
 public void testBuildTreeWithUpperLessThanLower() {
 BSPTree<Euclidean1D> treeWithUpperLessThanLower = IntervalsSet.buildTree(5.0, 1.0);
 IntervalsSet intervalsSetWithTree = new IntervalsSet(treeWithUpperLessThanLower);
 assertEquals(1.0, intervalsSetWithTree.getInf(), 0.01);
 assertEquals(5.0, intervalsSetWithTree.getSup(), 0.01);
 }

 @Test
 public void testComputeGeometricalProperties() {
 double[] barycenters = {2.5, 4.5};
 for (int i = 0; i < 2; i++) {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(i * 2.0, (i + 1) * 2.0));
 intervalsSet.computeGeometricalProperties();
 assertEquals(barycenters[i], intervalsSet.getBarycenter().getX(), 0.01);
 }
 intervalsSet.computeGeometricalProperties();
 Interval last = intervalsSet.asList().get(intervalsSet.asList().size() - 1);
 assertEquals(last.getSup(), intervalsSet.getBarycenter().getX(), 0.01);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = this.intervalsSet;
 assertEquals(0.0, intervalsSet.getInf(), 0.01);
 intervalsSet = new IntervalsSet(new Interval(2.0, 3.0));
 assertEquals(2.0, intervalsSet.getInf(), 0.01);
 intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.01);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = this.intervalsSet;
 assertEquals(5.0, intervalsSet.getSup(), 0.01);
 intervalsSet = new IntervalsSet(new Interval(2.0, 3.0));
 assertEquals(3.0, intervalsSet.getSup(), 0.01);
 intervalsSet = new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.01);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = this.intervalsSet;
 List<Interval> list = intervalsSet.asList();
 assertEquals(5, list.size());
 for (int i = 0; i < 5; i++) {
 Interval interval = list.get(i);
 double x = i * 1.0;
 assertEquals(x, interval.getInf(), 0.01);
 assertEquals(x + 1.0, interval.getSup(), 0.01);
 }
 intervalsSet = new IntervalsSet(new Interval(2.0, 3.0));
 list = intervalsSet.asList();
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(2.0, interval.getInf(), 0.01);
 assertEquals(3.0, interval.getSup(), 0.01);
 }
}