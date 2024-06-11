import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testFiniteIntervalConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 Interval expectedInterval = new Interval(1.0, 3.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(expectedInterval, intervals.get(0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testInfiniteLowerBoundConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(-1.0, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(new Interval(Double.NEGATIVE_INFINITY, 0.0), intervals.get(0));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testInfiniteUpperBoundConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(new Interval(Double.NEGATIVE_INFINITY, 5.0), intervals.get(0));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testBSPTreeConstruction() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE, null), null);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 assertEquals(new Interval(Double.NEGATIVE_INFINITY, 2.0), intervals.get(0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testCollectionConstruction() {
 List<Interval> intervals = new ArrayList<>();
 intervals.add(new Interval(1.0, 3.0));
 IntervalsSet intervalsSet = new IntervalsSet(intervals);
 assertEquals(1, intervalsSet.asList().size());
 assertEquals(new Interval(1.0, 3.0), intervalsSet.asList().get(0));
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet1 = new IntervalsSet(1.0, 3.0);
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(2.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE, null), null);
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
 assertEquals(intervalsSet1, intervalsSet2);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.0, intervalsSet.getSize(), 0.0);
 assertEquals(new Vector1D(2.5), intervalsSet.getBarycenter(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 assertEquals(1.0, intervalsSet.getInf(), 0.0);
 intervalsSet = new IntervalsSet(new IntervalsSet(1.0, 3.0).buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE)));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 assertEquals(3.0, intervalsSet.getSup(), 0.0);
 intervalsSet = new IntervalsSet(new IntervalsSet(1.0, 3.0).buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE)));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(expectedIntervals, intervals);
 }

 @Test
 public void testEmptyAsList() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(0, intervals.size());
 }
}