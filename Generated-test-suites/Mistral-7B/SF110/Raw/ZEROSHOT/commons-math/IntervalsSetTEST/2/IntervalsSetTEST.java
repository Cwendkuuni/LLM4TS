import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithSingleInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getSize(), 0.01);
 assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(3.0, intervalsSet.getSize(), 0.01);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithNegativeAndPositiveInfiniteIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithIntervalsOverlappingAtPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0));
 assertEquals(1.0, intervalsSet.getSize(), 0.01);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithIntervalsNotOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(2.0, intervalsSet.getSize(), 0.01);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testBuildNewWithEmptyBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(0.0, newIntervalsSet.getSize(), 0.0);
 assertNull(newIntervalsSet.getBarycenter());
 }

 @Test
 public void testBuildNewWithSingletonBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.5), true).wholeHyperplane()));
 assertEquals(1.0, newIntervalsSet.getSize(), 0.01);
 assertEquals(new Vector1D(1.5), newIntervalsSet.getBarycenter());
 }

 @Test
 public void testBuildNewWithTwoIntervalsBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(buildTree(1.0, 4.0));
 assertEquals(2.0, newIntervalsSet.getSize(), 0.01);
 assertNull(newIntervalsSet.getBarycenter());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(-Double.INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(4.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertTrue(intervals.isEmpty());
 }

 @Test
 public void testAsListSingletonIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(new Interval(1.0, 2.0), intervals.get(0));
 }

 @Test
 public void testAsListTwoIntervalsIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertEquals(new Interval(1.0, 2.0), intervals.get(0));
 assertEquals(new Interval(3.0, 4.0), intervals.get(1));
 }

 @Test
 public void testAsListNegativeAndPositiveInfiniteIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), intervals.get(0));
 assertEquals(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), intervals.get(1));
 }

 @Test
 public void testGetSizeAndBarycenterAfterComputingGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.0, intervalsSet.getSize(), 0.01);
 assertEquals(new Vector1D(2.5), intervalsSet.getBarycenter());
 }

 @Test
 public void testGetSizeAndBarycenterWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.computeGeometricalProperties();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 private static BSPTree<Euclidean1D> buildTree(double lower, double upper) {
 return IntervalsSet.buildTree(lower, upper);
 }
}