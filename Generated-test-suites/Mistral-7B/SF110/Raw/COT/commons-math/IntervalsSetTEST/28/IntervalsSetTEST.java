import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(lower, upper);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertNull(intervalsSet.getTree());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(intervalsSet.getInf(), 1.0, Precision.EPSILON);
 assertEquals(intervalsSet.getSup(), 3.0, Precision.EPSILON);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
 assertEquals(intervalsSet.getInf(), Double.NEGATIVE_INFINITY);
 assertEquals(intervalsSet.getSup(), Double.POSITIVE_INFINITY);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(intervalsSet.getInf(), 1.0, Precision.EPSILON);
 assertEquals(intervalsSet.getSup(), 4.0, Precision.EPSILON);
 }

 @Test
 public void testBuildTreeWithLowerAndUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 3.0);
 assertEquals(tree.getClass(), BSPTree.class);
 assertFalse(tree.getCut() instanceof OrientedPoint);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidLower() {
 IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidUpper() {
 IntervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testBuildNewWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(newIntervalsSet.getInf(), Double.NEGATIVE_INFINITY);
 assertEquals(newIntervalsSet.getSup(), Double.POSITIVE_INFINITY);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 assertNull(intervalsSet.getBarycenter());
 assertEquals(intervalsSet.getSize(), Double.POSITIVE_INFINITY);

 intervalsSet = createIntervalsSet(0.0, 2.0);
 intervalsSet.computeGeometricalProperties();
 assertNull(intervalsSet.getBarycenter());
 assertEquals(intervalsSet.getSize(), 0.0);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(4.0, 5.0)));
 intervalsSet.computeGeometricalProperties();
 assertNull(intervalsSet.getBarycenter());
 assertEquals(intervalsSet.getSize(), 2.0);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(6.0, 7.0)));
 intervalsSet.computeGeometricalProperties();
 assertNotNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(intervalsSet.getInf(), 1.0, Precision.EPSILON);

 intervalsSet = createIntervalsSet(0.0, 2.0);
 assertEquals(intervalsSet.getInf(), 0.0, Precision.EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(intervalsSet.getSup(), 3.0, Precision.EPSILON);

 intervalsSet = createIntervalsSet(0.0, 2.0);
 assertEquals(intervalsSet.getSup(), 2.0, Precision.EPSILON);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(6.0, 7.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(list.size(), 3);
 assertEquals(list.get(0).getInf(), 1.0, Precision.EPSILON);
 assertEquals(list.get(0).getSup(), 2.0, Precision.EPSILON);
 assertEquals(list.get(1).getInf(), 3.0, Precision.EPSILON);
 assertEquals(list.get(1).getSup(), 4.0, Precision.EPSILON);
 assertEquals(list.get(2).getInf(), 6.0, Precision.EPSILON);
 assertEquals(list.get(2).getSup(), 7.0, Precision.EPSILON);
 }
}