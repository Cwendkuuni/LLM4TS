import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBSPTreeConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(buildBSPTree(0.0, 5.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBoundaryConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Line(0.0, 1.0), new Line(2.0, 3.0)));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 assertEquals(0.0, firstInterval.getInf(), 0.0);
 assertEquals(1.0, firstInterval.getSup(), 0.0);
 Interval secondInterval = intervals.get(1);
 assertEquals(2.0, secondInterval.getInf(), 0.0);
 assertEquals(3.0, secondInterval.getSup(), 0.0);
 }

 @Test
 public void testInfGetters() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 assertEquals(0.0, intervalsSet.getInf(0), 0.0);
 assertEquals(5.0, intervalsSet.getSup(0), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 assertEquals(7.0, intervalsSet.getInf(1), 0.0);
 assertEquals(10.0, intervalsSet.getSup(1), 0.0);
 }

 @Test
 public void testSizeGetter() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 assertEquals(11.0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testBarycenterGetter() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 assertEquals(5.5, intervalsSet.getBarycenter().getX(), 0.01);
 }

 @Test
 public void testAsListMethod() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testAddMethod() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInterval() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(null);
 }

 @Test
 public void testAddOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(4.0, 8.0));

 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testAddNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(12.0, 15.0));

 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testCheckPointMethod() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.add(new Interval(0.0, 5.0));
 intervalsSet.add(new Interval(7.0, 10.0));

 Region.Location location1 = intervalsSet.checkPoint(0.0,
 new Vector1D(0.0));
 assertEquals(Region.Location.INSIDE, location1);

 Region.Location location2 = intervalsSet.checkPoint(6.0,
 new Vector1D(0.0));
 assertEquals(Region.Location.INSIDE, location2);

 Region.Location location3 = intervalsSet.checkPoint(11.0,
 new Vector1D(0.0));
 assertEquals(Region.Location.OUTSIDE, location3);
 }

 private static BSPTree<Euclidean1D> buildBSPTree(double lower, double upper) {
 return IntervalsSet.buildTree(lower, upper);
 }
}