import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(2.0, 4.0));
 assertEquals(2.0, intervalsSet.getInf(), 0.0);
 assertEquals(4.0, intervalsSet.getSup(), 0.0);
 assertEquals(2.0, intervalsSet.getBarycenter().getX(), 0.0);
 assertEquals(2.0, intervalsSet.getInf(), 2.0);
 assertEquals(4.0, intervalsSet.getSup(), 2.0);
 assertEquals(2.0, intervalsSet.asList().get(0).getInf(), 0.0);
 assertEquals(4.0, intervalsSet.asList().get(0).getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
 assertEquals(3.0, intervalsSet.getSize(), 0.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 assertEquals(3.0, intervalsSet.getBarycenter().getX(), 0.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2.0, intervals.get(0).getInf(), 0.0);
 assertEquals(3.0, intervals.get(0).getSup(), 0.0);
 assertEquals(3.0, intervals.get(1).getInf(), 0.0);
 assertEquals(5.0, intervals.get(1).getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
 assertEquals(4.0, intervalsSet.getSize(), 0.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 assertEquals(6.0, intervalsSet.getSup(), 0.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(1.0, intervalsList.get(0).getInf(), 0.0);
 assertEquals(3.0, intervalsList.get(0).getSup(), 0.0);
 assertEquals(4.0, intervalsList.get(1).getInf(), 0.0);
 assertEquals(6.0, intervalsList.get(1).getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithEmptyBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setBoundary(Collections.emptyList());
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithBoundedBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(2.0), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfiniteBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(Double.NEGATIVE_INFINITY), new Vector1D(1.0)));
 boundary.add(new Line(new Point2D(Double.POSITIVE_INFINITY), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithSelfIntersectionBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(1.0), new Vector1D(1.0)));
 boundary.add(new Line(new Point2D(3.0), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(null);
 intervalsSet.setBoundary(boundary);
 }

 @Test
 public void testBuildTreeWithTwoIntervals() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 5.0);
 assertEquals(3, tree.getDepth());
 assertEquals(true, tree.getCut(0).isDirect());
 assertEquals(false, tree.getCut(1).isDirect());
 assertEquals(false, tree.getCut(2).isDirect());
 }

 @Test
 public void testBuildTreeWithOneInterval() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 1.0);
 assertEquals(2, tree.getDepth());
 assertEquals(true, tree.getCut(0).isDirect());
 }

 @Test
 public void testBuildTreeWithInfiniteIntervals() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, tree.getDepth());
 assertEquals(true, tree.getCut() == null);
 }

 @Test
 public void testGetInfWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithEmptyBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setBoundary(Collections.emptyList());
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithBoundedBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(2.0), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithInfiniteBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(Double.NEGATIVE_INFINITY), new Vector1D(1.0)));
 boundary.add(new Line(new Point2D(Double.POSITIVE_INFINITY), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
 assertEquals(6.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithEmptyBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setBoundary(Collections.emptyList());
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithBoundedBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(2.0), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithInfiniteBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Point2D(Double.NEGATIVE_INFINITY), new Vector1D(1.0)));
 boundary.add(new Line(new Point2D(Double.POSITIVE_INFINITY), new Vector1D(1.0)));
 intervalsSet.setBoundary(boundary);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.asList().size());
 }

 @Test
 public void testAsListWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(1, intervalsList.size());
 assertEquals(1.0, intervalsList.get(0).getInf(), 0.0);
 assertEquals(2.0, intervalsList.get(0).getSup(), 0.0);
 }

 @Test
 public void testAsListWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 assertEquals(1.0, intervalsList.get(0).getInf(), 0.0);
 assertEquals(3.0, intervalsList.get(0).getSup(), 0.0);
 assertEquals(3.0, intervalsList.get(1).getInf(), 0.0);
 assertEquals(5.0, intervalsList.get(1).getSup(), 0.0);
 }

 @Test
 public void testAsListWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 6.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 assertEquals(1.0, intervalsList.get(0).getInf(), 0.0);
 assertEquals(3.0, intervalsList.get(0).getSup(), 0.0);
 assertEquals(4.0, intervalsList.get(1).getInf(), 0.0);
 assertEquals(6.0, intervalsList.get(1).getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptyBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.setBoundary(Collections.emptyList());
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(0, intervalsList.size());
 }
}