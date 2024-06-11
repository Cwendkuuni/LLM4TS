import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;

 @Before
 public void setUp() {
 intervalsSet = new IntervalsSet(0.0, 5.0);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet emptySet = new IntervalsSet();
 assertTrue(emptySet.isEmpty());
 }

 @Test
 public void testConstructorWithLimit() {
 IntervalsSet set1 = new IntervalsSet(0.0, 5.0);
 IntervalsSet set2 = new IntervalsSet(-1.0, 5.0);
 IntervalsSet set3 = new IntervalsSet(0.0, -1.0);
 IntervalsSet set4 = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

 assertEquals(5.0, set1.getInf(), Precision.EPSILON);
 assertEquals(5.0, set1.getSup(), Precision.EPSILON);
 assertEquals(5.0, set1.asList().get(0).getSup(), Precision.EPSILON);

 assertEquals(-1.0, set3.getInf(), Precision.EPSILON);
 assertEquals(-1.0, set3.getSup(), Precision.EPSILON);
 assertEquals(-1.0, set3.asList().get(0).getInf(), Precision.EPSILON);

 assertTrue(set2.isEmpty());
 assertTrue(set4.isEmpty());
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet set = new IntervalsSet(buildTree(0.0, 5.0));
 IntervalsSet expectedSet = new IntervalsSet(0.0, 5.0);

 assertEquals(expectedSet, set);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 IntervalsSet expectedSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));

 assertEquals(expectedSet, set);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTree() {
 IntervalsSet set = new IntervalsSet(null);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet set = new IntervalsSet();
 IntervalsSet newSet = set.buildNew(buildTree(0.0, 5.0));

 assertEquals(set, newSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));

 assertEquals(Double.POSITIVE_INFINITY, set.getSize(), Precision.EPSILON);
 assertNotNull(set.getBarycenter());
 }

 @Test
 public void testGetInf() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 assertEquals(1.0, set.getInf(), Precision.EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 assertEquals(5.0, set.getSup(), Precision.EPSILON);
 }

 @Test
 public void testAsList() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(1.0, 3.0), new Interval(4.0, 5.0)));
 List<Interval> intervals = set.asList();

 assertEquals(2, intervals.size());
 }
}