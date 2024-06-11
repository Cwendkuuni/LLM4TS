import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertNull(intervalsSet.getTree());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertEquals(2, tree.getNumLeaves());
 assertEquals(1, tree.getNumInternalNodes());
 assertEquals(6, tree.getDepth());
 }

 @Test
 public void testConstructorWithBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane()));
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertEquals(1, tree.getNumLeaves());
 assertEquals(1, tree.getNumInternalNodes());
 assertEquals(0, tree.getDepth());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertEquals(3, tree.getNumLeaves());
 assertEquals(2, tree.getNumInternalNodes());
 assertEquals(2, tree.getDepth());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBSPTreeConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane(), null, null, null));
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree.getMinus().getPlus());
 assertEquals(intervalsSet.getTree(), tree);
 assertEquals(newIntervalsSet.getTree(), tree.getPlus());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), Precision.SAFE_MIN);
 assertNaN(intervalsSet.getBarycenter());

 intervalsSet = createIntervalsSet(1.0, 4.0);
 intervalsSet.computeGeometricalProperties();
 List<Interval> intervals = intervalsSet.asList();
 double sum = 0.0;
 for (Interval interval : intervals) {
 sum += interval.getSize() * interval.getBarycenter();
 }
 assertEquals(3.5, sum, Precision.SAFE_MIN);
 assertEquals(3.5 / 3.0, intervalsSet.getBarycenter().getX(), Precision.SAFE_MIN);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf());

 intervalsSet = createIntervalsSet(1.0, 5.0);
 assertEquals(1.0, intervalsSet.getInf());

 intervalsSet = createIntervalsSet(5.0, 10.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf());
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup());

 intervalsSet = createIntervalsSet(1.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup());

 intervalsSet = createIntervalsSet(0.0, 1.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup());
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 assertEquals(new Interval(1.0, 2.0), list.get(0));
 assertEquals(new Interval(3.0, 5.0), list.get(1));
 }

 @Test
 public void testRecursiveList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(2, list.size());
 assertEquals(new Interval(1.0, 2.0), list.get(0));
 assertEquals(new Interval(3.0, 5.0), list.get(1));
 }

 @Test
 public void testEdgeCasesGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf());

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());
 }

 @Test
 public void testEdgeCasesGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup());

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE, null, null, null));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
 }

 @Test
 public void testExceptionHandling() {
 try {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

}