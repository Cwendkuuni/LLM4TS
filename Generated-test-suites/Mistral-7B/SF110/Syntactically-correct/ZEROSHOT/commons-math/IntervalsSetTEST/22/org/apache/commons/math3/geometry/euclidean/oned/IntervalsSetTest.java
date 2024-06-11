import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(lower, upper);
 }

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithBoundariesConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithTreeConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithCollectionConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetWithDoubleLowerAndUpperConstructor() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertFalse(intervalsSet.isEmpty());
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval interval1 = intervals.get(0);
 Interval interval2 = intervals.get(1);
 assertEquals(1.0, interval1.getInf(), 0.0);
 assertEquals(2.0, interval1.getSup(), 0.0);
 assertEquals(2.0, interval2.getInf(), 0.0);
 assertEquals(3.0, interval2.getSup(), 0.0);
 }

 @Test
 public void testInfAndSup() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetWithInfiniteLowerAndUpper() {
 IntervalsSet intervalsSet = createIntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testCheckPoint() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 Vector1D point = new Vector1D(2.0);
 Region location = intervalsSet.checkPoint(new BSPTree<Euclidean1D>(intervalsSet.getTree()), point);
 assertEquals(Region.Location.INSIDE, location);
 }

 @Test
 public void testGetBarycenter() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 Vector1D barycenter = intervalsSet.getBarycenter();
 assertNotNull(barycenter);
 }

 @Test
 public void testGetSize() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 double size = intervalsSet.getSize();
 assertEquals(2.0, size, 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>(intervalsSet.getTree()));
 assertSame(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 assertNotNull(intervalsSet.getBarycenter());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetTree() {
 IntervalsSet intervalsSet = new IntervalsSet();
 try {
 intervalsSet.setTree(null);
 } catch (UnsupportedOperationException e) {
 assertEquals("Cannot set tree", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testCheckRegionIntersection() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 2.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(3.0, 4.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 assertNull(region);
 }

 @Test
 public void testCheckRegionIntersectionWithIntersection() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 Interval intersection = region.getIntersection();
 assertNotNull(intersection);
 }

 @Test
 public void testCheckRegionIntersectionWithEmptyIntersection() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(0.0, 2.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 assertNull(region);
 }

 @Test
 public void testCheckRegionIntersectionWithUnion() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 Interval union = intervalsSet1.union(intervalsSet2);
 assertNotNull(region);
 assertEquals(union, region.getUnion());
 }

 @Test
 public void testCheckRegionIntersectionWithDifference() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 Interval difference = intervalsSet1.difference(intervalsSet2);
 assertNotNull(region);
 assertEquals(difference, region.getDifference());
 }

 @Test
 public void testCheckRegionIntersectionWithComplement() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 Region region = intervalsSet1.checkRegionIntersection(intervalsSet2, null);
 IntervalsSet complement = intervalsSet1.complement(intervalsSet2);
 assertNotNull(region);
 assertEquals(complement, region.getComplement());
 }

 @Test
 public void testUnion() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 2.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(3.0, 4.0);
 IntervalsSet unionIntervalsSet = intervalsSet1.union(intervalsSet2);
 List<Interval> unionIntervals = unionIntervalsSet.asList();
 assertEquals(3, unionIntervals.size());
 }

 @Test
 public void testDifference() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 IntervalsSet differenceIntervalsSet = intervalsSet1.difference(intervalsSet2);
 List<Interval> differenceIntervals = differenceIntervalsSet.asList();
 assertEquals(1, differenceIntervals.size());
 }

 @Test
 public void testComplement() {
 IntervalsSet intervalsSet1 = createIntervalsSet(1.0, 3.0);
 IntervalsSet intervalsSet2 = createIntervalsSet(2.0, 4.0);
 IntervalsSet complementIntervalsSet = intervalsSet1.complement(intervalsSet2);
 List<Interval> complementIntervals = complementIntervalsSet.asList();
 assertEquals(1, complementIntervals.size());
 }
}