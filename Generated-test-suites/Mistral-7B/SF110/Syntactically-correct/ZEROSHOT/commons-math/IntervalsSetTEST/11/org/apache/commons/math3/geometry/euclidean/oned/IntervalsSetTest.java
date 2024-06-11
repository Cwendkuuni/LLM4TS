import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private IntervalsSet emptyIntervalsSet;
 private IntervalsSet intervalsSetWithOneInterval;
 private IntervalsSet intervalsSetWithTwoIntervals;
 private IntervalsSet intervalsSetWithThreeIntervals;
 private IntervalsSet intervalsSetWithOverlappingIntervals;
 private IntervalsSet intervalsSetWithInfAndNegInf;

 @Before
 public void setUp() {
 intervalsSet = new IntervalsSet();
 emptyIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 intervalsSetWithOneInterval = new IntervalsSet(0.0, 1.0);
 intervalsSetWithTwoIntervals = new IntervalsSet(0.0, 1.0, new Interval(2.0, 3.0));
 intervalsSetWithThreeIntervals = new IntervalsSet(0.0, 1.0, new Interval(2.0, 3.0), new Interval(4.0, 5.0));
 intervalsSetWithOverlappingIntervals = new IntervalsSet(0.0, 2.0, new Interval(1.0, 2.0), new Interval(1.5, 2.5));
 intervalsSetWithInfAndNegInf = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testEmptyIntervalsSet() {
 assertEquals(0, emptyIntervalsSet.size());
 assertFalse(emptyIntervalsSet.contains(0.0));
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 List<Interval> intervals = intervalsSetWithOneInterval.asList();
 assertEquals(1, intervals.size());
 assertEquals(1.0, intervals.get(0).getInf(), 0.0);
 assertEquals(1.0, intervals.get(0).getSup(), 0.0);
 assertTrue(intervalsSetWithOneInterval.contains(0.5));
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 List<Interval> intervals = intervalsSetWithTwoIntervals.asList();
 assertEquals(2, intervals.size());
 assertEquals(0.0, intervals.get(0).getInf(), 0.0);
 assertEquals(1.0, intervals.get(0).getSup(), 0.0);
 assertEquals(2.0, intervals.get(1).getInf(), 0.0);
 assertEquals(3.0, intervals.get(1).getSup(), 0.0);
 assertFalse(intervalsSetWithTwoIntervals.contains(2.1));
 }

 @Test
 public void testIntervalsSetWithThreeIntervals() {
 List<Interval> intervals = intervalsSetWithThreeIntervals.asList();
 assertEquals(3, intervals.size());
 assertEquals(0.0, intervals.get(0).getInf(), 0.0);
 assertEquals(1.0, intervals.get(0).getSup(), 0.0);
 assertEquals(2.0, intervals.get(1).getInf(), 0.0);
 assertEquals(3.0, intervals.get(1).getSup(), 0.0);
 assertEquals(4.0, intervals.get(2).getInf(), 0.0);
 assertEquals(5.0, intervals.get(2).getSup(), 0.0);
 assertFalse(intervalsSetWithThreeIntervals.contains(3.1));
 }

 @Test
 public void testIntervalsSetWithOverlappingIntervals() {
 List<Interval> intervals = intervalsSetWithOverlappingIntervals.asList();
 assertEquals(2, intervals.size());
 assertEquals(0.0, intervals.get(0).getInf(), 0.0);
 assertEquals(2.0, intervals.get(0).getSup(), 0.0);
 assertEquals(1.5, intervals.get(1).getInf(), 0.0);
 assertEquals(2.5, intervals.get(1).getSup(), 0.0);
 assertTrue(intervalsSetWithOverlappingIntervals.contains(1.75));
 }

 @Test
 public void testIntervalsSetWithInfAndNegInf() {
 List<Interval> intervals = intervalsSetWithInfAndNegInf.asList();
 assertEquals(0, intervals.size());
 assertTrue(intervalsSetWithInfAndNegInf.isEmpty());
 assertFalse(intervalsSetWithInfAndNegInf.contains(0.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSetWithInfAndNegInf.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithInfAndNegInf.getSup(), 0.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidConstructor() {
 new IntervalsSet(null);
 }
}