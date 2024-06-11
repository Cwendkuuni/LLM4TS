import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.001);
 assertEquals(2.0, interval.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 assertEquals(0.0, firstInterval.getInf(), 0.001);
 assertEquals(1.0, firstInterval.getSup(), 0.001);
 Interval secondInterval = intervals.get(1);
 assertEquals(2.0, secondInterval.getInf(), 0.001);
 assertEquals(3.0, secondInterval.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithInfiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.001);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0, intervalsSet.asList().size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testGetInfWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(2.0, intervalsSet.getSup(), 0.001);
 }

 @Test
 public void testGetSupWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(2.0, intervalsSet.getSup(), 0.001);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInfWithEmptyIntervalsSetAndNoAttribute() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setSize(0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.001);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSupWithEmptyIntervalsSetAndAttribute() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setSize(Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(3, intervals.size());
 }

 @Test
 public void testAsListWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }
}