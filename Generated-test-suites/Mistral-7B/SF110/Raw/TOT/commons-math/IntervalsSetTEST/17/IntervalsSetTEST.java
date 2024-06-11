import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithFiniteBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 Interval[] intervals = intervalsSet.asList().toArray(new Interval[0]);
 assertEquals(5.0, intervalsSet.getSize(), 0.0);
 assertArrayEquals(new Interval[]{new Interval(0.0, 5.0)}, intervals);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(-1.0, 5.0);
 Interval[] intervals = intervalsSet.asList().toArray(new Interval[0]);
 assertEquals(6.0, intervalsSet.getSize(), 0.0);
 assertArrayEquals(new Interval[]{new Interval(-1.0, 5.0), new Interval(0.0, Double.POSITIVE_INFINITY)}, intervals);
 }

 @Test
 public void testIntervalsSetWithInfiniteUpperBound() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 Interval[] intervals = intervalsSet.asList().toArray(new Interval[0]);
 assertEquals(1.0, intervals.length, 0.0);
 assertArrayEquals(new Interval[]{new Interval(0.0, Double.POSITIVE_INFINITY)}, intervals);
 }

 @Test
 public void testIntervalsSetWithBoundsOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 4.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(2.0, 5.0);
 IntervalsSet mergedIntervalsSet = intervalsSet.union(intervalsSet2);
 Interval[] mergedIntervals = mergedIntervalsSet.asList().toArray(new Interval[0]);
 assertEquals(5.0, mergedIntervalsSet.getSize(), 0.0);
 assertArrayEquals(new Interval[]{new Interval(1.0, 5.0)}, mergedIntervals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidBounds() {
 new IntervalsSet(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setTree(null);
 }
}