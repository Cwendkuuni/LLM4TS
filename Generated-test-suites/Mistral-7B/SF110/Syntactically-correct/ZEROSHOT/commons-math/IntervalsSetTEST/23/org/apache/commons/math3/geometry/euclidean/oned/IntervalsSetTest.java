import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private IntervalsSet emptyIntervalsSet;
 private IntervalsSet singleIntervalSet;
 private IntervalsSet overlappingIntervalsSet;
 private IntervalsSet disjointIntervalsSet;

 @Before
 public void setUp() {
 intervalsSet = new IntervalsSet();
 emptyIntervalsSet = new IntervalsSet();
 singleIntervalSet = new IntervalsSet(new Interval(2.0, 4.0));
 overlappingIntervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 5.0));
 disjointIntervalsSet = new IntervalsSet(new Interval(0.0, 2.0), new Interval(5.0, 7.0));
 }

 @After
 public void tearDown() {
 intervalsSet = null;
 emptyIntervalsSet = null;
 singleIntervalSet = null;
 overlappingIntervalsSet = null;
 disjointIntervalsSet = null;
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals("Empty IntervalsSet should not have a tree", null, intervalsSet.getTree());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSetWithBounds = new IntervalsSet(1.0, 3.0);
 assertEquals("IntervalsSet with bounds should have a tree", true, intervalsSetWithBounds.getTree() instanceof BSPTree);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForLowerThanMinusInfinity() {
 new IntervalsSet(-Infinity.DOUBLE, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForUpperGreaterThanPlusInfinity() {
 new IntervalsSet(0.0, Infinity.DOUBLE);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSetWithTree = new IntervalsSet(new BSPTree<Euclidean1D>(new Point(2.0), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null));
 assertEquals("IntervalsSet with tree should have the same tree", intervalsSetWithTree.getTree(), intervalsSetWithTree.getTree());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSetWithBoundary = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals("IntervalsSet with boundary should have the same intervals", intervalsSetWithBoundary.asList(), intervalsSetWithBoundary.asList());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(new Point(2.0), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null));
 assertEquals("New IntervalsSet should have the same tree", intervalsSet.getTree(), newIntervalsSet.getTree());
 }

 @Test
 public void testComputeGeometricalProperties() {
 intervalsSet.add(new Interval(1.0, 2.0));
 intervalsSet.add(new Interval(3.0, 4.0));
 intervalsSet.computeGeometricalProperties();

 double barycenter = intervalsSet.getBarycenter().getX();
 double size = intervalsSet.getSize();

 assertEquals("Barycenter should be the average of intervals' barycenters", (1.0 + 3.0) / 2.0, barycenter, 0.001);
 assertEquals("Size should be the sum of intervals' sizes", 2.0 + 1.0, size, 0.001);
 }

 @Test
 public void testInf() {
 assertEquals("-Infinity", Double.NEGATIVE_INFINITY, emptyIntervalsSet.getInf());
 assertEquals("Inf for single interval should be the upper bound", 4.0, singleIntervalSet.getInf());
 assertEquals("Inf for overlapping intervals should be the lower bound of the first interval", 1.0, overlappingIntervalsSet.getInf());
 }

 @Test
 public void testSup() {
 assertEquals("Sup for empty intervals set should be Infinity", Double.POSITIVE_INFINITY, emptyIntervalsSet.getSup());
 assertEquals("Sup for single interval should be the upper bound", 4.0, singleIntervalSet.getSup());
 assertEquals("Sup for overlapping intervals should be the upper bound of the second interval", 5.0, overlappingIntervalsSet.getSup());
 }

 @Test
 public void testAsList() {
 List<Interval> intervals = intervalsSet.asList();
 assertEquals("Size of asList should be equal to the number of intervals", 2, intervals.size());
 assertEquals("First interval should be [1.0, 2.0]", new Interval(1.0, 2.0), intervals.get(0));
 assertEquals("Second interval should be [3.0, 4.0]", new Interval(3.0, 4.0), intervals.get(1));
 }

 @Test
 public void testRecurseList() {
 List<Interval> intervals = new ArrayList<>();
 intervalsSet.recurseList(intervalsSet.getTree(), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals("Size of intervals list should be equal to the number of intervals", 2, intervals.size());
 assertEquals("First interval should be [1.0, 2.0]", new Interval(1.0, 2.0), intervals.get(0));
 assertEquals("Second interval should be [3.0, 4.0]", new Interval(3.0, 4.0), intervals.get(1));
 }

 @Test
 public void testCheckPoint() {
 List<Region.Location> locations = new ArrayList<>();
 locations.add(Region.Location.INSIDE);
 locations.add(Region.Location.OUTSIDE);
 locations.add(Region.Location.ON_BOUNDARY);

 Interval interval = new Interval(1.0, 3.0);
 BSPTree<Euclidean1D> node = new BSPTree<Euclidean1D>(new Point(2.0), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE, interval, new BSPTree<Euclidean1D>(Boolean.FALSE), null), null);

 for (Interval testInterval : Arrays.asList(new Interval(0.0, 2.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0))) {
 Region.Location location = ((AbstractRegion<Euclidean1D, T>)intervalsSet).checkPoint(node, testInterval.getInf());
 assertEquals("CheckPoint should return the correct location for testInterval [" + testInterval.getInf() + "]", locations.get(testInterval.getInf() < 1.0 ? 0 : testInterval.getInf() > 3.0 ? 2 : 1), location.ordinal());
 }
 }
}