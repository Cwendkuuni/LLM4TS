import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithSingleInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 assertEquals(1.0, intervalsSet.getSize(), 1.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(intervals.get(0), new Interval(1.0, 2.0));
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
 assertEquals(0.5, intervalsSet.getInf(), 0.0);
 assertEquals(3.5, intervalsSet.getSup(), 0.0);
 assertEquals(3.0, intervalsSet.getSize(), 0.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 claiming(intervalsList.get(0), new Interval(0.5, 1.5));
 claiming(intervalsList.get(1), new Interval(2.5, 3.5));
 }

 @Test
 public void testIntervalsSetWithInfAndFiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(2.0, Double.POSITIVE_INFINITY));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(1, intervalsList.size());
 // Inf is considered as a single interval, so it's size is infinite
 claiming(intervalsList.get(0), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithNullCollection() {
 assertThrows(NullPointerException.class, () -> new IntervalsSet(null));
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.5), true).wholeHyperplane()));
 assertEquals(intervalsSet1, intervalsSet2);
 }

 @Test
 public void testGetInfAndGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
 assertEquals(0.5, intervalsSet.getInf(), 0.0);
 assertEquals(3.5, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 claiming(intervalsList.get(0), new Interval(0.5, 1.5));
 claiming(intervalsList.get(1), new Interval(2.5, 3.5));
 }

 private void claiming(Interval expected, Interval actual) {
 assertEquals("Intervals do not match! Expected: " + expected + " Actual: " + actual, expected, actual);
 }

}