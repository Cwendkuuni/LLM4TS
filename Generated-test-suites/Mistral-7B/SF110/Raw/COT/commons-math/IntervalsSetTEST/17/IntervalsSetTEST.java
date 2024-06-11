import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testConstructorEmpty() {
 IntervalsSet set = new IntervalsSet();
 assertNull(set.getTree());
 }

 @Test
 public void testConstructorLowerUpper() {
 IntervalsSet set = new IntervalsSet(0.0, 5.0);
 assertNotNull(set.getTree());
 assertEquals(5.0, set.getSup(), 0.0);
 assertEquals(0.0, set.getInf(), 0.0);
 }

 @Test
 public void testConstructorBSPTree() {
 IntervalsSet set = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane()));
 assertNotNull(set.getTree());
 assertEquals(5.0, set.getSup(), 0.0);
 assertEquals(0.0, set.getInf(), 0.0);
 }

 @Test
 public void testConstructorCollection() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
 IntervalsSet set = new IntervalsSet(boundary);
 assertNotNull(set.getTree());
 assertEquals(5.0, set.getSup(), 0.0);
 assertEquals(0.0, set.getInf(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionLowerInfiniteNegative() {
 IntervalsSet set = new IntervalsSet(-1.0, Double.NEGATIVE_INFINITY);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionUpperInfinitePositive() {
 IntervalsSet set = new IntervalsSet(Double.POSITIVE_INFINITY, 5.0);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionLowerAndUpperIdentical() {
 IntervalsSet set = new IntervalsSet(0.0, 0.0);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalSet = new IntervalsSet(0.0, 5.0);
 IntervalsSet newSet = originalSet.buildNew(originalSet.getTree());
 assertEquals(originalSet, newSet);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet set = new IntervalsSet(0.0, 5.0);
 set.computeGeometricalProperties();
 assertEquals(5.0, set.getSize(), 0.0);
 assertNull(set.getBarycenter());
}

 @Test
 public void testGetInf() {
 IntervalsSet set = new IntervalsSet(0.0, 5.0);
 assertEquals(0.0, set.getInf(), 0.0);
}

 @Test
 public void testGetSup() {
 IntervalsSet set = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, set.getSup(), 0.0);
}

 @Test
 public void testAsList() {
 IntervalsSet set = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = set.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
}

 @Test
 public void testAsListEmpty() {
 IntervalsSet set = new IntervalsSet();
 List<Interval> intervals = set.asList();
 assertEquals(0, intervals.size());
}

 @Test
 public void testAsListMultipleIntervals() {
 IntervalsSet set = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane()));
 List<Interval> intervals = set.asList();
 assertEquals(3, intervals.size());
 Interval firstInterval = intervals.get(0);
 Interval secondInterval = intervals.get(1);
 Interval thirdInterval = intervals.get(2);
 assertEquals(0.0, firstInterval.getInf(), 0.0);
 assertEquals(2.0, firstInterval.getSup(), 0.0);
 assertEquals(2.0, secondInterval.getInf(), 0.0);
 assertEquals(4.0, secondInterval.getSup(), 0.0);
 assertEquals(4.0, thirdInterval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, thirdInterval.getSup(), 0.0);
}

 @Test
 public void testCheckPoint() {
 IntervalsSet set = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane()));
 assertEquals(Region.Location.INSIDE, set.checkPoint(new Vector1D(1.5), null));
 assertEquals(Region.Location.BOUNDARY, set.checkPoint(new Vector1D(2.0), null));
 assertEquals(Region.Location.OUTSIDE, set.checkPoint(new Vector1D(6.0), null));
}

 @Test
 public void testCheckPointWithSquareRoot() {
 IntervalsSet set = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane()));
 assertEquals(Region.Location.INSIDE, set.checkPoint(new Vector1D(1.0), Math.sqrt(2.0)));
 assertEquals(Region.Location.BOUNDARY, set.checkPoint(new Vector1D(2.0), Math.sqrt(2.0)));
 assertEquals(Region.Location.OUTSIDE, set.checkPoint(new Vector1D(6.0), Math.sqrt(2.0)));
}

}