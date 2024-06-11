import org.apache.commons.math3.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private static final double EPSILON = 1e-9;

 @Before
 public void setUp() {
 IntervalsSet intervalsSet = new IntervalsSet();
 this.intervalsSet = intervalsSet;
 }

 @Test
 public void testConstructor() {
 IntervalsSet emptyIntervalsSet = new IntervalsSet();
 IntervalsSet intervalsSet1 = new IntervalsSet(0.0, 5.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(Collections.singletonList(new Interval(1.0, 4.0)));
 }

 @Test
 public void testBuildTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = intervalsSet.buildTree(-5.0, 5.0);
 assertEquals(Boolean.TRUE, tree.getAttribute());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidLower() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.buildTree(Double.NEGATIVE_INFINITY, 5.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidUpper() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithBothInfinite() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), EPSILON);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 assertEquals(-Infinity.DOUBLE, intervalsSet.getInf(), EPSILON);
 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(2.0, 5.0), new Interval(0.0, 1.0)));
 assertEquals(0.0, intervalsSet.getInf(), EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 assertEquals(5.0, intervalsSet.getSup(), EPSILON);
 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(2.0, 5.0), new Interval(0.0, 1.0)));
 assertEquals(5.0, intervalsSet.getSup(), EPSILON);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testAsListEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> list = intervalsSet.asList();
 assertEquals(0, list.size());
 }

 @Test
 public void testRecurseList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 List<Interval> list = new ArrayList<>();List<Interval> expectedList = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0));
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(expectedList, list);
 }

 @Test
 public void testRecurseListEmpty() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 List<Interval> list = new ArrayList<>();
 intervalsSet.recurseList(tree, list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(0, list.size());
 }

 @Test
 public void testCheckPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 Region location = intervalsSet.checkPoint(intervalsSet.getTree(false), new Point2D(0.5));
 assertEquals(Region.Location.OUTSIDE, location);
 location = intervalsSet.checkPoint(intervalsSet.getTree(false), new Point2D(2.5));
 assertEquals(Region.Location.INSIDE, location);
 location = intervalsSet.checkPoint(intervalsSet.getTree(false), new Point2D(3.5));
 assertEquals(Region.Location.INSIDE, location);
 }

 @Test
 public void testCheckPointOnBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 5.0)));
 Region location = intervalsSet.checkPoint(intervalsSet.getTree(false), new Point2D(1.5));
 assertEquals(Region.Location.BOUNDARY, location);
 }
}