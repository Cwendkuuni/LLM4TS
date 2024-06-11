package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(lower, upper);
 }

 @Test
 public void testIntervalsSetDefaultConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertNull(intervalsSet.getTree());
 }

 @Test
 public void testIntervalsSetConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertNotNull(intervalsSet.getTree());
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetConstructorWithBSPTree() {
 BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
 new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane()), null);
 IntervalsSet intervalsSet = new IntervalsSet(bspTree);
 assertNotNull(intervalsSet.getTree());
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetConstructorWithBoundary() {
 Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
 boundary.add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertNotNull(intervalsSet.getTree());
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
 new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane()), null);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(bspTree);
 assertNotNull(newIntervalsSet.getTree());
 assertEquals(2.0, newIntervalsSet.getSup(), 0.0);
 assertEquals(1.0, newIntervalsSet.getInf(), 0.0);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 intervalsSet.computeGeometricalProperties();
 assertNotNull(intervalsSet.getBarycenter());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeGeometricalPropertiesWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.computeGeometricalProperties();
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 for (Interval interval : list) {
 assertNotNull(interval.getInf());
 assertNotNull(interval.getSup());
 }
 }

 @Test
 public void testAsListWithNegativeInf() {
 IntervalsSet intervalsSet = createIntervalsSet(Double.NEGATIVE_INFINITY, 2.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf(), 0.0);
 assertEquals(2.0, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testAsListWithPositiveInf() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, Double.POSITIVE_INFINITY);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 assertEquals(1.0, list.get(0).getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testAsListWithSingleInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 1.5);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 assertEquals(1.0, list.get(0).getInf(), 0.0);
 assertEquals(1.5, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testAsListWithOverlappingIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.5, 1.5);
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 Interval firstInterval = list.get(0);
 Interval secondInterval = list.get(1);
 assertFalse(firstInterval.overlaps(secondInterval));
 assertFalse(secondInterval.overlaps(firstInterval));
 }

 @Test
 public void testAsListWithIntersectingIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.5, 1.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertFalse(interval.isSingleton());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAsListWithInvalidInterval() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
 List<Interval> list = intervalsSet.asList();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAsListWithEmptyInterval() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(1.0, 1.0));
 List<Interval> list = intervalsSet.asList();
 }
}