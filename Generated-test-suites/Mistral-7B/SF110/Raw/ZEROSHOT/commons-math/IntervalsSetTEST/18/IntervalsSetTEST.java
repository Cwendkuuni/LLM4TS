import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testSingleIntervalIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getSize(), 0.0);
 assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(3.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithNegativeInfAndPositiveInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoIntervalsOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(2.0, 4.0)));
 assertEquals(3.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoIntervalsNotOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(3.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(-Double.INFINITY, intervalsSet.getInf(), 0.0);
 intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertSame(intervals.get(0), new Interval(1.0, 2.0));
 assertSame(intervals.get(1), new Interval(3.0, 4.0));
 }

 @Test
 public void testIntervalsSetAsListSingleInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertSame(intervals.get(0), new Interval(1.0, 2.0));
 }

 @Test
 public void testIntervalsSetAsListEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }
}