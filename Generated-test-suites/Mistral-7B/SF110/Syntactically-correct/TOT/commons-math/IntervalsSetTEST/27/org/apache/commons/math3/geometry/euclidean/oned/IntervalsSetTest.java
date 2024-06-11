import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptySet() {
 IntervalsSet set = new IntervalsSet();
 assertEquals(0, set.asList().size());
 assertTrue(set.isEmpty());
 }

 @Test
 public void testSingleIntervalSet() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, set.asList().size());
 Interval interval = set.asList().get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testInfiniteSet() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, set.asList().size());
 Interval interval = set.asList().get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), Precision.EPSILON);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testSetWithMultipleIntervals() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
 assertEquals(3, set.asList().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidLowerBound() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidUpperBound() {
 IntervalsSet set = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testSetWithEmptyCollection() {
 IntervalsSet set = new IntervalsSet(new ArrayList<>());
 assertEquals(0, set.asList().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetModifyList() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> list = set.asList();
 list.remove(0);
 }
}