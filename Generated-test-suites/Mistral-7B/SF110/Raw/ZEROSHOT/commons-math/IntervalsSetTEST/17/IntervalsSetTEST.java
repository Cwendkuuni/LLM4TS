import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testFiniteIntervalsConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(5, intervals.size());
 for (int i = 0; i < intervals.size(); i++) {
 Interval interval = intervals.get(i);
 assertEquals(i + 1.0, interval.getInf(), 0.0);
 assertEquals(i + 1.0 + 0.01, interval.getSup(), 0.01);
 }
 }

 @Test
 public void testInfAndNegInfConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(2, intervals.size());
 Interval infInterval = intervals.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, infInterval.getInf(), 0.0);
 assertEquals(0.0, infInterval.getSup(), 0.0);
 Interval supInterval = intervals.get(1);
 assertEquals(0.0, supInterval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, supInterval.getSup(), 0.0);
 }

 @Test
 public void testBSPTreeConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 5.0));
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(5, intervals.size());
 for (int i = 0; i < intervals.size(); i++) {
 Interval interval = intervals.get(i);
 assertEquals(i + 1.0, interval.getInf(), 0.0);
 assertEquals(i + 1.0 + 0.01, interval.getSup(), 0.01);
 }
 }

 @Test
 public void testCollectionConstruction() {
 List<Interval> intervals = new ArrayList<>();
 intervals.add(new Interval(1.0, 2.0));
 intervals.add(new Interval(5.0, 6.0));
 IntervalsSet intervalsSet = new IntervalsSet(intervals);
 List<Interval> list = intervalsSet.asList();
 assertEquals(intervals.size(), list.size());
 for (int i = 0; i < intervals.size(); i++) {
 Interval interval = intervals.get(i);
 Interval testInterval = list.get(i);
 assertEquals(interval.getInf(), testInterval.getInf(), 0.0);
 assertEquals(interval.getSup(), testInterval.getSup(), 0.0);
 }
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(0.0, intervalsSet.getInf(), 0.0);
 IntervalsSet intervalsSetInf = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSetInf.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertEquals(5.0, intervalsSet.getSup(), 0.0);
 IntervalsSet intervalsSetSup = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetSup.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> intervals = intervalsSet.asList();
 assertEquals(5, intervals.size());
 for (int i = 0; i < intervals.size(); i++) {
 Interval interval = intervals.get(i);
 assertEquals(i + 1.0, interval.getInf(), 0.0);
 assertEquals(i + 1.0 + 0.01, interval.getSup(), 0.01);
 }
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 BSPTree<Euclidean1D> tree = buildTree(0.0, 5.0);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 List<Interval> intervals = newIntervalsSet.asList();
 assertEquals(5, intervals.size());
 for (int i = 0; i < intervals.size(); i++) {
 Interval interval = intervals.get(i);
 assertEquals(i + 1.0, interval.getInf(), 0.0);
 assertEquals(i + 1.0 + 0.01, interval.getSup(), 0.01);
 }
 }
}