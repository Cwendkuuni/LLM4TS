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
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithMultipleIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 assertEquals(3, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfiniteInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, intervalsSet.size());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullCollection() {
 IntervalsSet intervalsSet = new IntervalsSet((Collection<SubHyperplane<Euclidean1D>>)null);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(3, list.size());
 }

 @Test
 public void testIntervalsSetBarycenter() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 Vector1D barycenter = intervalsSet.getBarycenter();
 assertEquals(3.5, barycenter.getX(), 0.001);
 }
}