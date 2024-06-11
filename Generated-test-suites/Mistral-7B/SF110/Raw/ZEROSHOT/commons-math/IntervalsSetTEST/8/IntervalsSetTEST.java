import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any static resources or initialize any class-level variables here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Clean up any static resources or tear down any class-level variables here
 }

 @Before
 public void setUp() {
 // Set up any test-specific resources or initialize any test variables here
 }

 @After
 public void tearDown() {
 // Clean up any test-specific resources or tear down any test variables here
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(1, intervalsSet.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(createBSPTree(1.0, 3.0));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(1, intervalsSet.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(2, intervalsSet.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildTree() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(-1.0, 5.0);
 List<Interval> intervals = intervalsSet.asList(tree);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(-1.0, 0.0), new Interval(0.0, 5.0));
 assertEquals(2, intervals.size());
 assertEquals(expectedIntervals, intervals);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet1 = new IntervalsSet(createBSPTree(1.0, 3.0));
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(createBSPTree(1.5, 4.5));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.5, 3.0), new Interval(3.0, 4.5));
 List<Interval> actualIntervals = intervalsSet2.asList();
 assertEquals(2, intervalsSet2.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 intervalsSet.computeGeometricalProperties();
 double expectedInf = 1.0;
 double expectedSup = 4.0;
 double expectedSize = 3.0;
 double expectedBarycenterX = 2.5;
 Vector1D expectedBarycenter = new Vector1D(expectedBarycenterX);
 assertEquals(expectedInf, intervalsSet.getInf(), 0.0);
 assertEquals(expectedSup, intervalsSet.getSup(), 0.0);
 assertEquals(expectedSize, intervalsSet.size(), 0.0);
 assertEquals(expectedBarycenter, intervalsSet.getBarycenter(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(4.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(2, intervalsSet.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testRecurseList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 List<Interval> actualIntervals = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), actualIntervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(2, actualIntervals.size());
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructor() {
 new IntervalsSet(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructorInf() {
 new IntervalsSet(Double.NaN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructorInfAndNegative() {
 new IntervalsSet(Double.NaN, -1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructorUpper() {
 new IntervalsSet(1.0, Double.NaN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructorUpperAndInf() {
 new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputToConstructorMixed() {
 new IntervalsSet(1.0, Double.NaN, Arrays.asList(new Interval(3.0, 4.0)));
 }

 private static BSPTree<Euclidean1D> createBSPTree(double lower, double upper) {
 return IntervalsSet.buildTree(lower, upper);
 }
}