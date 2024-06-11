import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertFalse(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertFalse(intervalsSet.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionWithInvalidLower() {
 IntervalsSet intervalsSet = new IntervalsSet(-1.0, 5.0);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionWithInvalidUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, -1.0);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testBuildNewWithTree() {
 IntervalsSet sourceIntervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null));
 IntervalsSet targetIntervalsSet = new IntervalsSet().buildNew(sourceIntervalsSet.getTree());
 assertEquals(sourceIntervalsSet, targetIntervalsSet);
 }

 @Test
 public void testComputeGeometricalPropertiesWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());
 }

 @Test
 public void testComputeGeometricalPropertiesWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(3.0, intervalsSet.getBarycenter().getX(), 0.0);
 assertEquals(6.0, intervalsSet.getSize());
 }

 @Test(expected = ArithmeticException.class)
 public void testArithmeticExceptionWithDivisionByZero() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 1.0);
 intervalsSet.computeGeometricalProperties();
 intervalsSet.getBarycenter().divide(0.0);
 fail("ArithmeticException expected but not thrown");
 }

 @Test
 public void testGetInfWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf());
 }

 @Test
 public void testGetInfWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup());
 }

 @Test
 public void testGetSupWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsListWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervalsList = intervalsSet.asList();
 assertTrue(intervalsList.isEmpty());
 }

 @Test
 public void testAsListWithNonEmptySet() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertFalse(intervalsList.isEmpty());
 assertEquals(3, intervalsList.size());
 }

 @Test
 public void testListContainsExpectedInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 List<Interval> intervalsList = intervalsSet.asList();
 Interval expectedInterval = new Interval(2.0, 4.0);
 assertTrue(intervalsList.contains(expectedInterval));
 }

 @Test
 public void testListDoesNotContainUnexpectedInterval() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 5.0);
 List<Interval> intervalsList = intervalsSet.asList();
 Interval unexpectedInterval = new Interval(3.0, 2.0);
 assertFalse(intervalsList.contains(unexpectedInterval));
 }
}