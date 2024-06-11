import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertNotNull(intervalsSet.getTree());

 IntervalsSet intervalsSetWithBounds = new IntervalsSet(0.0, 10.0);
 assertNotNull(intervalsSetWithBounds.getTree());
 }

 @Test
 public void testBuildTree() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(Double.NEGATIVE_INFINITY, 0.0));
 assertEquals(Boolean.TRUE, intervalsSet.getTree().getCut() == null);

 IntervalsSet intervalsSetWithOneInterval = new IntervalsSet(createIntervalsSet(0.0, Double.POSITIVE_INFINITY));
 assertNotNull(intervalsSetWithOneInterval.getTree().getCut());

 IntervalsSet intervalsSetWithTwoIntervals = new IntervalsSet(createIntervalsSet(0.0, 2.0), createIntervalsSet(3.0, 5.0));
 assertNotNull(intervalsSetWithTwoIntervals.getTree().getCut());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidArguments() {
 new IntervalsSet(Double.NaN, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 10.0));
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(intervalsSet.getTree());
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 intervalsSet.computeGeometricalProperties();
 assertNotNull(intervalsSet.getSize());
 assertNotNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);

 IntervalsSet intervalsSetWithLowerBound = new IntervalsSet(0.0, 5.0);
 assertEquals(0.0, intervalsSetWithLowerBound.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

 IntervalsSet intervalsSetWithUpperBound = new IntervalsSet(5.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithUpperBound.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());

 Interval firstInterval = list.get(0);
 assertEquals(0.0, firstInterval.getInf(), 0.0);
 assertEquals(5.0, firstInterval.getSup(), 0.0);

 Interval secondInterval = list.get(1);
 assertEquals(6.0, secondInterval.getInf(), 0.0);
 assertEquals(10.0, secondInterval.getSup(), 0.0);
 }

 @Test
 public void testRecursiveList() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 2.0), createIntervalsSet(3.0, 5.0), createIntervalsSet(6.0, 10.0));
 List<Interval> list = new ArrayList<Interval>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(3, list.size());

 Interval firstInterval = list.get(0);
 assertEquals(0.0, firstInterval.getInf(), 0.0);
 assertEquals(2.0, firstInterval.getSup(), 0.0);

 Interval secondInterval = list.get(1);
 assertEquals(3.0, secondInterval.getInf(), 0.0);
 assertEquals(5.0, secondInterval.getSup(), 0.0);

 Interval thirdInterval = list.get(2);
 assertEquals(6.0, thirdInterval.getInf(), 0.0);
 assertEquals(10.0, thirdInterval.getSup(), 0.0);
 }

 @Test
 public void testRecursiveListWithEmptyTree() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(Double.NEGATIVE_INFINITY, 0.0));
 List<Interval> list = new ArrayList<Interval>();
 intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertTrue(list.isEmpty());
 }

 @Test
 public void testCheckPointWithPointInsideInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 assertEquals(Region.Location.INSIDE, intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(4.5)));
 }

 @Test
 public void testCheckPointWithPointOutsideLowerBound() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 assertEquals(Region.Location.OUTSIDE, intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(3.0)));
 }

 @Test
 public void testCheckPointWithPointOutsideUpperBound() {
 IntervalsSet intervalsSet = new IntervalsSet(createIntervalsSet(0.0, 5.0), createIntervalsSet(6.0, 10.0));
 assertEquals(Region.Location.OUTSIDE, intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(11.0)));
 }
}