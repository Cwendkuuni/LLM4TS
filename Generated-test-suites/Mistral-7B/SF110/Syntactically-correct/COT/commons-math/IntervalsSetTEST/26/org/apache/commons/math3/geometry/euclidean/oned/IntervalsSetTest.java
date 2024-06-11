import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new Vector1D(lower), new Vector1D(upper));
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertNull(intervalsSet.getTree());
 }

 @Test
 public void testBuildTreeWithLowerThanZeroAndUpperGreaterThanZero() {
 IntervalsSet intervalsSet = IntervalsSet.buildTree(-1.0, 3.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertEquals(3.0, tree.getCut(0).getHyperplane().getLocation().getX(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerEqualToZeroAndUpperGreaterThanZero() {
 IntervalsSet intervalsSet = IntervalsSet.buildTree(0.0, 3.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertEquals(3.0, tree.getCut(0).getHyperplane().getLocation().getX(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerLessThanZeroAndUpperLessThanZero() {
 IntervalsSet intervalsSet = IntervalsSet.buildTree(-2.0, -1.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertTrue(tree.isLeaf());
 }

 @Test
 public void testBuildTreeWithLowerGreaterThanZeroAndUpperLessThanZero() {
 IntervalsSet intervalsSet = IntervalsSet.buildTree(1.0, -2.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree();
 assertTrue(tree.isLeaf());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(tree, newIntervalsSet.getTree());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 5.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(5.0, intervalsSet.getSize(), Precision.SAFE_MIN);
 }

 @Test
 public void testGetInfWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NaN, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithSetContainingSingleInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetInfWithSetContainingMultipleIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0).union(createIntervalsSet(2.0, 3.0));
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NaN, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithSetContainingSingleInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 assertEquals(2.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testGetSupWithSetContainingMultipleIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0).union(createIntervalsSet(2.0, 3.0));
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 }

 @Test (expected = IllegalStateException.class)
 public void testAsListWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.asList();
 }

 @Test
 public void testAsListWithSetContainingSingleInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(new Interval(1.0, 2.0), intervals.get(0));
 }

 @Test
 public void testAsListWithSetContainingMultipleIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(0.0, 1.0).union(createIntervalsSet(2.0, 3.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertTrue(intervals.contains(new Interval(0.0, 1.0)));
 assertTrue(intervals.contains(new Interval(2.0, 3.0)));
 }
}