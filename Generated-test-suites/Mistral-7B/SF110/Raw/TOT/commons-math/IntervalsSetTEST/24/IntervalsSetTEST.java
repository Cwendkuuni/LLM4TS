import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 assertTrue(intervalsSet.isEmpty());
 assertTrue(intervalsSet.contains(new Euclidean1D(0.0)));
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 Interval secondInterval = intervals.get(1);
 assertEquals(1.0, firstInterval.getInf(), 0.0);
 assertEquals(3.0, firstInterval.getSup(), 0.0);
 assertEquals(2.0, secondInterval.getInf(), 0.0);
 assertEquals(3.0, secondInterval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithInfiniteUpperBound() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<>());
 assertEquals(0, intervalsSet.asList().size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullTree() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 }

 @Test
 public void testIntervalsSetWithTreeWithOnlyOneCut() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Euclidean1D(2.5), true).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(2.5, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithTreeWithTwoCuts() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(
 new OrientedPoint(new Euclidean1D(1.0), false).wholeHyperplane(),
 new OrientedPoint(new Euclidean1D(3.0), true).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 Interval secondInterval = intervals.get(1);
 assertEquals(1.0, firstInterval.getInf(), 0.0);
 assertEquals(3.0, firstInterval.getSup(), 0.0);
 assertEquals(2.0, secondInterval.getInf(), 0.0);
 assertEquals(3.0, secondInterval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithTreeWithInfAndFiniteCuts() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(
 new OrientedPoint(new Euclidean1D(Double.NEGATIVE_INFINITY), false).wholeHyperplane(),
 new OrientedPoint(new Euclidean1D(3.0), true).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 Interval secondInterval = intervals.get(1);
 assertEquals(Double.NEGATIVE_INFINITY, firstInterval.getInf(), 0.0);
 assertEquals(3.0, firstInterval.getSup(), 0.0);
 assertEquals(3.0, secondInterval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, secondInterval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithTreeWithDegenerateCut() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(
 new OrientedPoint(new Euclidean1D(1.0), false).wholeHyperplane(),
 new OrientedPoint(new Euclidean1D(1.0), true).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(1.0, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetBarycenter() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 intervalsSet.computeGeometricalProperties();
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(2.0, barycenter, 0.05); // Approximate value
 }

 @Test
 public void testIntervalsSetEmptySize() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testIntervalsSetNonEmptySize() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testIntervalsSetInfSize() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(Double.NEGATIVE_INFINITY), new Euclidean1D(Double.POSITIVE_INFINITY));
 intervalsSet.computeGeometricalProperties();
 assertTrue(Double.isInfinite(intervalsSet.getSize()));
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetGetInfWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetGetInfWithInfiniteLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(Double.NEGATIVE_INFINITY), new Euclidean1D(5.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetGetSupWithInfiniteUpperBound() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(Double.POSITIVE_INFINITY));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetGetSupWithSingleInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetContainsPointInside() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertTrue(intervalsSet.contains(new Euclidean1D(2.0)));
 }

 @Test
 public void testIntervalsSetContainsPointOutsideLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertFalse(intervalsSet.contains(new Euclidean1D(0.0)));
 }

 @Test
 public void testIntervalsSetContainsPointOutsideUpperBound() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertFalse(intervalsSet.contains(new Euclidean1D(4.0)));
 }

 @Test
 public void testIntervalsSetContainsPointOnBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(new Euclidean1D(1.0), new Euclidean1D(3.0));
 assertTrue(intervalsSet.contains(new Euclidean1D(3.0)));
 }
}