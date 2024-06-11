import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;
import java.util.Collections;

import static org.apache.commons.math3.geometry.Point2D.Function.IDENTITY;
import static org.apache.commons.math3.geometry.Point2D.Function.NEGATE_X;
import static org.apache.commons.math3.geometry.Interval.interval;
import static org.apache.commons.math3.geometry.Region.*;
import static org.apache.commons.math3.geometry.Region.Location.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double... values) {
 return new IntervalsSet(Arrays.asList(interval(values[i], values[i + 1])));
 }

 @Test
 public void testConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithValues() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 List<Interval> list = intervalsSet.asList();
 assertEquals(4, list.size());
 for (int i = 0; i < 4; i += 2) {
 Interval interval = list.get(i / 2);
 assertEquals(i, interval.getInf(), 0.0);
 assertEquals(i + 1, interval.getSup(), 0.0);
 }
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.FALSE));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testBuildTreeWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(-1.0, 1.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(0.0, list.get(0).getSup(), 0.0);
 assertEquals(0.0, list.get(1).getInf(), 0.0);
 assertEquals(1.0, list.get(1).getSup(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.FALSE, new BSPTree<Euclidean1D>(Boolean.TRUE)));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
 List<Interval> list = newIntervalsSet.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 assertNull(intervalsSet.getBarycenter());

 intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0});
 intervalsSet.computeGeometricalProperties();
 double barycenterX = intervalsSet.getBarycenter().getX();
 double sum = 0.0;
 for (Interval interval : intervalsSet.asList()) {
 sum += interval.getSize() * interval.getBarycenter().getX();
 }
 assertEquals(barycenterX, sum / intervalsSet.getSize(), 0.01);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);

 intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0, 4.0});
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

 intervalsSet = new IntervalSet(new double[]{0.0, 1.0, 2.0, 3.0, 4.0});
 assertEquals(4.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 List<Interval> list = intervalsSet.asList();
 assertEquals(4, list.size());
 for (int i = 0; i < 4; i += 2) {
 Interval interval = list.get(i / 2);
 assertEquals(i, interval.getInf(), 0.0);
 assertEquals(i + 1, interval.getSup(), 0.0);
 }
 }

 @Test
 public void testAsListWithNegativeInfAndPositiveInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 0.0, 1.0});
 List<Interval> list = intervalsSet.asList();
 assertEquals(3, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(0.0, list.get(1).getInf(), 0.0);
 assertEquals(0.0, list.get(1).getSup(), 0.0);
 assertEquals(1.0, list.get(2).getInf(), 0.0);
 assertEquals(1.0, list.get(2).getSup(), 0.0);
 }

 @Test
 public void testCheckPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{0.0, 1.0, 2.0, 3.0});
 List<Interval> list = intervalsSet.asList();
 for (double point : new double[]{0.5, 1.5, 2.5}) {
 assertEquals(INTERSECTS, ((AbstractRegion<Euclidean1D, T>)intervalsSet).checkPoint(new IntervalsSet(Collections.singletonList(interval(point))), new Point2D(point)), INTERSECTS);
 }

 for (double point : new double[]{0.25, 1.75, 3.5}) {
 assertEquals(EXTERIOR, ((AbstractRegion<Euclidean1D, T>)intervalsSet).checkPoint(new IntervalsSet(Collections.singletonList(interval(point))), new Point2D(point)), EXTERIOR);
 }

 for (double point : new double[]{-0.5, 4.5}) {
 assertEquals(DISJOINT, ((AbstractRegion<Euclidean1D, T>)intervalsSet).checkPoint(new IntervalsSet(Collections.singletonList(interval(point))), new Point2D(point)), DISJOINT);
 }
 }

 private BSPTree<Euclidean1D> buildTree(double lower, double upper) {
 return new IntervalsSet().buildTree(lower, upper);
 }

 @Test
 public void testBuildTreeWithDoubleInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 try {
 intervalsSet.buildTree(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testBuildTreeWithLowerNegativeInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 try {
 intervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testBuildTreeWithOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet();
 try {
 intervalsSet.buildTree(0.0, 1.0);
 intervalsSet.buildTree(0.5, 2.0);
 intervalsSet.buildTree(2.0, 3.0);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Pass
 }
 }

 @Test
 public void testBuildTreeWithEmptyIntervalSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 try {
 intervalsSet.buildTree(new BSPTree<Euclidean1D>(Collections.emptyList()));
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Pass
 }
 }

 @Test
 public void testBuildTreeWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet();
 Interval interval = interval(0.0);
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(interval);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(1, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 assertEquals(0.0, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet();
 Interval interval1 = interval(0.0);
 Interval interval2 = interval(1.0);
 Interval interval3 = interval(2.0);
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(interval1, interval2, interval3);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(3, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 assertEquals(0.0, list.get(0).getSup(), 0.0);
 assertEquals(1.0, list.get(1).getInf(), 0.0);
 assertEquals(1.0, list.get(1).getSup(), 0.0);
 assertEquals(2.0, list.get(2).getInf(), 0.0);
 assertEquals(2.0, list.get(2).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithNegativeInfAndPositiveInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(new OrientedPoint(new Vector1D(-1.0), true).wholeHyperplane(), new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(2, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(1.0, list.get(0).getSup(), 0.0);
 assertEquals(1.0, list.get(1).getInf(), 0.0);
 assertEquals(1.0, list.get(1).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerNegativeInfAndUpperPositiveInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(new OrientedPoint(new Vector1D(-1.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(2, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(1.0, list.get(0).getSup(), 0.0);
 assertEquals(1.0, list.get(1).getInf(), 0.0);
 assertEquals(1.0, list.get(1).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerNegativeInfAndUpperFinite() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(new OrientedPoint(new Vector1D(-1.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(3, list.size());
 assertEquals(-1.0, list.get(0).getInf(), 0.0);
 assertEquals(2.0, list.get(0).getSup(), 0.0);
 assertEquals(2.0, list.get(1).getInf(), 0.0);
 assertEquals(2.0, list.get(1).getSup(), 0.0);
 assertEquals(2.0, list.get(2).getInf(), 0.0);
 assertEquals(2.0, list.get(2).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerFiniteAndUpperPositiveInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(3, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 assertEquals(3.0, list.get(0).getSup(), 0.0);
 assertEquals(3.0, list.get(1).getInf(), 0.0);
 assertEquals(3.0, list.get(1).getSup(), 0.0);
 assertEquals(3.0, list.get(2).getInf(), 0.0);
 assertEquals(3.0, list.get(2).getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerFiniteAndUpperFinite() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(3.0), false).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> list = newIntervalsSet.asList();
 assertEquals(2, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 assertEquals(3.0, list.get(0).getSup(), 0.0);
 assertEquals(3.0, list.get(1).getInf(), 0.0);
 assertEquals(3.0, list.get(1).getSup(), 0.0);
 }
}