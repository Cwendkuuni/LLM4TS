import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 assertEquals(Double.NaN, intervalsSet.getBarycenter(), 0.0);
 assertTrue(intervalsSet.contains(new Vector1D(0.0)));
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(1.5, intervals.get(0).getBarycenter(), 0.01);
 assertTrue(intervalsSet.contains(new Vector1D(1.5)));
 assertFalse(intervalsSet.contains(new Vector1D(3.0)));
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(1.0, 2.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 assertEquals(1.0, intervalsList.get(0).getBarycenter(), 0.01);
 assertEquals(1.5, intervalsList.get(1).getBarycenter(), 0.01);
 assertTrue(intervalsSet.contains(new Vector1D(1.0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 }

 @Test
 public void testIntervalsSetWithInfiniteInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(1, intervalsList.size());
 assertTrue(intervalsSet.contains(new Vector1D(0.0)));
 assertTrue(intervalsSet.contains(new Vector1D(100.0)));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0, intervalsSet.asList().size());
 }

 @Test
 public void testIntervalsSetWithNullCollection() {
 assertThrows(NullPointerException.class, () -> new IntervalsSet(null));
 }

 @Test
 public void testIntervalsSetWithMultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
 new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)
 ));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(3, intervalsList.size());
 }

 @Test
 public void testIntervalsSetWithInfAndFiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(
 new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, 4.0), new Interval(5.0, Double.POSITIVE_INFINITY)
 ));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(3, intervalsList.size());
 }

 @Test
 public void testInfiniteIntervalsSetInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 double inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
 }

 @Test
 public void testInfiniteIntervalsSetSup() {
 IntervalsSet intervalsSet = new IntervalsSet();
 double sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
 }

 @Test
 public void testFiniteIntervalsSetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0)));
 double inf = intervalsSet.getInf();
 assertEquals(1.0, inf, 0.01);
 }

 @Test
 public void testFiniteIntervalsSetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0)));
 double sup = intervalsSet.getSup();
 assertEquals(2.0, sup, 0.01);
 }

 @Test
 public void testSize() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 double size = intervalsSet.getSize();
 assertEquals(4.0, size, Precision.SAFE_MIN);
 }

 @Test
 public void testBarycenter() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 Vector1D barycenter = intervalsSet.getBarycenter();
 assertEquals(2.5, barycenter.getX(), 0.01);
 }
}