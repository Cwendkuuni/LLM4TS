import org.apache.commons.math3.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 assertEquals(0.0, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
 }

 @Test
 public void testFiniteIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 assertFalse(intervalsSet.isEmpty());
 assertEquals(-1.0, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(4.0, intervalsSet.getSup(), Precision.EPSILON);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(3, intervals.size());
 for (Interval interval : intervals) {
 assertEquals(-1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }
 }

 @Test
 public void testInfiniteIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertTrue(intervalsSet.isEmpty());
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(0.0, intervalsSet.getSup(), Precision.EPSILON);
 IntervalsSet intervalsSet2 = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertFalse(intervalsSet2.isEmpty());
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet2.getInf(), Precision.EPSILON);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet2.getSup(), Precision.EPSILON);
 }

 @Test
 public void testBuildTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
 assertEquals(3, tree.getLeaves().size());
 for (Object leaf : tree.getLeaves()) {
 Interval interval = (Interval)leaf;
 assertEquals(-1.0, interval.getInf(), Precision.EPSILON);
 assertEquals(2.0, interval.getSup(), Precision.EPSILON);
 }
 }

 @Test
 public void testReceivePoint() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 List<Interval> intervals = intervalsSet.asList();
 Point2D point = new Vector1D(1.5);
 Region.Location location = intervalsSet.checkPoint(intervals.get(0).getBoundary(), point);
 assertEquals(Region.Location.LEFT, location);
 location = intervalsSet.checkPoint(intervals.get(1).getBoundary(), point);
 assertEquals(Region.Location.INSIDE, location);
 location = intervalsSet.checkPoint(intervals.get(2).getBoundary(), point);
 assertEquals(Region.Location.RIGHT, location);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(buildTree(-1.0, 4.0));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 intervalsSet.computeGeometricalProperties();
 assertEquals(6.0, intervalsSet.getSize(), Precision.EPSILON);
 assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());
 }

 @Test
 public void testGetInfAndSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new double[]{-1.0, 2.0, 4.0});
 assertEquals(-1.0, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(4.0, intervalsSet.getSup(), Precision.EPSILON);
 }
}