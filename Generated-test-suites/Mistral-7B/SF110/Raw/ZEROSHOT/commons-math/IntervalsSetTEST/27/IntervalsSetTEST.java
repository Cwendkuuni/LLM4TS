import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptySet() {
 IntervalsSet set = new IntervalsSet();
 assertEquals(0.0, set.getInf(), 0.0);
 assertEquals(0.0, set.getSup(), 0.0);
 assertFalse(set.isEmpty());
 assertTrue(set.isInfinite());
 assertNotNull(set.getTree());
 assertEquals(0, set.size());
 assertNull(set.getBarycenter());
 }

 @Test
 public void testFiniteSet() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, set.getInf(), 0.0);
 assertEquals(2.0, set.getSup(), 0.0);
 assertFalse(set.isEmpty());
 assertFalse(set.isInfinite());
 assertNotNull(set.getTree());
 assertEquals(1, set.size());
 assertNotNull(set.getBarycenter());
 }

 @Test
 public void testInfiniteSetBelow() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0);
 assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 0.0);
 assertEquals(1.0, set.getSup(), 0.0);
 assertFalse(set.isEmpty());
 assertTrue(set.isInfinite());
 assertNotNull(set.getTree());
 assertEquals(0, set.size());
 assertNull(set.getBarycenter());
 }

 @Test
 public void testInfiniteSetAbove() {
 IntervalsSet set = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
 assertEquals(1.0, set.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 0.0);
 assertFalse(set.isEmpty());
 assertTrue(set.isInfinite());
 assertNotNull(set.getTree());
 assertEquals(0, set.size());
 assertNull(set.getBarycenter());
 }

 @Test
 public void testBuildTree() {
 Euclidean1D p1 = new Vector1D(1.0);
 Euclidean1D p2 = new Vector1D(2.0);
 Euclidean1D p3 = new Vector1D(3.0);
 SubHyperplane<Euclidean1D> lowerCut1 = new OrientedPoint(p1, false).wholeHyperplane();
 SubHyperplane<Euclidean1D> upperCut1 = new OrientedPoint(p2, true).wholeHyperplane();
 SubHyperplane<Euclidean1D> lowerCut2 = new OrientedPoint(p2, false).wholeHyperplane();
 SubHyperplane<Euclidean1D> upperCut3 = new OrientedPoint(p3, true).wholeHyperplane();
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(lowerCut1, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(upperCut1, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), null);
 IntervalsSet set = new IntervalsSet(buildTree(1.0, 3.0));
 assertEquals(expectedTree, set.getTree());
 }

 @Test
 public void testBuildNew() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet set = new IntervalsSet();
 IntervalsSet newSet = set.buildNew(tree);
 assertEquals(tree, newSet.getTree());
 }

 @Test
 public void testAsList() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> expectedList = Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> list = set.asList();
 assertEquals(expectedList, list);
 }

 @Test
 public void testGetInf() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(1.0, set.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(4.0, set.getSup(), 0.0);
 }

 @Test
 public void testRecurseList() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet set = new IntervalsSet(tree);
 List<Interval> expectedList = new ArrayList<Interval>();
 // Empty set
 List<Interval> list = set.asList();
 assertEquals(expectedList, list);
 // Single interval
 set = new IntervalsSet(new Interval(1.0, 2.0));
 list = set.asList();
 expectedList.add(new Interval(1.0, 2.0));
 assertEquals(expectedList, list);
 // Intervals overlapping at the left
 set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0));
 list = set.asList();
 expectedList.add(new Interval(1.0, 2.0));
 expectedList.add(new Interval(2.0, 3.0));
 assertEquals(expectedList, list);
 // Intervals not overlapping at the left
 set = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 list = set.asList();
 expectedList.add(new Interval(1.0, 2.0));
 expectedList.remove(expectedList.size() - 1);
 expectedList.add(new Interval(3.0, 4.0));
 assertEquals(expectedList, list);
 }

}