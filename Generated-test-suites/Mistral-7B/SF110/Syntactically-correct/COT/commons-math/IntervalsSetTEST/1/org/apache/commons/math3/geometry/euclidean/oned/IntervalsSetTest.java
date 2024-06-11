package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.numberOfIntervals());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(1, intervalsSet.numberOfIntervals());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane()));
 assertEquals(0, intervalsSet.numberOfIntervals());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0)));
 assertEquals(2, intervalsSet.numberOfIntervals());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(newIntervalsSet, new IntervalsSet(tree));
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList_empty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testAsList_multipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAsList_intervalsOverlap() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(2.0, 4.0)));
 List<Interval> list = intervalsSet.asList();
 list.remove(1);
 assertEquals(1, list.size());
 intervalsSet = new IntervalsSet(list);
 list = intervalsSet.asList();
 assertEquals(1, list.size());
 list.remove(0);
 assertEquals(0, list.size());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 intervalsSet.computeGeometricalProperties();
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(2.5, barycenter, 0.0);
 }

 @Test
 public void testNumberOfIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(1, intervalsSet.numberOfIntervals());
 }

 @Test
 public void testNumberOfIntervals_multipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0)));
 assertEquals(2, intervalsSet.numberOfIntervals());
 }

 @Test(expected = IllegalStateException.class)
 public void testNumberOfIntervals_noTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.numberOfIntervals();
 }

 @Test
 public void testCheckPoint_outside() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(Region.Location.OUTSIDE, intervalsSet.checkPoint(new Euclidean1D(10.0), new Vector1D(0.0, 10.0)));
 }

 @Test
 public void testCheckPoint_inside() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(Region.Location.INSIDE, intervalsSet.checkPoint(new Euclidean1D(3.0), new Vector1D(0.0, 3.0)));
 }

 @Test
 public void testCheckPoint_boundary() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(Region.Location.BOUNDARY, intervalsSet.checkPoint(new Euclidean1D(2.5), new Vector1D(0.0, 2.5)));
 }
}