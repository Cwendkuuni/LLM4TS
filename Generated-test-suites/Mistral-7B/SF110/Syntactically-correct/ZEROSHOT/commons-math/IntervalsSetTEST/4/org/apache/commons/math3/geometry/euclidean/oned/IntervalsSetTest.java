import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(0.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(2.0, 4.0)));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(1, intervals.size());
 Interval interval = intervals.get(0);
 assertEquals(2.0, interval.getInf(), 0.0);
 assertEquals(4.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalIntervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 IntervalsSet newIntervalsSet = originalIntervalsSet.buildNew(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane()));
 List<Interval> originalIntervals = originalIntervalsSet.asList();
 List<Interval> newIntervals = newIntervalsSet.asList();
 assertEquals(originalIntervals.size(), newIntervals.size());
 for (int i = 0; i < originalIntervals.size(); i++) {
 Interval originalInterval = originalIntervals.get(i);
 Interval newInterval = newIntervals.get(i);
 assertEquals(originalInterval.getInf(), newInterval.getInf(), 0.0);
 assertEquals(originalInterval.getSup(), newInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 intervalsSet.computeGeometricalProperties();
 assertEquals(3.0, intervalsSet.getBarycenter().getX(), 0.0);
 assertEquals(5.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(-Double.INFINITY, intervalsSet.getInf(), 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 }
}