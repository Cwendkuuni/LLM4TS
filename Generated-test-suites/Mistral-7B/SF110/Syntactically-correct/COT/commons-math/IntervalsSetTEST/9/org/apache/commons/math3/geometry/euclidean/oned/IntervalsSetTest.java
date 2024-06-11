import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double... intervals) {
 return new IntervalsSet(Arrays.asList(new Interval(intervals[0], intervals[1])));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBoundary() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Hyperplane<Euclidean1D>(new Vector1D(1.0), false));
 boundary.add(new Hyperplane<Euclidean1D>(new Vector1D(2.0), false));
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval interval1 = intervals.get(0);
 assertEquals(1.0, interval1.getInf(), 0.0);
 assertEquals(1.0, interval1.getSup(), 0.0);
 Interval interval2 = intervals.get(1);
 assertEquals(2.0, interval2.getInf(), 0.0);
 assertEquals(2.0, interval2.getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerAndUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerNegativeInfiniteAndUpperPositiveInfinite() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testBuildTreeWithLowerNegativeInfiniteAndUpperFinite() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 1.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(1.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerFiniteAndUpperPositiveInfinite() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerFiniteAndUpperFinite() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalIntervalsSet = createIntervalsSet(1.0, 2.0);
 IntervalsSet newIntervalsSet = originalIntervalsSet.buildNew(originalIntervalsSet.getTree());
 List<Interval> originalIntervals = originalIntervalsSet.asList();
 List<Interval> newIntervals = newIntervalsSet.asList();
 assertEquals(originalIntervals.size(), newIntervals.size());
 for (int i = 0; i < originalIntervals.size(); i++) {
 Interval originalInterval = originalIntervals.get(i);
 Interval newInterval = newIntervals.get(i);
 assertEquals(originalInterval.getInf(), newInterval.getInf(), 0.0);
 assertEquals(originalInterval.getSup(), newInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());

 IntervalsSet intervalsSetWithOnePoint = createIntervalsSet(1.0, 1.1);
 intervalsSetWithOnePoint.computeGeometricalProperties();
 assertEquals(0.1, intervalsSetWithOnePoint.getSize(), 0.0);
 Interval interval = intervalsSetWithOnePoint.asList().get(0);
 assertEquals(1.0, interval.getBarycenter().getX(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);

 IntervalsSet intervalsSetWithLowerBound = createIntervalsSet(1.0, 1.1);
 assertEquals(1.0, intervalsSetWithLowerBound.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);

 IntervalsSet intervalsSetWithUpperBound = createIntervalsSet(1.9, 2.0);
 assertEquals(1.9, intervalsSetWithUpperBound.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.0, interval.getSup(), 0.0);
 }

 @Test
 public void testAsListWithMultipleIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 1.5, 2.5, 3.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(3, intervals.size());
 Interval interval1 = intervals.get(0);
 assertEquals(1.0, interval1.getInf(), 0.0);
 assertEquals(1.5, interval1.getSup(), 0.0);

 Interval interval2 = intervals.get(1);
 assertEquals(1.5, interval2.getInf(), 0.0);
 assertEquals(2.5, interval2.getSup(), 0.0);

 Interval interval3 = intervals.get(2);
 assertEquals(2.5, interval3.getInf(), 0.0);
 assertEquals(3.0, interval3.getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptyIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertTrue(intervals.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAsListWithModifyList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 intervals.add(new Interval(3.0, 4.0));
 }

 @Test
 public void testCheckPointWithPointInside() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Region.Location.INSIDE, intervalsSet.checkPoint(new Vector1D(1.5), null));
 }

 @Test
 public void testCheckPointWithPointOutsideLowerBound() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Region.Location.OUTSIDE, intervalsSet.checkPoint(new Vector1D(0.5), null));
 }

 @Test
 public void testCheckPointWithPointOutsideUpperBound() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Region.Location.OUTSIDE, intervalsSet.checkPoint(new Vector1D(3.5), null));
 }

 @Test
 public void testCheckPointWithPointOnBoundaryLower() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Region.Location.BOUNDARY, intervalsSet.checkPoint(new Vector1D(1.0), null));
 }

 @Test
 public void testCheckPointWithPointOnBoundaryUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(Region.Location.BOUNDARY, intervalsSet.checkPoint(new Vector1D(2.0), null));
 }

 @Test
 public void testCheckPointWithPointBetweenIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 1.5, 2.5, 3.0);
 assertEquals(Region.Location.INSIDE, intervalsSet.checkPoint(new Vector1D(2.0), null));
 }

 @Test
 public void testCheckPointWithNullVector() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertNull(intervalsSet.checkPoint(null, null));
 }

 @Test
 public void testCheckPointWithNullVectorAndTree() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertNull(intervalsSet.checkPoint(null, intervalsSet.getTree()));
 }
}