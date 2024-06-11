import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidLower() {
 IntervalsSet intervalsSet = new IntervalsSet(-1.0, 10.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, -1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<>());
 }

 @Test
 public void testBuildTree() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 5.0);
 assertTrue(tree.getCut() == null || tree.getCut().getHyperplane() instanceof WholeHyperplane);
 tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
 assertTrue(tree instanceof BSPTreeWithSplittingNode);
 tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 assertTrue(tree instanceof BSPTreeWithTwoChildren);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalIntervalsSet = createIntervalsSet(0.0, 5.0);
 IntervalsSet newIntervalsSet = originalIntervalsSet.buildNew(originalIntervalsSet.getTree());
 assertEquals(originalIntervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 intervalsSet.computeGeometricalProperties();
 double expectedBarycenter = 2.5;
 assertTrue(Math.abs(intervalsSet.getBarycenter().getX() - expectedBarycenter) < 0.001);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(intervalsSet.getInf(), 0.0, 0.001);
 intervalsSet = createIntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(intervalsSet.getInf(), Double.NEGATIVE_INFINITY, 0.001);
 intervalsSet = createIntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(intervalsSet.getInf(), Double.NEGATIVE_INFINITY, 0.001);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(intervalsSet.getSup(), 5.0, 0.001);
 intervalsSet = createIntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(intervalsSet.getSup(), Double.POSITIVE_INFINITY, 0.001);
 intervalsSet = createIntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(intervalsSet.getSup(), Double.POSITIVE_INFINITY, 0.001);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 List<Interval> expectedList = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(expectedList.size(), list.size());
 for (int i = 0; i < expectedList.size(); i++) {
 Interval expectedInterval = expectedList.get(i);
 Interval interval = list.get(i);
 assertEquals(expectedInterval.getInf(), interval.getInf(), 0.001);
 assertEquals(expectedInterval.getSup(), interval.getSup(), 0.001);
 }
 }

 @Test
 public void testRecurseListWithEmptyTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertTrue(list.isEmpty());
 }

 @Test
 public void testRecursiveListWithOneInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, list.size());
 assertEquals(new Interval(0.0, 5.0), list.get(0));
 }

 @Test
 public void testRecursiveListWithTwoIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 6.0);
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(2, list.size());
 assertEquals(new Interval(0.0, 3.0), list.get(0));
 assertEquals(new Interval(3.0, 6.0), list.get(1));
 }

 @Test
 public void testRecursiveListWithThreeIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 8.0);
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(3, list.size());
 assertEquals(new Interval(0.0, 2.0), list.get(0));
 assertEquals(new Interval(2.0, 6.0), list.get(1));
 assertEquals(new Interval(6.0, 8.0), list.get(2));
 }

 @Test
 public void testCheckPointWithPointInside() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 Region.Location expectedLocation = Region.Location.INSIDE;
 Region.Location actualLocation = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(3.0));
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointWithPointOutsideLowerBound() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 Region.Location expectedLocation = Region.Location.OUTSIDE;
 Region.Location actualLocation = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(0.0));
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointWithPointOutsideUpperBound() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 Region.Location expectedLocation = Region.Location.OUTSIDE;
 Region.Location actualLocation = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(6.0));
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointWithPointOnBoundaryLower() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 Region.Location expectedLocation = Region.Location.BOUNDARY_LOWER;
 Region.Location actualLocation = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(1.0));
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testCheckPointWithPointOnBoundaryUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 Region.Location expectedLocation = Region.Location.BOUNDARY_UPPER;
 Region.Location actualLocation = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(5.0));
 assertEquals(expectedLocation, actualLocation);
 }

}