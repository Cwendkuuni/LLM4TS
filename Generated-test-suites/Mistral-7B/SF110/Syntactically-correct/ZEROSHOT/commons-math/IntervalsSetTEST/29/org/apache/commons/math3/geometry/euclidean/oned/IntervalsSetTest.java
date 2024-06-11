import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0 - 0.0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new Vector1D(0.0), new BSPTree<Euclidean1D>(new Vector1D(5.0))));
 assertEquals(5.0 - 0.0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testConstructorWithBoundary() {
 Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new Line(new Vector1D(0.0), new Vector1D(1.0)));
 boundary.add(new Line(new Vector1D(2.0), new Vector1D(3.0)));
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertEquals(3.0 - 0.0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testBuildTreeWithLowerAndUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 5.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 assertEquals(5.0 - 0.0, intervalsSet.size(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(new BSPTree<Euclidean1D>(new Vector1D(-1.0), new BSPTree<Euclidean1D>(new Vector1D(5.0))));
 assertEquals(-1.0, intervalsSet2.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 IntervalsSet intervalsSet2 = new IntervalsSet(new BSPTree<Euclidean1D>(new Vector1D(0.0), new BSPTree<Euclidean1D>(new Vector1D(4.0))));
 assertEquals(4.0, intervalsSet2.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(5, list.size());
 for (int i = 0; i < list.size(); i++) {
 Interval interval = list.get(i);
 double expectedInf = i * 1.0 + 0.0;
 double expectedSup = (i + 1) * 1.0;
 assertEquals(expectedInf, interval.getInf(), 0.0);
 assertEquals(expectedSup, interval.getSup(), 0.0);
 }
 }
}