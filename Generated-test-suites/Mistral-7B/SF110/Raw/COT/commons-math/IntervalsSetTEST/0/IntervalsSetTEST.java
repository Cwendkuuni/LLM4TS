import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testConstructorEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorLowerUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 assertEquals(10.0, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testConstructorTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> newList = newIntervalsSet.asList();
 assertEquals(0, newList.size());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 assertEquals(-Infinity.DOUBLE, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 assertEquals(6.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAsListConcurrentModification() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 list.remove(0);
 }

 @Test
 public void testBuildTreeInfiniteLowerNegative() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertTrue(tree.getCut() == null);
 }

 @Test
 public void testBuildTreeInfiniteLowerPositive() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
 assertTrue(tree.getCut() != null);
 }

 @Test
 public void testBuildTreeFiniteLowerFiniteUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 10.0);
 assertFalse(tree.getCut() == null);
 }

 @Test
 public void testBuildTreeFiniteLowerInfiniteUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 assertFalse(tree.getCut() == null);
 }

 @Test
 public void testBuildTreeFiniteUpperFiniteLower() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 10.0);
 assertFalse(tree.getCut() == null);
 }

 @Test
 public void testBuildTreeFiniteUpperInfiniteLower() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertTrue(tree.getCut() == null);
 }
}