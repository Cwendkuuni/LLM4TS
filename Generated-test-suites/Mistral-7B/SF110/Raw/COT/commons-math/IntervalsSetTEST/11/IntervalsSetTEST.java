package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class IntervalsSetTest {

 private IntervalsSet createEmptyIntervalsSet() {
 return new IntervalsSet();
 }

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(lower, upper);
 }

 private IntervalsSet createIntervalsSet(BSPTree<Euclidean1D> tree) {
 return new IntervalsSet(tree);
 }

 private IntervalsSet createIntervalsSet(Collection<SubHyperplane<Euclidean1D>> boundary) {
 return new IntervalsSet(boundary);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorsWithArguments() {
 // testing constructors with arguments
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane()));
 IntervalsSet intervalsSet3 = createIntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 assertEquals(intervalsSet1, intervalsSet3);
 }

 @Test
 public void testBuildTreeMethod() {
 // testing buildTree method
 double lower = 0.0;
 double upper = 3.0;
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(lower, upper));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(expectedIntervals, intervals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidArguments() {
 // testing buildTree method with invalid arguments
 IntervalsSet.buildTree(Double.NaN, 2.0);
 }

 @Test
 public void testBuildNewMethod() {
 // testing buildNew method
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
 assertEquals(intervalsSet2, new IntervalsSet(tree));
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 double expectedBarycenter = (1.0 + 3.0) / 2.0;
 assertEquals(expectedBarycenter, intervalsSet.getBarycenter().getX(), 0.0);
 }

 @Test
 public void testGetInfMethod() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 double expectedInf = 1.0;
 assertEquals(expectedInf, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupMethod() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 double expectedSup = 3.0;
 assertEquals(expectedSup, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListMethodWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = createEmptyIntervalsSet();
 List<Interval> expectedList = Collections.emptyList();
 List<Interval> list = intervalsSet.asList();
 assertEquals(expectedList, list);
 }

 @Test
 public void testAsListMethodWithIntervalsSetContainingOneInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 List<Interval> expectedList = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(expectedList, list);
 }

 @Test
 public void testAsListMethodWithIntervalsSetContainingMultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0)));
 List<Interval> expectedList = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 4.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(expectedList, list);
 }

 @Test
 public void testRecurseListMethod() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 List<Interval> expectedList = Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
 List<Interval> list = new ArrayList<Interval>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(expectedList, list);
 }

 @Test
 public void testCheckPointMethodWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = createEmptyIntervalsSet();
 Region expectedLocation = Region.OUTSIDE;
 Region actualLocation = intervalsSet.checkPoint(new Point1D(1.5), null);
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointMethodWithPointOutsideIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 Region expectedLocation = Region.OUTSIDE;
 Region actualLocation = intervalsSet.checkPoint(new Point1D(0.5), null);
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointMethodWithPointInsideIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 Region expectedLocation = Region.INSIDE;
 Region actualLocation = intervalsSet.checkPoint(new Point1D(2.5), null);
 assertEquals(expectedLocation, actualLocation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPointMethodWithNullPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 intervalsSet.checkPoint(null, null);
 }
}