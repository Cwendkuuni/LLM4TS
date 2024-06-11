import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new Vector1D(lower), new Vector1D(upper));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet is = new IntervalsSet();
 assertTrue(is.isEmpty());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 assertEquals(2, is.size());
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet is = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
 assertEquals(1, is.size());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet is = new IntervalsSet(Arrays.asList(new Interval(0.5, 1.5), new Interval(2.5, 3.5)));
 assertEquals(2, is.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLowerBound() {
 this.createIntervalsSet(-1.0, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidUpperBound() {
 this.createIntervalsSet(1.0, Double.NEGATIVE_INFINITY);
 }

 @Test
 public void testBuildTreeWithFiniteBounds() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 BSPTree<Euclidean1D> tree = is.getTree(false);
 assertEquals(2, tree.getLeafCount());
 }

 @Test
 public void testBuildTreeWithLowerInfinite() {
 IntervalsSet is = new IntervalsSet(new Vector1D(-1.0), new Vector1D(3.0));
 BSPTree<Euclidean1D> tree = is.getTree(false);
 assertEquals(1, tree.getLeafCount());
 }

 @Test
 public void testBuildTreeWithUpperInfinite() {
 IntervalsSet is = new IntervalsSet(new Vector1D(1.0), new Vector1D(Double.POSITIVE_INFINITY));
 BSPTree<Euclidean1D> tree = is.getTree(false);
 assertEquals(1, tree.getLeafCount());
 }

 @Test
 public void testBuildTreeWithBoundsOverlapping() {
 IntervalsSet is = this.createIntervalsSet(0.5, 3.5);
 BSPTree<Euclidean1D> tree = is.getTree(false);
 assertEquals(1, tree.getLeafCount());
 }

 @Test
 public void testBuildTreeWithBoundsNotOverlapping() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is = is1.union(is2);
 BSPTree<Euclidean1D> tree = is.getTree(false);
 assertEquals(2, tree.getLeafCount());
 }

 @Test
 public void testGetInf() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 assertEquals(3.0, is.getInf(), 0.0);
 }

 @Test
 public void testGetInfForEmptySet() {
 IntervalsSet is = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, is.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 assertEquals(1.0, is.getSup(), 0.0);
 }

 @Test
 public void testGetSupForEmptySet() {
 IntervalsSet is = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, is.getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptySet() {
 IntervalsSet is = new IntervalsSet();
 List<Interval> list = is.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testAsList() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 List<Interval> list = is.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testAsListWithLowerInfinite() {
 IntervalsSet is = new IntervalsSet(new Vector1D(-1.0), new Vector1D(3.0));
 List<Interval> list = is.asList();
 assertEquals(1, list.size());
 assertEquals(0.0, list.get(0).getInf(), 0.0);
 }

 @Test
 public void testAsListWithUpperInfinite() {
 IntervalsSet is = new IntervalsSet(new Vector1D(1.0), new Vector1D(Double.POSITIVE_INFINITY));
 List<Interval> list = is.asList();
 assertEquals(1, list.size());
 assertEquals(Double.POSITIVE_INFINITY, list.get(0).getSup(), 0.0);
 }

 @Test
 public void testAsListWithOverlappingBounds() {
 IntervalsSet is = this.createIntervalsSet(0.5, 3.5);
 List<Interval> list = is.asList();
 assertEquals(1, list.size());
 }

 @Test
 public void testAsListWithNonOverlappingBounds() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is = is1.union(is2);
 List<Interval> list = is.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testSize() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 assertEquals(2, is.size());
 }

 @Test
 public void testSizeForEmptySet() {
 IntervalsSet is = new IntervalsSet();
 assertEquals(0, is.size());
 }

 @Test
 public void testIsEmpty() {
 IntervalsSet is = new IntervalsSet();
 assertTrue(is.isEmpty());
 }

 @Test
 public void testIsEmptyForSetWithBounds() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 assertFalse(is.isEmpty());
 }

 @Test
 public void testUnion() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is = is1.union(is2);
 assertEquals(2, is.size());
 }

 @Test
 public void testUnionWithEmptySet() {
 IntervalsSet is1 = new IntervalsSet();
 IntervalsSet is = is1.union(this.createIntervalsSet(1.0, 3.0));
 assertEquals(1, is.size());
 }

 @Test
 public void testUnionWithSelf() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = is;
 IntervalsSet is3 = is2.union(is);
 assertEquals(1, is3.size());
 }

 @Test
 public void testUnionWithEmptyIntervalSet() {
 IntervalsSet is1 = new IntervalsSet();
 IntervalsSet is2 = new IntervalsSet();
 IntervalsSet is = is1.union(is2);
 assertEquals(0, is.size());
 }

 @Test
 public void testUnionWithOverlappingIntervals() {
 IntervalsSet is1 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is2 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is = is1.union(is2);
 assertEquals(2, is.size());
 }

 @Test
 public void testIntersection() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(2.0, 2.5);
 IntervalsSet is = is1.intersection(is2);
 assertEquals(0, is.size());
 }

 @Test
 public void testIntersectionWithEmptySet() {
 IntervalsSet is1 = new IntervalsSet();
 IntervalsSet is = is1.intersection(this.createIntervalsSet(1.0, 3.0));
 assertEquals(0, is.size());
 }

 @Test
 public void testIntersectionWithSelf() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = is;
 IntervalsSet is3 = is.intersection(is2);
 assertEquals(1, is3.size());
 }

 @Test
 public void testIntersectionWithNonOverlappingIntervals() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is = is1.intersection(is2);
 assertEquals(0, is.size());
 }

 @Test
 public void testDifference() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.5);
 IntervalsSet is = is1.difference(is2);
 assertEquals(1, is.size());
 }

 @Test
 public void testDifferenceWithEmptySet() {
 IntervalsSet is1 = new IntervalsSet();
 IntervalsSet is = is1.difference(this.createIntervalsSet(1.0, 3.0));
 assertEquals(0, is.size());
 }

 @Test
 public void testDifferenceWithSelf() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = is;
 IntervalsSet is3 = is.difference(is2);
 assertEquals(0, is3.size());
 }

 @Test
 public void testDifferenceWithIntervalsNotOverlapping() {
 IntervalsSet is1 = this.createIntervalsSet(1.0, 3.0);
 IntervalsSet is2 = this.createIntervalsSet(0.5, 2.0);
 IntervalsSet is = is1.difference(is2);
 assertEquals(0, is.size());
 }

 @Test
 public void testComplement() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 Region region = new Line(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet isComplement = is.complement(region);
 assertEquals(1, isComplement.size());
 }

 @Test
 public void testComplementWithEmptySet() {
 IntervalsSet is = new IntervalsSet();
 Region region = new Line(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet isComplement = is.complement(region);
 assertEquals(1, isComplement.size());
 }

 @Test
 public void testComplementWithSelf() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 Region region = new Line(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet isComplement = is.complement(region);
 assertEquals(0, isComplement.size());
 }

 @Test
 public void testComplementWithRegionOutside() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 Region region = new Line(new Vector1D(0.0), new Vector1D(2.0));
 IntervalsSet isComplement = is.complement(region);
 assertEquals(1, isComplement.size());
 }

 @Test
 public void testComplementWithRegionInside() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 Region region = new Interval(1.5, 2.5);
 IntervalsSet isComplement = is.complement(region);
 assertEquals(1, isComplement.size());
 }

 @Test
 public void testComplementWithIntervalPartiallyInside() {
 IntervalsSet is = this.createIntervalsSet(1.0, 3.0);
 Region region = new Interval(1.2, 2.8);
 IntervalsSet isComplement = is.complement(region);
 assertEquals(1, isComplement.size());
 }
}