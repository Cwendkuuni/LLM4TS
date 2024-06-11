import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @BeforeClass
 public static void setUp() {
 // Set up any static resources or initialize any static variables
 }

 @AfterClass
 public static void tearDown() {
 // Clean up any static resources
 }

 @Before
 public void setUpEach() {
 // Set up any instance variables for each test
 }

 @After
 public void tearDownEach() {
 // Clean up any instance variables
 }

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithInfAndNegInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTwoIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 2.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 assertEquals(1.0, intervals.get(0).getInf(), 0.001);
 assertEquals(2.0, intervals.get(0).getSup(), 0.001);
 assertEquals(0.0, intervals.get(1).getInf(), 0.001);
 assertEquals(1.0, intervals.get(1).getSup(), 0.001);
 }

 @Test
 public void testIntervalsSetWithThreeIntervalsOverlap() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
 intervalsSet.add(new Interval(1.0, 2.0));
 intervalsSet.add(new Interval(2.0, 3.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(3, intervalsList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNegativeLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(-1.0, 0.0);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithUpperBoundLowerThanLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(2.0, 1.0);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddIntervalToFullIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 3.0);
 try {
 intervalsSet.add(new Interval(1.0, 2.0));
 fail("Expected an UnsupportedOperationException to be thrown");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }
}