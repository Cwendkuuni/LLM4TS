package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1, 2});
 List<Interval> expectedIntervals = new ArrayList<>();
 expectedIntervals.add(new Interval(-1, 0));
 expectedIntervals.add(new Interval(0, 2));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.001);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.001);
 }
 }

 @Test
 public void testIntervalsSetWithInfiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> expectedIntervals = new ArrayList<>();
 expectedIntervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.001);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.001);
 }
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0});
 List<Interval> expectedIntervals = new ArrayList<>();
 expectedIntervals.add(new Interval(0, 0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals.size(), actualIntervals.size());
 for (int i = 0; i < expectedIntervals.size(); i++) {
 Interval expectedInterval = expectedIntervals.get(i);
 Interval actualInterval = actualIntervals.get(i);
 assertEquals(expectedInterval.getInf(), actualInterval.getInf(), 0.001);
 assertEquals(expectedInterval.getSup(), actualInterval.getSup(), 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1, 2, 1});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 }

 @Test
 public void testIntervalsSetCopyConstructor() {
 IntervalsSet originalIntervalsSet = new IntervalsSet(new double[]{-1, 0, 2});
 IntervalsSet copiedIntervalsSet = new IntervalsSet(originalIntervalsSet);
 List<Interval> originalIntervals = originalIntervalsSet.asList();
 List<Interval> copiedIntervals = copiedIntervalsSet.asList();
 assertEquals(originalIntervals.size(), copiedIntervals.size());
 for (int i = 0; i < originalIntervals.size(); i++) {
 Interval originalInterval = originalIntervals.get(i);
 Interval copiedInterval = copiedIntervals.get(i);
 assertEquals(originalInterval.getInf(), copiedInterval.getInf(), 0.001);
 assertEquals(originalInterval.getSup(), copiedInterval.getSup(), 0.001);
 }
 }

 @Test
 public void testIntervalsSetWithNullArgument() {
 try {
 new IntervalsSet(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}