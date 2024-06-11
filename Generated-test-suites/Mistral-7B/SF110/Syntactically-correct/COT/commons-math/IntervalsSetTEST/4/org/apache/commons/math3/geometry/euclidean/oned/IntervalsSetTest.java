import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testConstructorWithEmptyIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testConstructorWithBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testConstructorWithCollectionOfSubHyperplanes() {
 List<SubHyperplane<Euclidean1D>> subHyperplanes = new ArrayList<>();
 subHyperplanes.add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 IntervalsSet intervalsSet = new IntervalsSet(subHyperplanes);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testBuildTreeWithFiniteLowerAndUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testBuildTreeWithNegativeInfiniteLower() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 2.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testBuildTreeWithPositiveInfiniteUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testBuildTreeWithNegativeInfiniteLowerAndPositiveInfiniteUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), Precision.EPSILON);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> newIntervals = newIntervalsSet.asList();
 assertEquals(1, newIntervals.size());
 Interval newInterval = newIntervals.get(0);
 assertEquals(1.0, newInterval.getInf(), Precision.EPSILON);
 assertEquals(2.0, newInterval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
 intervalsSet.computeGeometricalProperties();
 assertEquals(1.0, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(1.0, intervalsSet.getSup(), Precision.EPSILON);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
 assertEquals(1.0, intervalsSet.getInf(), Precision.EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
 assertEquals(1.0, intervalsSet.getSup(), Precision.EPSILON);
 }

 @Test
 public void testGetInfWithEmptyIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), Precision.EPSILON);
 }

 @Test
 public void testGetSupWithInfiniteIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), Precision.EPSILON);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testAsListWithEmptyIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testListWithNegativeInfiniteLowerAndPositiveInfiniteUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), Precision.EPSILON);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), Precision.EPSILON);
 }
}