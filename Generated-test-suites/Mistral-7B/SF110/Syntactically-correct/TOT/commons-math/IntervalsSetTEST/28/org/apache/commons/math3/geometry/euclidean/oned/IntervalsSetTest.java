package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.AbstractRegion;
import org.apache.commons.math3.geometry.Interval;
import org.apache.commons.math3.geometry.Region;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.util.Vector1D;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, intervalsSet.asList().size());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(1.0, interval.getInf(), 0.001);
 assertEquals(2.0, interval.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 2.0), new Interval(1.0, 4.0));
 assertEquals(2, intervalsSet.asList().size());
 Interval interval1 = intervalsSet.asList().get(0);
 Interval interval2 = intervalsSet.asList().get(1);
 assertEquals(0.0, interval1.getInf(), 0.001);
 assertEquals(2.0, interval1.getSup(), 0.001);
 assertEquals(1.0, interval2.getInf(), 0.001);
 assertEquals(4.0, interval2.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
 assertEquals(2, intervalsSet.asList().size());
 Interval interval1 = intervalsSet.asList().get(0);
 Interval interval2 = intervalsSet.asList().get(1);
 assertEquals(0.0, interval1.getInf(), 0.001);
 assertEquals(2.0, interval1.getSup(), 0.001);
 assertEquals(3.0, interval2.getInf(), 0.001);
 assertEquals(5.0, interval2.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<>());
 assertEquals(0, intervalsSet.asList().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithLowerGreaterThanUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(3.0, 2.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithDegeneratedTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
 assertEquals(1, intervalsSet.asList().size());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.001);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.001);
 }
}