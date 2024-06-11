import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1, intervalsSet.size());
 assertEquals(1.5, intervalsSet.getBarycenter().getX(), 0.001);
 }

 @Test
 public void testIntervalsSetWithTwoIntervalsOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(2.0, 4.0));
 assertEquals(2, intervalsSet.size());
 assertEquals(2.5, intervalsSet.getBarycenter().getX(), 0.001);
 }

 @Test
 public void testIntervalsSetWithTwoIntervalsNotOverlapping() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertEquals(2, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithInfiniteInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInvalidInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertEquals(0, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithNullCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 assertNull(intervalsSet);
 }

 @Test
 public void testIntervalsSetWithNullTree() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 assertNull(intervalsSet);
 }

 @Test
 public void testBuildNew() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
 assertEquals(1, intervalsSet.size());
 assertEquals(1.0, intervalsSet.getInf(), 0.001);
 assertEquals(1.0, intervalsSet.getSup(), 0.001);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 intervalsSet.computeGeometricalProperties();
 assertEquals(2, intervalsSet.size());
 assertNull(intervalsSet.getBarycenter());
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
}