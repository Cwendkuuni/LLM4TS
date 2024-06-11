import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptySet() {
 IntervalsSet emptySet = new IntervalsSet();
 assertEquals(0.0, emptySet.getSize(), Precision.EPSILON);
 assertNull(emptySet.getBarycenter());
 }

 @Test
 public void testSingleIntervalSet() {
 IntervalsSet singleSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertEquals(1.0, singleSet.getSize(), Precision.EPSILON);
 assertEquals(new Vector1D(1.5), singleSet.getBarycenter());
 }

 @Test
 public void testInfiniteIntervalSet() {
 IntervalsSet infiniteSetLower = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 IntervalsSet infiniteSetUpper = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 IntervalsSet unionSet = new IntervalsSet(infiniteSetLower, infiniteSetUpper);
 assertEquals(Double.POSITIVE_INFINITY, unionSet.getSize(), Precision.EPSILON);
 assertNull(unionSet.getBarycenter());
 }

 @Test
 public void testFiniteAndInfiniteIntervalSet() {
 IntervalsSet finiteSet = new IntervalsSet(new Interval(1.0, 2.0));
 IntervalsSet infiniteSetLower = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 IntervalsSet infiniteSetUpper = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 IntervalsSet unionSet = new IntervalsSet(finiteSet, infiniteSetLower, infiniteSetUpper);
 assertEquals(Double.POSITIVE_INFINITY, unionSet.getSize(), Precision.EPSILON);
 assertNull(unionSet.getBarycenter());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIntervalSet() {
 IntervalsSet invalidSet = new IntervalsSet(new Interval(0.0, -1.0));
 }

 @Test
 public void testGetInfAndSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
 assertEquals(1.0, intervalsSet.getInf(), Precision.EPSILON);
 assertEquals(7.0, intervalsSet.getSup(), Precision.EPSILON);
 }

 @Test
 public void testList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 assertEquals(new Interval(1.0, 3.0), intervalsList.get(0));
 assertEquals(new Interval(5.0, 7.0), intervalsList.get(1));
 }

 @Test
 public void testBSPTreeBuilding() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 7.0));
 IntervalsSet newSet = intervalsSet.buildNew(intervalsSet.getTree());
 assertEquals(intervalsSet, newSet);
 }

}