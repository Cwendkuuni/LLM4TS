package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.0);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 5.0));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.0);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testConstructorWithBoundary() {
 SubHyperplane<Euclidean1D> lowerBound = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
 SubHyperplane<Euclidean1D> upperBound = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(lowerBound, upperBound));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.0);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(-Infinity.doubleValue(), intervalsSet.getInf(), 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(Infinity.doubleValue(), intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(Infinity.doubleValue(), intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList_EmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(0, intervalsSet.asList().size());
 }

 @Test
 public void testAsList_OneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.0);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testAsList_MultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.0);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.0);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullBoundary() {
 new IntervalsSet(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithEmptyBoundary() {
 new IntervalsSet(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullTree() {
 new IntervalsSet(null);
 }
}