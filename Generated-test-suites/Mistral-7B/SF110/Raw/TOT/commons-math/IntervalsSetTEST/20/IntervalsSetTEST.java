import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet emptySet = new IntervalsSet();
 assertEquals(0, emptySet.size());
 assertTrue(emptySet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithMultipleIntervals() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 assertEquals(3, set.size());
 assertFalse(set.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidArguments() {
 IntervalsSet set = new IntervalsSet(null);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerBound() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfiniteUpperBound() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet set = new IntervalsSet(Collections.emptyList());
 assertEquals(0, set.size());
 assertTrue(set.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIntervalsSetWithModifyCollection() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0)));
 List<Interval> list = set.asList();
 list.remove(1);
 }

 @Test
 public void testIntervalsSetWithInfAndFiniteValues() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOverlappingIntervals() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(2.0, 3.0), new Interval(3.0, 4.0), new Interval(2.0, 4.0)));
 assertEquals(3, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTouchedEndpoints() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 2.0), new Interval(2.0, 4.0)));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithEmptyOverlap() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithNoOverlap() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfiniteIntervals() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfAndFiniteValuesAsArguments() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0, Double.POSITIVE_INFINITY);
 assertEquals(1, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOverlappingInfValues() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
 assertEquals(1, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithBoundaryPoints() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(1.0, 3.0)));
 assertEquals(3, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfAndFiniteIntervalsAsArguments() {
 IntervalsSet set = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 2.0), new Interval(5.0, Double.POSITIVE_INFINITY));
 assertEquals(2, set.size());
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(-Infinity.doubleValue(), set.getInf(), 0.001);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(4.0, set.getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> list = set.asList();
 assertEquals(2, list.size());
 }
}