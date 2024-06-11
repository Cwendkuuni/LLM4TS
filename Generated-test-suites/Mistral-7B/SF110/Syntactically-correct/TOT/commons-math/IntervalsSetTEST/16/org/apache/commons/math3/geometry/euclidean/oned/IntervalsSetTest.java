import org.apache.commons.math3.geometry.euclidean.oned.*;
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
 public void testFiniteIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 2.0));
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testInfiniteLowerBoundIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0);
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testInfiniteUpperBoundIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithMultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
 assertEquals(2, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBoundariesCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBoundariesCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithDuplicateIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(0.0, 1.0));
 }

 @Test
 public void testInfAndSupValues() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 2.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testInfSupValuesForInfiniteIntervalsSet() {
 IntervalsSet intervalsSet1 = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet1.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet1.getSup(), 0.0);

 IntervalsSet intervalsSet2 = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet2.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet2.getSup(), 0.0);
 }

 @Test
 public void testListIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testListIntervalsWhenNoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(0, intervalsList.size());
 }

 @Test
 public void testListIntervalsWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(0, intervalsList.size());
 }
}