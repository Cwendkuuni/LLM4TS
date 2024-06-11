import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 Assert.assertEquals(0.0, intervalsSet.getInf(), 0.0);
 Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 Assert.assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getSize(), 0.0);
 Assert.assertEquals(1.0, intervalsSet.getInf(), 0.0);
 Assert.assertEquals(2.0, intervalsSet.getSup(), 0.0);
 Assert.assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 Assert.assertEquals(1.0, intervalsSet.getInf(), 0.0);
 Assert.assertEquals(4.0, intervalsSet.getSup(), 0.0);
 Assert.assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 assertFalse(list.get(0).overlaps(list.get(1)));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 Assert.assertTrue(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNegativeLowerBoundary() {
 new IntervalsSet(-1.0, 2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNegativeUpperBoundary() {
 new IntervalsSet(1.0, -1.0);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 Assert.assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 Assert.assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfiniteUpperBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 Assert.assertEquals(1.0, intervalsSet.getInf(), 0.0);
 Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 Assert.assertFalse(intervalsSet.isEmpty());
 }
}