import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testFiniteIntervalsConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(3.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testInfiniteIntervalsConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }

 @Test
 public void testTreeConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(1.0, 3.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(3.0, interval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testBoundaryConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new SubHyperplane<Euclidean1D>(){}));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertTrue(newIntervalsSet.isEmpty());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 double size = intervalsSet.getSize();
 assertEquals(2.0, size, Precision.EPSILON);
 }

 @Test(expected = IllegalStateException.class)
 public void testNullTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.computeGeometricalProperties();
 intervalsSet.setTree(null);
 intervalsSet.computeGeometricalProperties();
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 double inf = intervalsSet.getInf();
 assertEquals(-Double.MAX_VALUE, inf, Precision.EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 double sup = intervalsSet.getSup();
 assertEquals(3.0, sup, Precision.EPSILON);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0, 3.0, 4.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval firstInterval = intervals.get(0);
 Interval secondInterval = intervals.get(1);
 assertEquals(1.0, firstInterval.getInf(), Precision.EPSILON);
 assertEquals(2.0, firstInterval.getSup(), Precision.EPSILON);
 assertEquals(3.0, secondInterval.getInf(), Precision.EPSILON);
 assertEquals(4.0, secondInterval.getSup(), Precision.EPSILON);
 }

 @Test
 public void testRecurseList() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0, 3.0, 4.0);
 List<Interval> intervals = new ArrayList<Interval>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(4, intervals.size());
 }

}