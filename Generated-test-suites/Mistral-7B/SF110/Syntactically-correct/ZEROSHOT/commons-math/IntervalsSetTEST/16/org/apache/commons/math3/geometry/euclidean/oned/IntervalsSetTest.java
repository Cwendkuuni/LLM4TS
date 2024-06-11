import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet set = new IntervalsSet();
 assertTrue(set.isEmpty());
 }

 @Test
 public void testConstructorWithLimit() {
 IntervalsSet set = new IntervalsSet(0.0, 1.0);
 assertFalse(set.isEmpty());
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet set = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 assertFalse(set.isEmpty());
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 assertFalse(set.isEmpty());
 }

 @Test
 public void testBuildTreeWithLimit() {
 IntervalsSet set = new IntervalsSet();
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 1.0);
 IntervalsSet set2 = set.buildNew(tree);
 assertEquals(new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane())), set2);
 }

 @Test
 public void testGetInf() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 assertEquals(Double.NEGATIVE_INFINITY, set.getInf(), 1e-15);
 set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(-1.0, set.getInf(), 1e-15);
 }

 @Test
 public void testGetSup() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 assertEquals(Double.POSITIVE_INFINITY, set.getSup(), 1e-15);
 set = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(3.0, set.getSup(), 1e-15);
 }

 @Test
 public void testAsList() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> list = set.asList();
 assertEquals(3, list.size());
 for (int i = 0; i < 3; i++) {
 Interval interval = list.get(i);
 assertEquals(i == 0 ? new Interval(0.0, 1.0) : i == 1 ? new Interval(1.0, 2.0) : new Interval(3.0, 4.0), interval);
 }
 }

 @Test
 public void testIsEmpty() {
 IntervalsSet set = new IntervalsSet();
 assertTrue(set.isEmpty());
 set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0)));
 assertFalse(set.isEmpty());
 }

 @Test
 public void testSize() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(2.0, set.size(), 1e-15);
 }

 @Test
 public void testBarycenter() {
 IntervalsSet set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 assertEquals(Vector1D.NaN, set.getBarycenter());
 set = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(new Vector1D(2.0 / 3.0), set.getBarycenter(), 1e-15);
 }
}