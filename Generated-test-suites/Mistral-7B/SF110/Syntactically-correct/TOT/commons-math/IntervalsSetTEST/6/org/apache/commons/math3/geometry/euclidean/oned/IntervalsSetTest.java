import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @BeforeClass
 public static void setupClass() {
 // Initiate empty set
 IntervalsSet emptySet = new IntervalsSet();
 // Initiate set with infinite bounds
 IntervalsSet infiniteSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 // Initiate set with finite bounds
 IntervalsSet finiteSet = new IntervalsSet(0.0, 5.0);
 }

 @Test
 public void testEmptySet() {
 Assert.assertEquals(0, emptySet.size());
 Assert.assertTrue(emptySet.isEmpty());
 Assert.assertNull(emptySet.getBarycenter());
 }

 @Test
 public void testInfiniteSet() {
 Assert.assertEquals(Double.POSITIVE_INFINITY, infiniteSet.size());
 Assert.assertTrue(infiniteSet.isInfinite());
 Assert.assertNull(infiniteSet.getBarycenter());
 }

 @Test
 public void testFiniteSet() {
 // Test getInf() and getSup()
 assertEquals(0.0, finiteSet.getInf(), 0.001);
 assertEquals(5.0, finiteSet.getSup(), 0.001);

 // Test size and barycenter
 assertEquals(5.0, finiteSet.size());
 Vector1D expectedBarycenter = new Vector1D(2.5);
 assertEquals(expectedBarycenter, finiteSet.getBarycenter(), 0.001);

 // Test asList()
 List<Interval> intervals = finiteSet.asList();
 assertEquals(5, intervals.size());

 for (int i = 0; i < 5; i++) {
 double expectedInf = i;
 double expectedSup = i + 1;
 Interval expectedInterval = new Interval(expectedInf, expectedSup);
 assertEquals(expectedInterval, intervals.get(i));
 }
 }

 @Test
 public void testBuildNew() {
 // Create a new IntervalsSet from a BSPTree
 IntervalsSet treeSet = new IntervalsSet(buildTree(0.0, 5.0));

 // Test size, barycenter, and asList()
 assertEquals(5.0, treeSet.size());
 assertEquals(expectedBarycenter, treeSet.getBarycenter(), 0.001);
 List<Interval> treeIntervals = treeSet.asList();
 assertEquals(5, treeIntervals.size());
 }

 @Test
 public void testGetInfWithEmptySet() {
 IntervalsSet emptySet = new IntervalsSet();
 Assert.assertEquals(Double.POSITIVE_INFINITY, emptySet.getInf(), 0.001);
 }

 @Test
 public void testGetInfWithFiniteSet() {
 IntervalsSet finiteSet = new IntervalsSet(0.0, 5.0);
 assertEquals(0.0, finiteSet.getInf(), 0.001);
 }

 @Test
 public void testGetSupWithEmptySet() {
 IntervalsSet emptySet = new IntervalsSet();
 Assert.assertEquals(Double.NEGATIVE_INFINITY, emptySet.getSup(), 0.001);
 }

 @Test
 public void testGetSupWithFiniteSet() {
 IntervalsSet finiteSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, finiteSet.getSup(), 0.001);
 }

 @Test
 public void testAsListWithEmptySet() {
 IntervalsSet emptySet = new IntervalsSet();
 Assert.assertEquals(0, emptySet.asList().size());
 }

 @Test
 public void testAsListWithFiniteSet() {
 IntervalsSet finiteSet = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = finiteSet.asList();
 assertEquals(5, intervals.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentsForBuildTree() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NaN, Double.POSITIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentsForBuildTree2() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.NaN);
 }
}