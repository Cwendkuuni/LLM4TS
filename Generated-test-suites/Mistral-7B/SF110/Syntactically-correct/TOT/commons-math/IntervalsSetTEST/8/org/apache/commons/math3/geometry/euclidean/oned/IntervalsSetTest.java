import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 4.0));
 assertEquals(2, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
 assertEquals(2, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0));
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIntervalsSetWithInfiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(1, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithSingularPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 1.0));
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
 assertEquals(-Double.INFINITY, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
 assertEquals(5.0, intervalsSet.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 }
}