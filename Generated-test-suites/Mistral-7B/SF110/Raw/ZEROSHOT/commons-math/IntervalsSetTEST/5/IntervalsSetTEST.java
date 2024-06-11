import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertArrayEquals(new double[] {1.5}, intervalsSet.getBarycenter().toArray(), 0.0);
 }

 @Test
 public void testIntervalSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(1.0, 3.0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertArrayEquals(new double[] {1.5}, intervalsSet.getBarycenter().toArray(), 0.0);
 }

 @Test
 public void testIntervalSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalSetWithInfAndFiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, Double.POSITIVE_INFINITY));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalSetWithTwoInfIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalSetWithInfAndFiniteAndEmptyInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, Double.POSITIVE_INFINITY), new Interval(2.0, 2.0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, Double.POSITIVE_INFINITY));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet2.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(4.0, intervalsSet.getSup(), 0.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, Double.POSITIVE_INFINITY));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet2.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> intervalList = intervalsSet.asList();
 assertEquals(2, intervalList.size());
 for (Interval interval : intervalList) {
 assertNotNull(interval);
 }
 IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(3.0, Double.POSITIVE_INFINITY));
 List<Interval> intervalList2 = intervalsSet2.asList();
 assertEquals(0, intervalList2.size());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(0.0, newIntervalsSet.getSize(), 0.0);
 assertTrue(newIntervalsSet.isEmpty());
 }
}