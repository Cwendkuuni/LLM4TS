import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithTree() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane());
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBoundary() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
 SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();
 boundary.add(lowerCut);
 boundary.add(upperCut);
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testBuildTree() {
 double lower = -3.0;
 double upper = 7.0;
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
 assertTrue(tree.isLeaf()) ;// For empty set

 lower = 0.0;
 upper = 5.0;
 tree = IntervalsSet.buildTree(lower, upper);
 assertFalse(tree.isLeaf()) ;// For set with one interval

 lower = -3.0;
 upper = 7.0;
 tree = IntervalsSet.buildTree(lower, upper);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval interval1 = intervals.get(0);
 Interval interval2 = intervals.get(1);
 assertEquals(lower, interval1.getInf(), 0.0);
 assertEquals(0.0, interval1.getSize(), 0.0);
 assertEquals(upper, interval2.getSup(), 0.0);
 assertEquals(upper - lower, interval2.getSize(), 0.0);
 }

 @Test
 public void testInf() {
 Interval intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);

 Interval intervalsSet2 = new IntervalsSet(new Interval(-3.0, 3.0));
 assertEquals(-3.0, intervalsSet2.getInf(), 0.0);
 }

 @Test
 public void testSup() {
 Interval intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
 assertEquals(5.0, intervalsSet.getSup(), 0.0);

 Interval intervalsSet2 = new IntervalsSet(new Interval(-3.0, 3.0));
 assertEquals(3.0, intervalsSet2.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 Interval intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(4.0, interval.getSup(), 0.0);
 }

 @Test
 public void testEmptyAsList() {
 Interval intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testMergeIntervals() {
 Interval interval1 = new Interval(1.0, 3.0);
 Interval interval2 = new Interval(2.0, 5.0);
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 assertEquals(1.0, mergedInterval.getInf(), 0.0);
 assertEquals(5.0, mergedInterval.getSup(), 0.0);
 }

 @Test
 public void testMergeEmptyIntervals() {
 Interval interval1 = new Interval();
 Interval interval2 = new Interval();
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 assertTrue(mergedInterval.isEmpty());
 }

 @Test
 public void testMergeOverlappingIntervals() {
 Interval interval1 = new Interval(1.0, 4.0);
 Interval interval2 = new Interval(2.0, 5.0);
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 assertEquals(1.0, mergedInterval.getInf(), 0.0);
 assertEquals(5.0, mergedInterval.getSup(), 0.0);
 }

 @Test
 public void testMergeNonOverlappingIntervals() {
 Interval interval1 = new Interval(1.0, 3.0);
 Interval interval2 = new Interval(6.0, 8.0);
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 assertTrue(mergedInterval.isEmpty());
 }

 @Test
 public void testSize() {
 Interval intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
 assertEquals(4.0, intervalsSet.getSize(), 0.0);

 IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(-3.0, 3.0));
 assertEquals(6.0, intervalsSet2.getSize(), 0.0);
 }

 @Test
 public void testBarycenter() {
 Interval intervalsSet = new IntervalsSet(new Interval(1.0, 5.0));
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(3.0, barycenter, 0.0);

 IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(-3.0, 3.0));
 double barycenter2 = intervalsSet2.getBarycenter().getX();
 assertEquals(0.0, barycenter2, 0.0);
 }

 @Test
 public void testEmptyBarycenter() {
 Interval intervalsSet = new IntervalsSet();
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testMergeAndSize() {
 Interval interval1 = new Interval(1.0, 3.0);
 Interval interval2 = new Interval(2.0, 5.0);
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 IntervalsSet intervalsSet = new IntervalsSet(mergedInterval);
 assertEquals(9.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testMergeAndBarycenter() {
 Interval interval1 = new Interval(1.0, 3.0);
 Interval interval2 = new Interval(2.0, 5.0);
 Interval mergedInterval = IntervalsSet.mergeIntervals(interval1, interval2);
 IntervalsSet intervalsSet = new IntervalsSet(mergedInterval);
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(3.5, barycenter, 0.0);
 }
}