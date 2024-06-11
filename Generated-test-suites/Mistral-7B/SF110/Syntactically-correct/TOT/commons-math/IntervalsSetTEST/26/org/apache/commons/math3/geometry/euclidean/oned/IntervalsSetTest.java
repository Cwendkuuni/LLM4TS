import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any class-level variables here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static resources here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or initialize any instance-level variables here
 }

 @After
 public void tearDown() {
 // Clean up any instance resources here
 }

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertFalse(intervalsSet.contains(new Euclidean1D(0.0)));
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, intervalsSet.size());
 assertTrue(intervalsSet.contains(new Euclidean1D(1.5)));
 assertFalse(intervalsSet.contains(new Euclidean1D(0.5)));
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 4.0));
 assertEquals(1, intervalsSet.size());
 assertTrue(intervalsSet.contains(new Euclidean1D(2.5)));
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(2, intervalsSet.size());
 assertFalse(intervalsSet.contains(new Euclidean1D(2.5)));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0, intervalsSet.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 }

 @Test
 public void testBuildTreeEdgeCases() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
 assertEquals(Boolean.TRUE, tree.getAttribute());

 tree = IntervalsSet.buildTree(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Boolean.TRUE, tree.getAttribute());

 tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(Boolean.FALSE, tree.getCut().isDirect());

 tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 assertEquals(Boolean.FALSE, tree.getCut().isDirect());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(1.0, intervalsSet.getInf(), 0.001);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(4.0, intervalsSet.getSup(), 0.001);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.001);
 }
}