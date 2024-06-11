import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any static resources or configuration here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Clean up any static resources or configuration here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or objects here
 }

 @After
 public void tearDown() {
 // Clean up any instance variables or objects here
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup());
 assertEquals(0.0, intervalsSet.getInf());
 assertEquals(5.0, intervalsSet.size());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }
 }

 @Test
 public void testConstructorWithBSPTree() {
 BSPTree<Euclidean1D> bspTree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane(),
 new BSPTree<>(Boolean.FALSE), new BSPTree<>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(),
 new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
 IntervalsSet intervalsSet = new IntervalsSet(bspTree);
 assertEquals(5.0, intervalsSet.getSup());
 assertEquals(0.0, intervalsSet.getInf());
 assertEquals(5.0, intervalsSet.size());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }
 }

 @Test
 public void testConstructorWithCollection() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane());
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertEquals(5.0, intervalsSet.getSup());
 assertEquals(0.0, intervalsSet.getInf());
 assertEquals(5.0, intervalsSet.size());
 List<Interval> intervals = intervalsSet.asList();
 for (Interval interval : intervals) {
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(3.0, interval.getInf2(), 0.0);
 assertEquals(3.0, interval.getSup(), 0.0);
 assertEquals(5.0, interval.getSup2(), 0.0);
 }
 }

 @Test
 public void testBuildNew() {
 BSPTree<Euclidean1D> bspTree = new BSPTree<>(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane(),
 new BSPTree<>(Boolean.FALSE), new BSPTree<>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(),
 new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(bspTree);
 assertEquals(intervalsSet1, intervalsSet2);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
 new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane(),
 new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane());intervalsSet.computeGeometricalProperties();assertEquals(5.0, intervalsSet.getSize());assertEquals(3.0, intervalsSet.getBarycenter().getX(), 0.0); }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(); intervalsSet.add(new Interval(1.0, 2.0)); intervalsSet.add(new Interval(4.0, 5.0)); assertEquals(5.0, intervalsSet.getSup()); assertEquals(1.0, intervalsSet.getInf(), 0.0); }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(); intervalsSet.add(new Interval(1.0, 2.0)); intervalsSet.add(new Interval(4.0, 5.0)); assertEquals(5.0, intervalsSet.getSup()); assertEquals(5.0, intervalsSet.getSup(), 0.0); }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
 new OrientedPoint(new Vector1D(3.0), true).wholeHyperplane(),
 new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane()); List<Interval> intervals = intervalsSet.asList(); assertEquals(3, intervals.size()); Interval interval = intervals.get(0); assertEquals(0.0, interval.getInf(), 0.0); assertEquals(3.0, interval.getSup(), 0.0); interval = intervals.get(1); assertEquals(3.0, interval.getInf(), 0.0); assertEquals(5.0, interval.getSup(), 0.0); }
}