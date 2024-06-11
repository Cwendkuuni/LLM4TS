import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptySet() {
 IntervalsSet set = new IntervalsSet();
 assertEquals(0, set.size(), 0.0);
 assertTrue(set.isEmpty());
 }

 @Test
 public void testFiniteIntervalSet() {
 IntervalsSet set = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 assertEquals(4.0, set.size(), 0.0);
 assertFalse(set.isEmpty());
 }

 @Test
 public void testInfiniteIntervalSetBelowZero() {
 IntervalsSet set = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(Double.POSITIVE_INFINITY, set.size(), Double.POSITIVE_INFINITY);
 assertTrue(set.isEmpty());
 }

 @Test
 public void testInfiniteIntervalSetAboveZero() {
 IntervalsSet set = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, set.size(), Double.POSITIVE_INFINITY);
 assertTrue(set.isEmpty());
 }

 @Test
 public void testBoundaryPoints() {
 IntervalsSet set = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 List<Interval> intervals = set.asList();
 assertEquals(4, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.5, interval.getBarycenter(), 0.0);
 assertEquals(3.0, interval.getSup(), 0.0);
 interval = intervals.get(1);
 assertEquals(3.0, interval.getInf(), 0.0);
 assertEquals(3.75, interval.getBarycenter(), 0.0);
 assertEquals(4.0, interval.getSup(), 0.0);
 interval = intervals.get(2);
 assertEquals(4.0, interval.getInf(), 0.0);
 assertEquals(4.5, interval.getBarycenter(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 interval = intervals.get(3);
 assertEquals(5.0, interval.getInf(), 0.0);
 assertEquals(NaN.INSTANCE, interval.getBarycenter(), 0.0);
 assertEquals(NaN.INSTANCE, interval.getSup(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet set = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 assertEquals(1.0, set.getInf(), 0.0);
 set = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), Double.NEGATIVE_INFINITY);
 set = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, set.getInf(), Double.POSITIVE_INFINITY);
 }

 @Test
 public void testGetSup() {
 IntervalsSet set = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 assertEquals(5.0, set.getSup(), 0.0);
 set = new IntervalsSet(Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(Double.NEGATIVE_INFINITY, set.getSup(), Double.NEGATIVE_INFINITY);
 set = new IntervalsSet(0.0, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, set.getSup(), Double.POSITIVE_INFINITY);
 }

 @Test
 public void testContainsPoint() {
 IntervalsSet set = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 assertTrue(set.contains(new Vector1D(2.5)));
 assertFalse(set.contains(new Vector1D(0.5)));
 }

 @Test
 public void testBuildTree() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 5.0);
 assertNotNull(tree);
 }
}