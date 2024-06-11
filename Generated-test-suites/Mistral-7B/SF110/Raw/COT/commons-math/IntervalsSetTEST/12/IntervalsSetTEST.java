import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new Vector1D(lower), new Vector1D(upper));
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(2, intervalsSet.size());
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(2, intervalsSet.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidLower() {
 IntervalsSet.buildTree(-1.0, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidUpper() {
 IntervalsSet.buildTree(1.0, Double.NEGATIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidLowerAndUpper() {
 IntervalsSet.buildTree(-1.0, Double.NEGATIVE_INFINITY);
 }

 @Test
 public void testBuildNewWithValidTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertTrue(newIntervalsSet.isEmpty());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testGetInfWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf());
 }

 @Test
 public void testGetInfWithNonEmptyIntervalsSet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup());
 }

 @Test
 public void testGetSupWithNonEmptyIntervalsSet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 assertTrue(list.isEmpty());
 }

 @Test
 public void testAsListWithNonEmptyIntervalsSet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testListContainsCorrectIntervals() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 List<Interval> list = intervalsSet.asList();
 assertTrue(list.contains(new Interval(1.0, 2.0)));
 assertTrue(list.contains(new Interval(2.0, 3.0)));
 }

}