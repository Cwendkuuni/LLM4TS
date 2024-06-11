import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 Assert.assertEquals(0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 assertFalse(intervalsSet.isEmpty());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 Assert.assertTrue(interval.getInf() >= 0.0 && interval.getSup() <= 10.0);
 }
 Assert.assertEquals(1, intervals.size(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 10.0));
 assertFalse(intervalsSet.isEmpty());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 Assert.assertTrue(interval.getInf() >= 0.0 && interval.getSup() <= 10.0);
 }
 Assert.assertEquals(1, intervals.size(), 0.0);
 }

 @Test
 public void testConstructorWithBoundary() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
 boundary.add(lowerCut);
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertFalse(intervalsSet.isEmpty());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 Assert.assertTrue(interval.getInf() >= 0.0);
 }
 Assert.assertEquals(1, intervals.size(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalIntervalsSet = new IntervalsSet(0.0, 10.0);
 IntervalsSet newIntervalsSet = originalIntervalsSet.buildNew(buildTree(0.0, 5.0));
 List<Interval> originalIntervals = originalIntervalsSet.asList();
 List<Interval> newIntervals = newIntervalsSet.asList();
 for (int i = 0; i < originalIntervals.size(); i++) {
 if (originalIntervals.get(i).getInf() <= 5.0 && originalIntervals.get(i).getSup() >= 5.0) {
 Assert.assertEquals(originalIntervals.get(i), newIntervals.get(i));
 } else {
 Assert.assertNotSame(originalIntervals.get(i), newIntervals.get(i));
 }
 }
 Assert.assertEquals(originalIntervals.size(), newIntervals.size(), 0.0);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 intervalsSet.computeGeometricalProperties();
 double size = intervalsSet.size();
 double barycenter = intervalsSet.getBarycenter().getX();
 List<Interval> intervals = intervalsSet.asList();
 double sum = 0.0;
 for (Interval interval : intervals) {
 sum += interval.getSize() * interval.getBarycenter().getX();
 }
 assertEquals(size, sum / intervals.size(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 assertEquals(intervalsSet.getInf(), 0.0, 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane()));
 assertEquals(intervalsSet.getInf(), Double.NEGATIVE_INFINITY, 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 assertEquals(intervalsSet.getSup(), 10.0, 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane()));
 assertEquals(intervalsSet.getSup(), Double.POSITIVE_INFINITY, 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 Assert.assertTrue(interval.getInf() >= 0.0 && interval.getSup() <= 10.0);
 Assert.assertFalse(interval.getInf() == interval.getSup());
 }
 Assert.assertEquals(1, intervals.size(), 0.0);
 }

 @Test
 public void testEmptyList() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertFalse(intervalsSet.isEmpty());
 List<Interval> intervals = intervalsSet.asList();
 assertTrue(intervals.isEmpty());
 }

 @Test
 public void testMergeIntervals() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane()), null, null));
 IntervalsSet intervalsSet2 = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane()), null, null));
 IntervalsSet mergedIntervalsSet = new IntervalsSet(intervalsSet1.getTree());
 List<Interval> mergedIntervals = mergedIntervalsSet.asList();
 List<Interval> intervals1 = intervalsSet1.asList();
 List<Interval> intervals2 = intervalsSet2.asList();
 for (int i = 0; i < intervals1.size(); i++) {
 Interval mergedInterval = null;
 for (Interval interval : mergedIntervals) {
 if (interval.getInf() == intervals1.get(i).getInf()) {
 mergedInterval = interval;
 break;
 }
 }
 for (Interval interval : intervals2) {
 if (interval.getInf() > mergedInterval.getInf() && interval.getInf() < mergedInterval.getSup()) {
 mergedInterval.setSup(interval.getSup());
 break;
 }
 }
 assertEquals(mergedInterval, intervals1.get(i));
 }
 for (int i = 0; i < intervals2.size(); i++) {
 Interval mergedInterval = null;
 for (Interval interval : mergedIntervals) {
 if (interval.getSup() == intervals2.get(i).getInf()) {
 mergedInterval = interval;
 break;
 }
 }
 for (Interval interval : intervals1) {
 if (interval.getInf() < mergedInterval.getInf()) {
 mergedInterval.setInf(interval.getInf());
 break;
 }
 }
 assertEquals(mergedInterval, intervals2.get(i));
 }
 assertEquals(intervals1.size() + intervals2.size() - 1, mergedIntervals.size(), 0.0);
 }
}